package db;
import java.io.File;
import java.util.*;
import static org.junit.Assert.*;

import edu.princeton.cs.algs4.In;
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
        ArrayList<Integer> l = new ArrayList<>();
        l.add(0);
        l.add(1);
        assertEquals(l, pair.getFirstValue());
        assertEquals(l, pair.getSecondValue());

        // test when there is more titles in common
        Table t5 = new Table("t5", "test/t5.tbl");
        Table t6 = new Table("t6", "test/t6.tbl");
        Map<String, Integer> common_title2 = JoinHelper.commonTitle(t5, t6);
        JoinHelper.commonRowIndexPair pair2 = JoinHelper.filter(common_title2, t5, t6);
        // should come out [1,2] for first list, [0,3] for second list
        ArrayList<Integer> l_first = new ArrayList<>();
        l_first.add(1);
        l_first.add(2);
        ArrayList<Integer> l_second = new ArrayList<>();
        l_second.add(0);
        l_second.add(3);
        assertEquals(l_first, pair2.getFirstValue());
        assertEquals(l_second,pair2.getSecondValue());

        // test when when the first match column has more than one value equal to the second
        Table t7 = new Table("t7", "test/t7.tbl");
        Table t8 = new Table("t8", "test/t8.tbl");
        Map<String, Integer> common_title3 = JoinHelper.commonTitle(t7, t8);
        JoinHelper.commonRowIndexPair pair3 = JoinHelper.filter(common_title3, t7, t8);
        // should come out [0,0,0] for first list and [0,2,3] for second
        ArrayList<Integer> l2_first = new ArrayList<>();
        ArrayList<Integer> l2_second = new ArrayList<>();
        for (int i=0; i<3; i+=1) {l2_first.add(0);}
        l2_second.add(0);
        l2_second.add(2);
        l2_second.add(3);
        assertEquals(l2_first, pair3.getFirstValue());
        assertEquals(l2_second,pair3.getSecondValue());

        // test when no value match
        Table t9 = new Table("t9", "test/t9.tbl");
        Table t10 = new Table("t10", "test/t10.tbl");
        Map<String, Integer> common_title4 = JoinHelper.commonTitle(t9, t10);
        JoinHelper.commonRowIndexPair pair4 = JoinHelper.filter(common_title4, t9, t10);
        // should have [] for both lists
        ArrayList<Integer> l3_first = new ArrayList<>();
        ArrayList<Integer> l3_second = new ArrayList<>();
        assertEquals(l3_first, pair4.getFirstValue());
        assertEquals(l3_second,pair4.getSecondValue());
    }

    @Test
    public void TestCompleteTable() {
        // test when there is only one title in common
        Table t1 = new Table ("t1", "test/t1.tbl" );
        Table t2 = new Table ("t2", "test/t2.tbl" );
        Map<String, Integer> common_title = JoinHelper.commonTitle(t1, t2);
        JoinHelper.commonRowIndexPair pair = JoinHelper.filter(common_title, t1, t2);
        Table join = JoinHelper.completeTable("join1", pair.getFirstValue(), pair.getSecondValue(),
                common_title, t1, t2);
        System.out.println(join.gettitle());
        System.out.println(join.getbody());
    }
}
