package db;
import static org.junit.Assert.*;

import edu.princeton.cs.introcs.In;
import org.junit.Test;

import java.lang.reflect.Array;
import java.util.*;

/**
 * Created by Xiao Shi on 2017/4/18.
 */
public class TestTable {

    @Test
    public void testConstructor() {
        ArrayList<String> rs = ReadSource.readSource(new In("test/test.tbl"));
        ParseSource ps = new ParseSource(rs);
        TableBuilder tb = new TableBuilder("test table", ps);
        Table table = new Table("test table", ps);

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
        assertEquals("12", table.body.get(0).get(5));
        assertEquals(null, table.body.get(4).get(5));




    }

}
