/**
 * Created by Xiao Shi on 2017/8/3.
 */

import edu.princeton.cs.algs4.*;
import edu.princeton.cs.algs4.BinaryStdOut;
import edu.princeton.cs.algs4.BinaryStdIn;
import java.util.TreeMap;
import java.util.Map;
import java.io.Serializable;
import java.io.IOException;

public class BinaryTrie implements Serializable{
    private Node root;

    public BinaryTrie(Map<Character, Integer> frequencyTable) {
        MinPQ<Node> pq = new MinPQ<>();
        for (Map.Entry<Character, Integer> entry : frequencyTable.entrySet()) {
            Node node = new Node(entry.getKey(), entry.getValue(), null, null);
            pq.insert(node);
        }
        while (pq.size() > 1) {
            Node left = pq.delMin();
            Node right = pq.delMin();
            Node parent = new Node('\0', left.freq + right.freq, left, right);
            pq.insert(parent);
        }
        root = pq.delMin();
    }

    private static class Node implements Comparable<Node>, Serializable {
        private final char ch;
        private final int freq;
        private final Node left, right;

        Node(char ch, int freq, Node l, Node r) {
            this.ch = ch;
            this.freq = freq;
            left = l;
            right = r;
        }

        private boolean isLeaf() {
            return (left == null) && (right == null);
        }

        public int compareTo (Node that) {
            return this.freq - that.freq;
        }
    }

    public Match longestPrefixMatch(BitSequence querySequence) {
        return longestPrefixMatch(root, querySequence, 0, -1, '\0');
    }

    private Match longestPrefixMatch(Node n, BitSequence bs, int d, int length, char character) {
        if (n == null) return new Match(bs.firstNBits(length), character);
        if (n.ch != '\0') {
            length = d;
            character = n.ch;
        }
        if (d == bs.length()) return new Match(bs.firstNBits(length), character);
        int bit = bs.bitAt(d);
        if (bit == 1) {
            return longestPrefixMatch(n.right, bs, d + 1, length, character);
        } else if (bit == 0) {
            return longestPrefixMatch(n.left, bs, d + 1, length, character);
        } else throw new RuntimeException();
    }

    public Map<Character, BitSequence> buildLookupTable(){
        Map<Character, BitSequence> mp = new TreeMap<>();
        buildLookupTable(root, mp, new BitSequence());
        return mp;
    }

    private void buildLookupTable(Node n, Map<Character, BitSequence> mp, BitSequence bs) {
        if (n == null) return;
        if (n.ch != '\0') mp.put(n.ch, bs);
        buildLookupTable(n.left, mp, bs.appended(0));
        buildLookupTable(n.right, mp, bs.appended(1));
    }

    /*
    private void writeObject(java.io.ObjectOutputStream out)
            throws IOException {
        writeTrie(root);
    }

    private void writeTrie (Node n) {
        if (n.isLeaf()) {
            BinaryStdOut.write(true);
            BinaryStdOut.write(n.ch, 8);
            return;
        }
        BinaryStdOut.write(false);
        writeTrie(n.left);
        writeTrie(n.right);
    }

    private void readObject(java.io.ObjectInputStream in)
            throws IOException, ClassNotFoundException {
        root = readTrie();
    }

    private Node readTrie() {
        boolean isLeaf = BinaryStdIn.readBoolean();
        if (isLeaf) {
            return new Node(BinaryStdIn.readChar(), -1, null, null);
        } else {
            return new Node('\0', -1, readTrie(), readTrie());
        }
    }*/

}
