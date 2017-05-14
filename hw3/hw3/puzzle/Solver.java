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
        private LinkedList<WorldState> parent_lst;

        public Node(WorldState wd) {
            value = wd;
            path2Goal = wd.estimatedDistanceToGoal();
            parent_lst = new LinkedList<>();
            path2Initial = 0;
        }

        public Node(WorldState wd, Node parent) {
            value = wd;
            path2Goal = wd.estimatedDistanceToGoal();
            WorldState p = parent.value;
            parent_lst = new LinkedList<>();
            parent_lst.addAll(parent.parent_lst);
            parent_lst.addLast(p);
            path2Initial = parent_lst.size();
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
                if (! min_wd_node.parent_lst.contains(w) && !existed_nodes.contains(w)) {
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

    /*Returns a sequence of WorldStates from the initial WorldState
    to the solution.*/
    public Iterable<WorldState> solution() {
        LinkedList<WorldState> sol = new LinkedList<>();
        sol.addAll(goal.parent_lst);
        sol.add(goal.value);
        return sol;
    }
}
