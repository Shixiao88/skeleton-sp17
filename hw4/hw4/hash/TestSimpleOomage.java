package hw4.hash;

import org.junit.Test;
import static org.junit.Assert.*;

import java.util.Set;
import java.util.HashSet;
import java.util.List;
import java.util.ArrayList;


public class TestSimpleOomage {

    @Test
    public void testHashCodeDeterministic() {
        SimpleOomage so = SimpleOomage.randomSimpleOomage();
        int hashCode = so.hashCode();
        for (int i = 0; i < 100; i += 1) {
            assertEquals(hashCode, so.hashCode());
        }
    }

    @Test
    public void testHashCodePerfect() {
        /*meaning no two SimpleOomages should EVER have the same
          hashCode!
         */
        SimpleOomage oo0_1 = new SimpleOomage(0, 0, 5);
        SimpleOomage oo0_2 = new SimpleOomage(0, 5, 0);
        assertNotEquals(oo0_1, oo0_2);

        SimpleOomage oo1_1 = new SimpleOomage(0, 0, 5);
        SimpleOomage oo1_2 = new SimpleOomage(5, 0, 0);
        assertNotEquals(oo1_1, oo1_2);

        SimpleOomage oo2_1 = new SimpleOomage(0, 5, 0);
        SimpleOomage oo2_2 = new SimpleOomage(5, 0, 0);
        assertNotEquals(oo2_1, oo2_2);

        SimpleOomage oo3_1 = new SimpleOomage(5, 10, 20);
        SimpleOomage oo3_2 = new SimpleOomage(20, 10, 5);
        assertNotEquals(oo3_1, oo3_2);

    }

    @Test
    public void testEquals() {
        SimpleOomage ooA = new SimpleOomage(5, 10, 20);
        SimpleOomage ooA2 = new SimpleOomage(5, 10, 20);
        SimpleOomage ooB = new SimpleOomage(50, 50, 50);
        assertEquals(ooA, ooA2);
        assertNotEquals(ooA, ooB);
        assertNotEquals(ooA2, ooB);
        assertNotEquals(ooA, "ketchup");
    }

    @Test
    public void testHashCodeAndEqualsConsistency() {
        SimpleOomage ooA = new SimpleOomage(5, 10, 20);
        SimpleOomage ooA2 = new SimpleOomage(5, 10, 20);

        HashSet<SimpleOomage> hashSet = new HashSet<>();
        hashSet.add(ooA);
        assertTrue(hashSet.contains(ooA2));
    }

    /* Once you've finished haveNiceHashCodeSpread,
    in OomageTestUtility, uncomment this test. */

    @Test
    public void testRandomOomagesHashCodeSpread() {
        List<Oomage> oomages = new ArrayList<>();
        int N = 10000;

        for (int i = 0; i < N; i += 1) {
            oomages.add(SimpleOomage.randomSimpleOomage());
        }

        assertTrue(OomageTestUtility.haveNiceHashCodeSpread(oomages, 10));
    }

    /** Calls tests for SimpleOomage. */
    public static void main(String[] args) {
        jh61b.junit.textui.runClasses(TestSimpleOomage.class);
    }
}
