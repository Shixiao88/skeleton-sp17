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
            return createTable(m.group(1));
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
            select(m.group(1));
        } else {
            System.err.printf("Malformed query: %s\n", query);
        }
        return "";
    }


    private static String createTable(String expr) {
        Matcher m;
        if ((m = CREATE_NEW.matcher(expr)).matches()) {
            createNewTable(m.group(1), m.group(2).split(COMMA));
        } else if ((m = CREATE_SEL.matcher(expr)).matches()) {
            createSelectedTable(m.group(1), m.group(2), m.group(3), m.group(4));
        } else {
            System.err.printf("Malformed create: %s\n", expr);
        }
        return "";
    }

    private static String createNewTable(String name, String[] cols) {
        StringJoiner joiner = new StringJoiner(", ");
        for (int i = 0; i < cols.length-1; i++) {
            joiner.add(cols[i]);
        }

        String colSentence = joiner.toString() + " and " + cols[cols.length-1];
        System.out.printf("You are trying to create a table named %s with the columns %s\n", name, colSentence);
        return "";
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
            System.out.println("Sucessfully store the table " + name);
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

    private static String insertRow(String expr, Database db) {
        Matcher m = INSERT_CLS.matcher(expr);
        String table_name = m.group(1);
        String [] value_lst = m.group(2).split(",");
        List<String> row_str = new ArrayList<String>(Arrays.asList(value_lst));
        ArrayList<MSQContainer> row_ctn = new ArrayList<>();
        for (String s : row_str) {
            row_ctn.add(new MSQContainer(s));
        }
        try {
            db.selectTableByName("table_name").rowAdd(row_ctn);
        } catch (RuntimeException e) {
            System.out.println ("Error: " + e);
        }
        return "";
    }

    public static String testPrintTable(String name, Database db) {
        return printTable(name, db);
    }

    private static String printTable(String name, Database db) {
        Table t = db.selectTableByName(name);
        return t.toString();
    }

    private static void select(String expr) {
        Matcher m = SELECT_CLS.matcher(expr);
        if (!m.matches()) {
            System.err.printf("Malformed select: %s\n", expr);
            return;
        }

        select(m.group(1), m.group(2), m.group(3));
    }

    private static void select(String exprs, String tables, String conds) {
        System.out.printf("You are trying to select these expressions:" +
                " '%s' from the join of these tables: '%s', filtered by these conditions: '%s'\n", exprs, tables, conds);
    }
}
