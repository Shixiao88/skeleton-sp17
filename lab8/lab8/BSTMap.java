package lab8;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by Administrator on 2017/5/4.
 */
public class BSTMap<K extends Comparable<K>, V> implements Map61B<K, V> {
    private Node root;

    private class Node {
        private K key;
        private V value;
        private int size;
        private Node left;
        private Node right;

        public Node(K k, V v, int s) {
            key = k;
            value = v;
            size = s;
        }

    }

    public BSTMap() {}

    public BSTMap(K key, V value) {
        root = new Node(key, value, 1);
    }

    /** Removes all of the mappings from this map. */
    @Override
    public void clear() {
        clear(root);
    }

    private  void clear(Node node) {
        node.left = null;
        node.right = null;
        node.size = 0;
        node = null;
    }

    /* Returns true if this map contains a mapping for the specified key. */
    @Override
    public boolean containsKey(K key) {
        return containsKey(root, key);
    }

    private boolean containsKey(Node node, K key) {
        if (node == null) {return false;}
        if (key == null) {throw new RuntimeException("key nust be a meaningfull unit");}
        int cmp = key.compareTo(node.key);
        if (cmp < 0) {containsKey(node.left, key);}
        if (cmp > 0) {containsKey(node.right, key);}
        if (cmp == 0) { return true; }
        return false;
    }

    /* Returns the value to which the specified key is mapped, or null if this
     * map contains no mapping for the key.
     */
    @Override
    public V get(K key) {
        return get(root, key);
    }

    private V get(Node node, K key) {
        if (node == null) {return null;}
        if (key == null) {throw new RuntimeException("key nust be a meaningfull unit");}
        int cmp = key.compareTo(node.key);
        if (cmp < 0) {get(node.left, key); }
        if (cmp > 0) {get(node.right, key); }
        if (cmp == 0) { return node.value; }
        return null;
    }

    /* Returns the number of key-value mappings in this map. */
    @Override
    public int size() {
        return size(root);
    }

    private int size(Node node) {
        if (node == null) {
            return 0;
        }
        return 1 + size(node.left) + size(node.right);
    }

    /* Associates the specified value with the specified key in this map. */
    @Override
    public void put(K key, V value) {
        if (key == null) {throw new RuntimeException("must enter a valid key");}
//        if (value == null) {delete(key);}
        root = put(root, key, value);
    }

    private Node put(Node node, K key, V value) {
        if (node == null) {return new Node(key, value, 1); }
        int cmp = node.key.compareTo(key);
        if (cmp < 0) {node.left = put(node.left, key, value); }
        if (cmp > 0) {node.right = put(node.right, key, value); }
        if (cmp == 0) {node.value = value; }
        node.size = size(node.left) + size(node.right) + 1;
        return node;
    }

    public void printInOrder(){
        printInOrder(root);
    }

    private void printInOrder(Node node) {
        if (node == null) { return; }
        if (node.left == null) {
            System.out.println(node.key + ":" +  node.value);
            printInOrder(node.right);
        }
        if (node.right == null) {
            printInOrder(node.left);
            System.out.println(node.key + ":" +  node.value);
        }
        printInOrder(node.left);
        System.out.println(node.key + ":" +  node.value);
        printInOrder(node.right);
    }


//
//    private void delete(K key) {
//        if (key == null) {return; }
//        root = delete(root, key);
//    }
//
//    private Node delete(Node node, K key) {
//        if (node == null) { return null; }
//        int cmp = node.key.compareTo(key);
//        if (cmp < 0) {node.left = delete(node.left, key); }
//        if (cmp > 0) {node.right = delete(node.right, key); }
//        if (cmp == 0) {
//            if (node.right == null) { return node.left; }
//            if (node.left == null) { return node.right; }
//
//        }
//}


    /* Returns a Set view of the keys contained in this map. */
    @Override
    public Set<K> keySet() {
        throw new RuntimeException("UnsupportedOperationException");
    }

    /* Removes the mapping for the specified key from this map if present.
     * Not required for Lab 8. If you don't implement this, throw an
     * UnsupportedOperationException. */
    @Override
    public V remove(K key) {
        throw new RuntimeException("UnsupportedOperationException");
    }

    /* Removes the entry for the specified key only if it is currently mapped to
     * the specified value. Not required for Lab 8. If you don't implement this,
     * throw an UnsupportedOperationException.*/
    @Override
    public V remove(K key, V value) {
        throw new RuntimeException("UnsupportedOperationException");
    }

    @Override
    public Iterator<K> iterator() {
        return new BSTIterator();
    }

    private class BSTIterator implements Iterator<K> {

        private Node ptr;

        public BSTIterator() {
            Node ptr = root;
        }

        @Override
        public boolean hasNext() {
            throw new RuntimeException("UnsupportedOperationException");
        }

        @Override
        public K next() {
            throw new RuntimeException("UnsupportedOperationException");
        }
    }

}
