package db;
import static org.junit.Assert.*;

import edu.princeton.cs.algs4.In;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

/**
 * Created by Administrator on 2017/4/24.
 */
public class TestMSQFloat {

    @Test
    public void TestOperation() {
        MSQFloat f1 = new MSQFloat("0.2");
        MSQFloat f2 = new MSQFloat("-0.4");

        // operation between two floats
        assertEquals("-0.200", f1.add(f2).toString());
        assertEquals("-0.600", f2.minus(f1).toString());

        // operation between float and integer
        MSQInt i1 = new MSQInt("2");
        assertEquals("2.200", f1.add(i1).toString());
        assertEquals("2.400", i1.minus(f2).toString());
        assertEquals("-2.400", f2.minus(i1).toString());
    }

}
