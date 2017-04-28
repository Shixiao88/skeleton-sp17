package db;
import java.util.*;
import static org.junit.Assert.*;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

/**
 * Created by Xiao Shi on 2017/4/28.
 */
public class TestOperation {

    // tset add
    @Test
    public void TestAdd() {
        // test add of two columns of strings
        Table test = new Table("test");
        ArrayList<MSQContainer> col_res1 = Operation.add("T", "T", test);
        assertEquals("'Golden Bears Golden Bears'", col_res1.get(0).toString());
        assertEquals("'Steelers Steelers'", col_res1.get(3).toString());
        assertEquals("'Patriots Patriots'", col_res1.get(11).toString());

        // test add of one string columns and one string
        ArrayList<MSQContainer> col_res2 = Operation.add("T", "'haloha'", test);
        assertEquals("'Golden Bears haloha'", col_res2.get(0).toString());
        assertEquals("'Steelers haloha'", col_res2.get(3).toString());
        assertEquals("'Patriots haloha'", col_res2.get(11).toString());

        // test add of two columns of int
        ArrayList<MSQContainer> col_res3 = Operation.add("W", "L", test);
        assertEquals("12", col_res3.get(0).toString());
        assertEquals("16", col_res3.get(3).toString());
        assertEquals("162", col_res3.get(6).toString());
        assertEquals("16", col_res3.get(11).toString());

        // test add of one column and one int
        ArrayList<MSQContainer> col_res4 = Operation.add("W", "100", test);
        assertEquals("105", col_res4.get(0).toString());
        assertEquals("110", col_res4.get(3).toString());
        assertEquals("190", col_res4.get(6).toString());
        assertEquals("112", col_res4.get(11).toString());

        // test add of one column string and one column string NOVALUE
        Table test2 = new Table("test_Novalue_Nan");
        ArrayList<MSQContainer> col_res5 = Operation.add("T", "TNO", test2);
        assertEquals("'Golden Bears'", col_res5.get(0).toString());
        assertEquals("'Steelers'", col_res5.get(3).toString());
        assertEquals("'Patriots'", col_res5.get(11).toString());

        // test add of one column int and one column int NOVALUE
        ArrayList<MSQContainer> col_res6 = Operation.add("S", "SNO", test2);
        assertEquals("2016", col_res6.get(0).toString());
        assertEquals("2013", col_res6.get(11).toString());

        // test add of one column int and one column int NAN
        ArrayList<MSQContainer> col_res7 = Operation.add("S", "WNO", test2);
        assertEquals("NAN", col_res7.get(0).toString());
        assertEquals("NAN", col_res7.get(11).toString());

        // test add of two columns of float
        ArrayList<MSQContainer> col_res8 = Operation.add("F", "F", test2);
        assertEquals("2.000", col_res8.get(0).toString());
        assertEquals("6.000", col_res8.get(5).toString());

        // tset add of one column of float and one int
        ArrayList<MSQContainer> col_res9 = Operation.add("F", "0.0", test2);
        assertEquals("1.000", col_res9.get(0).toString());
        assertEquals("3.000", col_res9.get(5).toString());

        // test add of one colum of float and one colum of int
        ArrayList<MSQContainer> col_res10 = Operation.add("F", "W", test2);
        assertEquals("6.000", col_res10.get(0).toString());
        assertEquals("12.000", col_res10.get(11).toString());

        // tset add of one column of NOVALUE and one column of NAN
        ArrayList<MSQContainer> col_res11 = Operation.add("SNO", "WNO", test2);
        assertEquals("NAN", col_res11.get(0).toString());
        assertEquals("NAN", col_res11.get(11).toString());

    }

    @Rule
    public ExpectedException expectedEx = ExpectedException.none();

    @Test
    public void TestExceptionCallAdd() throws Exception {
        expectedEx.expect(RuntimeException.class);
        expectedEx.expectMessage("Bad element type doing add operation");

        Table test2 = new Table("test_Novalue_Nan");
        // error when string add int
        Operation.add("T", "S", test2);
        // error when string add int type NOVALUE
        Operation.add("T", "SNO", test2);
        // error when string add int type NAN
        Operation.add("T", "WNO", test2);
        // error when add different type anyway
        Operation.add("TNO", "WNO", test2);
    }

}
