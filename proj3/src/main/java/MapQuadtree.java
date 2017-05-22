import java.lang.reflect.Array;
import java.util.*;

/**
 * Created by Xiao Shi on 2017/5/19.
 */

/**
 * LONGITUD = X, LATITUD = Y
 */

public class MapQuadtree {
    /**
     * self-build Quadralic Tree, the NODE is the tile of the MAP
     * - isLeaf method, return if the tree is leaf
     * - isEmpty method, return true if the tree is empty
     * - branches, return given node's iterable of its branches
     * - parent, return given node's parent
     * - getBetween method, return iterable of the nodes in between
     * @return
     */

    private ArrayList<Tile> mqt;
    private int size;
    private int depth;

    class Tile {
        /**
         * ATRRIBUTE:
         * - string filename
         * - double UlLon
         * - double UlLat
         * - double LrLon
         * - double Lrlat
         * <p>
         * METHOD:
         * ??- CompareTo method, return -1,0,1 coresponding to the four children node.
         * - Children method, return a Iterable list of all its children, return null if leaf
         * - Parent method, return the parent Node
         * - Depth method, return the level depth of the node
         * - Name method, return the pic name
         * - isIn method, return true if the client's screen UL/LR Point is inside the Node Area.
         */
        private String tileName;
        private double tileUlLon;
        private double tileUlLat;
        private double tileLrLon;
        private double tileLrLat;
        private double tileWidth=256.0;
        private int tileIndex;

        public Tile(String name, double ul_lon, double ul_lat, double lr_lon, double lr_lat, double width) {
            tileName = name;
            tileUlLon = ul_lon;
            tileUlLat = ul_lat;
            tileLrLon = lr_lon;
            tileLrLat = lr_lat;
            tileWidth = width;
        }

        public Tile(String name, double ul_lon, double ul_lat, double lr_lon, double lr_lat, int index) {
            tileName = name;
            tileUlLon = ul_lon;
            tileUlLat = ul_lat;
            tileLrLon = lr_lon;
            tileLrLat = lr_lat;
            tileIndex = index;
        }

        public boolean isIn(boolean isUl, Tile client) {
            if (isUl) {
                double copy_tileUlLon = client.tileUlLon;
                double copy_tileUlLat = client.tileUlLat;
                if (client.tileUlLon < MapServer.ROOT_ULLON ) { copy_tileUlLon = MapServer.ROOT_ULLON;}
                if (client.tileUlLat > MapServer.ROOT_ULLAT) {copy_tileUlLat = MapServer.ROOT_ULLAT; }
                return copy_tileUlLon >= tileUlLon && copy_tileUlLat <= tileUlLat
                        && copy_tileUlLon < tileLrLon && copy_tileUlLat  > tileLrLat;
//                boolean a = copy_tileUlLon >= tileUlLon;
//                boolean b = copy_tileUlLat <= tileUlLat;
//                boolean c = copy_tileUlLon < tileLrLon;
//                boolean d = copy_tileUlLat > tileLrLat;
//                return a && b && c && d;
            } else {
                double copy_tileLrLon = client.tileLrLon;
                double copy_tileLrLat = client.tileLrLat;
                if (client.tileLrLon > MapServer.ROOT_LRLON ) { copy_tileLrLon = MapServer.ROOT_LRLON; }
                if (client.tileLrLat < MapServer.ROOT_LRLAT) { copy_tileLrLat = MapServer.ROOT_LRLAT; }
                return copy_tileLrLon > tileUlLon && copy_tileLrLat < tileUlLat
                        && copy_tileLrLon <= tileLrLon && copy_tileLrLat >= tileLrLat;
//                boolean e = copy_tileLrLon > tileUlLon;
//                boolean f = copy_tileLrLat < tileUlLat;
//                boolean g = copy_tileLrLon <= tileLrLon;
//                boolean h = copy_tileLrLat >= tileLrLat;
//                return e && f && g && h;
            }
        }

        public String getTileName(){ return tileName; }

        public Iterable<Double> getParams() {
            LinkedList<Double> x_y = new LinkedList<>();
            x_y.add(tileUlLon);
            x_y.add(tileUlLat);
            x_y.add(tileLrLon);
            x_y.add(tileLrLat);
            return x_y;
        }
    }



