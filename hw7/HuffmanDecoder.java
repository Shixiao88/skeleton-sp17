import java.util.ArrayList;

/**
 * Created by Xiao Shi on 2017/8/4.
 */
public class HuffmanDecoder {
    public static void main(String[] args) {
        //1: Read the Huffman coding trie.
        ObjectReader or = new ObjectReader(args[0]);
        Object readin = or.readObject();
        BinaryTrie bt = (BinaryTrie) readin;
        //2: If applicable, read the number of symbols.
        Object readSymNum = or.readObject();
        int symNum = (Integer) readSymNum;
        //3: Read the massive bit sequence corresponding to the original txt.
        Object readBsArry = or.readObject();
        BitSequence bss = (BitSequence) readBsArry;
        //4: Repeat until there are no more symbols:
        //      4a: Perform a longest prefix match on the massive sequence.
        //      4b: Record the symbol in some data structure.
        //      4c: Create a new bit sequence containing the remaining unmatched bits.
        char[] chs = new char[symNum];
        int j = 0;
        while (j < symNum) {
            Match m = bt.longestPrefixMatch(bss);
            chs[j] = m.getSymbol();
            int len = m.getSequence().length();
            bss = bss.allButFirstNBits(len);
            j += 1;
        }
        //5: Write the symbols in some data structure to the specified file.
        FileUtils.writeCharArray(args[1], chs);
    }
}
