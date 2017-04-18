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
        TableBuilder tb = new TableBuilder("test table", ps);
        assertEquals("test table", tb.gettaName());
        //System.out.print(tb.gettaTitle().keySet().size());
        //System.out.print(tb.gettaBody());
        assertEquals((Integer)0, (Integer)tb.gettaTitle().get("T str"));
        assertEquals((Integer)4, (Integer)tb.gettaTitle().get("T int"));
        assertEquals("2016", tb.gettaBody().get(0).get(1));
        assertEquals("Patriots", tb.gettaBody().get(11).get(0));
    }

    public void checkOnlyTitle() {
        ArrayList<String>rs1 = ReadSource.readSource(new In("test/test1.tbl"));
        ParseSource ps1 = new ParseSource(rs1);
        TableBuilder tb1 = new TableBuilder("test table2", ps1);
        assertEquals("test table2", tb1.gettaName());
        assertEquals((Integer)0, (Integer)tb1.gettaTitle().get("T str"));
        assertEquals((Integer)4, (Integer)tb1.gettaTitle().get("T int"));
        assertEquals(0, tb1.gettaBody().size());
    }

    public void checkEmptyTalbe() {
        ArrayList<String>rs2 = ReadSource.readSource(new In("test/test2.tbl"));
        ParseSource ps2 = new ParseSource(rs2);
        TableBuilder tb2 = new TableBuilder("test table3", ps2);
        assertEquals("test table3", tb2.gettaName());
        assertEquals(0, tb2.gettaTitle().size());
        assertEquals(0, tb2.gettaBody().size());
    }

}
