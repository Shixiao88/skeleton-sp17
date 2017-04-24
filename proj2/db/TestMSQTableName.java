package db;
import static org.junit.Assert.*;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

/**
 * Created by Xiao Shi on 2017/4/24.
 */
public class TestMSQTableName {

    @Test
    public void TestTableNameParse() {
        String s1 = "table1_testall";
        MSQTableName name1 = new MSQTableName(s1);
        assertEquals("table1_testall", name1.toString());
        assertEquals("_MSQTableName", name1.getType());
    }

    @Rule
    public ExpectedException expectedEx = ExpectedException.none();

    @Test
    public void TestExceptionStringParse() throws Exception {
        expectedEx.expect(RuntimeException.class);
        expectedEx.expectMessage("only letters, numbers and underscores, and must start with a letter.");
        // the one has space and other symbols
        String s1 = "table ..num#1";
        MSQTableName name1 = new MSQTableName(s1);
        // the one not start with letter
        String s2 = "11table";
        MSQTableName name2 = new MSQTableName(s2);
        // the one with empty content
        String s3 = "";
        MSQTableName name3 = new MSQTableName(s3);
    }
}
