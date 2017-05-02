package db;
import java.lang.reflect.Array;
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

        // test add of one column of int and one int
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

        // test add of one column int and one column float NOVALUE
        ArrayList<MSQContainer> col_res12 = Operation.add("W", "FNO", test2);
        assertEquals("5.000", col_res12.get(0).toString());
        assertEquals("12.000", col_res12.get(11).toString());

        // test add of one column float and one column float NOVALUE
        ArrayList<MSQContainer> col_res13 = Operation.add("F", "FNO", test2);
        assertEquals("1.000", col_res13.get(0).toString());
        assertEquals("0.000", col_res13.get(11).toString());

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

        // test add of one column of float and one column of int
        ArrayList<MSQContainer> col_res10 = Operation.add("F", "W", test2);
        assertEquals("6.000", col_res10.get(0).toString());
        assertEquals("12.000", col_res10.get(11).toString());

        // test add of two columns of NOVALUE
        ArrayList<MSQContainer> col_res14 = Operation.add("SNO", "FNO", test2);
        assertEquals("NOVALUE", col_res14.get(0).toString());
        assertEquals("NOVALUE", col_res14.get(11).toString());

        // tset add of one column of NOVALUE and one column of NAN
        ArrayList<MSQContainer> col_res11 = Operation.add("SNO", "WNO", test2);
        assertEquals("NAN", col_res11.get(0).toString());
        assertEquals("NAN", col_res11.get(11).toString());

    }

    @Test
    public void TestMinus() {
        // test sub of two columns of int
        Table test = new Table("test_Novalue_Nan");
        ArrayList<MSQContainer> col_res = Operation.minus("W", "L", test);
        assertEquals("-2", col_res.get(0).toString());
        assertEquals("4", col_res.get(3).toString());
        assertEquals("18", col_res.get(6).toString());
        assertEquals("8", col_res.get(11).toString());

        // test add of one column of int and one int
        ArrayList<MSQContainer> col_res2 = Operation.minus("W", "10", test);
        assertEquals("-5", col_res2.get(0).toString());
        assertEquals("0", col_res2.get(3).toString());
        assertEquals("80", col_res2.get(6).toString());
        assertEquals("2", col_res2.get(11).toString());

        // test add of one column int and one column int NOVALUE
        ArrayList<MSQContainer> col_res3 = Operation.minus("S", "SNO", test);
        assertEquals("2016", col_res3.get(0).toString());
        assertEquals("2013", col_res3.get(11).toString());

        // test add of one column int and one column float NOVALUE
        ArrayList<MSQContainer> col_res4 = Operation.minus("W", "FNO", test);
        assertEquals("5.000", col_res4.get(0).toString());
        assertEquals("12.000", col_res4.get(11).toString());

        // test add of one column float and one column float NOVALUE
        ArrayList<MSQContainer> col_res5 = Operation.minus("F", "FNO", test);
        assertEquals("1.000", col_res5.get(0).toString());
        assertEquals("0.000", col_res5.get(11).toString());

        // test add of one column int and one column int NAN
        ArrayList<MSQContainer> col_res6 = Operation.minus("S", "WNO", test);
        assertEquals("NAN", col_res6.get(0).toString());
        assertEquals("NAN", col_res6.get(11).toString());

        // test add of two columns of float
        ArrayList<MSQContainer> col_res7 = Operation.minus("F", "F", test);
        assertEquals("0.000", col_res7.get(0).toString());
        assertEquals("0.000", col_res7.get(5).toString());

        // test add of one column of float and one int
        ArrayList<MSQContainer> col_res8 = Operation.minus("F", "0.0", test);
        assertEquals("1.000", col_res8.get(0).toString());
        assertEquals("3.000", col_res8.get(5).toString());

        // test add of one column of int and one column of float
        ArrayList<MSQContainer> col_res10 = Operation.minus("W", "F", test);
        assertEquals("4.000", col_res10.get(0).toString());
        assertEquals("12.000", col_res10.get(11).toString());

        // tset add of one column of NOVALUE and one column of NAN
        ArrayList<MSQContainer> col_res11 = Operation.minus("SNO", "WNO", test);
        assertEquals("NAN", col_res11.get(0).toString());
        assertEquals("NAN", col_res11.get(11).toString());

        // test add of two columns of NOVALUE
        ArrayList<MSQContainer> col_res13 = Operation.minus("SNO", "FNO", test);
        assertEquals("NOVALUE", col_res13.get(0).toString());
        assertEquals("NOVALUE", col_res13.get(11).toString());

    }

    @Test
    public void TestComparison() {
        // test two string comparison
        Table test = new Table("test_Novalue_Nan");
        ArrayList<Integer> col_res1 = Operation.compare("T", "'Mets'", test);
        assertTrue(col_res1.get(0) < 0);
        assertTrue(col_res1.get(3) > 0);
        assertTrue(col_res1.get(6) == 0);
        assertTrue(col_res1.get(9) > 0);

        // test two int comparison
        ArrayList<Integer> col_res2 = Operation.compare("S", "2014",test);
        assertTrue(col_res2.get(0) > 0);
        assertTrue(col_res2.get(1) > 0);
        assertTrue(col_res2.get(2) == 0);
        assertTrue(col_res2.get(5) < 0);

        // test int column compare to float
        ArrayList<Integer> col_res3 = Operation.compare("S", "2014.0",test);
        assertTrue(col_res3.get(0) > 0);
        assertTrue(col_res3.get(1) > 0);
        assertTrue(col_res3.get(2) == 0);
        assertTrue(col_res3.get(5) < 0);

        // test NOVALUE string column compare to string
        ArrayList<Integer> col_res4 = Operation.compare("TNO", "'Mets'", test);
        assertTrue(col_res4.get(0) < 0);
        assertTrue(col_res4.get(3) < 0);
        assertTrue(col_res4.get(6) < 0);
        assertTrue(col_res4.get(9) < 0);


        // test NOVALUE int column compare toint
        ArrayList<Integer> col_res5 = Operation.compare("SNO", "0", test);
        assertTrue(col_res5.get(0) == 0);
        assertTrue(col_res5.get(3) == 0);
        assertTrue(col_res5.get(6) == 0);
        assertTrue(col_res5.get(9) == 0);

        // test NOVALUE int column compare toint
        ArrayList<Integer> col_res6 = Operation.compare("WNO", "10000", test);
        assertTrue(col_res6.get(0) > 0);
        assertTrue(col_res6.get(3) > 0);
        assertTrue(col_res6.get(6) > 0);
        assertTrue(col_res6.get(9) > 0);

        // test NAN with NAN
        ArrayList<Integer> col_res7 = Operation.compare("WNO", "NAN", test);
        assertTrue(col_res7.get(0) == 0);
        assertTrue(col_res7.get(3) == 0);
        assertTrue(col_res7.get(6) == 0);
        assertTrue(col_res7.get(9) == 0);

        // test NOVALUE string with NOVALUE
        ArrayList<Integer> col_res8 = Operation.compare("TNO", "NOVALUE", test);
        assertTrue(col_res8.get(0) == 0);
        assertTrue(col_res8.get(3) == 0);
        assertTrue(col_res8.get(6) == 0);
        assertTrue(col_res8.get(9) == 0);
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


    @Test
    public void TestExceptionCallSub() throws Exception {
        expectedEx.expect(RuntimeException.class);
        expectedEx.expectMessage("Bad element type doing sub operation");

        Table test = new Table("test_Novalue_Nan");
        // error when sub two columns of string
        Operation.minus("T", "T", test);
        // error when sub one column of string and one string literal
        Operation.minus("T", "'haloha'", test);
        // error when string sub int
        Operation.minus("T", "S", test);
        // error when string sub int type NOVALUE
        Operation.minus("T", "SNO", test);
        // error when string sub int type NAN
        Operation.minus("T", "WNO", test);
        // error when sub different type anyway
        Operation.minus("TNO", "WNO", test);
    }

    @Test
    public void TestExceptionCallCompare() throws Exception {
        expectedEx.expect(RuntimeException.class);
        expectedEx.expectMessage("Bad formed comparision form");
        Table test = new Table("test_Novalue_Nan");
        // error when compare string to int
        Operation.compare("T", "200", test);
        // error when compare int to string
        Operation.compare("S", "'200'", test);
        // error when compare NOVALUE string to int
        Operation.compare("TNO", "0", test);

    }



}
