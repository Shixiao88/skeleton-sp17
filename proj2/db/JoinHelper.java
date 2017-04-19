package db;
import edu.princeton.cs.introcs.In;

import java.lang.reflect.Array;
import java.util.*;


/**
 * Created by Administrator on 2017/4/18.
 */

/* the class that have some helper method to support Join*/
public class JoinHelper {

    static Map<String, Integer> commonTitle (Table table1, Table table2) {

        // the set does not garantee the order
        Set<String> title_key1 = table1.gettitle().keySet();
        Set<String> title_key2 = table2.gettitle().keySet();
        Map<String, Integer> title_common = new HashMap<>();
        int common_key_index = 0;

        for (String k1:title_key1) {
            if (title_key2.contains(k1)) {
                title_common.put(k1, common_key_index);
                common_key_index += 1;
                }
            }
        return title_common;
    }

    /* a helper class that let me return two values in a method */
    static class commonRowIndexPair {

        private ArrayList<Integer> rowIndexList1;
        private ArrayList<Integer> rowIndexList2;

        commonRowIndexPair(ArrayList<Integer> first, ArrayList<Integer> second) {
            rowIndexList1 = first;
            rowIndexList2 = second;
        }

        ArrayList<Integer> getFirstValue() {
            return rowIndexList1;
        }

        ArrayList<Integer> getSecondValue() {
            return rowIndexList2;
        }
    }

    /* pass in the filtered titles that are in common, and two tables,
    *  this method will filter out all the common title that do not share value
    *  and return a pair isntance of tow integer list, which will correspondently
    *  refer to row index of table1's body and table2's body */
    static commonRowIndexPair filter(Map<String, Integer> title_common, Table t1, Table t2) {
        // make copy of the two tables's body, will cut the body later
        ArrayList<Integer> common_row_index_t1 = new ArrayList<>();
        ArrayList<Integer> common_row_index_t2 = new ArrayList<>();
        ArrayList<String> title_common_keys= new ArrayList<>(title_common.keySet());
        // i cannot call get method in set because there is no get method in stupid java
        // so i will just iterate for one loop (get the first key)
        String k = title_common_keys.get(0);
        ArrayList<String> col1 = t1.columnGet(k);
        ArrayList<String> col2 = t2.columnGet(k);
        for (int i = 0; i < col1.size(); i += 1) {
            for (int i2 = 0; i2 < col2.size(); i2 += 1) {
                String s1 = col1.get(i);
                String s2 = col2.get(i2);
                if (s1.equals(s2)) {
                    common_row_index_t1.add(i);
                    common_row_index_t2.add(i2);
                }
            }
        }
        if (title_common.size() > 1 ) {
            commonRowIndexPair pair =
                    filterTail(title_common, t1, t2, common_row_index_t1, common_row_index_t2);
            return pair;
        }
        commonRowIndexPair pair = new commonRowIndexPair(common_row_index_t1, common_row_index_t2);
        return pair;

    }


    /* after the filter out the first common title, filter out the no common ones with continues common
    / titles */
    static commonRowIndexPair filterTail(Map<String, Integer> mp, Table t1, Table t2,
                           ArrayList<Integer> l1, ArrayList<Integer> l2) {
        ArrayList<String> keys= new ArrayList<>(mp.keySet());
        for (int i = 1; i < keys.size(); i += 1) {
            String k = keys.get(i);
            ArrayList<String> col1 = t1.columnGet(k);
            ArrayList<String> col2 = t2.columnGet(k);
            for (int l = 0; l < l1.size(); l += 1) {
                if (col1.get(l1.get(l)) != col2.get(l2.get(l))) {
                    l1.remove(l);
                    l2.remove(l);
                }
            }
        }
        commonRowIndexPair pair = new commonRowIndexPair(l1, l2);
        return pair;
    }



}
