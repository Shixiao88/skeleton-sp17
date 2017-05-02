package db;


import javafx.scene.control.Tab;

import javax.xml.crypto.Data;
import java.lang.reflect.Array;
import java.util.StringJoiner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.*;

/**
 * Created by Xiao Shi on 2017/4/21.
 */
public class Parse {

    // Various common constructs, simplifies parsing.
    // \s is space or return, to avoid misunderstanding of interpretor and garantee the \ to be passed
    // use two \\, so it is \\s
    private static final String SPACE = "\\s*",
            REST  = "\\s*(.*)\\s*",
            COMMA = "\\s*,\\s*",
            AND   = "\\s+(and)\\s+",
            AS    = "\\s+(as)\\s+";

    // Stage 1 syntax, contains the command name.
    private static final Pattern CREATE_CMD = Pattern.compile(SPACE + "create table " + REST),
            LOAD_CMD   = Pattern.compile(SPACE + "load " + REST),
            STORE_CMD  = Pattern.compile(SPACE + "store " + REST),
            DROP_CMD   = Pattern.compile(SPACE + "drop table " + REST),
            INSERT_CMD = Pattern.compile(SPACE + "insert into " + REST),
            PRINT_CMD  = Pattern.compile(SPACE + "print " + REST),
            SELECT_CMD = Pattern.compile(SPACE + "select " + REST);

    // Stage 2 syntax, contains the clauses of commands.
    private static final Pattern CREATE_NEW  = Pattern.compile("(\\S+)\\s+\\(\\s*(\\S+\\s+\\S+\\s*" +
            "(?:,\\s*\\S+\\s+\\S+\\s*)*)\\)"),
            SELECT_CLS  = Pattern.compile("([^,]+?(?:,[^,]+?)*)\\s+from\\s+" +
                    "(\\S+\\s*(?:,\\s*\\S+\\s*)*)(?:\\s+where\\s+" +
                    "([\\w\\s+\\-*/'<>=!.]+?(?:\\s+and\\s+" +
                    "[\\w\\s+\\-*/'<>=!.]+?)*))?"),
            CREATE_SEL  = Pattern.compile("(\\S+)\\s+as select\\s+" +
                    SELECT_CLS.pattern()),
            INSERT_CLS  = Pattern.compile("(\\S+)\\s+values\\s+(.+?" +
                    "\\s*(?:,\\s*.+?\\s*)*)");

    public static String main(String[] args, Database db) {
        if (args.length != 1) {
            System.err.println("Expected a single query argument");
            return "";
        }

        return eval(args[0], db);
    }

    private static String eval(String query, Database db) {
        Matcher m;
        if ((m = CREATE_CMD.matcher(query)).matches()) {
            return createTable(m.group(1), db);
        } else if ((m = LOAD_CMD.matcher(query)).matches()) {
            return loadTable(m.group(1), db);
        } else if ((m = STORE_CMD.matcher(query)).matches()) {
            return storeTable(m.group(1), db);
        } else if ((m = DROP_CMD.matcher(query)).matches()) {
            return dropTable(m.group(1), db);
        } else if ((m = INSERT_CMD.matcher(query)).matches()) {
            return insertRow(m.group(1), db);
        } else if ((m = PRINT_CMD.matcher(query)).matches()) {
            return printTable(m.group(1), db);
        } else if ((m = SELECT_CMD.matcher(query)).matches()) {
            return select(m.group(1), db).toString();
        } else {
            System.err.printf("Malformed query: %s\n", query);
        }
        return "";
    }


    private static String createTable(String expr, Database db) {
        Matcher m;
        if ((m = CREATE_NEW.matcher(expr)).matches()) {
            createNewTable(m.group(1), m.group(2).split(COMMA), db);
        } else if ((m = CREATE_SEL.matcher(expr)).matches()) {
            createSelectedTable(m.group(1), m.group(2), m.group(3), m.group(4), db);
        } else {
            System.err.printf("Malformed create: %s\n", expr);
        }
        return "";
    }

