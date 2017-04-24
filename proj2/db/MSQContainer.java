package db;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 2017/4/22.
 */
public class MSQContainer {

    private MSQOperable contains_element;
    // the container-type will be the same as the title marked type
    private String container_type;

    private static final Pattern _STRING = Pattern.compile("^\\'(\\w+)\\'$"),
                                 _INT = Pattern.compile("[-]?\\d+"),
                                 _FLOAT = Pattern.compile("[-]?\\d*.\\d+|[-]?\\d+.\\d*"),
                                 _NOVALUE = Pattern.compile("");


    /* constructor of the string format, will detect the format of the string and decide if it is a
     *  MSQString, MSQInt or MSQFloat and create a new instance of the corresponding
     */
    MSQContainer (String format, String coltype) {
        /* because the string format will have uncertain number of spaces, and uncertain position, and only string will have spaces
        *  i will first split() to get out of the space, then change to an arraylist, check if it fit the string pattern
        *  check the list's size to know that the space number will be size() - 1, and adjust the final value to use in toString()
        *  method */

        container_type = coltype;
        Matcher m;
        String format_no_space = delSpaces(format);

        if ((m = _STRING.matcher(format_no_space)).matches() && coltype.equals("string")) {
            contains_element = new MSQString(format);
        } else if ((m=_INT.matcher(format)).matches() && coltype.equals("int")) {
            contains_element = new MSQInt(format);
        } else if (_FLOAT.matcher(format).matches() && coltype.equals("float")) {
            contains_element = new MSQFloat(format);
        } else if (_NOVALUE.matcher(format).matches()) {
            contains_element = new MSQNovalue();
            contains_element.setType(coltype);
        } else {
            throw new RuntimeException("the column's type is not matching what the column contains.");
        }
    }

    MSQContainer (MSQOperable element, String col_type) {
        contains_element = element;
        container_type = col_type;
    }

    /* constructor that create an instance with a single literal that is not in the table, so there is no
     * column type
     */
    MSQContainer (String format) {
        Matcher m;
        String format_no_space = delSpaces(format);
        if ((m = _STRING.matcher(format)).matches()) {
            contains_element = new MSQString(format);
        } else if ((m=_INT.matcher(format)).matches()) {
            contains_element = new MSQInt(format);
        } else if (_FLOAT.matcher(format).matches() ) {
            contains_element = new MSQFloat(format);
        } else {
            throw new RuntimeException("Malformed literal");
        }

    }

    /* helper method for the string format, to delete all the spaces and return the list of string */
    private String delSpaces(String s) {
            String[] lst = s.split(" ");
            return (String.join("", lst));
    }

    public MSQContainer copy() {
        MSQOperable copy_contains_element = contains_element.copy();
        MSQContainer copy_ctn = new MSQContainer(copy_contains_element, container_type);
        return copy_ctn;
    }

    public String getColType() { return container_type; }

    public String getRealType() {
        return contains_element.getType();
    }

    public MSQOperable getContainedElement() {return contains_element; }

    public String toString() {
        return contains_element.toString();
    }

    public MSQOperable add(MSQOperable oprl) {
        try {
            MSQOperable res = contains_element.add(oprl);
            return res;
        } catch (RuntimeException e) {
            throw new RuntimeException("error elements type whe doing add operation");
        }
    }

    public MSQOperable minus(MSQOperable oprl) {
        try {
            MSQOperable res = contains_element.minus(oprl);
            return res;
        } catch (RuntimeException e) {
            throw new RuntimeException("error elements type whe doing add operation");
        }
    }

}
