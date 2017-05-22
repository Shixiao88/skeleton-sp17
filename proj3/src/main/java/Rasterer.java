import java.util.*;
import java.io.File;

/**
 * LONGITUD = X, LATITUD = Y
 */

/**
 * This class provides all code necessary to take a query box and produce
 * a query result. The getMapRaster method must return a Map containing all
 * seven of the required fields, otherwise the front end code will probably
 * not draw the output correctly.
 */
public class Rasterer {
    // Recommended: QuadTree instance variable. You'll need to make
    //              your own QuadTree since there is no built-in quadtree in Java.

    /** imgRoot is the name of the directory containing the images.
     *  You may not actually need this for your class. */
    private MapQuadtree qt;
    public static final double TOLERANCE = 1.00;

    public Rasterer(String imgRoot) {
        String[] names = new String[30000];
        int i = 1;
        for (final File file : sortByFileNameLen(imgRoot)) {
            names[i] = file.getName();
            i += 1;
        }
        qt = new MapQuadtree(names, i);
    }

    public static Iterable<File> sortByFileNameLen(String imgRoot) {
        List<File> files = Arrays.asList(new File(imgRoot).listFiles());
        Collections.sort(files, new Comparator<File>() {
            @Override
            public int compare(File o1, File o2) {
                return Integer.compare(o1.getName().length(), o2.getName().length());
            }
        });
        return files;
    }

    /**
     * Takes a user query and finds the grid of images that best matches the query. These
     * images will be combined into one big image (rastered) by the front end. <br>
     * <p>
     *     The grid of images must obey the following properties, where image in the
     *     grid is referred to as a "tile".
     *     <ul>
     *         <li>The tiles collected must cover the most longitudinal distance per pixel
     *         (LonDPP) possible, while still covering less than or equal to the amount of
     *         longitudinal distance per pixel in the query box for the user viewport size. </li>
     *         <li>Contains all tiles that intersect the query bounding box that fulfill the
     *         above condition.</li>
     *         <li>The tiles must be arranged in-order to reconstruct the full image.</li>
     *     </ul>
     * </p>
     * @param params Map of the HTTP GET request's query parameters - the query box and
     *               the user viewport width and height.
     *
     * @return A map of results for the front end as specified:
     * "render_grid"   -> String[][], the files to display
     * "raster_ul_lon" -> Number, the bounding upper left longitude of the rastered image <br>
     * "raster_ul_lat" -> Number, the bounding upper left latitude of the rastered image <br>
     * "raster_lr_lon" -> Number, the bounding lower right longitude of the rastered image <br>
     * "raster_lr_lat" -> Number, the bounding lower right latitude of the rastered image <br>
     * "depth"         -> Number, the 1-indexed quadtree depth of the nodes of the rastered image.
     *                    Can also be interpreted as the length of the numbers in the image
     *                    string. <br>
     * "query_success" -> Boolean, whether the query was able to successfully complete. Don't
     *                    forget to set this to true! <br>
     * @see //#REQUIRED_RASTER_REQUEST_PARAMS
     */
    public Map<String, Object> getMapRaster(Map<String, Double> params) {
        System.out.println(params);
        Map<String, Object> results = new HashMap<>();

//        results.put("raster_ul_lon",-122.2998046875);
//        results.put("depth",2);
//        results.put("raster_lr_lon",-122.2119140625);
//        results.put("raster_lr_lat",37.82280243352756);
//        String[][] s_res = {{"img/13.png", "img/14.png", "img/23.png", "img/24.png"}, {"img/31.png", "img/32.png", "img/41.png",
//                "img/42.png"}, {"img/33.png", "img/34.png", "img/43.png", "img/44.png"}};
//        results.put("render_grid", s_res);
//        results.put("raster_ul_lat",37.87484726881516);
//        results.put("query_success",true);

//        System.out.println("Since you haven't implemented getMapRaster, nothing is displayed in "
//                           + "your browser.");
        return results;
    }

}
