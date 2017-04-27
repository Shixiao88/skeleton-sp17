package db;
import static org.junit.Assert.*;
import org.junit.Rule;
import org.junit.Test;

import javax.xml.crypto.Data;
import java.util.ArrayList;
import java.util.Arrays;


/**
 * Created by Administrator on 2017/4/25.
 */
public class TestDatabase {

    @Test
    public void TestDatabase() {
        Database db = new Database();
        Parse.testLoadTable("test", db);
        String res = Parse.testPrintTable("test", db);
        System.out.println(res);
    }

    @Test
    public void TestCreateNewTable() {
        Database db = new Database();
        String[] title = {"x int", "y int", "z int"};
        String res = Parse.testCreateNewTable("t", title, db);
        String print_res = Parse.testPrintTable("t", db);
        System.out.println(print_res);
    }

    @Test
    public void TestInsertRow() {
        Database db = new Database();
        Parse.testLoadTable("t1", db);
        String s = Parse.TestInsertRow("t1 values ,2", db);
    }

    @Test
    public void TestSelect() {
        // select "*" from single table
        Database db = new Database();
        Parse.testLoadTable("test", db);
        assertEquals(db.selectTableByName("test").toString(),
                Parse.testSelect("* from test", db));

        Parse.testLoadTable("t1", db);
        Parse.testLoadTable("t2", db);
        Parse.testLoadTable("t4", db);
        /*      x int, y int, z int, d int, c int
         *      8,3,9,2,2
         **/
        System.out.println(Parse.testSelect("* from t1,t2,t4", db));

        // sekect certain columns from one table
        System.out.println(Parse.testSelect("T from test", db));

        // select certain column from tables
        System.out.println(Parse.testSelect("x,d,c from t1,t2,t4", db));
        /*      x int,d int, c int
         *      8,2,2
         **/
    }

}
