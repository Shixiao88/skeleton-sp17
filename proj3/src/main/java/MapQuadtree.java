import java.util.*;

/**
 * Created by Xiao Shi on 2017/5/19.
 */

/**
 * LONGITUD = X, LATITUD = Y
 */

public class MapQuadtree<Node>{
    /**
     * self-build Quadralic Tree, the NODE is the tile of the MAP
     * - isLeaf method, return if the tree is leaf
     * - isEmpty method, return true if the tree is empty
     * - branches, return given node's iterable of its branches
     * - parent, return given node's parent
     * - getBetween method, return iterable of the nodes in between
     * @return
     */

    private Tile[] mqt;
    private int size;
    private int depth;

    public MapQuadtree(String[] img_source) {
        /**
         * img_source, index 0 is null, index 1 is root
         * constructor: like the complete minPQ, the Tree can be a Node[]
         * Index of children of node with Index=k: 4k-2 ~ 4k + 1
         * Index of parent of node with Index=k: if k>1 and (k+1)%4==0 or (k+2)%4==0 or (k-1)%4==0 or k%4==0:
         *                                      then parent k+1/k+2/k-1/k / 4
         */
        mqt = (Tile[]) new Object[img_source.length + 1];
        mqt[0] = null;
        // the special root case
        mqt[1] = new Tile("root", MapServer.ROOT_ULLON, MapServer.ROOT_ULLAT, MapServer.ROOT_LRLON, MapServer.ROOT_LRLAT,1);
        depth = 1;

        // the first four special tiles 1,2,3,4
        mqt[2] = new Tile("1", mqt[1].tileUlLon, mqt[1].tileUlLat,
                (mqt[1].tileUlLon - mqt[1].tileLrLon)/2.0, (mqt[1].tileUlLat - mqt[1].tileLrLat)/2.0,2);
        mqt[3] = new Tile("2", mqt[2].tileLrLon, mqt[2].tileUlLat,
                        mqt[1].tileLrLon, mqt[2].tileLrLat,3);
        mqt[4] = new Tile("3", mqt[2].tileUlLon, mqt[2].tileLrLat, mqt[2].tileLrLon, mqt[1].tileLrLat,4);
        mqt[5] = new Tile("4", mqt[2].tileLrLon, mqt[2].tileLrLat, mqt[3].tileLrLon, mqt[4].tileLrLat,5);
        depth += 1;

        // start from tile 11, 12, ...
        for (int i = 6; i <= img_source.length; i += 1) {
            // Left Upper node, sample node : [2]
            if (i % 4 == 2) {
                int parent = (i + 2) / 4;
                mqt[i] = new Tile(img_source[i], MapServer.ROOT_ULLON, MapServer.ROOT_ULLAT,
                        (mqt[parent].tileLrLon - mqt[parent].tileLrLon)/2.0,
                        (mqt[parent].tileUlLat - mqt[parent].tileUlLat)/2.0, i);
            //  Right Upper node, sample node [3]
            } else if (i % 4 == 3) {
                int parent = (i + 1) / 4;
                mqt[i] = new Tile(img_source[i], mqt[i-1].tileLrLon, mqt[i-1].tileUlLat,
                        mqt[parent].tileLrLon, mqt[i-1].tileLrLat, i);
            // Left Lower node, sample node [4]
            } else if (i % 4 == 0) {
                int parent = i / 4;
                mqt[i] = new Tile(img_source[i], mqt[i-2].tileUlLon, mqt[i-2].tileLrLat, mqt[i-2].tileLrLon,
                        mqt[parent].tileLrLat, i);
            // Right Lower node, sample node [5]
            } else if (i % 4 == 1) {
                mqt[i] = new Tile(img_source[i], mqt[i-3].tileLrLon, mqt[i-3].tileLrLat, mqt[i-2].tileLrLon,
                        mqt[i-1].tileLrLat, i);
            }
        }
    }

    public Tile getTile(int index) { return mqt[index]; }

