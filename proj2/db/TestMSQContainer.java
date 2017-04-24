package db;
import static org.junit.Assert.*;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;


/**
 * Created by Xiao Shi on 2017/4/24.
 */
public class TestMSQContainer {

    @Test
    public void TestStringInTableParse () {
        String s1 = "'hello'";
        MSQContainer ctn1 = new MSQContainer(s1, "string");
        assertEquals("'hello'",  ctn1.getContainedElement().toString());
        assertEquals("hello", ctn1.getContainedElement().getOprValue());

        String s2 = "'123_tsetnumber_and_underscore'";
        MSQContainer ctn2 = new MSQContainer(s2, "string");
        assertEquals("'123_tsetnumber_and_underscore'",  ctn2.getContainedElement().toString());
        assertEquals("123_tsetnumber_and_underscore", ctn2.getContainedElement().getOprValue());

        String s3 = "' test all the spaces that    fuck          me                   out'";
        MSQContainer ctn3 = new MSQContainer(s3, "string");
        assertEquals("' test all the spaces that    fuck          me                   out'", ctn3.getContainedElement().toString());
        assertEquals(" test all the spaces that    fuck          me                   out", ctn3.getContainedElement().getOprValue());
    }

    @Test
    public void TestFloatInTableParse() {
        // point in the middle, positive
        String s1 = "123.3";
        MSQContainer ctn1 = new MSQContainer(s1, "float");
        assertEquals("123.300",  ctn1.getContainedElement().toString());
        assertEquals((float)123.3, ctn1.getContainedElement().getOprValue());
        // point in the last, positive
        String s2 = "123.";
        MSQContainer ctn2 = new MSQContainer(s2, "float");
        assertEquals("123.000",  ctn2.getContainedElement().toString());
        assertEquals((float)123.0, ctn2.getContainedElement().getOprValue());
        // point in the last, positive
        String s3 = ".33";
        MSQContainer ctn3 = new MSQContainer(s3, "float");
        assertEquals("0.330",  ctn3.getContainedElement().toString());
        assertEquals((float)0.33, ctn3.getContainedElement().getOprValue());
        // negative
        String s4 = "-0.2";
        MSQContainer ctn4 = new MSQContainer(s4, "float");
        assertEquals("-0.200",  ctn4.getContainedElement().toString());
        assertEquals((float)-0.2, ctn4.getContainedElement().getOprValue());
    }

    @Test
    public void TestIntegerInTableParse() {
        // positive integer
        String s1 = "123";
        MSQContainer ctn1 = new MSQContainer(s1, "int");
        assertEquals("123",  ctn1.getContainedElement().toString());
        assertEquals(123, ctn1.getContainedElement().getOprValue());
        // negative integer
        String s2 = "-123";
        MSQContainer ctn2 = new MSQContainer(s2, "int");
        assertEquals("-123",  ctn2.getContainedElement().toString());
        assertEquals(-123, ctn2.getContainedElement().getOprValue());
    }

    @Test
    public void TestSingleLiteralParser () {
        // create string container instance
        String s1 = "'hello'";
        MSQContainer ctn1 = new MSQContainer(s1);
        assertEquals("string", ctn1.getContainedElement().getType());
        assertEquals("hello", ctn1.getContainedElement().getOprValue());
        assertEquals("'hello'", ctn1.getContainedElement().toString());

        // create float container instance
        String s2 = "3.8";
        MSQContainer ctn2 = new MSQContainer(s2);
        assertEquals("float", ctn2.getContainedElement().getType());
        assertEquals((float)3.8, ctn2.getContainedElement().getOprValue());
        assertEquals("3.800", ctn2.getContainedElement().toString());

        // create integer container instance
        String s3 = "3";
        MSQContainer ctn3 = new MSQContainer(s3);
        assertEquals("int", ctn3.getContainedElement().getType());
        assertEquals(3, ctn3.getContainedElement().getOprValue());
        assertEquals("3", ctn3.getContainedElement().toString());

        // create float container instance
        String s4 = "-3.";
        MSQContainer ctn4 = new MSQContainer(s4);
        assertEquals("float", ctn4.getContainedElement().getType());
        assertEquals((float)-3.0, ctn4.getContainedElement().getOprValue());
        assertEquals("-3.000", ctn4.getContainedElement().toString());
    }



    @Rule
    public ExpectedException expectedEx = ExpectedException.none();

    @Test
    public void TestExceptionStringParse() throws Exception {
        expectedEx.expect(RuntimeException.class);
        expectedEx.expectMessage("the column's type is not matching what the column contains.");
        // the one do not has ''
        String s1 = "hello";
        MSQContainer ctn1 = new MSQContainer(s1, "string");
        // the one has space
        String s2 = "'hello ll'";
        MSQContainer ctn2 = new MSQContainer(s2, "string");
        // the one have only one side '
        String s3= "'hello";
        MSQContainer ctn3 = new MSQContainer(s3, "string");
        // the one with wrong column type
        String s4 = "123";
        MSQContainer ctn4 = new MSQContainer(s4, "string");
    }

    @Test
    public void TestExceptionFloatParse() throws Exception {
        expectedEx.expect(RuntimeException.class);
        expectedEx.expectMessage("the column's type is not matching what the column contains.");
        // the one has ''
        String s1 = "'4.2'";
        MSQContainer ctn1 = new MSQContainer(s1, "float");
        // the one do not has dot
        String s2 = "23";
        MSQContainer ctn2 = new MSQContainer(s2, "float");
    }
}
