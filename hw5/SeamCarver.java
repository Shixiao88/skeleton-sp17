/**
 * Created by Xiao Shi on 2017/6/2.
 */
import edu.princeton.cs.algs4.Picture;
import java.awt.Color;
import java.util.Comparator;
import java.util.Arrays;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.Stack;

public class SeamCarver {
    private Picture pic;

    public SeamCarver(Picture picture) {
        pic = picture;
    }

    /* current picture */
    public Picture picture() {
        return pic;
    }

    /* width of current picture */
    public     int width() {
        return pic.width();
    }

    /* height of current picture*/
    public     int height() {return pic.width();}

    /* energy of pixel at column x and row y */
    public  double energy(int x, int y) {
        double energy_col_sqrt = energyHelper(x, y, width()-1);
        double energy_row_sqrt = energyHelper(y, x, height()-1);
        return energy_col_sqrt + energy_row_sqrt;
    }

    private double energyHelper(int change, int fix, int max_change) {
        int diff_red, diff_green, diff_blue;
        int next, prev;
        if (change == 0) {
            prev = max_change;
            next = change + 1;
        } else if(change == max_change) {
            prev = change - 1;
            next = 0;
        } else {
            prev = change - 1;
            next = change + 1;
        }
        diff_red = pic.get(next, fix).getRed() - pic.get(prev, fix).getRed();
        diff_green = pic.get(next, fix).getGreen() - pic.get(prev, fix).getGreen();
        diff_blue = pic.get(next, fix).getBlue() - pic.get(prev, fix).getBlue();
        return Math.pow(diff_red, 2) + Math.pow(diff_green, 2) + Math.pow(diff_blue, 2);
    }

    /* sequence of indices for horizontal seam*/
    public   int[] findHorizontalSeam() {
        Queue<Node> q = new Queue<>();
        int minX = 0;
        for (int i = 0; i < width() - 1; i += 1) {
            Node nd = new Node(i, 0);
            for (int j = 1; j < height()-1; j += 1 ) {
                Node min_adj = min(nd.adjacent());
                nd = min_adj;
            }
            q.enqueue(nd);
        }
        Node minaccu = minAccumulate(q);
        Stack<Node> stk = new Stack<>();
        while (minaccu.parent != null) {
            stk.push(minaccu);
            minaccu = minaccu.parent;
        }
        int[] horizontal = new int[height()];
        for (int i = 0; i < horizontal.length; i += 1) {
            horizontal[i] = stk.pop().x;
        }
        return horizontal;
    }

    private int[] adjcent(int change, int fix, int max) {
        int[] adj = new int[3];
        if (change == 0) { adj[0] = max; adj[1] = change; adj[2] = change+1;}
        else if(change == max) { adj[0] = change - 1; adj[1] = change; adj[2] = 0;}
        else { adj[0] = change - 1; adj[1] = change; adj[2] = change+1; }
        return adj;
    }

    Node min(Node[] node_lst) {
        Arrays.sort(node_lst);
        return node_lst[node_lst.length-1];
    }

    Node minAccumulate(Queue<Node> q) {
        Node nd = q.dequeue();
        while (q.size() != 0) {
            Node comparator = q.dequeue();
            if (comparator.accumulate_energy < nd.accumulate_energy) {
                nd = comparator;
            }
        }
        return nd;
    }

    /* sequence of indices for vertical seam*/
    public   int[] findVerticalSeam() {return new int[1];}

    /* remove horizontal seam from picture*/
    public    void removeHorizontalSeam(int[] seam) {}

    public    void removeVerticalSeam(int[] seam) {}

    private class Node implements Comparable<Node>{
        private int x;
        private int y;
        private double energy;
        private double accumulate_energy;
        private Node parent;

        Node(int x, int y){
            this.x = x;
            this.y = y;
            this.energy = energy(x, y);
            this.accumulate_energy = this.energy;
        }

        Node (int x, int y, Node parent) {
            this.x = x;
            this.y = y;
            this.energy = energy(x, y);
            this.parent = parent;
            this.accumulate_energy = parent.accumulate_energy + this.energy;
        }

        double getEnergy() {return this.energy;}

        Node[] adjacent() {
            Node[] res = new Node[3];
            int[] res_x = adjcent(x, y, width()-1);
            if (y == height() - 1) { return res;}
            else {
                Node n1 = new Node(res_x[0], y + 1, this);
                Node n2 = new Node(res_x[1], y + 1, this);
                Node n3 = new Node(res_x[2], y + 1, this);
                res[0] = n1;
                res[1] = n2;
                res[2] = n3;
                return res;
            }
        }

        @Override
        public int compareTo(Node other) {
            return ((Double)getEnergy()).compareTo(other.getEnergy());
        }

    }
}
