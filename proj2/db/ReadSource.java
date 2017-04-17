package db;

import edu.princeton.cs.introcs.In;
import java.util.ArrayList;

/**
 * Created by Xiao Shi on 2017/4/17.
 */
public class ReadSource {

    /* read the source file of a table and return a string*/
    static ArrayList<String> readSource(String source) {
        In in = new In();
        ArrayList<String> res = new ArrayList<>();
        String title = in.readLine();
        res.add(title);
        while (!in.hasNextLine()) {
            res.add(in.readLine());
        }
        return res;
    }

}
