
import com.sun.java.util.jar.pack.ConstantPool;

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

    public MapQuadtree(String[] img_source) {
        /**
         * constructor: like the complete minPQ, the Tree can be a Node[]
         * Index of children of node with Index=k: 4k-2 ~ 4k + 1
         * Index of paret of node with Index=k: if k>1 and (k+1)%4==0 or (k+2)%4==0 or (k-1)%4==0 or k%4==0:
         *                                      then parent k+1/k+2/k-1/k / 4
         */
        mqt = (Tile[]) new Object[img_source.length + 1];
        mqt[0] = null;
        // the special root case
        mqt[1] = new Tile("root", MapServer.ROOT_ULLON, MapServer.ROOT_ULLAT, MapServer.ROOT_LRLON, MapServer.ROOT_LRLAT);
        // the first four special tiles 1,2,3,4
        mqt[2] = new Tile("1", mqt[1].tileUlLon, mqt[1].tileUlLat,
                (mqt[1].tileUlLon - mqt[1].tileLrLon)/2.0, (mqt[1].tileUlLat - mqt[1].tileLrLat)/2.0);
        mqt[3] = new Tile("2", mqt[2].tileLrLon, mqt[2].tileUlLat,
                        mqt[1].tileLrLon, mqt[2].tileLrLat);
        mqt[4] = new Tile("3", mqt[2].tileUlLon, mqt[2].tileLrLat, mqt[2].tileLrLon, mqt[1].tileLrLat);
        mqt[5] = new Tile("4", mqt[2].tileLrLon, mqt[2].tileLrLat, mqt[3].tileLrLon, mqt[4].tileLrLat);

        // start from tile 11, 12, ...
        for (int i = 6; i <= img_source.length; i += 1) {
            // Up Left node
            if (i == 2 || i % 4 == 2) {
                mqt[i] = new Tile(img_source[i], MapServer.ROOT_ULLON, MapServer.ROOT_ULLAT, );
            }
        }
    }


    // Tile on this index has no children
    public boolean isLeaf(int index) {
        return mqt[4*index-2] == null;
    }

    // return the children of the Tile on this index
    public Iterable<Tile> branches(int index) {
        List<Tile> tileBranches = new LinkedList<>();
        if (!isLeaf(index)) {
            for (int i = index * 4 - 2; i <= index * 4 + 1; i +=1)
            try {
                tileBranches.add(mqt[i]);
            } catch (IndexOutOfBoundsException e) {break;}
        }
        // could be Null!
        return tileBranches;
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

        public Tile(String name, double ul_lon, double ul_lat, double lr_lon, double lr_lat) {
            tileName = name;
            tileUlLon = ul_lon;
            tileUlLat = ul_lat;
            tileLrLon = lr_lon;
            tileLrLat = lr_lat;
        }

    }
}
