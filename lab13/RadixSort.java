/**
 * Class for doing Radix sort
 *
 * @author Akhil Batra
 * @version 1.4 - April 14, 2016
 *
 **/
public class RadixSort {

    /**
     * Does Radix sort on the passed in array with the following restrictions:
     * The array can only have ASCII Strings (sequence of 1 byte characters)
     * The sorting is stable and non-destructive
     * The Strings can be variable length (all Strings are not constrained to 1 length)
     *
     * @param asciis String[] that needs to be sorted
     * @return String[] the sorted array
     **/
    public static String[] sort(String[] asciis) {
        //to find the max length
        int max = Integer.MIN_VALUE;
        int R = 256;
        int N = asciis.length;
        for (int i = 0; i < asciis.length; i += 1) {
            if (max < asciis[i].length()) {
                max = asciis[i].length();
            }
        }

        int[] count = new int[R + 1];
        String[] sorted = new String[N];
        for (int d = max - 1; d >= 0; d -= 1) {

            // create count array
            for (int i = 0; i < asciis.length; i += 1) {
                count[asciis[i].charAt(d)] += 1;
            }
            // change count array to indices
            for (int i = 0; i < count.length; i += 1) {
                count[i + 1] += count[i];
            }

            // sort the array based on dth digit and count array
            for (int i = 0; i < asciis.length; i += 1) {
                sorted[count[asciis[i].charAt(d)]] = asciis[i];
                count[count[asciis[i].charAt(d)]] += 1;
            }
        }
        return sorted;
    }

    /**
     * Radix sort helper function that recursively calls itself to achieve the sorted array
     * destructive method that changes the passed in array, asciis
     *
     * @param asciis String[] to be sorted
     * @param start  int for where to start sorting in this method (includes String at start)
     * @param end    int for where to end sorting in this method (does not include String at end)
     * @param index  the index of the character the method is currently sorting on
     **/
    private static void sortHelper(String[] asciis, int start, int end, int index) {
        return;
    }
}
