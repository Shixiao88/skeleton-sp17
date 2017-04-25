package db;
import static org.junit.Assert.*;
import org.junit.Rule;
import org.junit.Test;


/**
 * Created by Administrator on 2017/4/25.
 */
public class TestDatabase {

    @Test
    public void TestDatabase() {
        Database db = new Database();
        Parse.testLoadTable("test", db);
        String res = Parse.testPrintTable("test", db);
        System.out.print(res);
    }

    @Test
    public void TestMain () {

    }

}
