package db;
import static org.junit.Assert.*;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.*;

/**
 * Created by Xiao Shi on 2017/4/18.
 */
public class TestTable {

    Table table = new Table("test_table", "test");

    @Test
    public void testConstructor() {

        /* test of source constructor with the tbl name will be table's own name*/

        Table table_auto_name = new Table("test");
        assertEquals("test", table_auto_name.getname());
        assertEquals(5, table_auto_name.getColumnNum());
        assertEquals(12, table_auto_name.getRowNum());

        /* test constructor by calling the source path */

//        System.out.println(table.gettitle());
//        System.out.println(table.getbody());

        /* test method GETNAME()*/
        assertEquals("test_table", table.getname());

        /* test method TITLEIN()*/
        assertTrue(table.titleIn("W int"));
        assertFalse(table.titleIn("22"));

        /* test method GETCOLUMNNOM()*/
        assertEquals(5, table.getColumnNum());

        /* test method GETROWNUM() */
        assertEquals(12, table.getRowNum());

        /* test method COLUMNGET() */
        ArrayList<MSQContainer> select_col = table.columnGet("T string");
        assertEquals(12, select_col.size());
        assertEquals("'Golden Bears'", select_col.get(0).toString());

        /* test method ROWGET() */
        ArrayList<MSQContainer> select_row  = table.rowGet(0);
        assertEquals(5, select_row.size());
        assertEquals("2016", select_row.get(1).toString());
        ArrayList<MSQContainer> select_row1  = table.rowGet(110);
        assertEquals(null, select_row1);

        /* test method COLUMNADD() */
        ArrayList<MSQContainer> col = new ArrayList<>();
        col.add(new MSQContainer("12", "int"));
        col.add(new MSQContainer("22", "int"));
        table.columnAdd("T int", col);
        assertEquals(5, table.getColumnNum());
        assertEquals("12", table.getbody().get(0).get(4).toString());
        assertEquals("NOVALUE", table.getbody().get(4).get(4).toString());
        table.columnAdd("Newcol int", col);
        assertEquals(6, table.getColumnNum());
//      System.out.print(table.gettitle());
        assertEquals(5, table.getTitleIndex("Newcol int"));
        assertEquals("12", table.getbody().get(0).get(5).toString());
        assertEquals("NOVALUE", table.getbody().get(4).get(5).toString());

        /* test method COLUMNDEL()*/
        //System.out.println(table.gettitle());
        table.columnDel("T string");
        assertEquals(5, table.getColumnNum());
        assertEquals(4, table.getTitleIndex("Newcol int"));

        /* test method ROWADD() */
        ArrayList<MSQContainer> row = new ArrayList<>();
        System.out.println(table);
        row.add(new MSQContainer("2017", "int"));
        row.add(new MSQContainer("10", "int"));
        table.rowAdd(row);
        assertEquals(13, table.getRowNum());
        assertEquals("2017", table.getbody().get(12).get(0).toString());
        assertEquals("NOVALUE", table.getbody().get(12).get(3).toString());

        /* test method ROWADD() with index */
        ArrayList<MSQContainer> row2 = new ArrayList<>();
        row2.add(new MSQContainer("2018", "int"));
        row2.add(new MSQContainer("2", "int"));
        table.rowAdd(row2, 0);
        assertEquals(14, table.getRowNum());
//        System.out.print(table.gettitle());
//        System.out.print(table.getbody());
        assertEquals("2018", table.getbody().get(0).get(0).toString());
        assertEquals("2", table.getbody().get(0).get(1).toString());
        assertEquals("NOVALUE", table.getbody().get(0).get(2).toString());

        /* test method ROWDEL() with row index */
        Table table3 = new Table("test_table2", "test");
        table3.rowDel(0);
        assertEquals(11, table3.getRowNum());
        assertEquals("2015", table3.getbody().get(0).get(1).toString());

        Table table_for_prt = new Table("t1");
        String prt = "x int,y int\n2,5\n8,3\n13,7";
        assertEquals(prt, table_for_prt.toString());
    }

    @Test
    /* I made a real mistake to add all the test into one big giant method, this is very not flexible if I want to
    * add in more methods, fortunatly I do others in small pieces */
    public void TestRowAddEmptyElInMiddle() {
        Table t = new Table("test");
        ArrayList<MSQContainer> row = new ArrayList<>();
        row.add(new MSQContainer(""));
        row.add(new MSQContainer("2017"));
        t.rowAdd(row);
        assertEquals(13, t.getRowNum());
        assertEquals("string", t.getbody().get(12).get(0).getRealType());
        assertEquals("int", t.getbody().get(12).get(4).getRealType());
    }

    public void TestColAddEmptyElInMiddle() {
        Table t = new Table("test");
        ArrayList<MSQContainer> col = new ArrayList<>();
        col.add(new MSQContainer("111"));
        col.add(new MSQContainer(""));
        col.add(new MSQContainer("2017"));
        t.columnAdd("title int", col);
        assertEquals(6, t.getColumnNum());
        assertEquals("int", t.getbody().get(0).get(6).getRealType());
        assertEquals("int", t.getbody().get(1).get(6).getRealType());
    }

    @Test
    public void TestCopy() {
        Table target = new Table("target", "test");
        Table copy = target.copy("copy");
        assertEquals(target.getRowNum(), copy.getRowNum());
        assertEquals(target.getColumnNum(), copy.getColumnNum());
        assertEquals(target.toString(), copy.toString());
        assertNotEquals(target.gettitle(), copy.gettitle());
        assertNotEquals(target.getbody(), copy.getbody());
        copy.rowDel(0);
        assertEquals(target.getRowNum()-1, copy.getRowNum());
    }

    @Test
    public void TestSaveToFile() {
        Table t = new Table("test");
        t.saveTableToFile("copy_test");

        Table t1 = new Table("t1");
        t1.saveTableToFile("copy_test");
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
            ele.add(new MSQContainer("'fantastic'", "string"));
        }
        MSQColName t = new MSQColName("newcol string");
        table.columnAdd("newcol string", ele);

        /* test when add a too log row */
//        MSQContainer first =new MSQContainer("'year'", "string");
//        ele.add(0, first);
        table.rowAdd(ele);
    }

    @Test
    public void TestExceptionCallRowAddMalType() throws Exception {
        expectedEx.expect(RuntimeException.class);
        expectedEx.expectMessage("added in row must be the right type");
        Table t = new Table("t1");
        ArrayList<MSQContainer> row = new ArrayList<>();
        row.add(new MSQContainer("2"));
        row.add(new MSQContainer("'s'"));
        t.rowAdd(row);
    }

    @Test
    public void TestExceptionCallColAddMalType() throws Exception {
        expectedEx.expect(RuntimeException.class);
        expectedEx.expectMessage("added in column must be the right type");
        Table t = new Table("t1");
        ArrayList<MSQContainer> col = new ArrayList<>();
        col.add(new MSQContainer("2"));
        col.add(new MSQContainer("'s'"));
        t.columnAdd("title int", col);
    }

    @Test
    public void TestExceptionsCallColumnDel() throws Exception {
        expectedEx.expect(RuntimeException.class);
        expectedEx.expectMessage("Can not delete the title because does not exist in the table");

        /* test when delete no existing column*/
        table.columnDel("something");
    }
}
