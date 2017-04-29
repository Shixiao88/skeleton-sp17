package db;

import org.jcp.xml.dsig.internal.MacOutputStream;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by Xiao Shi on 2017/4/28.
 */
public class Operation {

    public static ArrayList<MSQContainer> add(String first, String second, Table table) {
        String col_fullname_first = table.getFullTitleNameByRealName(first);
        String col_fullname_second = table.getFullTitleNameByRealName(second);
        if (col_fullname_first == null) {
            throw new RuntimeException("Missing elements for add operation");
        }
        // the second is literal
        if (col_fullname_second == null) {
            MSQContainer second_ctn = new MSQContainer(second);
            ArrayList<MSQContainer> col_first_column = table.columnGet(col_fullname_first);
            ArrayList<MSQContainer> result_column = new ArrayList<>();
            for (MSQContainer k : col_first_column) {
                try {
                    String res = add(k, second_ctn);
                    MSQContainer add_res = new MSQContainer(res);
                    result_column.add(add_res);
                } catch (RuntimeException e) {
                    throw new RuntimeException ("Bad element type doing add operation");
                }
            } return result_column;
        }
        // the second is instance of MSQColname
        ArrayList<MSQContainer> col_first_column = table.columnGet(col_fullname_first);
        ArrayList<MSQContainer> col_second_column = table.columnGet(col_fullname_second);
        ArrayList<MSQContainer> result_column = new ArrayList<>();
        for (int i = 0; i < col_first_column.size(); i += 1 ) {
            try {
                String res = add(col_first_column.get(i),col_second_column.get(i));
                MSQContainer add_res = new MSQContainer(res);
                result_column.add(add_res);
            } catch (RuntimeException e) {
                throw new RuntimeException("Bad element type doing add operation");
            }
        }  return result_column;
    }

    public static String add (MSQContainer c1, MSQContainer c2) {
        MSQOperable o1 = c1.getContainedElement();
        MSQOperable o2 = c2.getContainedElement();
        if (o1 instanceof MSQNan || o2 instanceof MSQNan) {
            return "NAN";
        } else if (o1 instanceof MSQNovalue && o2 instanceof MSQNovalue) {
            return "NOVALUE";
        } else {
            try {
                MSQOperable res = o1.add(o2);
                return res.toString();
            } catch (RuntimeException e) {
                throw new RuntimeException("Bad element type doing add operation");
            }
        }
    }


    public static ArrayList<MSQContainer> minus (String first, String second, Table table) {
        String col_fullname_first = table.getFullTitleNameByRealName(first);
        String col_fullname_second = table.getFullTitleNameByRealName(second);
        if (col_fullname_first == null) {
            throw new RuntimeException("Missing elements for sub operation");
        }
        // the second is literal
        if (col_fullname_second == null) {
            MSQContainer second_ctn = new MSQContainer(second);
            ArrayList<MSQContainer> col_first_column = table.columnGet(col_fullname_first);
            ArrayList<MSQContainer> result_column = new ArrayList<>();
            for (MSQContainer k : col_first_column) {
                try {
                    String res = minus(k, second_ctn);
                    MSQContainer add_res = new MSQContainer(res);
                    result_column.add(add_res);
                } catch (RuntimeException e) {
                    throw new RuntimeException ("Bad element type doing sub operation");
                }
            } return result_column;
        }
        // the second is instance of MSQColname
        ArrayList<MSQContainer> col_first_column = table.columnGet(col_fullname_first);
        ArrayList<MSQContainer> col_second_column = table.columnGet(col_fullname_second);
        ArrayList<MSQContainer> result_column = new ArrayList<>();
        for (int i = 0; i < col_first_column.size(); i += 1 ) {
            try {
                String res = minus(col_first_column.get(i),col_second_column.get(i));
                MSQContainer sub_res = new MSQContainer(res);
                result_column.add(sub_res);
            } catch (RuntimeException e) {
                throw new RuntimeException("Bad element type doing sub operation");
            }
        }  return result_column;
    }

    public static String minus (MSQContainer c1, MSQContainer c2) {
        MSQOperable o1 = c1.getContainedElement();
        MSQOperable o2 = c2.getContainedElement();
        if (o1 instanceof MSQNan || o2 instanceof MSQNan) {
            return "NAN";
        } else if (o1 instanceof MSQNovalue && o2 instanceof MSQNovalue) {
            return "NOVALUE";
        } else {
            try {
                MSQOperable res = o1.minus(o2);
                return res.toString();
            } catch (RuntimeException e) {
                throw new RuntimeException("Bad element type doing add operation");
            }
        }
    }


//    public static int compareTo(String first, String second, Table table, String comparison_opr) {
//        String col_fullname_first = table.getFullTitleNameByRealName(first);
//        String col_fullname_second = table.getFullTitleNameByRealName(second);
//        if (col_fullname_first == null) {
//            throw new RuntimeException("Missing elements for comparison operation");
//        }
//        // the second is literal
//        if (col_fullname_second == null) {
//            MSQContainer second_ctn = new MSQContainer(second);
//            ArrayList<MSQContainer> col_first_column = table.columnGet(col_fullname_first);
//            ArrayList<MSQContainer> result_column = new ArrayList<>();
//            for (MSQContainer k : col_first_column) {
//                try {
//                    int res = compareTo(k, second_ctn);
//                    if (comparison_opr.equals(">")) {
//
//                    }
//                } catch (RuntimeException e) {
//                    throw new RuntimeException ("Bad element type doing sub operation");
//                }
//            } return result_column;
//        }
//        // the second is instance of MSQColname
//        ArrayList<MSQContainer> col_first_column = table.columnGet(col_fullname_first);
//        ArrayList<MSQContainer> col_second_column = table.columnGet(col_fullname_second);
//        ArrayList<MSQContainer> result_column = new ArrayList<>();
//        for (int i = 0; i < col_first_column.size(); i += 1 ) {
//            try {
//                String res = minus(col_first_column.get(i),col_second_column.get(i));
//                MSQContainer sub_res = new MSQContainer(res);
//                result_column.add(sub_res);
//            } catch (RuntimeException e) {
//                throw new RuntimeException("Bad element type doing sub operation");
//            }
//        }  return result_column;
//    }
}
