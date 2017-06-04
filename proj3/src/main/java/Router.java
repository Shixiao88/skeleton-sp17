
import sun.awt.image.ImageWatched;

import java.util.*;

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

    static GraphDB g;
    static HashMap<Long, Long> edgeTo = new HashMap<>();
    static HashMap<Long, Double> distanceAccumMap = new HashMap<>();
    static DistanceComparator<Tile> cptr = new DistanceComparator<>();
    static PriorityQueue<Tile> hp = new PriorityQueue<>(4, cptr);
    static long start;
    static long dest;

    public static LinkedList<Long> shortestPath(GraphDB graph, double stlon, double stlat, double destlon, double destlat) {
        /* closest is O(V)*/
        g = graph;
        start = g.closest(stlon, stlat);
        //System.out.println("start node id: " + start);
        dest = g.closest(destlon, destlat);
        //System.out.println("destination node id: " + dest);

        setAllToInfinite_addAllIntoPQ(distanceAccumMap, hp, graph, dest);
        distanceAccumMap.put(start, 0.0);
        edgeTo.put(start, 0L);
        for (Long adj : g.adjacent(start)) {
            edgeTo.put(adj, start);
            distanceAccumMap.put(adj, g.distance(start, adj));
            hp.add(new Tile(adj, dest));
        }

        while ( hp.size() != 0) {
            Tile min_tile = hp.poll();
            long min = min_tile.getID();
            if (min == dest) { break;}
            for (Long adj : g.adjacent(min)) {
                double new_distance = g.distance(adj, min) + distanceAccumMap.get(min);
                checkIfBetter(min, adj, new_distance);
            }
        }

        Stack<Long> paths = new Stack<>();
        for (Long l = dest; edgeTo.get(l) != 0L; l = edgeTo.get(l)) {
            paths.push(l);
        }
        paths.push(start);

        LinkedList result = new LinkedList();
        while (paths.size() != 0) {
            result.add(paths.pop());
            //System.out.println(paths.pop());
        }
        return result;
    }

    private static void checkIfBetter(long parent, long nd, double new_distance) {
        if (distanceAccumMap.containsKey(nd)) {
            if (new_distance < distanceAccumMap.get(nd)){
                edgeTo.put(nd, parent);
                distanceAccumMap.put(nd, new_distance);
                hp.remove(new Tile(nd, dest));
                hp.add(new Tile(nd, dest));
            }
        } else {
            distanceAccumMap.put(nd, new_distance);
            edgeTo.put(nd,parent);
            hp.add(new Tile(nd, dest));
        }
    }

    private static void setAllToInfinite_addAllIntoPQ(HashMap<Long, Double> dist,PriorityQueue<Tile> hp, GraphDB graph, long dest) {
        for ( Map.Entry<Long, GraphDB.Node> entry : graph.nodes_lst.entrySet() ) {
            dist.put(entry.getKey(), Double.POSITIVE_INFINITY);
            //hp.add(new Tile(entry.getKey(), dest));
        }
    }

    static class Tile implements Comparable<Tile>{
        private long self_id;
        private long dest;

        Tile(long id, long dest_id) {
            self_id = id;
            this.dest = dest_id;
        }

        double distance() { return distanceAccumMap.get(self_id) + g.distance(self_id, dest);}

        long getID() { return self_id;}

        @Override
        public int compareTo(Tile that) {
            return ((Double)distance()).compareTo(((Double)that.distance()));
        }

        @Override
        public boolean equals (Object that) {
            if (that == null) {return false;}
            else if (! (that instanceof Tile)) {return false;}
            else if (this == that ) {return true;}
            else {

                return (this.distance() == ((Tile)that).distance());
            }
        }
    }

    static class DistanceComparator<T> implements Comparator<T> {


        public DistanceComparator() {
        }

        @Override
        public int compare(T nd1, T nd2) {
//            double distance1 = ((Tile) nd1).distance();
//            double distance2 = ((Tile) nd2).distance();
//            if (distance1 > distance2) {return 1;}
//            else if (distance1 < distance2) {return -1;}
//            else {return 0;}
            return ((Tile)nd1).compareTo((Tile)nd2);
        }
    }

}
