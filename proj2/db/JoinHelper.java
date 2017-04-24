package db;
import edu.princeton.cs.introcs.In;

import java.lang.reflect.Array;
import java.util.*;


/**
 * Created by Administrator on 2017/4/18.
 */

/* the class that have some helper method to support Join*/
public class JoinHelper {

    static Map<MSQColName, Integer> commonTitle (Table table1, Table table2) {

        // the set does not garantee the order
        Set<MSQColName> title_key1 = table1.gettitle().keySet();
        Set<MSQColName> title_key2 = table2.gettitle().keySet();
        Map<MSQColName, Integer> title_common = new LinkedHashMap<>();
        int common_key_index = 0;

        for (MSQColName k1:title_key1) {
            if (table2.titleIn(k1.getValue())) {
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
    static commonRowIndexPair filter(Map<MSQColName, Integer> title_common, Table t1, Table t2) {
        // make copy of the two tables's body, will cut the body later
        ArrayList<Integer> common_row_index_t1 = new ArrayList<>();
        ArrayList<Integer> common_row_index_t2 = new ArrayList<>();
        ArrayList<MSQColName> title_common_keys= new ArrayList<>(title_common.keySet());
        // i cannot call get method in set because there is no get method in stupid java
        // so i will just iterate for one loop (get the first key)
        MSQColName k = title_common_keys.get(0);
        ArrayList<MSQContainer> col1 = t1.columnGet(k.getValue());
        ArrayList<MSQContainer> col2 = t2.columnGet(k.getValue());
        for (int i = 0; i < col1.size(); i += 1) {
            for (int i2 = 0; i2 < col2.size(); i2 += 1) {
                MSQContainer s1 = col1.get(i);
                MSQContainer s2 = col2.get(i2);
                if (s1.toString().equals(s2.toString())) {
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
    static commonRowIndexPair filterTail(Map<MSQColName, Integer> mp, Table t1, Table t2,
                           ArrayList<Integer> l1, ArrayList<Integer> l2) {
        ArrayList<MSQColName> keys= new ArrayList<>(mp.keySet());
        for (int i = 1; i < keys.size(); i += 1) {
            MSQColName k = keys.get(i);
            ArrayList<MSQContainer> col1 = t1.columnGet(k.getValue());
            ArrayList<MSQContainer> col2 = t2.columnGet(k.getValue());
            for (int l = 0; l < l1.size(); l += 1) {
                if (! col1.get(l1.get(l)).toString().equals(col2.get(l2.get(l)).toString())) {
                    l1.remove(l);
                    l2.remove(l);
                }
            }
        }
        commonRowIndexPair pair = new commonRowIndexPair(l1, l2);
        return pair;
    }

    /* helper method to return the list that filter out the common title, by order from table1 to table2*/
    static Map<MSQColName, Integer> completeTitle(Map<MSQColName, Integer> common_title,
                                              Map<MSQColName, Integer> title1,
                                              Map<MSQColName, Integer> title2) {
            Map<MSQColName, Integer> whole_keys = new LinkedHashMap<>();
        int key_index = 0;
        for (MSQColName common_k : common_title.keySet()) {
            whole_keys.put(common_k, key_index);
            key_index += 1;
        }

        Table temp1 = new Table ("temp1", title1, null);
        Table temp2 = new Table ("temp2", title2, null);
        for (MSQColName k1 : title1.keySet()) {
            if (! temp2.titleIn(k1.getValue())) {
                whole_keys.put(k1, key_index);
                key_index += 1;
            }
        }
        for (MSQColName k2 : title2.keySet()) {
            if (! temp1.titleIn(k2.getValue())) {
                whole_keys.put(k2, key_index);
                key_index += 1;
            }
        }
        return whole_keys;
    }

    /* helper method to return the not common key list */
    static ArrayList<MSQColName> getNoCommonTitle (Map<MSQColName, Integer> common_title,
                                               Map<MSQColName, Integer> title1,
                                               Map<MSQColName, Integer> title2) {
        ArrayList<MSQColName> no_common_keys = new ArrayList<>();

        Table temp = new Table ("temp", common_title, null);
        for (MSQColName k1 : title1.keySet()) {
            if (!temp.titleIn(k1.toString())) {
                no_common_keys.add(k1);
            }
        }
        for (MSQColName k2 : title2.keySet()) {
            if (!temp.titleIn(k2.getValue())) {
                no_common_keys.add(k2);
            }
        }
        return no_common_keys;
    }


    static Table completeTable(String name, ArrayList<Integer> row_num_1, ArrayList<Integer> row_num_2,
                               Map<MSQColName, Integer> common_title, Table t1, Table t2) {
        Map<MSQColName, Integer> title1 = t1.gettitle();
        Map<MSQColName, Integer> title2 = t2.gettitle();
        Map<MSQColName, Integer> all_titles = completeTitle(common_title, title1, title2);
        ArrayList<ArrayList<MSQContainer>> new_body = new ArrayList<>();
        for (int row=0; row < row_num_1.size(); row +=1 ) {
            ArrayList<MSQContainer> line = new ArrayList<>();
            int row_num = row_num_1.get(row);
            for (MSQColName k : common_title.keySet()) {
                int col_num = t1.getTitleIndex(k.getValue());
                MSQContainer element = t1.getbody().get(row_num).get(col_num);
                line.add(element);
            } new_body.add(line);
        }
        Table res = new Table(name, all_titles, new_body);
        // we finish the table with common titles
        ArrayList<MSQContainer> column = new ArrayList<>();
        ArrayList<MSQColName> no_common_title_key = getNoCommonTitle(common_title, t1.gettitle(), t2.gettitle());
        for (MSQColName nckey : no_common_title_key) {
            try {
                int col_num = t1.getTitleIndex(nckey.getValue());

                for (int row = 0; row < row_num_1.size(); row += 1) {
                    int row_num = row_num_1.get(row);
                    MSQContainer element = t1.getbody().get(row_num).get(col_num);
                    column.add(element);
                }
            } catch (RuntimeException e) {
                int col_num = t2.getTitleIndex(nckey.getValue());
                for (int row = 0; row < row_num_2.size(); row += 1) {
                    int row_num = row_num_2.get(row);
                    MSQContainer element = t2.getbody().get(row_num).get(col_num);
                    column.add(element);
            }
        }   res.columnAdd(nckey.getValue(), column);
            column.clear();
    }
    return res;
    }


    /* complete the table when there is no common title, a.k.a the common_title parameter is []
     * create a new line to hold elements
     * create a new body to hold the line

     * iterate first table's row
     *     iterate second table's row
     *         iterate common title,
     *             add each element in line
     *      add line to the body
     *      clear the line to hold new element
     * finish the filling, and create the new table using title, and body parameter.

     * */
    static Table cartesianProductCompleteTable (String name, Map<MSQColName, Integer> common_title, Table t1, Table t2) {
        Map<MSQColName, Integer> total_title = completeTitle(common_title, t1.gettitle(), t2.gettitle());
        int row_num_size1 = t1.getRowNum();
        int row_num_size2 = t2.getRowNum();
        ArrayList<ArrayList<MSQContainer>> new_body = new ArrayList<>();
        Table res = new Table(name, total_title, new_body);
        for (int row1 = 0; row1 < row_num_size1; row1 += 1) {
            ArrayList<MSQContainer> table1_line = t1.rowGet(row1);
            for (int row2 = 0; row2 < row_num_size2; row2 += 1) {
                ArrayList<MSQContainer> table2_line = t2.rowGet(row2);
                ArrayList<MSQContainer> line = new ArrayList<>();
                line.addAll(table1_line);
                line.addAll(table2_line);
                res.rowAdd(line);
            }
        }
        return res;
    }
}
