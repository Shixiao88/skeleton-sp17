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
            AND   = "\\s+and\\s+";

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
            return select(m.group(1), db);
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
            createSelectedTable(m.group(1), m.group(2), m.group(3), m.group(4));
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

    private static void createSelectedTable(String name, String exprs, String tables, String conds) {
        /* name is the table name
        *  exprs is comma split
        *  tables is comma split
        *  cons is and split
        *  */

        System.out.printf("You are trying to create a table named %s by selecting these expressions:" +
                " '%s' from the join of these tables: '%s', filtered by these conditions: '%s'\n", name, exprs, tables, conds);
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
        String[] value_lst = m.group(2).split(",");
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
        return select(expr, db);
    }


    private static String select(String expr, Database db) {
        Matcher m = SELECT_CLS.matcher(expr);
        if (!m.matches()) {
            System.err.printf("Malformed select: %s\n", expr);
            return "";
        }
        String slc_titles = m.group(1);
        String table_name = m.group(2);
        String cond = m.group(3);
        if (cond == null) {
            return selectNoCond(slc_titles, table_name, db);
        }
            return selectCond(slc_titles, table_name, cond, db);

    }

    private static String selectNoCond(String exprs, String tables, Database db) {
        String[] slc_titles = exprs.split(",");
        String[] slc_tables = tables.split(",");
        ArrayList<Table> ins_tables = new ArrayList<>();
        for (int i = 0; i < slc_tables.length; i += 1) {
            ins_tables.add(db.selectTableByName(slc_tables[i]));
        }
        // the case select "*" from the given table(s)
        if (slc_titles.length == 1 && slc_titles[0].equals("*")) {
            Table res = Join.join("temp", ins_tables);
            return res.toString();
        } else {
            ArrayList<Table> table_selected = new ArrayList<>();
            for (Table t: ins_tables) {
                Table temp = new Table("temp");
                for (String title_real_name : slc_titles) {
                    try {
                        String title_full_name = t.getFullTitleNameByRealName(title_real_name);
                        temp.columnAdd(title_full_name, t.columnGet(title_full_name));
                    } catch (RuntimeException e) { }
                } table_selected.add(temp);
            }
            Table join_temp  = Join.join("join_temp", table_selected);
            return join_temp.toString();
        }
    }

    private static String selectCond(String exprs, String tables, String conds, Database db) {
        System.out.printf("You are trying to select these expressions:" +
                " '%s' from the join of these tables: '%s', filtered by these conditions: '%s'\n", exprs, tables, conds);
        return "";
    }
}
