package hw3.puzzle;

import java.lang.invoke.WrongMethodTypeException;
import java.util.*;
import edu.princeton.cs.algs4.MinPQ;
import sun.awt.image.ImageWatched;

import javax.naming.LinkLoopException;
import java.util.Comparator;
/**
 * Created by Xiao Shi on 2017/5/9.
 */
public class Solver {
    private MinPQ<Node> pq;
    private Node goal;

    private class Node implements Comparable<Node> {
        private Word value;
        private int path2Goal;
        private int path2Initial;
        private LinkedList<Word> parent_lst;

        public Node(Word wd) {
            value = wd;
            path2Goal = wd.estimatedDistanceToGoal();
            parent_lst = new LinkedList<>();
            path2Initial = 0;
        }

        public Node(Word wd, Node parent) {
            value = wd;
            path2Goal = wd.estimatedDistanceToGoal();
            Word p = parent.value;
            parent_lst = new LinkedList<>();
            parent_lst.addAll(parent.parent_lst);
            parent_lst.addLast(p);
            path2Initial = parent_lst.size();
        }

        public Integer getPath() {
            return path2Goal + path2Initial;
        }

        @Override
        public int compareTo(Node other) {
            return this.getPath().compareTo(other.getPath());
        }

    }

    /* Constructor which solves the puzzle, computing
everything necessary for moves() and solution() to
not have to solve the problem again. Solves the
puzzle using the A* algorithm. Assumes a solution exists.*/
    public Solver(Word initial) {
        pq = new MinPQ<>();
        pq.insert(new Node(initial));
        goal = solve();
    }

    public Node solve() {
        while (! pq.min().value.isGoal()) {
            Node min_wd_node = pq.delMin();
            Word min_wd = min_wd_node.value;
            for (WorldState w : min_wd_node.value.neighbors()) {
                if (! min_wd_node.parent_lst.contains(w)) {
                    Node w_node = new Node ((Word)w, min_wd_node);
                    pq.insert(w_node);
                }
            }
        }
        return pq.min();
    }

    /* Returns the minimum number of moves to solve the puzzle starting
    * at the initial WorldState.*/
    public int moves() {
        return goal.getPath() + 1;
    }

    /*Returns a sequence of WorldStates from the initial WorldState
    to the solution.*/
    public Iterable<Word> solution() {
        LinkedList<Word> sol = new LinkedList<>();
        sol.addAll(goal.parent_lst);
        sol.add(goal.value);
        return sol;
    }
}
