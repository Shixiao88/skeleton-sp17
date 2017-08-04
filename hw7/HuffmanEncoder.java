/**
 * Created by Xiao Shi on 2017/8/3.
 */

import edu.princeton.cs.algs4.*;
import oracle.jrockit.jfr.events.Bits;

import java.util.ArrayList;
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
        //1: Read the file as 8 bit symbols.
        String filename = args[0];
        char[] inputsSymbol = FileUtils.readFile(filename);
        //2: Build frequency table.
        Map<Character, Integer> freqT = buildFrequencyTable(inputsSymbol);
        //3: Use frequency table to construct a binary decoding trie.
        BinaryTrie bt = new BinaryTrie(freqT);
        //4: Write the binary decoding trie to the .huf file.
        ObjectWriter ow = new ObjectWriter(filename + ".huf");
        ow.writeObject(bt);
        //5: (optional: write the number of symbols to the .huf file)
        int symNum = inputsSymbol.length;
        ow.writeObject(symNum);
        //6: Use binary trie to create lookup table for encoding.
        Map<Character, BitSequence> lookUpTb = bt.buildLookupTable();
        //7: Create a list of bitsequences.
        ArrayList<BitSequence> bsArry = new ArrayList<>();
        //8: For each 8 bit symbol:
        //      Lookup that symbol in the lookup table.
        //      Add the appropriate bit sequence to the list of bitsequences.
        for (int i = 0; i < symNum; i += 1) {
            bsArry.add(lookUpTb.get(inputsSymbol[i]));
        }
        //9: Assemble all bit sequences into one huge bit sequence.
        BitSequence allBits = BitSequence.assemble(bsArry);
        //10: Write the huge bit sequence to the .huf file.
        ow.writeObject(allBits);
    }
}
