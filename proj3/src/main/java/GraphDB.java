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

    HashMap<Long, Node> nodes_lst = new HashMap<>();
    HashMap<Long, Node> complets_nodes_lst = new HashMap<>();
    HashMap<Long, Way> ways_lst = new HashMap<>();
    Node last_node;
    Way last_way;
    /*the trie initiation*/
    MapTrie mt = new MapTrie();
    HashMap<Long, String> id_to_name = new HashMap<>();

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
    double lon(long v) { return complets_nodes_lst.get(v).lon;}

    /** Latitude of vertex v. */
    double lat(long v) { return complets_nodes_lst.get(v).lat;}

    public void addNode(Attributes attr) {
        Node nd = new Node (attr);
        nodes_lst.put(nd.getId(), nd);
        complets_nodes_lst.put(nd.getId(), nd);
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

    public boolean isInGraph(long id) {
        return nodes_lst.containsKey(id);
    }


    void setNodeName(Node nd, String k, Long id) {
        String clean_k = cleanString(k).toLowerCase();
        mt.put(clean_k, id);
        id_to_name.put(id, k);
    }

    void setWayName(Way wy, String k, Long id) {
        mt.put(k.toLowerCase(), id);
        wy.setWayName(k);
        //id_to_name.put(id, k);
    }

    ArrayList<String> getLocationsByPrefix (String pre) {
        String clean_pre = cleanString(pre);
        ArrayList<String> res = new ArrayList<>();
        for (long id : mt.findPrefix(clean_pre)) {
            res.add(id_to_name.get(id));
        }
        return res;
    }

    List<Map<String, Object>> getLocations (String name) {
        List<Map<String, Object>> locations = new ArrayList<>();
        String clean_name = cleanString(name);
        for (long id : mt.get(clean_name)) {
            Map<String, Object> each_param = new HashMap<>();
            each_param.put("name", id_to_name.get(id));
            each_param.put("lon", lon(id));
            each_param.put("id", id);
            each_param.put("lat", lat(id));
            locations.add(each_param);
        }
        return locations;
    }

    class Node {
        LinkedList<Long> adjacentsId;
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

        long getId() {
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
    class MapTrie {
        private Name root;

        public MapTrie() {
            root = new Name();
        }

        private class Name {
            private ArrayList<Long> value;
            private Map<Character, Name> child_lst;

            public Name() {
                value = new ArrayList<>();
                child_lst = new HashMap<>();
            }
        }

        //private Name root = new Name();

        void put(String k, long value) {
            put(root, k, 0, value);
        }

        private Name put(Name n, String k, int d, long value) {
            if (n == null) {
                n = new Name();
            }
            if (d == k.length()) {
                n.value.add(value);
                return n;
            }
            char c = k.charAt(d);
            n.child_lst.put(c, put(n.child_lst.get(c), k, d + 1, value));
            return n;
        }

        public ArrayList<Long> get(String k) {
            k = k.toLowerCase();
            return get(root, k, 0).value;
        }

        private Name get(Name n, String s, int d) {
            //Name res = new Name();
            if (n == null) {
                n = new Name();
            }
            if (d == s.length()) {
                //res.add(n);
                return n;
            }
            char c = s.charAt(d);
            return get(n.child_lst.get(c),s, d + 1);
            //return res;
        }

        public Iterable<Long> findPrefix(String k) {
            Queue<Long> q = new ArrayDeque<>();
            Name prefix_found = get(root, k, 0);
            //List<Name> prefix_found_lst = get(root, k, 0);
            //for (Name prefix_found : prefix_found_lst) {
            collect(prefix_found, q);
            //}
            return q;
        }

        private void collect(Name n, Queue<Long> q) {
            if (n == null) {
                return;
            }
            if (n.value.size() > 0) {
                q.add(n.value.get(0));
            }
            for (Map.Entry<Character, Name> child : n.child_lst.entrySet()) {
                collect(n.child_lst.get(child.getKey()), q);
            }
        }
    }
}
