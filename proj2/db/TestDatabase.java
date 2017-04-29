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

    public void TestCreateSelectTable() {
        Database db = new Database();
        Parse.testLoadTable("test", db);
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
        Parse.testLoadTable("fans", db);
        Parse.testLoadTable("teams", db);
        /*      x int, y int, z int, d int, c int
         *      8,3,9,2,2
         **/
        System.out.println(Parse.testSelect("* from t1,t2,t4", db));

        // select one column from one table
        System.out.println(Parse.testSelect("T from test", db));

        // select columns from one table
        System.out.println(Parse.testSelect("LastName,TeamName from fans", db));

        // select certain column from tables
        System.out.println(Parse.testSelect("x,d,c from t1,t2,t4", db));
        /*      x int,d int, c int
         *      8,2,2
         **/

        // select column operations from one table
        System.out.println(Parse.testSelect("d+c as f from t4", db));
        /*      f int
         *      16
         *      4
         **/
        System.out.println(db.selectTableByName("t4").toString());
        /*      unchanged
         * */

        // select one column and one column operation from one table
        System.out.println(Parse.testSelect("x,d+c as f from t4", db));
        /*      x int,f int
         *      0,16
         *      8,4
         **/

        // select more than one column operation from one table
        System.out.println(Parse.testSelect("YearEstablished,TeamName + City as f from teams", db));
        /*      YearEstablished int,f string
                1962,'Mets New York'
                1933,'Steelers Pittsburgh'
                1960,'Patriots New England'
                2012,'Cloud9 Los Angeles'
                2007,'EnVyUs Charlotte'
                1886,'Golden Bears Berkeley'
        */

        // select more than one column operation from more than one tables
        System.out.println(Parse.testSelect("YearEstablished, LastName + TeamName as LstTeamName from fans,teams", db));



    }

}
