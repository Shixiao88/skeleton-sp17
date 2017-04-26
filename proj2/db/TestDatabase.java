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
    public void TestInsertRow() {
        Database db = new Database();
        Parse.testLoadTable("t1", db);
        String s = Parse.TestInsertRow("t1 values 1,2", db);
        System.out.println(s);
    }

    @Test
    public void TestMain () {

    }

}
