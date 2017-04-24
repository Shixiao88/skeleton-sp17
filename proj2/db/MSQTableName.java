package db;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 2017/4/21.
 */
public class MSQTableName extends MSQUnoperable {

    private String Value;
    private String Type;
    private final Pattern _TABLETITLE = Pattern.compile("[a-zA-Z]+?\\w*");

    public MSQTableName(String table_name) {
        if ((_TABLETITLE.matcher(table_name)).matches()) {
            Value = table_name;
            Type = "_MSQTableName";
        }else {
            throw new RuntimeException("only letters, numbers and underscores, and must start with a letter.");
        }
    }


    @Override
    public String getType() {
        return Type;
    }

    @Override
    public String toString() {
        return Value;
    }
}
