package synthesizer;
import edu.princeton.cs.algs4.In;
import org.junit.Test;
import static org.junit.Assert.*;

/** Tests the ArrayRingBuffer class.
 *  @author Josh Hug
 */

public class TestArrayRingBuffer {
    @Test
    public void Tests() {
        //ArrayRingBuffer arb = new ArrayRingBuffer(10);

        /* Enqueue method tests */
        ArrayRingBuffer<Integer> arb = new ArrayRingBuffer<>(3);        // [null, null, null]
        assertTrue(arb.isEmpty());
        arb.enqueue(1);                             // [1, null, null]
        assertEquals(1, arb.fillCount());
        assertEquals(3, arb.capacity());
        arb.enqueue(2);                             // [1, 2, null]
        arb.enqueue(3);                             // [1,2,3]
        assertTrue(arb.isFull());

        /* Dequeue and peak method tests */
        assertEquals((Integer)1, arb.peek());
        assertEquals(3, arb.fillCount());
        assertEquals((Integer)1, arb.dequeue());        // [2,3]
        assertEquals((Integer)2, arb.dequeue());        // [3]
        assertEquals((Integer)3, arb.peek());
        assertEquals(1, arb.fillCount());
        assertEquals((Integer)3, arb.dequeue());        // []
        assertTrue(arb.isEmpty());
        assertEquals(3, arb.capacity());
        assertEquals(0, arb.fillCount());

        /* isIn method tests */
        arb.enqueue(1);
//        arb.enqueue(2);
//        arb.enqueue(3);
        assertTrue(arb.isIn(1));
        assertFalse(arb.isIn(2));
    }

    @Test
    public void testIterator() {
        ArrayRingBuffer<String> arb = new ArrayRingBuffer<>(3);
        arb.enqueue("hello");
        arb.enqueue("world");
        arb.enqueue("!!");
        for (String x : arb) {
            System.out.println(x);
        }
    }

    /** Calls tests for ArrayRingBuffer. */
    public static void main(String[] args) {
        jh61b.junit.textui.runClasses(TestArrayRingBuffer.class);
    }
} 
