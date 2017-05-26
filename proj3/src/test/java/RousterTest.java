import static org.junit.Assert.*;
import static spark.Spark.staticFileLocation;

import org.junit.Test;

import java.util.ArrayList;
import java.util.LinkedList;
import java.io.File;

/**
 * Created by Administrator on 2017/5/25.
 */
public class RousterTest {
    private final double start_lon = -122.2702276;
    private final double start_lat = 37.8514266;
    private final double end_lon = -122.2590140342143;
    private final double end_lat = 37.86876546243951;
    private static Rasterer rasterer;
    private static GraphDB graph;
    LinkedList<Long> route;

    public static void initialize() {
        graph = new GraphDB("berkeley.osm");
    }

    @Test
    public void testSmallest () {
        initialize();
        /* id = 275777651*/
        boolean isOnList = graph.isInGraph(275777651);
        double test_lon = -122.2702276;
        double test_lat = 37.8514266;

        assertEquals(275777651, graph.closest(test_lon,test_lat));

        Iterable<Long> s = graph.adjacent(288992434);
        for (long a : s) { System.out.println(a); }


    }

    @Test
    public void testRoute () {
        initialize();
        route = Router.shortestPath(graph, start_lon, start_lat, end_lon, end_lat);
    }
}
