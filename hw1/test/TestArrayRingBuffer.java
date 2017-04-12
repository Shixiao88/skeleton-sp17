package test;
import static org.junit.Assert.*;
import org.junit.Test;
import synthesizer.ArrayRingBuffer;


/**
 * Created by Administrator on 2017/4/12.
 */

public class TestArrayRingBuffer {

    @Test
    public void check() {

        /* Enqueue method tests */
        ArrayRingBuffer<Integer> arb = new ArrayRingBuffer<>(3);
        assertTrue(arb.isEmpty());
        arb.enqueue(1);
        assertEquals(1, arb.fillCount());
        assertEquals(3, arb.capacity());
        arb.enqueue(2);
        arb.enqueue(3);
        assertTrue(arb.isFull());
        arb.enqueue(4);

    }
}
