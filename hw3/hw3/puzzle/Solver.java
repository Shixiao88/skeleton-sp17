package hw3.puzzle;
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
        }

        @Override
        public int compareTo(Node other) {
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
    }
}