    public MapQuadtree(String[] img_source, int total_len) {
        /**
         * img_source, index 0 is null, index 1 is root
         * constructor: like the complete minPQ, the Tree can be a Node[]
         * Index of children of node with Index=k: 4k-2 ~ 4k + 1
         * Index of parent of node with Index=k: if k>1 and (k+1)%4==0 or (k+2)%4==0 or (k-1)%4==0 or k%4==0:
         *                                      then parent k+1/k+2/k-1/k / 4
         */
        size = img_source.length+1;
        mqt = new ArrayList<>();
        mqt.add(null);
        // the special root case
        mqt.add(new Tile("root", MapServer.ROOT_ULLON, MapServer.ROOT_ULLAT, MapServer.ROOT_LRLON, MapServer.ROOT_LRLAT,1));
        depth = 1;

        // the first four special tiles 1,2,3,4
        mqt.add(new Tile(img_source[1], mqt.get(1).tileUlLon, mqt.get(1).tileUlLat,
                (mqt.get(1).tileUlLon - mqt.get(1).tileLrLon)/2.0 + mqt.get(1).tileLrLon,
                (mqt.get(1).tileUlLat - mqt.get(1).tileLrLat)/2.0 + mqt.get(1).tileLrLat,2));
        mqt.add(new Tile(img_source[2], mqt.get(2).tileLrLon, mqt.get(2).tileUlLat,
                        mqt.get(1).tileLrLon, mqt.get(2).tileLrLat,3));
        mqt.add(new Tile(img_source[3], mqt.get(2).tileUlLon, mqt.get(2).tileLrLat, mqt.get(2).tileLrLon, mqt.get(1).tileLrLat,4));
        mqt.add(new Tile(img_source[4], mqt.get(2).tileLrLon, mqt.get(2).tileLrLat, mqt.get(3).tileLrLon, mqt.get(4).tileLrLat,5));
        depth += 1;

        if (total_len >= 6) {
        // start from tile 11, 12, ...
        for (int i = 6; i <= total_len; i += 1) {
            // Left Upper node, sample node : [2]
            if (i % 4 == 2) {
                int parent = (i + 2) / 4;
                mqt.add(new Tile(img_source[i-1], MapServer.ROOT_ULLON, MapServer.ROOT_ULLAT,
                        (mqt.get(parent).tileUlLon - mqt.get(parent).tileLrLon) / 2.0 + mqt.get(parent).tileLrLon,
                        (mqt.get(parent).tileUlLat - mqt.get(parent).tileLrLat) / 2.0 + mqt.get(parent).tileLrLat, i));
                //  Right Upper node, sample node [3]
            } else if (i % 4 == 3) {
                int parent = (i + 1) / 4;
                mqt.add(new Tile(img_source[i-1], mqt.get(i - 1).tileLrLon, mqt.get(i - 1).tileUlLat,
                        mqt.get(parent).tileLrLon, mqt.get(i - 1).tileLrLat, i));
                // Left Lower node, sample node [4]
            } else if (i % 4 == 0) {
                int parent = i / 4;
                mqt.add(new Tile(img_source[i-1], mqt.get(i - 2).tileUlLon, mqt.get(i - 2).tileLrLat, mqt.get(i - 2).tileLrLon,
                        mqt.get(parent).tileLrLat, i));
                // Right Lower node, sample node [5]
            } else if (i % 4 == 1) {
                mqt.add(new Tile(img_source[i-1], mqt.get(i - 3).tileLrLon, mqt.get(i - 3).tileLrLat, mqt.get(i - 2).tileLrLon,
                        mqt.get(i - 1).tileLrLat, i));
            }
        }
        }
    }

    public Tile getTile(int index) { return mqt.get(index); }

    public Map<String, Object> raster(double clientUlLon, double clientUlLat, double clientLrLon, double clientLrLat,
                                      double clientWidth, double clientHeight) {
        Tile clientTile = new Tile("client", clientUlLon, clientUlLat, clientLrLon, clientLrLat, clientWidth);
        ArrayList<Tile> raster_tile_lst = rasterList(clientTile);
        Map<String, Object> MapRaster = new HashMap<>();

        // to get the RasterUlLon
        double raster_ul_lon = raster_tile_lst.get(0).tileUlLon;
        MapRaster.put("raster_ul_lon", raster_ul_lon);

        // to get the RasterUlLat
        double raster_ul_lat = raster_tile_lst.get(0).tileUlLat;
        MapRaster.put("raster_ul_lat", raster_ul_lat);

        // to get the RasterLrLon
        double raster_lr_lon = raster_tile_lst.get(raster_tile_lst.size()-1).tileUlLon;
        MapRaster.put("raster_lr_lon", raster_lr_lon);

        // to get the RasterLrLat
        double raster_lr_lat = raster_tile_lst.get(raster_tile_lst.size()-1).tileLrLat;
        MapRaster.put("raster_lr_lat", raster_lr_lat);

        // to get the depth
        int depth = raster_tile_lst.get(0).tileName.length();
        MapRaster.put("depth", depth);

        // to build the name matrix
        int matrix_x_num = (int)clientWidth / MapServer.TILE_SIZE;
        int matrix_y_num = (int)clientHeight / MapServer.TILE_SIZE;

        ArrayList<ArrayList<String>> name_matrix = new ArrayList<>();
        for (Tile t : raster_tile_lst) {
            if (t.tileUlLon == raster_ul_lon){
                ArrayList<String> line = new ArrayList<>();
                line.add(t.getTileName());
                raster_tile_lst.remove(0);
                addTail(line, raster_tile_lst, raster_lr_lon);
                name_matrix.add(line);
            }
        }


    }

