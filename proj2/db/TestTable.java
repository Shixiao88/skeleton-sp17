package db;
import static org.junit.Assert.*;

import edu.princeton.cs.introcs.In;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.lang.reflect.Array;
import java.util.*;

/**
 * Created by Xiao Shi on 2017/4/18.
 */
public class TestTable {

    ArrayList<String> rs = ReadSource.readSource(new In("test/test.tbl"));
    ParseSource ps = new ParseSource(rs);
    TableBuilder tb = new TableBuilder("test table", ps);
    Table table = new Table(tb);


    @Test
    public void testConstructor() {

        /* test method GETNAME()*/
        assertEquals(tb.gettaName(), table.getname());

        /* test method TITLEIN()*/
        assertTrue(table.titleIn("W int"));
        assertFalse(table.titleIn("22"));

        /* test method GETCOLUMNNOM()*/
        assertEquals(5, table.getColumnNum());

        /* test method GETROWNUM() */
        assertEquals(12, table.getRowNum());

        /* test method COLUMNADD() */
        ArrayList<String> col = new ArrayList<>();
        col.add("12");
        col.add("22");
        table.columnAdd("T int", col);
        assertEquals(5, table.getColumnNum());
        assertEquals("12", table.getbody().get(0).get(4));
        assertEquals(null, table.getbody().get(4).get(4));
        table.columnAdd("New Col", col);
        assertEquals(6, table.getColumnNum());
//      System.out.print(table.gettitle());
        assertEquals((Integer)5, table.gettitle().get("New Col"));
        assertEquals("12", table.getbody().get(0).get(5));
        assertEquals(null, table.getbody().get(4).get(5));

        /* test method COLUMNDEL()*/
        table.columnDel("T str");
        assertEquals(5, table.getColumnNum());
        assertEquals(4, table.getTitleIndex("New Col"));

        /* test method ROWADD() */
        ArrayList<String> row = new ArrayList<>();
        row.add("MNS");
        row.add("2017");
        table.rowAdd(row);
        assertEquals(13, table.getRowNum());
        assertEquals("MNS", table.getbody().get(12).get(0));
        assertEquals(null, table.getbody().get(12).get(3));

        /* test method ROWADD() with index */
        ArrayList<String> row2 = new ArrayList<>();
        row2.add("MNS2");
        row2.add("2018");
        table.rowAdd(row2, 0);
        assertEquals(14, table.getRowNum());
//        System.out.print(table.gettitle());
//        System.out.print(table.getbody());
        assertEquals("MNS2", table.getbody().get(0).get(0));
        assertEquals("2018", table.getbody().get(0).get(1));
        assertEquals(null, table.getbody().get(0).get(2));
    }

    @Rule
    public ExpectedException expectedEx = ExpectedException.none();

    @Test
    public void TestExceptionCallColumnAddRowAdd() throws Exception {
        expectedEx.expect(RuntimeException.class);
        expectedEx.expectMessage("the added element is too long!");

        /* test when add a too long column*/
        ArrayList<String> ele = new ArrayList<>();
        for (int i=0; i<100; i+=1) {
            ele.add("fantastic!");
        }
        table.columnAdd("newCol String", ele);

        /* test when add a too log row */
        table.rowAdd(ele);
    }

    @Test
    public void TestExceptionsCallColumnDel() throws Exception {
        expectedEx.expect(RuntimeException.class);
        expectedEx.expectMessage("Can not delete the title because does not exist in the table");

        /* test when delete no existing column*/
        table.columnDel("something");
    }

}
