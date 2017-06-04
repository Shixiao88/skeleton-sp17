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
        int[] seam = new int[height()];
        int[] edgeTo = new int[height()];
        MinPQ<Picture> minpq = new MinPQ<>();
        for (int j = 0; j < height()-1; j += 1 ) {
            double accumulated = 0;
            for (int i = 0; i < width() - 1; i += 1) {
                accumulated = accumulateEnergy(i, j, accumulated);
            }

        }
    }

    private int[] adjcent(int change, int fix, int max) {
        int[] adj = new int[3];
        if (change == 0) { adj[0] = max; adj[1] = change; adj[2] = change+1;}
        else if(change == max) { adj[0] = change - 1; adj[1] = change; adj[2] = 0;}
        else { adj[0] = change - 1; adj[1] = change adj[2] = change+1; }
        return adj;
    }

    private double accumulateEnergy(int x, int y, double prev) {
        return energy(x, y) + prev;
    }

    private double nextMinEnergyEndTo(int x, int y, int prev_min) {
        return accumulateEnergy(x,y) + prev_min;
    }

    public   int[] findVerticalSeam()              // sequence of indices for vertical seam
    public    void removeHorizontalSeam(int[] seam)   // remove horizontal seam from picture
    public    void removeVerticalSeam(int[] seam)
}
