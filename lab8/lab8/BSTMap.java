package lab8;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by Administrator on 2017/5/4.
 */
public class BSTMap<K extends Comparable<K>, V> implements Map61B<K, V> {
    private Node root;

    private class Node {
        private K Key;
        private V Value;
        private int size;
        private Node left;
        private Node right;

        public Node(K k, V v, int s) {
            Key = k;
            Value = v;
            size = s;
        }

    }

    public BSTMap() {}

    /** Removes all of the mappings from this map. */
    @Override
    public void clear() {
        root = null;
    }

    /* Returns true if this map contains a mapping for the specified key. */
    @Override
    public boolean containsKey(K key) {
        return get(key) != null;
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
        int cmp = key.compareTo(node.Key);
        if (cmp < 0) {return get(node.left, key); }
        else if (cmp > 0) {return get(node.right, key); }
        else { return node.Value; }
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
        int cmp = key.compareTo(node.Key);
        if (cmp < 0) {node.left = put(node.left, key, value); }
        else if (cmp > 0) {node.right = put(node.right, key, value); }
        else {node.Value = value; }
        node.size = size(node.left) + size(node.right) + 1;
        return node;
    }

    public void printInOrder(){
        printInOrder(root);
    }

    private void printInOrder(Node node) {
        if (node == null) { return; }
        if (node.left == null) {
            System.out.println(node.Key + ":" +  node.Value);
            printInOrder(node.right);
        }
        if (node.right == null) {
            printInOrder(node.left);
            System.out.println(node.Key + ":" +  node.Value);
        }
        printInOrder(node.left);
        System.out.println(node.Key + ":" +  node.Value);
        printInOrder(node.right);
    }


    /* Returns a Set view of the keys contained in this map. */
    @Override
    public Set<K> keySet() {
        Set<K> key_set = new HashSet<K>();
        return keySet(root, key_set);
    }

    private Set<K> keySet(Node node, Set set_to_add) {
        if (node == null) { return set_to_add; }
        set_to_add.add(node.Key);
        set_to_add = (keySet(node.right, set_to_add));
        set_to_add = keySet(node.left, set_to_add);
        return set_to_add;
    }

    /* Removes the mapping for the specified key from this map if present.
     * Not required for Lab 8. If you don't implement this, throw an
     * UnsupportedOperationException. */
    @Override
    public V remove(K key) {
        throw new RuntimeException("UnsupportedOperationException"); }

    /* Removes the entry for the specified key only if it is currently mapped to
     * the specified value. Not required for Lab 8. If you don't implement this,
     * throw an UnsupportedOperationException.*/
    @Override
    public V remove(K key, V value) {
        throw new RuntimeException("UnsupportedOperationException");
    }

    public Node min() {
        return min(root);
    }

    private Node min(Node node) {
        Node minimum;
        if (node == null ) { return null; }
        if (node.left == null) { minimum = node; }
        minimum = min(node.left);
        return minimum;
    }

    public Node max() {
        return max(root);
    }

    private Node max(Node node) {
        Node maximum;
        if (node == null ) { return null; }
        if (node.right == null) { maximum = node; }
        maximum = min(node.left);
        return maximum;
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
            return ptr.right != null;
        }

        @Override
        public K next() {
            return findNextLarge(ptr.Key);
        }
    }

    public K findNextLarge(K key) {
        return findNextLarge(root, key);
    }

    private K findNextLarge(Node node, K key) {
        if (node == null) {
            System.out.println("the tree is empty");
        } else if (key == null) {
            throw new RuntimeException("must enter a valid key");
        }
        int cmp = key.compareTo(node.Key);
        if (cmp < 0) {
            return findNextLarge(node.left, key);
        } else if (cmp > 0) {
            return findNextLarge(node.right, key);
        } else {
            if (node.right == null) {
                return null;
            } else {
                return node.right.Key;
            }
        }
    }

}
