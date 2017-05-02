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
        Parse.TestInsertRow("t1 values ,2", db);
        System.out.println(Parse.testPrintTable("t1", db));
        /*
        x int,y int
        2,5
        8,3
        13,7
        NOVALUE,2
        * */
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
        Parse.testLoadTable("records", db);
        Parse.testLoadTable("test_Novalue_Nan2", db);
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

        // select more than one column operation from one table
        System.out.println(Parse.testSelect("City,Season,Wins/Losses as Ratio from teams,records", db));
        /*      City string,Season int,Ratio int
                'New York',2015,1
                'New York',2014,0
                'New York',2013,0
                'Pittsburgh',2015,1
                'Pittsburgh',2014,2
                'Pittsburgh',2013,1
                'New England',2015,3
                'New England',2014,3
                'New England',2013,3
                'Berkeley',2016,0
                'Berkeley',2015,1
                'Berkeley',2014,0
        */

        System.out.println(Parse.testSelect("City,Season,Wins*Losses as Ratio from teams,records", db));
         /*      'New York',2015,6480
                'New York',2014,6557
                'New York',2013,6512
                'Pittsburgh',2015,60
                'Pittsburgh',2014,55
                'Pittsburgh',2013,64
                'New England',2015,48
                'New England',2014,48
                'New England',2013,48
                'Berkeley',2016,35
                'Berkeley',2015,40
                'Berkeley',2014,35
        */

        // select more than one column operation from more than one tables
        System.out.println(Parse.testSelect("YearEstablished, LastName + TeamName as LstTeamName from fans,teams", db));

        System.out.println("\ntest the condition clause\n");


        System.out.println(Parse.testSelect("* from t1,t2,t4 where x>10", db));
        /*      x int, y int, z int, d int, c int
         **/

        System.out.println(Parse.testSelect("* from t1 where x<10 and y==3", db));
        /*      x int, y int
         *      8,3
         **/

        // select column operations from one table
        System.out.println(Parse.testSelect("x+z as f from t2 where f==6", db));
        /*      f int
         *      6
         **/

        // select one column and one column operation from one table
        System.out.println(Parse.testSelect("x,d+c as f from t4 where x == 0", db));
        /*      x int,f int
         *      0,16
         **/

        // select more than one column operation from one table
        System.out.println(Parse.testSelect("YearEstablished,TeamName + City as f from teams where YearEstablished < 2000", db));
        /*      YearEstablished int,f string
                1962,'Mets New York'
                1933,'Steelers Pittsburgh'
                1960,'Patriots New England'
                1886,'Golden Bears Berkeley'
        */

        // condition have NOVALUE and NAN
        System.out.println(Parse.testSelect("T,W from test_Novalue_Nan2 where T < 'Patriots' and W > 10", db));
        /*      T string,W int,T int
         *      NOVALUE,11,0
         *      'Mets',NAN,0
         *      'Mets',74,0
         */

        System.out.println(Parse.testSelect("F,W from test_Novalue_Nan2 where F == 0", db));
        /*   W int,F float
             8,NOVALUE
             10,NOVALUE
             NAN,0.000
         * */

        System.out.println(Parse.testSelect("T,W from test_Novalue_Nan2 where T == ''", db));
        /*   T String,W int,T int
             NOVALUE,8,0
             NOVALUE,11,0
         * */

        System.out.println(Parse.testSelect("* from t1 where y >= 5 ", db));
        /*   x int,y int
             2,5
             13,7
         * */

        System.out.println(Parse.testSelect("* from t1 where y != 5 and x != 13", db));
        /*   x int,y int
             8,3
         * */
    }

}
