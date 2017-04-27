package db;
import java.lang.reflect.Array;
import java.util.*;

/**
 * Created by Administrator on 2017/4/18.
 */
public class Join {

    public static Table join (String join_name, Table t1, Table t2) {
        // decide if either table is empty, will return the other one
        if (t1.isEmpty()) {
            Table res = t2.copy(join_name);
            return res;
        } else if (t2.isEmpty()) {
            Table res = t1.copy(join_name);
            return res;
        } else {
            Map<MSQColName, Integer> common_t = JoinHelper.commonTitle(t1, t2);

            // t1 and t2 share column
            if (common_t.size() > 0) {
                JoinHelper.commonRowIndexPair pair = JoinHelper.filter(common_t, t1, t2);
                Table res = JoinHelper.completeTable(join_name, pair.getFirstValue(), pair.getSecondValue(), common_t, t1, t2);
                return res;
            } else {
                // t1 and t2 do not share column
                // do the i dont remember join sutff
                Table res = JoinHelper.cartesianProductCompleteTable(join_name, common_t, t1, t2);
                return res;
            }
        }
    }

    public static Table join (String join_name, ArrayList<Table> table_list) {
        Table temp = table_list.get(0).copy("temp");
        for (Table t : table_list) {
            temp = join("temp", temp, t);
        }
        return temp;
    }
}
