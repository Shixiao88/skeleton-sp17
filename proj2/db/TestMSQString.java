package db;
import static org.junit.Assert.*;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

/**
 * Created by Xiao Shi on 2017/4/24.
 */
public class TestMSQString {

    @Test
    public void TestMSQStringOperation() {
        // add operation
        MSQString s1 = new MSQString("'hello world'");
        MSQString s2 = new MSQString("'how are you'");
        MSQNan nothing1 = new MSQNan();

        assertEquals("'hello world how are you'", s1.add(s2).toString());
        assertTrue(s1.add(nothing1) instanceof MSQNan);

        // compare operation
        assertTrue(s1.compare(s2) < 0);
        MSQString s3 = new MSQString("'hello world'");
        assertTrue(s1.compare(s3) == 0);
        assertTrue(s1.compare(nothing1) < 0);
    }


    @Rule
    public ExpectedException expectedEx = ExpectedException.none();

    @Test
    public void TestExceptionStringMinus() throws Exception {
        expectedEx.expect(RuntimeException.class);
        expectedEx.expectMessage("Malformed minus elements");
        MSQString s1 = new MSQString("'hello world'");
        MSQString s2 = new MSQString("'how are you'");
        s1.minus(s2);
    }

    @Test
    public void TestExceptionStringCompare() throws Exception {
        expectedEx.expect(RuntimeException.class);
        expectedEx.expectMessage("Bad comparison element types");
        MSQString s1 = new MSQString("'hello world'");
        MSQInt s2 = new MSQInt("0");
        s1.compare(s2);
    }
}
