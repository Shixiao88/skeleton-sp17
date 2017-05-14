package hw3.puzzle;
import edu.princeton.cs.algs4.MaxPQ;
import edu.princeton.cs.algs4.Queue;


public class Board implements WorldState {
    private int[][] board;
    private int[][] goal_tiles;
    private Board goal;

    public Board (int[][] tiles) {
        int n = tiles.length;
        board = new int[n][n];
        for (int i = 0; i < n; i += 1) {
            for (int j = 0; j < n; j += 1) {
                board[i][j] = tiles[i][j];
            }
        }
        goal_tiles = new int[n][n];
        for (int i = 0; i < n; i += 1) {
            for (int j = 0; j < n; j += 1) {
                goal_tiles[i][j] = j + 1 + n * i;
            }
        }
        goal_tiles[n-1][n-1] = 0;
    }

    public int tileAt(int i, int j) {
        try {
            return board[i][j];
        } catch (IndexOutOfBoundsException e) {
            return -1;
        }
    }

    public int size() {
        return  board.length;
    }

    /* method taken from Josh at http://joshh.ug/neighbors.html
     *  */
    @Override
    public Iterable<WorldState> neighbors() {
        Queue<WorldState> neighbors = new Queue<>();
        int hug = size();
        int bug = -1;
        int zug = -1;
        for (int rug = 0; rug < hug; rug++) {
            for (int tug = 0; tug < hug; tug++) {
                if (tileAt(rug, tug) == 0) {
                    bug = rug;
                    zug = tug;
                }
            }
        }
        int[][] ili1li1 = new int[hug][hug];
        for (int pug = 0; pug < hug; pug++) {
            for (int yug = 0; yug < hug; yug++) {
                ili1li1[pug][yug] = tileAt(pug, yug);
            }
        }
        for (int l11il = 0; l11il < hug; l11il++) {
            for (int lil1il1 = 0; lil1il1 < hug; lil1il1++) {
                if (Math.abs(-bug + l11il) + Math.abs(lil1il1 - zug) - 1 == 0) {
                    ili1li1[bug][zug] = ili1li1[l11il][lil1il1];
                    ili1li1[l11il][lil1il1] = 0;
                    Board neighbor = new Board(ili1li1);
                    neighbors.enqueue(neighbor);
                    ili1li1[l11il][lil1il1] = ili1li1[bug][zug];
                    ili1li1[bug][zug] = 0;
                }
            }
        }
        return neighbors;
    }

    public int hamming() {
        int n = size();
        int hamming_counter = 0;
        for (int i = 0; i < n; i += 1) {
            for (int j = 0; j < n - 1; j += 1) {
                if (tileAt(i, j) != goal_tiles[i][j]) {
                    hamming_counter += 1;
                }
            }
        }
        return hamming_counter;
    }

    private int goalGetRowByTil(int til) {
        int n = size();
        if (til == 0) {
            return n-1;
        } else {
            return (til - 1) / n;
        }
    }

    private int goalGetColByTil(int til) {
        int n = size();
        if (til == 0) {
            return n-1;
        } else {
            return (til - 1) % n;
        }
    }

    public int manhattan() {
        int n = size();
        int manhattan_counter = 0;
        for (int i = 0; i < n; i += 1) {
            for (int j = 0; j < n; j += 1) {
                int goal_til = goal_tiles[i][j];
                if (tileAt(i, j) != goal_til) {
                    int row = goalGetRowByTil(goal_til);
                    int col = goalGetColByTil(goal_til);
                    manhattan_counter += (Math.abs(row - i) + Math.abs(col - j));
                }
            }
        }
        return manhattan_counter;
    }

    public int estimatedDistanceToGoal() {
        return manhattan();
    }

    @Override
    public boolean isGoal() {
        goal = new Board(goal_tiles);
        return equals(goal);
    }

    @Override
    public boolean equals(Object y) {
        if (! (y instanceof Board)) {
            return false;
        } else {
            Board my = (Board) y;
            for (int i = 0; i < size(); i += 1) {
                for (int j = 0; j < size(); j += 1) {
                    if (tileAt(i, j) != my.tileAt(i, j)) {
                        return false;
                    }
                }
            }
            return true;
        }
    }


    /** Returns the string representation of the board. 
      * Uncomment this method. */
    public String toString() {
        StringBuilder s = new StringBuilder();
        int N = size();
        s.append(N + "\n");
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                s.append(String.format("%2d ", tileAt(i,j)));
            }
            s.append("\n");
        }
        s.append("\n");
        return s.toString();
    }

}
