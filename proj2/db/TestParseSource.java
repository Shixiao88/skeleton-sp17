package db;
import static org.junit.Assert.*;
import org.junit.Test;
import edu.princeton.cs.introcs.In;
import java.util.*;


/**
 * Created by Administrator on 2017/4/17.
 */
public class TestParseSource {

    @Test
    public void checkReadSource() {
        In in = new In("examples/test.tbl");
        ArrayList<String> s = ReadSource.readSource(in);
        assertEquals(13, s.size());
        System.out.println(s);
    }

    @Test
    public void checkParseSource() {
        In in = new In("test/test.tbl");
        ArrayList<String> s = ReadSource.readSource(in);
        ParseSource obj = new ParseSource(s);
        ArrayList<String> title = obj.parseSourceTitle();
        System.out.println(title);
        assertEquals(5, title.size());
        ArrayList<ArrayList<String>> body = obj.parseSourceBody();
        System.out.println(body);
        assertEquals(12, body.size());
        assertEquals(5, body.get(0).size());

        In in1 = new In("test/test1.tbl");

        ArrayList<String> s1 = ReadSource.readSource(in1);
        ParseSource obj1 = new ParseSource(s1);
        ArrayList<String> title1 = obj1.parseSourceTitle();
        System.out.println(title1);
        assertEquals(5, title1.size());
        ArrayList<ArrayList<String>> body1 = obj1.parseSourceBody();
        System.out.println(body1);
        assertEquals(0, body1.size());
        assertEquals(0, body1.get(0).size());
    }
}
