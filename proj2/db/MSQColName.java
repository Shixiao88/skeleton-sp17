package db;

import java.awt.*;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.*;

/**
 * Created by Administrator on 2017/4/21.
 */
public class MSQColName {

    private String Value;
    private String titleName;
    private String ColType;
    private String Type;
    private final Pattern _COLTITLE = Pattern.compile("([a-zA-z]:?\\w*)\\s+(\\w+)");
    private ArrayList<String> ColTypePattern = new ArrayList<>(Arrays.asList("string", "int", "float"));

    MSQColName (String col_name) {
        Matcher m;
        if ((m = _COLTITLE.matcher(col_name)).matches() &&
                (ColTypePattern.contains(m.group(2)))) {
                titleName = m.group(1);
                ColType = m.group(2);
                Value = titleName + " " + ColType;
                Type = "_MSQColName";
        } else {
            throw new RuntimeException("Bad formed column names");
        }
    }

    public String getTitleName() { return titleName; }

    public String getValue() {
        return Value;
    }

    public String getColType() {
        return ColType;
    }

    public String getType() {return Type; }

    public MSQColName copy() {
        return new MSQColName(Value);
    }

    public String toString() {
        return Value;
    }

}
