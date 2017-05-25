import static org.junit.Assert.*;
import static spark.Spark.staticFileLocation;

import org.junit.Test;
import java.util.LinkedList;
import java.io.File;

/**
 * Created by Administrator on 2017/5/25.
 */
public class RousterTest {
    private final double start_lon = -122.26772584909467;
    private final double start_lat = 37.86395402584392;
    private final double end_lon = -122.2590140342143;
    private final double end_lat = 37.86876546243951;
    private static Rasterer rasterer;
    private static GraphDB graph;
    LinkedList<Long> route;

    public static void initialize() {
        graph = new GraphDB("berkeley.osm");
    }

    @Test
    public void testRoute () {
        initialize();
        route = Router.shortestPath(graph, start_lon, start_lat, end_lon, end_lat);
    }
}
