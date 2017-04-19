package db;
import java.io.File;
import java.util.*;
import static org.junit.Assert.*;
import org.junit.Test;

/**
 * Created by Xiao Shi on 2017/4/19.
 */
public class TestJoinHelper {

    @Test
    public void TestCallcommonTitle() {

        /* test the first one in common */
        Table t1 = new Table ("t1", "test/t1.tbl" );
        Table t2 = new Table ("t2", "test/t2.tbl" );
        Map<String, Integer> common_title = JoinHelper.commonTitle(t1, t2);
        assertEquals(1, common_title.size());
        assertEquals((Integer)0, common_title.get("x int"));

        /* test no title in common */
        Table t3 = new Table ("t3", "test/t3.tbl" );
        Map<String, Integer> common_title2 = JoinHelper.commonTitle(t1, t3);
        assertEquals(0, common_title2.size());

        /* test more than one title in common, the order should follow the first table */
        Table t4 = new Table ("t4", "test/t4.tbl" );
        Map<String, Integer> common_title3 = JoinHelper.commonTitle(t3, t4);
        assertEquals(2, common_title3.size());
        assertEquals((Integer)0, common_title3.get("c int"));
        assertEquals((Integer)1, common_title3.get("d int"));
    }

    @Test
    public void TestFilter() {

        // test when there is only one title in common
        Table t1 = new Table ("t1", "test/t1.tbl" );
        Table t2 = new Table ("t2", "test/t2.tbl" );
        Map<String, Integer> common_title = JoinHelper.commonTitle(t1, t2);
        JoinHelper.commonRowIndexPair pair = JoinHelper.filter(common_title, t1, t2);
        System.out.print(pair.getFirstValue());
        System.out.print(pair.getSecondValue());
    }
}
