package db;
import static org.junit.Assert.*;
import org.junit.Test;
import java.util.*;
import edu.princeton.cs.introcs.In;

/**
 * Created by Xiao Shi on 2017/4/18.
 */
public class TestTableBuilder {

    @Test
    public void checkNormal() {
        ArrayList<String> rs = ReadSource.readSource(new In("test/test.tbl"));
        ParseSource ps = new ParseSource(rs);
        TableBuilder tb = new TableBuilder("test_table", ps);
        assertEquals("test_table", tb.gettaName());
        assertEquals(5, tb.gettaTitle().size());
        assertEquals("2016", tb.gettaBody().get(0).get(1).toString());
        assertEquals("'Patriots'", tb.gettaBody().get(11).get(0).toString());
    }

    @Test
    public void checkOnlyTitle() {
        ArrayList<String>rs1 = ReadSource.readSource(new In("test/test1.tbl"));
        ParseSource ps1 = new ParseSource(rs1);
        TableBuilder tb1 = new TableBuilder("test_table2", ps1);
        assertEquals("test_table2", tb1.gettaName());
        System.out.println(tb1.gettaTitle());
        assertEquals(5, tb1.gettaTitle().size());
        assertEquals(0, tb1.gettaBody().size());
    }

    @Test
    public void checkEmptyTalbe() {
        ArrayList<String>rs2 = ReadSource.readSource(new In("test/test2.tbl"));
        ParseSource ps2 = new ParseSource(rs2);
        TableBuilder tb2 = new TableBuilder("test_table3", ps2);
        assertEquals("test_table3", tb2.gettaName());
        assertEquals(0, tb2.gettaTitle().size());
        assertEquals(0, tb2.gettaBody().size());
    }

}
