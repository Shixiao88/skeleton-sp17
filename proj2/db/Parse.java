package db;

import sun.security.util.ByteArrayLexOrder;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.StringJoiner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    // Stage 3 syntax, the subexpressio
    private static final Pattern
            SUB_COMMA = Pattern.compile(COMMA),
            SUB_AND = Pattern.compile(AND);


    public static void main(String[] args) {
        if (args.length != 1) {
            System.err.println("Expected a single query argument");
            return;
        }

        eval(args[0]);
    }

    private static void eval(String query) {
        Matcher m;
        if ((m = CREATE_CMD.matcher(query)).matches()) {
            createTable(m.group(1));
        } else if ((m = LOAD_CMD.matcher(query)).matches()) {
            loadTable(m.group(1));
        } else if ((m = STORE_CMD.matcher(query)).matches()) {
            storeTable(m.group(1));
        } else if ((m = DROP_CMD.matcher(query)).matches()) {
            dropTable(m.group(1));
        } else if ((m = INSERT_CMD.matcher(query)).matches()) {
            insertRow(m.group(1));
        } else if ((m = PRINT_CMD.matcher(query)).matches()) {
            printTable(m.group(1));
        } else if ((m = SELECT_CMD.matcher(query)).matches()) {
            select(m.group(1));
        } else {
            System.err.printf("Malformed query: %s\n", query);
        }
    }

    private static ArrayList<String> evalSubExpr(String args) {
        Matcher m_sub;
        ArrayList<String> sub_args = new ArrayList<>();
        if (!(SUB_AND.matcher(args).matches() || SUB_COMMA.matcher(args).matches())) {
            sub_args.add(args);
        } else if ((m_sub = SUB_AND.matcher(args)).matches() ||
                   (m_sub = SUB_COMMA.matcher(args)).matches()) {
            sub_args.addAll(evalSubExpr(m_sub.group(0)));
            sub_args.addAll(evalSubExpr(m_sub.group(2)));
        }
        return sub_args;
    }

    private static void createTable(String expr) {
        Matcher m;
        if ((m = CREATE_NEW.matcher(expr)).matches()) {
            createNewTable(m.group(1), m.group(2).split(COMMA));
        } else if ((m = CREATE_SEL.matcher(expr)).matches()) {
            createSelectedTable(m.group(1), m.group(2), m.group(3), m.group(4));
        } else {
            System.err.printf("Malformed create: %s\n", expr);
        }
    }

    private static void createNewTable(String name, String[] cols) {
        StringJoiner joiner = new StringJoiner(", ");
        for (int i = 0; i < cols.length-1; i++) {
            joiner.add(cols[i]);
        }

        String colSentence = joiner.toString() + " and " + cols[cols.length-1];
        System.out.printf("You are trying to create a table named %s with the columns %s\n", name, colSentence);
    }

    private static void createSelectedTable(String name, String exprs, String tables, String conds) {
        System.out.printf("You are trying to create a table named %s by selecting these expressions:" +
                " '%s' from the join of these tables: '%s', filtered by these conditions: '%s'\n", name, exprs, tables, conds);
    }

    private static void loadTable(String name) {
        System.out.printf("You are trying to load the table named %s\n", name);
    }

    private static void storeTable(String name) {
        System.out.printf("You are trying to store the table named %s\n", name);
    }

    private static void dropTable(String name) {
        System.out.printf("You are trying to drop the table named %s\n", name);
    }

    private static void insertRow(String expr) {
        Matcher m = INSERT_CLS.matcher(expr);
        if (!m.matches()) {
            System.err.printf("Malformed insert: %s\n", expr);
            return;
        }

        System.out.printf("You are trying to insert the row \"%s\" into the table %s\n", m.group(2), m.group(1));
    }

    private static void printTable(String name) {
        System.out.printf("You are trying to print the table named %s\n", name);
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