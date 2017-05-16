package hw4.hash;

import java.util.List;

public class OomageTestUtility {
    public static boolean haveNiceHashCodeSpread(List<Oomage> oomages, int M) {
        /*
         * have hashCodes that would distribute them fairly evenly across
         * M buckets. To do this, convert each oomage's hashcode in the
         * same way as in the visualizer, i.e. (& 0x7FFFFFF) % M.
         * and ensure that no bucket has fewer than N / 50
         * Oomages and no bucket has more than N / 2.5 Oomages.
         */
        int[] numInBucket = new int[M];
        for (Oomage ooA : oomages) {
            int bucketNumber = (ooA.hashCode() & 0x7FFFFFF) % M;
            numInBucket[bucketNumber] += 1;
        }
        int N = oomages.size();
        for (int i = 0; i < M; i += 1) {
            if (numInBucket[i] < N/50 || numInBucket[i] > N/2.5) {
                return false;
            }
        }
        return true;
    }
}
