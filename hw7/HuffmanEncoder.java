/**
 * Created by Xiao Shi on 2017/8/3.
 */

import edu.princeton.cs.algs4.*;

import java.util.HashMap;
import java.util.Map;

public class HuffmanEncoder {
    public static Map<Character, Integer> buildFrequencyTable(char[] inputSymbols) {
        int size = inputSymbols.length;
        Map<Character, Integer> mp = new HashMap<>();
        for (int i = 0; i < size; i += 1) {
            char input = inputSymbols[i];
            if (mp.containsKey(input)) {
                int freq = mp.get(input);
                mp.put(input, freq + 1);
            } else {
                mp.put(input, 1);
            }
        }
        return mp;
    }


    public static void main(String[] args) {
        String filename = args[0];
        char[] inputsSymbol = FileUtils.readFile(filename);
        Map<Character, Integer> freqT = buildFrequencyTable(inputsSymbol);
        
    }
}
