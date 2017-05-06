package hw2;                       
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {

    private double[] lst_num_open_sites;

    // perform T independent experiments on an N-by-N grid
    public PercolationStats(int N, int T) {
        lst_num_open_sites = new double[T];
        for (int time = 0; time <= T; time += 1) {
            Percolation pcu = new Percolation(N);
            while (!pcu.percolates()) {
                int random_row = StdRandom.uniform(N + 1);
                int random_col = StdRandom.uniform(N + 1);
                pcu.open(random_row, random_col);
            }
            int num_open_sites = pcu.numberOfOpenSites();
            lst_num_open_sites[time] = (num_open_sites);
        }
    }

    // sample mean of percolation threshold
    public double mean() {
        return StdStats.mean(lst_num_open_sites);
    }

    // sample standard deviation of percolation threshold
    public double stddev() {
        return StdStats.stddev(lst_num_open_sites);
    }

    // low  endpoint of 95% confidence interval
    public double confidenceLow() {
        double mean = mean();
        double stddev = stddev();
        double sample_times = lst_num_open_sites.length;
        return (mean - (1.96 * stddev) / Math.pow(sample_times, 0.5));
    }

    // high endpoint of 95% confidence interval
    public double confidenceHigh() {
        double mean = mean();
        double stddev = stddev();
        double sample_times = lst_num_open_sites.length;
        return(mean +(1.96*stddev)/Math.pow(sample_times,0.5));
    }
}                       
