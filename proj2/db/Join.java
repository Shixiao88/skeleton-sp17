package db;
import java.util.*;

/**
 * Created by Administrator on 2017/4/18.
 */
public class Join {

    public static Table join (Table t1, Table t2) {
        Table target;


        Map<String, Integer> common_t = JoinHelper.commonTitle(t1, t2);
        // t1 and t2 share column
        if (common_t.size() >= 1) {

        }

        // t1 and t2 do not share column
        // do the i dont remember join sutff

        return null;
    }

}
