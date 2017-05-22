import org.junit.Test;
import java.io.File;
import java.util.ArrayList;
import static org.junit.Assert.*;

/**
 * Created by Administrator on 2017/5/21.
 */

public class TestQuadtree {

    @Test
    public void TestCreatQT() {
        String[] img_source = {"root", "1", "2","3","4"};
        MapQuadtree mqt = new MapQuadtree(img_source, img_source.length);
        mqt.treeIter();

        /**
         * -122.2998046875
         * 37.892195547244356
         * -122.2119140625
         * 37.82280243352756
         */
        System.out.println("tile root: ");
        for (Double tile_locates : (mqt.getTile(1).getParams())) {
            System.out.println(tile_locates);
        }

        /**
         * -122.2998046875
         * 37.892195547244356
         * -122.255859375
         * 37.85749899038596
         */
        System.out.println("tile 1: ");
        for (Double tile_locates : (mqt.getTile(2).getParams())) {
            System.out.println(tile_locates);
        }

        /**
         * -122.255859375
         * 37.892195547244356
         * -122.2119140625
         * 37.85749899038596
         */
        System.out.println("tile 2: ");
        for (Double tile_locates : (mqt.getTile(3).getParams())) {
            System.out.println(tile_locates);
        }

        /**
         * -122.2998046875
         * 37.85749899038596
         * -122.255859375
         * 37.82280243352756
         */
        System.out.println("tile 3: ");
        for (Double tile_locates : (mqt.getTile(4).getParams())) {
            System.out.println(tile_locates);
        }

        /**
         * -122.255859375
         * 37.85749899038596
         * -122.2119140625
         * 37.82280243352756
         */
        System.out.println("tile 4: ");
        for (Double tile_locates : (mqt.getTile(5).getParams())) {
            System.out.println(tile_locates);
        }
    }

    @Test
    public void testTestHtml() {
        String[] names = {"root", "1", "2","3","4","11","12","13","14","21","22","23","24","31","32","33","34",
        "41","42","43","44"};
        MapQuadtree mqt = new MapQuadtree(names, names.length);
        ArrayList<MapQuadtree.Tile> res = mqt.testRasterList(-122.30410170759153,37.870213571328854,
                -122.2104604264636, 37.8318576119893,1091.0, 566.0);
        assertEquals("13", res.get(0).getTileName());
        assertEquals("44", res.get(res.size()-1).getTileName());
    }

    @Test
    public void testFileIO() {
        String[] names = new String[30000];
        int i = 1;
        for (final File file : Rasterer.sortByFileNameLen("img/")) {
            names[i] = file.getName();
            i += 1;
        }
        MapQuadtree mqt = new MapQuadtree(names, i);

        ArrayList<MapQuadtree.Tile> res = mqt.testRasterList(-122.30410170759153,37.870213571328854,
                -122.2104604264636, 37.8318576119893,1091.0, 566.0);
        assertEquals("13.png", res.get(0).getTileName());
        assertEquals("44.png", res.get(res.size()-1).getTileName());
    }

}