    public static String testCreateNewTable(String name, String[] cols, Database db) {
        return createNewTable(name, cols, db);
    }

    private static String createNewTable(String name, String[] cols, Database db) {
        Map<MSQColName, Integer> t= new HashMap<>();
        for (int i = 0; i < cols.length; i++) {
            t.put(new MSQColName(cols[i]), i);
        }
        Table table = new Table(name, t, null);
        db.addTableByName(name, table);
        return "Susseccfully create the table name: " + name;
    }

    public static String TestCreateSelectedTable(String name, String exprs, String tables, String conds, Database db) {
        return createSelectedTable(name, exprs, tables, conds, db);
    }

    private static String createSelectedTable(String name, String exprs, String tables, String conds, Database db) {
        Table res = selectHelper(exprs, tables, conds, db);
        Table selected_table = res.copy(name);
        db.addTableByName(name, selected_table);
        return "";
    }


    public static String testLoadTable(String name, Database db) {
        return loadTable(name, db);
    }

    private static String loadTable(String name, Database db) {
        Table t = new Table(name);
        db.addTableByName(name, t);
        return "";
    }

    private static String storeTable(String name, Database db) {
        try {
            Table t = db.selectTableByName(name);
            t.saveTableToFile(name);
            System.out.println("Successfully store the table " + name);
        } catch (RuntimeException e) {
            System.out.println("Error: " + e);
        }
            return "";
    }

    private static String dropTable(String name, Database db) {
        try {
            db.selectTableByName(name);
            db.removeTableByName(name);
            System.out.println("Successfully drop the table " + name);
        } catch (RuntimeException e) {
            System.out.println("Error: " + e);
        }
        return "";
    }

    public static String TestInsertRow(String expr, Database db) {
        return insertRow(expr, db);
    }

    private static String insertRow(String expr, Database db) {
        Matcher m = INSERT_CLS.matcher(expr);
        if (!m.matches()) {
            throw new RuntimeException("Malformed insert: " + expr);
        }
        String table_name = m.group(1);
        String[] value_lst = m.group(2).split(COMMA);
        List<String> row_str = new ArrayList<String>(Arrays.asList(value_lst));
        ArrayList<MSQContainer> row_ctn = new ArrayList<>();
        for (String s : row_str) {
            row_ctn.add(new MSQContainer(s));
        }
        try {
            db.selectTableByName(table_name).rowAdd(row_ctn);
        } catch (RuntimeException e) {
            System.out.println ("Error: " + e);
        }
        return "";
    }

    public static String testPrintTable(String name, Database db) {
        return printTable(name, db);
    }

    private static String printTable(String name, Database db) {
        try {
            Table t = db.selectTableByName(name);
            return t.toString();
        } catch (NullPointerException e) {
            System.out.println ("Error: cannot find the table " + name);
        } catch (RuntimeException e) {
            System.out.println ("Error: " + e);
        }
        return "";
    }

    public static String testSelect(String expr, Database db) {
        return select(expr, db).toString();
    }


    private static Table select(String expr, Database db) {
        Matcher m = SELECT_CLS.matcher(expr);
        if (!m.matches()) {
            System.err.printf("Malformed select: %s\n", expr);
            return null;
        }
        String slc_exprs = m.group(1);
        String table_name = m.group(2);
        String cond = m.group(3);

        return selectHelper(slc_exprs, table_name, cond, db);
    }

