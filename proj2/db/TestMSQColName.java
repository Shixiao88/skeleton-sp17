package db;
import static org.junit.Assert.*;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import java.util.*;

/**
 * Created by Xiao Shi on 2017/4/24.
 */
public class TestMSQColName {

    @Test
    public void TestMSQColNameParse() {
        String s1 = "colum int";
        MSQColName c1 = new MSQColName(s1);
        assertEquals("colum", c1.getTitleName());
        assertEquals("colum int", c1.getValue());
        assertEquals("colum int", c1.toString());
        assertEquals("int", c1.getColType());
        assertEquals("_MSQColName", c1.getType());

        String s2 = "colum      int";
        MSQColName c2 = new MSQColName(s2);
        assertEquals("colum", c2.getTitleName());
        assertEquals("int", c2.getColType());
        assertEquals("_MSQColName", c2.getType());
        assertEquals("colum int", c2.getValue());
        assertEquals("colum int", c2.toString());

    }

    @Test
    public void TestMSQColNameAdd() {
        Table t1 = new Table("t1");
        Table t2 = new Table("t2");
        ArrayList<MSQContainer> res_compare = new ArrayList<>();
        res_compare.add(new MSQContainer("9"));
        res_compare.add(new MSQContainer("12"));
        res_compare.add(new MSQContainer("8"));
        MSQColName c1 = t1.getColNameByName("y");
        MSQColName c2 = t2.getColNameByName("z");
        assertEquals(res_compare.toString(), (c1.add(t1,c2,t2,new MSQColName("ex"))).toString());

    }

    @Rule
    public ExpectedException expectedEx = ExpectedException.none();

    @Test
    public void TestExceptionStringParse() throws Exception {
        expectedEx.expect(RuntimeException.class);
        expectedEx.expectMessage("Bad formed column names");
        // the one has space and other symbols
        String s1 = "table ..num#1";
        MSQColName name1 = new MSQColName(s1);
        // the one not start with letter
        String s2 = "11table";
        MSQColName name2 = new MSQColName(s2);
        // the one with empty content
        String s3 = "";
        MSQColName name3 = new MSQColName(s3);
        // the one with no space
        String s4 = "test";
        MSQColName name4 = new MSQColName(s4);
        // the one with wrong coltype
        String s5 = "test haloha";
        MSQColName name5 = new MSQColName(s5);
    }

}
