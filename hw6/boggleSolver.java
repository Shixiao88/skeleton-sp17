/**
 * Created by Xiao Shi on 2017/6/5.
 */
import edu.princeton.cs.algs4.TrieST;
import edu.princeton.cs.algs4.Bag;
import edu.princeton.cs.algs4.Stopwatch;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Queue;
import java.util.*;
import java.io.File;

/** just come up with the whole solution, not implement the top Kth
 *  not implement the command line arguments
 * */
public class boggleSolver {
    private TrieST<Integer> dictionary;
    private Character[][] board;
    private int row;
    private int col;
    int maxWordLen;

    public boggleSolver(String boardPath, String dicPath) {
        In in = new In(new File(boardPath));
        Queue<String> input = new Queue<>();
        row = 0;
        col = 0;
        while (!in.isEmpty()) {
            String i = in.readString();
            input.enqueue(i);
        }
       //System.out.println(input);
        row = input.size();
        col = input.peek().length();
        board = new Character[row][col];
        for (int r = 0; r < row; r += 1) {
            String line = input.dequeue();
            for (int c = 0; c < col; c += 1) {
                board[r][c] = line.charAt(c);
            }
        }

        dic2trie(dicPath);
    }

    private void dic2trie(String dicPath) {
        dictionary = new TrieST<>();
        In in = new In (new File(dicPath));
        while (!in.isEmpty()) {
            String i = in.readString();
            if (i.length() > maxWordLen) { maxWordLen = i.length(); }
            dictionary.put(i, 2);
        }
    }

    private int TD2OD (int r, int c) {
        return r * col + c;
    }

    private int OD2Row(int one) {
        return one / col;
    }

    private int OD2Col(int one) {
        return one % col;
    }

    private Character getCharById (int id) {
        int r = OD2Row(id);
        int c = OD2Col(id);
        return board[r][c];
    }

    private Iterable<Integer> adj (int r, int c) {
        Bag<Integer> adjs = new Bag<>();
        // upper, upperleft, upperright
        int upperrow = (r + row - 1) % row;
        int lowerrow = (r + 1) % row;
        int leftcol = (c + col - 1) % col;
        int rightcol = (c + 1) % col;
        adjs.add(TD2OD(r, leftcol));
        adjs.add(TD2OD(r, rightcol));
        adjs.add(TD2OD(upperrow, c));
        adjs.add(TD2OD(upperrow, leftcol));
        adjs.add(TD2OD(upperrow, rightcol));
        adjs.add(TD2OD(lowerrow, c));
        adjs.add(TD2OD(lowerrow, leftcol));
        adjs.add(TD2OD(lowerrow, rightcol));
        return adjs;
    }

    // core application, iterate all chars in board, seach if it can be found in dictionary
    private Iterable<String> search() {
        TreeSet<String> solutions = new TreeSet<>();
        for (int r = 0; r < row; r += 1) {
            for (int c = 0; c < col; c += 1) {
                search(r, c, board[r][c].toString(), solutions);
            }
        }
        ArrayList<String> solutionReversed = new ArrayList<>(solutions);
        Collections.sort(solutionReversed, Collections.reverseOrder());
        return solutionReversed;
    }

    private void search(int r, int c, String prefix, TreeSet<String> sol) {
        for (int id : adj(r, c)) {
            String prefixn = prefix + getCharById(id);
            if (checkPrefixIsEmpty(prefixn)) {
                continue;
            }
            if (prefixn.length() <= 3 ) {
                search(OD2Row(id), OD2Col(id), prefixn, sol);
            }
            else if (dictionary.contains(prefixn)) {
                sol.add(prefixn);
            }
            search(OD2Row(id), OD2Col(id), prefixn, sol);
        }
    }

    private boolean checkPrefixIsEmpty (String prefix) {
        Iterable<String> prefixes = dictionary.keysWithPrefix(prefix);
        int counter = 0;
        for (String s : prefixes) {
            counter ++;
        }
        return counter == 0;
    }

    @Override
    public String toString() {
        String res = "";
        for (int r = 0; r < row; r += 1) {
            for (int c = 0; c < col; c += 1) {
                res += board[r][c];
            }
            res += "\n";
        }
        return res;
    }


    public static void main(String[] args) {
        boggleSolver bs = new boggleSolver(args[0], args[1]);
        Stopwatch clock = new Stopwatch();
        Iterable<String> answer = bs.search();
        double stop = clock.elapsedTime();

        for (String s : answer) {
            System.out.println(s);
        }
        System.out.println("matrix size is: " + bs.col * bs.row);
        System.out.println("time to seach: " + stop);

    }
}
