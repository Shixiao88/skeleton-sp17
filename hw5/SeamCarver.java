/**
 * Created by Xiao Shi on 2017/7/5.
 */

import edu.princeton.cs.algs4.Picture;
import edu.princeton.cs.algs4.Bag;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.IndexMinPQ;
import java.awt.Color;

/*
Some possible optimizations include (in decreasing order of likely impact):

Avoiding recalculation of energies for the same pixel over and over
(e.g. through creation of an explicit energy matrix of type double[][]).
Essentially you want to memoize energy calculations.

Don't use a HashMap for looking up data by row and column.
Instead, use a 2D array. They are much faster.
HashMaps are constant time, but the constant factor is significant.

Not using Math.pow or Math.abs.

Not storing an explicit edgeTo data structure.
It is possible to rebuild the seam ONLY from the values for M(i, j)!
That is, you don't need to actually record the predecessor like you did in the 8puzzle assignment.

Using a more clever approach than transposing your images (though this is not required to pass the autograder).
* */

public class SeamCarver {
    private int height;
    private int width;
    private Picture p;
    private double[][] energyMatrix;
    private double[][] pixel;       // to store the first calculated Energy in the matrix
    private boolean isTrasposed;

    // create a seam carver object based on the given picture
    public SeamCarver(Picture picture) {
        width = picture.width();
        height = picture.height();
        p = new Picture(picture);
        createPixel();
        createEnergyMatrix();
        isTrasposed = false;
    }

    private void createPixel() {
        pixel = new double[height][width];
        for (int w = 0; w < width; w += 1) {
            for (int h = 0; h < height; h += 1) {
                double energySqrt = energySq(w, h);
                pixel[h][w] = energySqrt;
            }
        }
    }

    private void createEnergyMatrix () {
        energyMatrix = new double[height][width];
        for (int w = 0; w < width; w += 1) {
            energyMatrix[height -1][w] = pixel[height - 1] [w];
            for (int h = 0; h < height - 1; h += 1) {
                energyMatrix[h][w] = Double.POSITIVE_INFINITY;
            }
        }
    }

    // current picture
    public Picture picture() {
        return p;
    }

    // width of current picture
    public     int width() {
        return width;
    }

    // height of current picture
    public     int height() {
        return height;
    }

    // energy of pixel at column x and row y
    private  double energySq(int x, int y) {
        if (x < 0 || x >= width || y < 0 || y >= height) {throw new  java.lang.IndexOutOfBoundsException();}
        int lessx = (x + width - 1) % width;
        int largerx = (x + 1) % width;
        double xRSqrt = getRSqrt(lessx, y, largerx, y);
        double xGSqrt = getGSqrt(lessx, y, largerx, y);
        double xBSqrt = getBSqrt(lessx, y, largerx, y);
        int lessy = (y + height - 1) % height;
        int largery = (y + 1) % height;
        double yRSqrt = getRSqrt(x, lessy, x, largery);
        double yGSqrt = getGSqrt(x, lessy, x, largery);
        double yBSqrt = getBSqrt(x, lessy, x, largery);
        return xRSqrt + xGSqrt + xBSqrt + yGSqrt + yBSqrt + yRSqrt;
    }

    public double energy(int x, int y) {
        return Math.sqrt(energySq(x, y));
    }

    private double getRSqrt(int x1, int y1, int x2, int y2) {
        return Math.pow((p.get(x1, y1).getRed() - p.get(x2, y2).getRed()), 2);
    }

    private double getGSqrt(int x1, int y1, int x2, int y2) {
        return Math.pow((p.get(x1, y1).getGreen() - p.get(x2, y2).getGreen()), 2);
    }

    private double getBSqrt(int x1, int y1, int x2, int y2) {
        return Math.pow((p.get(x1, y1).getBlue() - p.get(x2, y2).getBlue()), 2);
    }

    private Iterable<Integer> findVerticalDaf(int w) {
        Bag<Integer> adjs = new Bag<>();
//        if (h == 0) {
//            return adjs;
//        }
        if (w == 0) {
            adjs.add(0);
            adjs.add(1);
        } else if (w == width - 1) {
            adjs.add(w - 1);
            adjs.add(w);
        } else {
            adjs.add(w + 1);
            adjs.add(w);
            adjs.add(w - 1);
        }
        return adjs;
    }