    private static Table selectHelper(String slc_exprs, String table_name, String cond, Database db) {

        /* parse the slc_exprs
         * column expression will be: "(<oprand><oprator><operand>)(<oprand><oprator><operand>)(<oprand><oprator><operand>)
         * as (column1, column2, column3)"
         */
        // 1. deal with the clause after FROM, joint all the tables
        Table join_table = selectTableJoin(table_name, db);
        // 2. deal with the column operations after SELECT, might be column names and column operations.

        String[] arry_operations = slc_exprs.split(COMMA);
        selectDoOprs(arry_operations, join_table);

        // 3. deal with only column name selection, delete all the columns that doesn't mentioned in the select clause
        selectColumns(arry_operations, join_table);

        // 4. if the cond is not null, filter the result from step3
        if (! (cond == null)) {
            String[] conds = cond.split(AND);
            filterCond(conds, join_table);
        }

        return join_table;
    }

    /* first step : to create a joint table for clause "FROM TABLE1, TABLE2, TABLE3 ... */
    private static Table selectTableJoin(String table_name, Database db) {
        String[] table_lst = table_name.split(COMMA);
        if (table_lst.length == 1) {
            String tablename = table_lst[0];
            return db.selectTableByName(tablename).copy(table_name + "Copy");
        } else {
            List<String> table_arry = new ArrayList<>(Arrays.asList(table_lst));
            ArrayList<Table> t_ins_lst = new ArrayList<>();
            for (String t_name : table_arry) {
                t_ins_lst.add(db.selectTableByName(t_name));
            }
            Table join = Join.join("join", t_ins_lst);
            return join;
        }
    }

    /* second step : do the operation "COLUM1 + COLUMN2 as SMTH" or "COLUMN1 * LITERAL as SMT" ...
    *  if there is no operations inside, do nothing instead*/
    private static void selectDoOprs(String[] opr_lst, Table table) {
        Map<String, List<MSQContainer>> colname_col = new HashMap<>();
        try {
            for (int i = 0; i < opr_lst.length; i += 1) {
                String operation = opr_lst[i];
                if (operation.contains("+")) {
                    String[] arry_operations = operation.split("\\s*\\+\\s*");
                    // to see if there is the "as" column rename clause
                    String[] rename_column_expr = arry_operations[1].split(AS);
                    String rename = rename_column_expr[1];
                    ArrayList<MSQContainer> one_column = Operation.add(arry_operations[0], rename_column_expr[0], table);
                    if (rename == "") {
                        String column_full_name = table.getFullTitleNameByRealName(arry_operations[0]);
                        table.columnAdd(column_full_name, one_column);
                    } else {
                        String new_column_type = type_selection(arry_operations[0], rename_column_expr[0], table);
                        table.columnAdd(rename + " " + new_column_type, one_column);
                    }

                } else if (operation.contains("-")) {
                    String[] arry_operations = operation.split("\\s*-\\s*");
                    // to see if there is the "as" column rename clause
                    String[] rename_column_expr = arry_operations[1].split(AS);
                    String rename = rename_column_expr[1];
                    ArrayList<MSQContainer> one_column = Operation.minus(arry_operations[0], rename_column_expr[0], table);
                    if (rename == "") {
                        String column_full_name = table.getFullTitleNameByRealName(arry_operations[0]);
                        table.columnAdd(column_full_name, one_column);
                    }
                    String new_column_type = type_selection(arry_operations[0], rename_column_expr[0], table);
                    table.columnAdd(rename + " " + new_column_type, one_column);
                }
            }
        } catch (RuntimeException e){
            throw new RuntimeException("Bad formed operation format: " + e);
        }
    }

    /* helper method to decide a type of the renamed column */
    private static String type_selection(String op1, String op2, Table table) {
        try {
            String op1_fullname = table.getFullTitleNameByRealName(op1);
            String op2_fullname = table.getFullTitleNameByRealName(op2);
            String type1 = op1_fullname.split(" ")[1];
            String type2;

            if (type1.equals("string")) {
                return "string";
            } else if (type1.equals("float")) {
                return "float";
            } else if (op2_fullname == null) {
                type2 = new MSQContainer(op2).getRealType();
                if ( type1.equals("int") && type2.equals( "int")) {
                    return "int";
                } else if (type2.equals("float")) {
                    return "float";
                }
            } else {
                type2 = op2_fullname.split(" ")[1];
                if ( type1.equals("int") && type2.equals("int")) {
                    return "int";
                } else if (type2.equals("float")) {
                    return "float";
                }
            }
        } catch (RuntimeException e) {
            throw new RuntimeException("Bad formed column type selection");
        } return null;
    }


