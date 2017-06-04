/**
 * Created by Xiao Shi on 2017/6/2.
 */
import edu.princeton.cs.algs4.Picture;
import java.awt.Color;
import edu.princeton.cs.algs4.MinPQ;

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
//        int[] seam = new int[height()];
        int[] edgeTo = new int[height()];
//        MinPQ<Double> minpq = new MinPQ<>();
        double[][] accumulateE = new double[width()][height()];
        int minX = 0;
        for (int i = 0; i < width() - 1; i += 1) {
            accumulateE[i][0] = energy(i, 0);
            for (int j = 1; j < height()-1; j += 1 ) {
                accumulateE[i][j] = energy(i, j) + minEnergy(i, j, width() - 1, edgeTo);
            }
            if (accumulateE[i][height()-1] < accumulateE[minX][height()-1]) {
                minX = i;
            }
        }
    }

    private int[] adjcent(int change, int fix, int max) {
        int[] adj = new int[3];
        if (change == 0) { adj[0] = max; adj[1] = change; adj[2] = change+1;}
        else if(change == max) { adj[0] = change - 1; adj[1] = change; adj[2] = 0;}
        else { adj[0] = change - 1; adj[1] = change; adj[2] = change+1; }
        return adj;
    }

    private double minEnergy(int x, int y, int max_x) {
        if (x == 0) {
            return Math.min(Math.min(energy(max_x, y - 1), energy(x, y - 1)), energy(x + 1, y - 1));
        } else if (x == max_x) {
            return Math.min(Math.min(energy(x - 1, y - 1), energy(x, y - 1)), energy(0, y - 1));
        } else {
            return Math.min(Math.min(energy(x - 1, y - 1), energy(x, y - 1)), energy(x + 1, y - 1));
        }
    }

    private double nextMinEnergyEndTo(int x, int y, int prev_min) {
        return accumulateEnergy(x,y) + prev_min;
    }

    public   int[] findVerticalSeam()              // sequence of indices for vertical seam
    public    void removeHorizontalSeam(int[] seam)   // remove horizontal seam from picture
    public    void removeVerticalSeam(int[] seam)
}