    public Map<String, Object> raster(double clientUlLon, double clientUlLat, double clientLrLon, double clientLrLat,
                                      double clientWidth, double clientHeight) {
        Tile clientTile = new Tile("client", clientUlLon, clientUlLat, clientLrLon, clientLrLat, clientWidth);
        Tile[] raster_tile_lst = rasterList(clientTile);
        Map<String, Object> MapRaster = new HashMap<>();

        // to get the RasterUlLon
        double raster_ul_lon = raster_tile_lst[0].tileUlLon;
        MapRaster.put("raster_ul_lon", raster_ul_lon);

        // to get the RasterUlLat
        double raster_ul_lat = raster_tile_lst[0].tileUlLat;
        MapRaster.put("raster_ul_lat", raster_ul_lat);

        // to get the RasterLrLon
        double raster_lr_lon = raster_tile_lst[raster_tile_lst.length-1].tileUlLon;
        MapRaster.put("raster_lr_lon", raster_lr_lon);

        // to get the RasterLrLat
        double raster_lr_lat = raster_tile_lst[raster_tile_lst.length-1].tileLrLat;
        MapRaster.put("raster_lr_lat", raster_lr_lat);

        // to build the name matrix
        String matrix_first = raster_tile_lst[0].tileName;
        String matrix_last = raster_tile_lst[raster_tile_lst.length].tileName;
        int matrix_x_num = (int)clientWidth / MapServer.TILE_SIZE;
        int matrix_y_num = (int)clientHeight / MapServer.TILE_SIZE;
        String[] line = new String[ matrix_x_num ];
        String[][] name_matrix = new String[ matrix_x_num ][ matrix_y_num ];
        for (int i = 0; i < matrix_y_num; i += 1) {
            for (int j = 0; )
            if (raster_tile_lst[i].tileUlLon <= raster_lr_lon) { line[i] = raster_tile_lst[i].tileName; }
            else { name_matrix[0] = line;}
        }
    }

    private Tile[] rasterList(Tile clientTile) {
        Tile ptr = mqt[1];
        Tile ul_tile = getUlTile(clientTile, ptr);
        Tile lr_tile = getLrTile(clientTile, ptr);
        int len = lr_tile.tileIndex-ul_tile.tileIndex;
        Tile[] rater_fit_lst = (Tile[]) new Object[len];
        System.arraycopy(mqt, ul_tile.tileIndex, rater_fit_lst, 0, len);
        return rater_fit_lst;
    }

    private Tile getUlTile(Tile client, Tile mqtTile) {
        if (isEqualLonDPP(client, mqtTile)) {
            if (mqtTile.isIn(true, client)) {
                return mqtTile;
            }
        } else {
            if (! isLeaf(mqtTile)) {   //branches could return null;
                for (Tile child : branches(mqtTile)) {
                    if (child.isIn(true, client)) {
                        return getUlTile(client, child);
                    }
                }
            } else {return mqtTile;}
        }
        return null;
    }

    private Tile getLrTile(Tile client, Tile mqtTile) {
        if (isEqualLonDPP(client, mqtTile)) {
            if (mqtTile.isIn(false, client)) {
                return mqtTile;
            }
        } else {
            if (! isLeaf(mqtTile)) {   //branches could return null;
                for (Tile child : branches(mqtTile)) {
                    if (child.isIn(false, client)) {
                        return getUlTile(client, child);
                    }
                }
            } else {return mqtTile;}
        }
        return null;
    }

    private boolean isEqualLonDPP(Tile client, Tile t) {
        return getLonDPP(t)/getLonDPP(client) <= Rasterer.TOLERANCE;
    }

    private double getLonDPP (Tile t) {
        return Math.abs(t.tileUlLon - t.tileLrLon)/ t.tileWidth;
    }

    // Tile on this index has no children
    private boolean isLeaf(Tile t) {
        int index = t.tileIndex;
        return mqt[4*index-2] == null;
    }

    // return the children of the Tile on this index
    private Iterable<Tile> branches(Tile t) {
        int index = t.tileIndex;
        List<Tile> tileBranches = new LinkedList<>();
        if (!isLeaf(t)) {
            for (int i = index * 4 - 2; i <= index * 4 + 1; i +=1)
            try {
                tileBranches.add(mqt[i]);
            } catch (IndexOutOfBoundsException e) {break;}
        }
        // could be Null!
        return tileBranches;
    }

    @Override
    public String toString() {
        String res = "";
        for (int i = 1; i <= depth; i += 1) {
            res += mqt[i].toString();
            res += "\n";
        }
        return res;
    }

    private class Tile {
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
                return client.tileUlLon >= tileUlLon && client.tileUlLat <= tileUlLat;
            } else {
                return client.tileLrLon <= tileLrLon && client.tileLrLat >= tileLrLat;
            }
        }
    }
}