    private void addTail (ArrayList<String> line, ArrayList<Tile> rest, double rasterLrLon) {
        for (Tile t : rest) {
            if (t.tileLrLon <= rasterLrLon) {
                line.add(t.getTileName());
                rest.remove(0);
            } else { return; }
        }
    }

    public ArrayList<Tile> testRasterList(double clientUlLon, double clientUlLat, double clientLrLon, double clientLrLat,
                                 double clientWidth, double clientHeight) {
        Tile clientTile = new Tile("client", clientUlLon, clientUlLat, clientLrLon, clientLrLat, clientWidth);
        return rasterList(clientTile);
    }

    private ArrayList<Tile> rasterList(Tile clientTile) {
        Tile ptr = mqt.get(1);
        System.out.print(ptr.tileName);
        Tile ul_tile = getUlTile(clientTile, ptr);
        Tile lr_tile = getLrTile(clientTile, ptr);
        int len = lr_tile.tileIndex-ul_tile.tileIndex;
        ArrayList<Tile> rater_fit_lst = new ArrayList<>();
        rater_fit_lst.addAll(mqt.subList(ul_tile.tileIndex, lr_tile.tileIndex + 1));
        return rater_fit_lst;
    }

    private Tile getUlTile(Tile client, Tile mqtTile) {
        if (isEqualLonDPP(client, mqtTile)) {
            System.out.print(mqtTile.tileName);
            return mqtTile;
        } else {
            if (!isLeaf(mqtTile)) {
                return getUlTile(client, branches(mqtTile));
            } else {
                return mqtTile;
            }
        }
    }

    private Tile getUlTile(Tile client, Iterable<Tile> branches) {
        for (Tile child : branches) {
            String name = child.tileName;
            if (child.isIn(true, client)) {
                return getUlTile(client, child);
            }
        } throw new RuntimeException("error in get Ul tile");
    }

    private Tile getLrTile(Tile client, Tile mqtTile) {
        if (isEqualLonDPP(client, mqtTile)) {
            return mqtTile;
        } else {
            if (!isLeaf(mqtTile)) {
                return getLrTile(client, branches(mqtTile));
            } else {
                return mqtTile;
            }
        }
    }

    private Tile getLrTile(Tile client, Iterable<Tile> branches) {
        for (Tile child : branches) {
            String name = child.tileName;
            if (child.isIn(false, client)) {
                return getLrTile(client, child);
            }
        } throw new RuntimeException("error in get Lr tile");
    }

    private boolean isEqualLonDPP(Tile client, Tile t) {
        double a = getLonDPP(t);
        double b = getLonDPP(client);
        return getLonDPP(t)/getLonDPP(client) <= Rasterer.TOLERANCE;
    }

    private double getLonDPP (Tile t) {
        return  (t.tileLrLon - t.tileUlLon)/ t.tileWidth;
    }

    // Tile on this index has no children
    private boolean isLeaf(Tile t) {
        int index = t.tileIndex;
        return mqt.size() < 4*index-2;
    }

    // return the children of the Tile on this index
    private Iterable<Tile> branches(Tile t) {
        int index = t.tileIndex;
        List<Tile> tileBranches = new LinkedList<>();
        if (!isLeaf(t)) {
            for (int i = index * 4 - 2; i <= index * 4 + 1; i +=1)
            try {
                tileBranches.add(mqt.get(i));
            } catch (IndexOutOfBoundsException e) {break;}
        }
        // could be Null!
        return tileBranches;
    }

    public void treeIter() {
        treeIter(mqt.get(1));
    }

    private void treeIter(Tile t) {
        System.out.println(t.tileName);
        if (isLeaf(t)) {return;}
        for(Tile child : branches(t)) {
            treeIter(child);
        }
    }
}
