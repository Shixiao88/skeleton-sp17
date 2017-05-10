package hw2;                       

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {

    private Boolean[][] field;
    private int virtualTop, virtualBottom;
    private int counter;
    private int size;
    private WeightedQuickUnionUF uf;

    // create N-by-N grid, with all sites initially blocked
    public Percolation(int N) {
        size = N;
        virtualTop = N;
        virtualBottom = N + 1;
        if (N <= 0) { throw new IllegalArgumentException(); }
            field = new Boolean[N][N];
        for (int row = 0; row < N; row += 1) {
            for (int col = 0; col < N; col += 1) {
                field[row][col] = false;
            }
        }
        uf = new WeightedQuickUnionUF(N*N + 2);
        for (int top_bottom = 0; top_bottom < N; top_bottom += 1) {
            int tops = XYto1D(0, top_bottom);
            uf.union(virtualTop, tops);
            int bottoms = XYto1D(N-1, top_bottom);
            uf.union(virtualBottom, bottoms);
        }
    }

    private int XYto1D(int x, int y) {
        return (x * size + y);
    }

    // open the site (row, col) if it is not open already
    public void open(int row, int col) {
        field[row][col] = true;
        counter += 1;
        int number_1D = XYto1D(row, col);
        try {
            if (isOpen(row-1,col)) {
                uf.union(number_1D, number_1D-size);
            } }catch (IndexOutOfBoundsException e) { }
        try {
            if (isOpen(row+1,col)) {
                uf.union(number_1D, number_1D+size);
            } }catch (IndexOutOfBoundsException e) { }
        try {
            if (isOpen(row,col-1)) {
                uf.union(number_1D, number_1D-1);
            } }catch (IndexOutOfBoundsException e) { }
        try {
            if (isOpen(row,col+1)) {
                uf.union(number_1D, number_1D+1);
            } }catch (IndexOutOfBoundsException e) { }
    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col) {
        return field[row][col] == true;
    }

    // is the site (row, col) full?
    public boolean isFull(int row, int col) {
        return  uf.connected(virtualTop, XYto1D(row, col)) && isOpen(row, col);
    }

    // number of open sites
    public int numberOfOpenSites() {
        return counter;
    }

    // does the system percolate?
    public boolean percolates() {
        return uf.connected(virtualTop, virtualBottom);
    }

    // unit testing (not required)
    public static void main(String[] args) {
        Percolation pc = new Percolation(5);
        pc.open(3,4);
        boolean testTrue = true;
        boolean testFalse = false;
        if (testTrue != pc.isOpen(3,4)) {
            System.out.println ("fail: ISOPEN(3,4), should be TRUE, but got FALSE");
        }
        if (testFalse != pc.isFull(3,4)) {
            System.out.println ("fail: ISFULL(3,4), should be FALSE, but got TRUE");
        }
        pc.open(2,4);
        pc.open(2,2);
        pc.open(2,3);
        pc.open(0,2);
        if (testFalse != pc.isFull(0,1)) {
            System.out.println ("fail: ISFULL(0, 1), should be FALSE, but got TRUE");
        }
        if (testTrue != pc.isFull(0,2)) {
            System.out.println ("fail: ISFULL(0, 2), should be TRUE, but got FALSE");
        }
        pc.open(1,2);
        if (testTrue != pc.isFull(3, 4)) {
            System.out.println ("fail: ISFULL(3,4), should be TRUE, but got FALSE");
        }
        if (testFalse != pc.percolates()) {
            System.out.println ("fail: PERCOLATES(), should be FALSE, but got TRUE");
        }
        if (6 != pc.numberOfOpenSites()) {
            System.out.println ("fail: NUMBEROFOPENSITES(), should be 6, but got "
                    + Integer.toString(pc.numberOfOpenSites()));
        }
        pc.open(4,4);
        if (testTrue != pc.percolates()) {
            System.out.println ("fail: PERCOLATES(), should be TRUE, but got FALSE");
        }
    }

}                       
