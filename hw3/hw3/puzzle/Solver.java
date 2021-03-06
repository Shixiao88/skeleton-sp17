package hw3.puzzle;
import java.util.*;

import edu.princeton.cs.algs4.MinPQ;
/**
 * Created by Xiao Shi on 2017/5/9.
 */
public class Solver {
    private MinPQ<Node> pq;
    private Node goal;
    private Set<WorldState> existed_nodes;

    private class Node implements Comparable<Node> {
        private WorldState value;
        private int path2Goal;
        private int path2Initial;
        private Node parent;


        public Node(WorldState wd) {
            value = wd;
            path2Goal = wd.estimatedDistanceToGoal();
            path2Initial = 0;
            this.parent = null;
        }

        public Node(WorldState wd, Node parent) {
            value = wd;
            path2Goal = wd.estimatedDistanceToGoal();
            this.parent = parent;
            path2Initial = parent.path2Initial + 1;
        }

        public Integer getPath() {
            return path2Goal + path2Initial;
        }

        @Override
        public int compareTo (Node other){
            return this.getPath().compareTo(other.getPath());
        }

    }

    /* Constructor which solves the puzzle, computing
        everything necessary for moves() and solution() to
        not have to solve the problem again. Solves the
        puzzle using the A* algorithm. Assumes a solution exists.
     */

    public Solver(WorldState initial) {
        existed_nodes = new HashSet<>();
        pq = new MinPQ<>();
        pq.insert(new Node(initial));
        goal = solve();
    }

    public Node solve() {
        while (! pq.min().value.isGoal()) {
            Node min_wd_node = pq.delMin();
            for (WorldState w : min_wd_node.value.neighbors()) {
                if ((!existed_nodes.contains(w)) && (! getParentList(min_wd_node).contains(w))) {
                    Node w_node = new Node (w, min_wd_node);
                    existed_nodes.add(w_node.value);
                    pq.insert(w_node);
                }
            }
        }
        return pq.min();
    }

    /* Returns the minimum number of moves to solve the puzzle starting
    * at the initial WorldState.*/
    public int moves() {
        return goal.getPath();
    }

    private ArrayList<WorldState> getParentList(Node i) {
        ArrayList<WorldState> helper = new ArrayList<>();
        while (i.parent != null) {
            helper.add(0, i.value);
            i = i.parent;
        }
        helper.add(0, i.value);
        return helper;
    }

    /*Returns a sequence of WorldStates from the initial WorldState
    to the solution.*/
    public Iterable<WorldState> solution() {
        ArrayList<WorldState> sol = getParentList(goal);
        return sol;
    }
}
