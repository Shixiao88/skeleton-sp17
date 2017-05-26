import org.junit.Test;

import java.util.HashMap;
import java.util.LinkedList;

import static org.junit.Assert.*;

public class AGRouterTest extends AGMapTest {
    /**
     * Test the route-finding functionality by comparing the node id list item by item.
     * @throws Exception
     */
    @Test
    public void testShortestPath() throws Exception {
        for (TestParameters p : params) {
            LinkedList<Long> studentRouteResult = Router.shortestPath(graph,
                    p.routeParams.get("start_lon"), p.routeParams.get("start_lat"),
                    p.routeParams.get("end_lon"), p.routeParams.get("end_lat"));
            assertEquals("Found route differs for input: " + p.routeParams + ".\n",
                    p.routeResult, studentRouteResult);
        }
    }

    @Test
    public void testTile() {

        Router.Tile t = new Router.Tile(9L);
        Router.Tile t2 = new Router.Tile(1L);
        Router.Tile t3 = new Router.Tile(111L);
        Router.distanceAccumMap = new HashMap<>();
        Router.distanceAccumMap.put(9L, 10.0);
        Router.distanceAccumMap.put(1L, 100.0);
        Router.distanceAccumMap.put(111L, 10.0);
        Router.DistanceComparator cmp = new Router.DistanceComparator();
        assertTrue(cmp.compare(t,t2) < 0);
        assertTrue(cmp.compare(t,t3) == 0);

        Router.hp.remove(new Router.Tile(111L));
        Router.hp.add(new Router.Tile(111L));
        Router.distanceAccumMap.put(111L, 1.0);
        assertTrue(cmp.compare(t,t3) > 0);
    }
}
