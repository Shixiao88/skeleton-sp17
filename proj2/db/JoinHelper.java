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
        Map<String, Integer> title_common = new LinkedHashMap<>();
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
                if (! col1.get(l1.get(l)).equals(col2.get(l2.get(l)))) {
                    l1.remove(l);
                    l2.remove(l);
                }
            }
        }
        commonRowIndexPair pair = new commonRowIndexPair(l1, l2);
        return pair;
    }

    /* helper method to return the list that filter out the common title, by order from table1 to table2*/
    static Map<String, Integer> completeTitle(Map<String, Integer> common_title,
                                              Map<String, Integer> title1,
                                              Map<String, Integer> title2) {
            Map<String, Integer> whole_keys = new LinkedHashMap<>();
        int key_index = 0;
        for (String common_k : common_title.keySet()) {
            whole_keys.put(common_k, key_index);
            key_index += 1;
        }
        for (String k1 : title1.keySet()) {
            if (! common_title.containsKey(k1)) {
                whole_keys.put(k1, key_index);
                key_index += 1;
            }
        }
        for (String k2 : title2.keySet()) {
            if (! common_title.containsKey(k2)) {
                whole_keys.put(k2, key_index);
                key_index += 1;
            }
        }
        return whole_keys;
    }

    /* helper method to return the not common key list */
    static ArrayList<String> getNoCommonTitle (Map<String, Integer> common_title,
                                               Map<String, Integer> title1,
                                               Map<String, Integer> title2) {
        ArrayList<String> no_common_keys = new ArrayList<>();
        for (String k1 : title1.keySet()) {
            if (!common_title.containsKey(k1)) {
                no_common_keys.add(k1);
            }
        }
        for (String k2 : title2.keySet()) {
            if (!common_title.containsKey(k2)) {
                no_common_keys.add(k2);
            }
        }
        return no_common_keys;
    }


    static Table completeTable(String name, ArrayList<Integer> row_num_1, ArrayList<Integer> row_num_2,
                               Map<String, Integer> common_title, Table t1, Table t2) {
        Map<String, Integer> title1 = t1.gettitle();
        Map<String, Integer> title2 = t2.gettitle();
        Map<String, Integer> all_keys = completeTitle(common_title, t1.gettitle(), t2.gettitle());
        ArrayList<ArrayList<String>> new_body = new ArrayList<>();
        for (int row=0; row < row_num_1.size(); row +=1 ) {
            ArrayList<String> line = new ArrayList<>();
            for (String k : common_title.keySet()) {
                Integer row_num = row_num_1.get(row);
                String element = t1.getbody().get(row_num).get(t1.getTitleIndex(k));
                line.add(element);
            }
            new_body.add(line);
        }
        Table res = new Table(name, all_keys, new_body);
        // we finish the table with common titles

        ArrayList<String> no_common_key = getNoCommonTitle(common_title, t1.gettitle(), t2.gettitle());
        ArrayList<String> column = new ArrayList<>();
        for (String nckey : no_common_key) {
            try {
                int col_num = t1.getTitleIndex(nckey);
                for (int row = 0; row < row_num_1.size(); row += 1) {
                    int row_num = row_num_1.get(row);
                    String element = t1.getbody().get(row_num).get(col_num);
                    column.add(element);
                }
            } catch (RuntimeException e) {
                int col_num = t2.getTitleIndex(nckey);
                for (int row = 0; row < row_num_2.size(); row += 1) {
                    int row_num = row_num_1.get(row);
                    String element = t2.getbody().get(row_num).get(col_num);
                    column.add(element);
            }
        } res.columnAdd(nckey, column);
    }
    return res;
    }

}
