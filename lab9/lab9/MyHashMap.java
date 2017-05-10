package lab9;

import java.util.*;

/**
 * Created by Administrator on 2017/5/6.
 */
public class MyHashMap<K extends Comparator<K>, V> implements Map61B<K, V> {

    private static final int INIT_CAPACITY = 4;
    private int n;
    private int m;
    private LinkedListNodes<K,V>[] hashTable;
    private int load_factor = 2;

    private class LinkedListNodes<K, V> {

        private Node sentinal;

        private class Node {
            K key;
            V val;
            Node next;

            public Node(K Key, V Value, Node Next) {
                key = Key;
                val = Value;
                next = Next;
            }
        }

        public void put(K Key, V Value) {
            for (Node x = sentinal; x != null; x = x.next) {
                if (Key.equals(x.key)) {
                    x.val = Value;
                }
            }
            sentinal = new Node(Key, Value, sentinal);
        }

        public V get(K Key) {
            for (Node x = sentinal; x != null; x = x.next) {
                if (Key.equals(x.key)) {
                    return x.val;
                }
            }
            return null;
        }

        // remove the first node
        public V remove() {
            if (sentinal != null) {
                V value = sentinal.next.val;
                sentinal = sentinal.next;
                return value;
            }
            else return null;
        }

        public Iterable<K> keys() {
            Queue<K> keys = new ArrayDeque<>();
            for (Node x = sentinal; x != null; x = x.next) {
                keys.add(x.key);
            }
            return keys;
        }

        public boolean containsKey(K key) {
            for (Node x = sentinal; x != null; x = x.next) {
                if (key == x.key) {
                    return true;
                }
            }
            return false;
        }
    }
    public MyHashMap() {
        this(INIT_CAPACITY);
    }

    public MyHashMap(int initialSize) {
        m = initialSize;
        hashTable =  new LinkedListNodes[initialSize];
    }

    public MyHashMap(int initialSize, double loadFactor) {
        m = initialSize;
        hashTable = new LinkedListNodes[initialSize];
        resize(loadFactor);
    }

    private void resize(double lf) {
        int new_m = (int)((double)m * lf);
        LinkedListNodes[] hashTable_copy = new LinkedListNodes[new_m];
        for (int i = 0; i < m; i += 1) {
            for (K key : hashTable[i].keys()) {
                int index = (hash(key) % new_m);
                hashTable_copy[index].put(key, hashTable[i].get(key));
            }
        }
        hashTable = hashTable_copy;
        m = new_m;
    }

    private int hash(K key) {
        return (key.hashCode() & 0x7fffffff) % m;
    }

    /** Removes all of the mappings from this map. */
    public void clear() {
        hashTable = new LinkedListNodes[m];
        n = 0;
        m = 0;
    }

    @Override
    /* Returns true if this map contains a mapping for the specified key. */
    public boolean containsKey(K key) {
        for (int i = 0; i < m; i += 1) {
            if (hashTable[i].containsKey(key)) {
                return true;
            }
        }
        return false;
    }

    @Override
    /* Returns the value to which the specified key is mapped, or null if this
     * map contains no mapping for the key.
     */
    public V get(K key) {
        for (int i = 0; i < m; i += 1) {
            V res = hashTable[i].get(key);
            if (res!= null) {
                return res;
            } else { }
        }
        return null;
    }

    /* Returns the number of key-value mappings in this map. */
    public int size() {
        return n;
    }

    @Override
    /* Associates the specified value with the specified key in this map. */
    public void put(K key, V value) {
        int hashCode = hash(key);
        int index = hashCode % m;
        hashTable[index].put(key, value);
    }

    @Override
    /* Returns a Set view of the keys contained in this map. */
    public Set<K> keySet() {
        Set<K> keys_sets = new HashSet<K>();
        for (int i = 0; i < m; i += 1) {
            for (K key : hashTable[i].keys()) {
                keys_sets.add(key);
            }
        }
        return keys_sets;
    }

    @Override
    public Iterator iterator() {
        return new HTIter();
    }

    private class HTIter implements Iterator<K> {
        private K iter_ptr;
        private int i;
        ArrayList<K> keys = new ArrayList<K>();

        public HTIter() {
            i = 0;
            ArrayList<K> keys = new ArrayList<K>();
            keys.addAll(keySet());
            iter_ptr = keys.get(i);
        }

        @Override
        public boolean hasNext() {
            return iter_ptr != null;
        }

        @Override
        public K next() {
            i += 1;
            iter_ptr = keys.get(i);
            return iter_ptr;
        }
    }

    @Override
    /* Removes the mapping for the specified key from this map if present.
     * Not required for Lab 8. If you don't implement this, throw an
     * UnsupportedOperationException. */
    public V remove(K key) {
        throw new UnsupportedOperationException();
    }

    @Override
    /* Removes the entry for the specified key only if it is currently mapped to
     * the specified value. Not required for Lab 8. If you don't implement this,
     * throw an UnsupportedOperationException.*/
    public V remove(K key, V value) {
        throw new UnsupportedOperationException();
    }
}