    private static void selectColumns(String[] slc_titles, Table table) {
        // the case select "*" from the given table(s)
        if (slc_titles[0].equals("*")) {
            // do nothing
        } else {
            // filter all the AS clause, add all the real column number in the list
            List<String> col_names = new ArrayList<>();
            for (int i = 0; i < slc_titles.length; i += 1) {
                String[] as_clause = slc_titles[i].split(AS);
                if (as_clause.length > 1) {
                    col_names.add(as_clause[1]);
                }
                col_names.add(as_clause[0]);
            }
            // iterate the table, if the title not in the list, delete the column
            ArrayList<MSQColName> title_copy = new ArrayList<>();
            title_copy.addAll(table.gettitle().keySet());
            for (MSQColName k : title_copy) {
                if (! col_names.contains(k.getTitleName())) {
                    table.columnDel(k.getValue());
                }
            }
        }
    }

    private static void filterCond(String[] conds, Table table) {
        try {
            for (int i = 0; i < conds.length; i += 1) {
                if (conds[i].contains("<=")) {
                    String[] cps = conds[i].split("\\s*<=\\s*");
                    String cp1 = cps[0];
                    String cp2 = cps[1];
                    ArrayList<Integer> res = Operation.compare(cp1, cp2, table);
                    for (int row_index = res.size()-1; row_index >= 0; row_index -= 1) {
                        if (res.get(row_index) > 0) {
                            table.rowDel(row_index);
                        }
                    }
                } else if (conds[i].contains("<")) {
                    String[] cps = conds[i].split("\\s*<\\s*");
                    String cp1 = cps[0];
                    String cp2 = cps[1];
                    ArrayList<Integer> res = Operation.compare(cp1, cp2, table);
                    for (int row_index = res.size()-1; row_index >= 0; row_index -= 1) {
                        if (res.get(row_index) >= 0) {
                            table.rowDel(row_index);
                        }
                    }
                } else if (conds[i].contains(">=")) {
                    String[] cps = conds[i].split("\\s*>=\\s*");
                    String cp1 = cps[0];
                    String cp2 = cps[1];
                    ArrayList<Integer> res = Operation.compare(cp1, cp2, table);
                    ArrayList<Integer> filtered_res = new ArrayList<>();

                    // because the iteration and delete will have the list mutation exception, so i will iterate from
                    // the end to the front, thus will not affect the table row index
                    for (int row_index = res.size() - 1; row_index >= 0; row_index -= 1) {
                        if (res.get(row_index) < 0) {
                            table.rowDel(row_index);
                        }
                    }
                } else if (conds[i].contains(">")) {
                    String[] cps = conds[i].split("\\s*>\\s*");
                    String cp1 = cps[0];
                    String cp2 = cps[1];
                    ArrayList<Integer> res = Operation.compare(cp1, cp2, table);
                    for (int row_index = res.size()-1; row_index >= 0; row_index -= 1) {
                        if (res.get(row_index) <= 0) {
                            table.rowDel(row_index);
                        }
                    }
                } else if (conds[i].contains("==")) {
                    String[] cps = conds[i].split("\\s*==\\s*");
                    String cp1 = cps[0];
                    String cp2 = cps[1];
                    ArrayList<Integer> res = Operation.compare(cp1, cp2, table);
                    for (int row_index = res.size()-1; row_index >= 0; row_index -= 1) {
                        if (! (res.get(row_index) == 0)) {
                            table.rowDel(row_index);
                        }
                    }
                }
            }
        } catch (RuntimeException e) {
            throw new RuntimeException("Bad formed comparison form");
        }
    }

}