    private void transpose() {
        if (isTrasposed) {
            return;
        }
        isTrasposed = true;
        int t = height;
        height = width;
        width = t;

        pixel = rotate(pixel);
        createEnergyMatrix();
        isTrasposed = true;
    }

    private double[][] rotate(double[][] matrix) {
        int row = matrix.length;
        int col = matrix[0].length;
        double[][] after = new double[col][row];
        for (int r = 0; r < row; r += 1) {
            for (int c = 0; c < col; c += 1) {
                after[c][r] = matrix[r][col - 1 - c];
            }
        }
        return after;
    }

    private void transposeBack() {
        if (!isTrasposed) {return;}
        int t = height;
        height = width;
        width = t;
        pixel = rotateBack(pixel);
        createEnergyMatrix();
        isTrasposed = false;
    }

    private double[][] rotateBack (double[][] matrix) {
        int row = matrix.length;
        int col = matrix[0].length;
        double[][] after = new double[col][row];
        for (int r = 0; r < row; r += 1) {
            for (int c = 0; c < col; c += 1) {
                after[c][r] = matrix[row - 1 - r][c];
            }
        }
        return after;
    }

    // sequence of indices for horizontal seam
    public   int[] findHorizontalSeam() {
        transpose();
        int[] res = findVerticalSeam();
        int[] seam = new int[height];
        for (int i = 0; i < height; i += 1){
            seam[i] = res[height - 1 - i];
        }
        transposeBack();
        return seam;
    }

    private void relax(int w, int h, int frmw, int frmh) {
        if (h < 0) {return;}
        if (energyMatrix[h][w]> energyMatrix[frmh][frmw] + pixel[h][w]) {
            //t.edgeTo = frmt;
            energyMatrix[h][w] = energyMatrix[frmh][frmw] + pixel[h][w];
        }
    }

    private int xy2OneD(int w, int h) {
        return w + h * width;
    }

    // sequence of indices for vertical seam
    public   int[] findVerticalSeam() {
        Queue<Integer> topoQueue = new Queue<>();
        IndexMinPQ<Double> pq1stLine = new IndexMinPQ<>(width);
        for (int h = height - 1; h >= 0; h -= 1) {
            for (int w = 0; w < width; w += 1) {
                topoQueue.enqueue(w);
            }
        }
        while (!topoQueue.isEmpty()) {
            //Tile next = topoQueue.dequeue();
            Integer frmw = topoQueue.dequeue();
            Integer frmh = topoQueue.size() / width;
            for (Integer w : findVerticalDaf(frmw)) {
                relax(w, frmh - 1, frmw, frmh);
            }
        }

        int[] seam = new int[height];
        for (int w = 0; w < width; w += 1) {
            pq1stLine.insert(w, energyMatrix[0][w]);
        }
        int w  = pq1stLine.delMin();
        seam[0] = w;
        for (int h = 1; h < height; h += 1) {
            int currentw = seam[h - 1];
            int wContinue = -1;
            double nextEnergy = Double.POSITIVE_INFINITY;
            for (int wadj : findVerticalDaf(currentw)) {
                if (energyMatrix[h][wadj] < nextEnergy) {
                    wContinue = wadj;
                    nextEnergy = energyMatrix[h][wadj];
                }
            }
            seam[h] = wContinue;
        }
        return seam;
    }

    // remove horizontal seam from current picture
    public    void removeHorizontalSeam(int[] seam) {
        p = SeamRemover.removeHorizontalSeam(p, seam);
        pixel = rotate(pixel);
        for (int i = 0; i < width; i += 1) {
            removeFromArray(seam[i], pixel[i]);
            //removeFromArray (seam[i], pixel[i]);
        }
        pixel = rotateBack(pixel);
        height -= 1;
        createEnergyMatrix();
    }

    // remove vertical seam from current picture
    public    void removeVerticalSeam(int[] seam) {
        p = SeamRemover.removeVerticalSeam(p, seam);
        for (int i = 0; i < height; i += 1) {
            removeFromArray(seam[i], pixel[i]);
            //removeFromArray (seam[i], pixel[i]);
        }
        width -= 1;
        createEnergyMatrix();
    }


    private void removeFromArray (int id, double[] arry) {
        int n = arry.length;
        for (int i = id; i < n - 1 ; i += 1) {
            arry[i] = arry[i + 1];
        }
    }

    // do unit testing of this class
    public static void main(String[] args) {}


}
