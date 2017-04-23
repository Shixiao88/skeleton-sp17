package db;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 2017/4/22.
 */
public class MSQContainer {

    private MSQOperable contains_element;
    // the container-type will be the same as the title marked type
    private String container_type;

    private static final Pattern _STRING = Pattern.compile("\\b\\'\\w+\\'\\b"),
                                 _INT = Pattern.compile("(:?-)\\d"),
                                 _FLOAT = Pattern.compile("(:?-)\\d*.\\d*"),
                                 _NOVALUE = Pattern.compile("");


    /* constructor of the string format, will detect the format of the string and decide if it is a
     *  MSQString, MSQInt or MSQFloat and create a new instance of the corresponding
     */
    MSQContainer (String format, String coltype) {
        container_type = coltype;

        if (_STRING.matcher(format).matches() && coltype.equals("string")) {
            contains_element = new MSQString(format);
        } else if (_INT.matcher(format).matches() && coltype.equals("int")) {
            contains_element = new MSQInt(format);
        } else if (_FLOAT.matcher(format).matches() && coltype.equals("float")) {
            contains_element = new MSQFloat(format);
        } else if (_NOVALUE.matcher(format).matches()) {
            contains_element = new MSQNovalue();
            contains_element.setType(coltype);
        }
        throw new RuntimeException("the column's type is not matching what the column contains.");
    }

    MSQContainer (MSQOperable element, String col_type) {
        contains_element = element;
        container_type = col_type;
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
