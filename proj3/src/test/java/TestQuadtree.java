import org.junit.Test;
import java.io.File;
import java.util.ArrayList;
import static org.junit.Assert.*;
import java.util.Map;

/**
 * Created by Administrator on 2017/5/21.
 */

public class TestQuadtree {

    @Test
    public void TestCreatQT() {
        String[] img_source = {"root", "1", "2","3","4"};
        MapQuadtree mqt = new MapQuadtree(img_source, img_source.length, "img/");
        mqt.treeIter();

        /**
         * -122.2998046875
         * 37.892195547244356
         * -122.2119140625
         * 37.82280243352756
         */
        System.out.println("tile root: ");
        for (Double tile_locates : (mqt.getTile(0).getParams())) {
            System.out.println(tile_locates);
        }

        /**
         * -122.2998046875
         * 37.892195547244356
         * -122.255859375
         * 37.85749899038596
         */
        System.out.println("tile 1: ");
        for (Double tile_locates : (mqt.getTile(1).getParams())) {
            System.out.println(tile_locates);
        }

        /**
         * -122.255859375
         * 37.892195547244356
         * -122.2119140625
         * 37.85749899038596
         */
        System.out.println("tile 2: ");
        for (Double tile_locates : (mqt.getTile(2).getParams())) {
            System.out.println(tile_locates);
        }

        /**
         * -122.2998046875
         * 37.85749899038596
         * -122.255859375
         * 37.82280243352756
         */
        System.out.println("tile 3: ");
        for (Double tile_locates : (mqt.getTile(3).getParams())) {
            System.out.println(tile_locates);
        }

        /**
         * -122.255859375
         * 37.85749899038596
         * -122.2119140625
         * 37.82280243352756
         */
        System.out.println("tile 4: ");
        for (Double tile_locates : (mqt.getTile(4).getParams())) {
            System.out.println(tile_locates);
        }
    }

    @Test
    public void testTestHtml() {
        String[] names = {"root", "1", "2","3","4","11","12","13","14","21","22","23","24","31","32","33","34",
        "41","42","43","44"};
        MapQuadtree mqt = new MapQuadtree(names, names.length, "img/");
        ArrayList<MapQuadtree.Tile> res = mqt.testRasterList(-122.30410170759153,37.870213571328854,
                -122.2104604264636, 37.8318576119893,1091.0, 566.0);
        assertEquals("13", res.get(0).getTileName());
        assertEquals("44", res.get(res.size()-1).getTileName());
    }

    @Test
    public void testFileIO() {
        String[] names = new String[30000];
        int i = 0;
        for (final File file : Rasterer.sortByFileNameLen("img/")) {
            names[i] = file.getName();
            i += 1;
        }
        MapQuadtree mqt = new MapQuadtree(names, i, "img/");

        ArrayList<MapQuadtree.Tile> res = mqt.testRasterList(-122.30410170759153,37.870213571328854,
                -122.2104604264636, 37.8318576119893,1091.0, 566.0);
        assertEquals("13.png", res.get(0).getTileName());
        assertEquals("44.png", res.get(res.size()-1).getTileName());
    }

    @Test
    public void testMapRaster() {
        String[] names = new String[30000];
        int i = 0;
        for (final File file : Rasterer.sortByFileNameLen("img/")) {
            names[i] = file.getName();
            i += 1;
        }
        MapQuadtree mqt = new MapQuadtree(names, i, "img/");

        Map<String, Object> map = mqt.raster(-122.30410170759153,37.870213571328854,
                -122.2104604264636, 37.8318576119893,1091.0, 566.0);

        String[][] s = (String[][]) map.get("render_grid");
        for (int x = 0 ; x < 3; x +=1 ) {
            for (int y = 0; y < 4; y +=1 ) {
                System.out.print(s[x][y]);
                System.out.print(", ");
            }
            System.out.print("\n");
        }
        assertEquals(-122.2998046875, map.get("raster_ul_lon"));
        assertEquals(2,map.get("depth"));
        assertEquals(-122.2119140625, map.get("raster_lr_lon"));
        assertEquals(37.82280243352756, map.get("raster_lr_lat"));
        assertEquals(37.87484726881516, map.get("raster_ul_lat"));
        assertTrue((boolean) map.get("query_success"));
    }

    @Test
    public void testDeeperMapRaster() {
        String[] names = new String[30000];
        int i = 0;
        for (final File file : Rasterer.sortByFileNameLen("img/")) {
            names[i] = file.getName();
            i += 1;
        }
        MapQuadtree mqt = new MapQuadtree(names, i, "img/");

        ArrayList<MapQuadtree.Tile> res = mqt.testRasterList(-122.24163047377972,37.87655856892288,
                -122.24053369025242, 37.87548268822065,892.0, 875.0);

        Map<String, Object> map = mqt.raster(-122.24163047377972,37.87655856892288,
                -122.24053369025242, 37.87548268822065,892.0, 875.0);

        String[][] s = (String[][]) map.get("render_grid");
        for (int x = 0 ; x < 3; x +=1 ) {
            for (int y = 0; y < 3; y +=1 ) {
                System.out.print(s[x][y]);
                System.out.print(", ");
            }
            System.out.print("\n");
        }
        assertEquals(-122.24212646484375, map.get("raster_ul_lon"));
        assertEquals(7,map.get("depth"));
        assertEquals(-122.24006652832031, map.get("raster_lr_lon"));
        assertEquals(37.87538940251607, map.get("raster_lr_lat"));
        assertEquals(37.87701580361881, map.get("raster_ul_lat"));
        assertTrue((boolean) map.get("query_success"));
    }
}
