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
        Table t1 = new Table ("t1", "t1" );
        Table t2 = new Table ("t2", "t2" );
        Map<MSQColName, Integer> common_title = JoinHelper.commonTitle(t1, t2);
        assertEquals(1, common_title.size());
        // just create a temporary table to use getTitleIndex()
        Table temp = new Table("temp", common_title, null);
        assertEquals(0, temp.getTitleIndex("x int"));

        /* test no title in common */
        Table t3 = new Table ("t3", "t3" );
        Map<MSQColName, Integer> common_title2 = JoinHelper.commonTitle(t1, t3);
        assertEquals(0, common_title2.size());

        /* test more than one title in common, the order should follow the first table */
        Table t4 = new Table ("t4", "t4" );
        Map<MSQColName, Integer> common_title3 = JoinHelper.commonTitle(t3, t4);
        assertEquals(2, common_title3.size());
        Table temp1 = new Table ("temp2", common_title3, null);
        assertEquals(0, temp1.getTitleIndex("c int"));
        assertEquals(1, temp1.getTitleIndex("d int"));
    }

    @Test
    public void TestFilter() {

        // test when there is only one title in common
        Table t1 = new Table ("t1", "t1" );
        Table t2 = new Table ("t2", "t2" );
        Map<MSQColName, Integer> common_title = JoinHelper.commonTitle(t1, t2);
        JoinHelper.commonRowIndexPair pair = JoinHelper.filter(common_title, t1, t2);
        ArrayList<Integer> l = new ArrayList<>();
        l.add(0);
        l.add(1);
        assertEquals(l, pair.getFirstValue());
        assertEquals(l, pair.getSecondValue());

        // test when there is more titles in common
        Table t5 = new Table("t5", "t5");
        Table t6 = new Table("t6", "t6");
        Map<MSQColName, Integer> common_title2 = JoinHelper.commonTitle(t5, t6);
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
        Table t7 = new Table("t7", "t7");
        Table t8 = new Table("t8", "t8");
        Map<MSQColName, Integer> common_title3 = JoinHelper.commonTitle(t7, t8);
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
        Table t9 = new Table("t9", "t9");
        Table t10 = new Table("t10", "t10");
        Map<MSQColName, Integer> common_title4 = JoinHelper.commonTitle(t9, t10);
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
        Table t1 = new Table ("t1", "t1" );
        Table t2 = new Table ("t2", "t2" );
        Map<MSQColName, Integer> common_title = JoinHelper.commonTitle(t1, t2);
        JoinHelper.commonRowIndexPair pair = JoinHelper.filter(common_title, t1, t2);
        Table join = JoinHelper.completeTable("join1", pair.getFirstValue(), pair.getSecondValue(),
                common_title, t1, t2);
        // {"x int" : 0, "y int" : 1, "z int" : 2}
        System.out.println(join.gettitle());
        // [[2,5,4],[8,3,9]]
        System.out.println(join.getbody());


        // test when there is more titles in common
        Table t5 = new Table("t5", "t5");
        Table t6 = new Table("t6", "t6");
        Map<MSQColName, Integer> common_title2 = JoinHelper.commonTitle(t5, t6);
        JoinHelper.commonRowIndexPair pair2 = JoinHelper.filter(common_title2, t5, t6);
        Table join2 = JoinHelper.completeTable("join2", pair2.getFirstValue(), pair2.getSecondValue(),
                common_title2, t5, t6);
        // {Z int=0 , W int=1, X int=2, Y int=3, B int=4}
        System.out.println(join2.gettitle());
        // [[4,1,7,7,7],[9,1,1,9,11]]
        System.out.println(join2.getbody());


        // test when when the first match column has more than one value equal to the second
        Table t7 = new Table("t7", "t7");
        Table t8 = new Table("t8", "t8");
        Map<MSQColName, Integer> common_title3 = JoinHelper.commonTitle(t7, t8);
        JoinHelper.commonRowIndexPair pair3 = JoinHelper.filter(common_title3, t7, t8);
        Table join3 = JoinHelper.completeTable("join3", pair3.getFirstValue(), pair3.getSecondValue(),
                common_title3, t7, t8);
        // {X int=0 ,Y int=1, Z int=2}
        System.out.println(join3.gettitle());
        // [[1,4,7],[1,4,9][1,4,11]]
        System.out.println(join3.getbody());


        // test when no value match
        Table t9 = new Table("t9", "t9");
        Table t10 = new Table("t10", "t10");
        Map<MSQColName, Integer> common_title4 = JoinHelper.commonTitle(t9, t10);
        JoinHelper.commonRowIndexPair pair4 = JoinHelper.filter(common_title4, t9, t10);
        Table join4 = JoinHelper.completeTable("join4", pair4.getFirstValue(), pair4.getSecondValue(),
                common_title4, t9, t10);
        // {X int=0 ,Y int=1, Z int=2}
        System.out.println(join4.gettitle());
        // []
        System.out.println(join4.getbody());
    }

    @Test
    public void TestCartesianProductCompleteTable() {
        // test the join method that no title is in common
        Table t11 = new Table("t11", "t11");
        Table t12 = new Table("t12", "t12");
        Map<MSQColName, Integer> common_title = JoinHelper.commonTitle(t11, t12);
        Table join = JoinHelper.cartesianProductCompleteTable("CPjoin", common_title, t11, t12);
//        X int	Y int	Z int	A int	B int
//        2	    5	    4	    7	    0
//        2	    5	    4	    2	    8
//        8	    3	    9	    7	    0
//        8	    3	    9	    2	    8
        System.out.println(join.gettitle());
        System.out.println(join.getbody());

    }

    @Test
    public void TestJoinOfEqualTable() {
        // test the join of two that have same content
        Table t1 = new Table ("t1", "test");
        Table t2 = new Table ("t2", "test");
        Table join = Join.join("join", t1, t2);
        assertEquals(t1.toString(), join.toString());
    }
}
