import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.IOException;
import javax.swing.plaf.basic.BasicScrollPaneUI;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.lang.reflect.Array;
import java.util.*;

/**
 * Graph for storing all of the intersection (vertex) and road (edge) information.
 * Uses your GraphBuildingHandler to convert the XML files into a graph. Your
 * code must include the vertices, adjacent, distance, closest, lat, and lon
 * methods. You'll also need to include instance variables and methods for
 * modifying the graph (e.g. addNode and addEdge).
 *
 * @author Alan Yao, Josh Hug
 */
public class GraphDB {
    /** Your instance variables for storing the graph. You should consider
     * creating helper classes, e.g. Node, Edge, etc. */

    /**
     * Example constructor shows how to create and start an XML parser.
     * You do not need to modify this constructor, but you're welcome to do so.
     * @param dbPath Path to the XML file to be parsed.
     */

    private HashMap<Long, Node> nodes_lst = new HashMap<>();
    private HashMap<Long, Way> ways_lst = new HashMap<>();
    Node last_node;
    Way last_way;


    public GraphDB(String dbPath) {
        try {
            File inputFile = new File(dbPath);
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser saxParser = factory.newSAXParser();
            GraphBuildingHandler gbh = new GraphBuildingHandler(this);
            saxParser.parse(inputFile, gbh);
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }
        clean();
    }

    /**
     * Helper to process strings into their "cleaned" form, ignoring punctuation and capitalization.
     * @param s Input string.
     * @return Cleaned string.
     */
    static String cleanString(String s) {
        return s.replaceAll("[^a-zA-Z ]", "").toLowerCase();
    }

    /**
     *  Remove nodes with no connections from the graph.
     *  While this does not guarantee that any two nodes in the remaining graph are connected,
     *  we can reasonably assume this since typically roads are connected.
     */
    private void clean() {
        for (Iterator<Map.Entry<Long, Node>> iterator = nodes_lst.entrySet().iterator(); iterator.hasNext();) {
            Map.Entry<Long, Node> entry = iterator.next();
            if (entry.getValue().adjacentsId.size() == 0) {
                iterator.remove();
            }
        }
    }

    /** Returns an iterable of all vertex IDs in the graph. */
    Iterable<Long> vertices() {
        return new ArrayList<>(nodes_lst.keySet());
    }

    /** Returns ids of all vertices adjacent to v. */
    Iterable<Long> adjacent(long v) {
        return nodes_lst.get(v).adjacentsId;
    }

    /** Returns the Euclidean distance between vertices v and w, where Euclidean distance
     *  is defined as sqrt( (lonV - lonW)^2 + (latV - latW)^2 ). */
    double distance(long v, long w) {
        return Math.sqrt(Math.pow((lon(v) - lon(w)),2) + Math.pow((lat(v) - lat(w)),2));
    }

    /** Returns the vertex id closest to the given longitude and latitude. */
    long closest(double lon, double lat) {
        long smallest = 0L;
        for (Map.Entry<Long, Node> entry : nodes_lst.entrySet()) {
            if (smallest == 0L) {
                smallest = entry.getKey();
            } else {
                if (distanceSqt(lon, lat, smallest) > distanceSqt(lon, lat, entry.getKey())) {
                    smallest = entry.getKey();
                }
            }
        }
        return smallest;
    }

    private double distanceSqt(double lon_x, double lat_x, Long id) {
        double i = lon(id);
        double j = lat(id);
        return Math.pow((lon_x - lon(id)), 2) + Math.pow((lat_x - lat(id)), 2);
    }


    /** Longitude of vertex v. */
    double lon(long v) { return nodes_lst.get(v).lon;}

    /** Latitude of vertex v. */
    double lat(long v) { return nodes_lst.get(v).lat;}

    public void addNode(Attributes attr) {
        Node nd = new Node (attr);
        nodes_lst.put(nd.getId(), nd);
    }

    public void addWay(Attributes attr) {
        Way way = new Way (attr);
    }

    public void addValidateWay (Way way) {
        if (way.isValid) {
            way.connect(way.contained_nodeId_lst);
            ways_lst.put(way.getId(), way);
        }
    }

    public void addEdge(long one, long other) {
        nodes_lst.get(one).addAdjcent(other);
        nodes_lst.get(other).addAdjcent(one);
    }

    public void delNode(long id) {
        try {
            nodes_lst.remove(id);
        } catch (IndexOutOfBoundsException e ) {
            System.out.println("Cannot find item you want to delete");
        }
    }

    public boolean isInGraph(long id) {
        return nodes_lst.containsKey(id);
    }

    class Node {
        LinkedList<Long> adjacentsId;
        private String locationName;
        long id;
        double lon;
        double lat;

        public Node (Attributes attributes) {
            last_node = this;
            adjacentsId = new LinkedList<>();
            id = Long.parseLong(attributes.getValue("id"));
            lon = Double.parseDouble(attributes.getValue("lon"));
            lat = Double.parseDouble(attributes.getValue("lat"));
        }

        public long getId() {
            return id;
        }

        @Override
        public boolean equals(Object that) {
            if (that == null){ return false; }
            else if (this == that) {return true;}
            else if (! (that instanceof Node)) {return false; }
            else {
                return id == ((Node) that).id;
            }
        }

        public void addAdjcent(Long other_node_id) {
            adjacentsId.add(other_node_id);
        }

        public void addLocation (String name) {
            locationName = name;
        }
    }

    class Way {
        private LinkedList<Long> contained_nodeId_lst;
        private boolean isValid = false;
        long last_nodeId;
        String way_name;
        long id;

        public Way (Attributes attributes) {
            last_way = this;
            contained_nodeId_lst = new LinkedList<>();
            id = Long.parseLong(attributes.getValue("id"));
        }

        public void addNodeToWay (Long id) {
            contained_nodeId_lst.add(id);
            last_nodeId = id;
        }

        private long getId() {
            return id;
        }

        @Override
        public boolean equals (Object that) {
            if (that == null) {return false;}
            else if (this == that) {return true; }
            else if (! (that instanceof Way)) {return false;}
            else {
                return id == ((Way) that).id;
            }
        }

        void setValid () { isValid = true;}

        void connect(LinkedList<Long> nds) {
            Long[] nds_array = nds.toArray(new Long[nds.size()]);
            for (int index = 0; index < nds_array.length - 1; index += 1) {
                addEdge(nds_array[index], nds_array[index + 1]);
            }
        }

        void setWayName (String name) { way_name = name; }
    }

    /* the implemention of Trie, wait for future learning and back to complete */
    private class Name {
        private boolean isExisted;
        private Map<Character, Name> child_lst;

        public Name () {
            isExisted = false;
            child_lst = new HashMap<>();
        }

        private Name root = new Name();

        public void put(String k) {
            put(root, k, 0);
        }

        private Name put(Name n, String k, int d) {
            if (n == null) {
                n = new Name();
            }
            if (d == k.length()) {
                n.isExisted = true;
                return n;
            }
            char c = k.charAt(d);
            n.child_lst.put(c, put(n, k, d-1));
            return n;
        }

        public Iterable<String> findPrefix(String k) {
            Queue<String> q = new ArrayDeque<>();
            collect(root, "", k, q);
            return q;
        }

        private void collect(Name n, String pre, String preffix, Queue<String> q) {
            int d = pre.length();
            if (n == null) { return; }
            if (d == preffix.length() && )
        }
    }
}
