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

    Table table = new Table("test table", "test/test.tbl");

    @Test
    public void testConstructor() {

        /* test constructor by calling the source path */

        System.out.println(table.gettitle());
        System.out.println(table.getbody());

        /* test method GETNAME()*/
        assertEquals("test table", table.getname());

        /* test method TITLEIN()*/
        assertTrue(table.titleIn("W int"));
        assertFalse(table.titleIn("22"));

        /* test method GETCOLUMNNOM()*/
        assertEquals(5, table.getColumnNum());

        /* test method GETROWNUM() */
        assertEquals(12, table.getRowNum());

        /* test method COLUMNGET() */
        ArrayList<MSQContainer> select_col = table.columnGet("T str");
        assertEquals(12, select_col.size());
        assertEquals("'Golden Bears'", select_col.get(0));

        /* test method ROWGET() */
        ArrayList<MSQContainer> select_row  = table.rowGet(0);
        assertEquals(5, select_row.size());
        assertEquals("2016", select_row.get(1));
        ArrayList<MSQContainer> select_row1  = table.rowGet(110);
        assertEquals(null, select_row1);

        /* test method COLUMNADD() */
        ArrayList<MSQContainer> col = new ArrayList<>();
        col.add(new MSQContainer("12", "int"));
        col.add(new MSQContainer("22", "int"));
        table.columnAdd("T int", col);
        assertEquals(5, table.getColumnNum());
        assertEquals("12", table.getbody().get(0).get(4));
        assertEquals(null, table.getbody().get(4).get(4));
        table.columnAdd("New Col", col);
        assertEquals(6, table.getColumnNum());
//      System.out.print(table.gettitle());
        assertEquals((Integer) 5, table.gettitle().get("New Col"));
        assertEquals("12", table.getbody().get(0).get(5));
        assertEquals(null, table.getbody().get(4).get(5));

        /* test method COLUMNDEL()*/
        table.columnDel("T str");
        assertEquals(5, table.getColumnNum());
        assertEquals(4, table.getTitleIndex("New Col"));

        /* test method ROWADD() */
        ArrayList<MSQContainer> row = new ArrayList<>();
        row.add(new MSQContainer("'MNS'", "string"));
        row.add(new MSQContainer("2017", "int"));
        table.rowAdd(row);
        assertEquals(13, table.getRowNum());
        assertEquals("'MNS'", table.getbody().get(12).get(0));
        assertEquals(null, table.getbody().get(12).get(3));

        /* test method ROWADD() with index */
        ArrayList<MSQContainer> row2 = new ArrayList<>();
        row2.add(new MSQContainer("'MNS2'", "string"));
        row2.add(new MSQContainer("2018", "int"));
        table.rowAdd(row2, 0);
        assertEquals(14, table.getRowNum());
//        System.out.print(table.gettitle());
//        System.out.print(table.getbody());
        assertEquals("'MNS2'", table.getbody().get(0).get(0));
        assertEquals("2018", table.getbody().get(0).get(1));
        assertEquals(null, table.getbody().get(0).get(2));

        /* test method ROWDEL() with row index */
        Table table3 = new Table("test_table2", "test/test.tbl");
        table3.rowDel(0);
        assertEquals(11, table3.getRowNum());
        assertEquals("2015", table3.getbody().get(0).get(1));
    }

    @Test
    public void TestCopy() {
        Table target = new Table("target", "test/t1.tbl");
        Table copy = target.copy("copy");
        assertEquals(target.gettitle(), copy.gettitle());
        assertEquals(target.getbody(), copy.getbody());
        copy.rowDel(0);
        assertNotEquals(target.getbody(), copy.getbody());
        assertEquals(target.getRowNum()-1, copy.getRowNum());
    }

    @Rule
    public ExpectedException expectedEx = ExpectedException.none();

    @Test
    public void TestExceptionCallColumnAddRowAdd() throws Exception {
        expectedEx.expect(RuntimeException.class);
        expectedEx.expectMessage("the added element is too long!");

        /* test when add a too long column*/
        ArrayList<MSQContainer> ele = new ArrayList<>();
        for (int i=0; i<100; i+=1) {
            ele.add(new MSQContainer("'fantastic!'", "string"));
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
