
import sun.awt.image.ImageWatched;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Stack;

/**
 * This class provides a shortestPath method for finding routes between two points
 * on the map. Start by using Dijkstra's, and if your code isn't fast enough for your
 * satisfaction (or the autograder), upgrade your implementation by switching it to A*.
 * Your code will probably not be fast enough to pass the autograder unless you use A*.
 * The difference between A* and Dijkstra's is only a couple of lines of code, and boils
 * down to the priority you use to order your vertices.
 */
public class Router {
    /**
     * Return a LinkedList of <code>Long</code>s representing the shortest path from st to dest, 
     * where the longs are node IDs.
     */
    public static LinkedList<Long> shortestPath(GraphDB g, double stlon, double stlat, double destlon, double destlat) {
        /* closest is O(V)*/
        Long start = g.closest(stlon, stlat);
        Long dest = g.closest(destlon, destlat);
        HashMap<Long, Long> edgeTo = new HashMap<>();
        HashMap<Long, Double> distanceAccumMap = new HashMap<>();
        ArrayHeap<Long> hp = new ArrayHeap<>();
        hp.insert(start, 0);

        while ( hp.size() != 0) {
            Long min = hp.removeMin();
            Double distance_so_far = distanceAccumMap.get(min);
            for (Long adj : g.adjacent(min)) {
                if (adj == dest) { break;}
                double priority = g.distance(adj, min) + distance_so_far;
                hp.changePriority(adj, priority);
                hp.insert(adj, g.distance(adj, start));
            }
            Long min_node_id = hp.removeMin();
            edgeTo.put(min_node_id, start);
        }

        Stack<Long> paths = new Stack<>();
        for (Long l = dest; edgeTo.get(l) != null; l = edgeTo.get(l)) {
            paths.push(l);
        }
        LinkedList result = new LinkedList();
        while (paths != null) {
            result.add(paths.pop());
        }
        return result;
    }

}
