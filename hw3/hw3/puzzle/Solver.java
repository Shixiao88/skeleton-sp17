package hw3.puzzle;
<<<<<<< HEAD

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
=======
import java.nio.file.StandardWatchEventKinds;
import java.util.HashSet;
import java.util.Set;

import edu.princeton.cs.algs4.MSD;
import edu.princeton.cs.algs4.MinPQ;
import java.util.Stack;

/**
 * Created by Administrator on 2017/5/7.
 */
public class Solver {

    private int path_prev;  // the words that the WorldStat already passed
    private Stack<WorldState> passdWDName;    // the WorldStat Name that has been recorded
    private MinPQ<Node> pq;

    private class Node implements Comparable<Node> {
        private Integer total_path;
        private WorldState wd_value;


        public Node(WorldState wd) {
            int temp = wd.estimatedDistanceToGoal();
            total_path = path_prev + wd.estimatedDistanceToGoal();
            wd_value = wd;
>>>>>>> 146b60508a9bf7076cc162bd3dc9f700c13493d0
        }

        @Override
        public int compareTo(Node other) {
<<<<<<< HEAD
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
=======
            return total_path.compareTo(other.total_path);
        }
    }

    public Solver(WorldState initial) {
        path_prev = 0;
        passdWDName = new Stack<>();
        //pqStack = new Stack<MinPQ>();
        pq = new MinPQ<>();
        pq.insert(new Node(initial));
        found();
    }

    private WorldState oneMove() {
        WorldState min = pq.delMin().wd_value;
        passdWDName.push(min);
        path_prev += 1;
        for (WorldState neighbour : min.neighbors()) {
            if (! passdWDName.contains(neighbour)) {
                pq.insert(new Node(neighbour));
            }
        }
        return min;
    }

    private void found() {
        WorldState res = oneMove();
        while (true) {
            if (res.isGoal()) {
                return;
            } else {
                res = oneMove();
            }
        }
    }


    /*
    private MinPQ<Node> createPqByWd (WorldState wd) {
        MinPQ<Node> pq = new MinPQ<>();
        for (WorldState neighbour : wd.neighbors()) {
            if (! passdWDName.contains(neighbour)) {
                Node node = new Node(neighbour);
                pq.insert(node);
            }
        }
        return pq;
    }

    private WorldState findMinWd (MinPQ<Node> pq) {
        if (! pq.isEmpty()) {
            pqStack.push(pq);
            WorldState min_wd = pq.delMin().wd_value;
            passdWDName.push(min_wd);
            path_prev += 1;
            return min_wd;
        } else {
            passdWDName.pop();
            path_prev -= 1;
            return findMinWd(pqStack.pop());
        }
    }

    private WorldState oneMove(WorldState wd) {
        MinPQ<Node> pq_wd = createPqByWd(wd);
        WorldState next_wd = findMinWd(pq_wd);
        return next_wd;
    }

    private boolean isFound(WorldState wd,int maxTimes) {
        int counter = 0;
        WorldState next_wd = oneMove(wd);
        while (counter < maxTimes) {
            if (next_wd.isGoal()) {
                return true;
            }
            counter += 1;
            next_wd = oneMove(next_wd);
        }
        return false;
    }*/

    public int moves() {
        return path_prev;
    }

    public Iterable<WorldState> solution() {
        return passdWDName;
>>>>>>> 146b60508a9bf7076cc162bd3dc9f700c13493d0
    }
}
