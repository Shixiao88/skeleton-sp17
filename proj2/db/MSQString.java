package db;

import java.util.Comparator;

/**
 * Created by Xiao Shi on 2017/4/21.
 */
public class MSQString extends MSQOperable {

    private String Value;
    private String Type;

    MSQString(String value) {
        this.Value = value;
        this.Type = "string";
    }

    @Override
    public String getType() {
        return Type;
    }

    public String getOprValue() {
        return Value;
    }

    @Override
    public MSQString copy() {
        return new MSQString(Value);
    }

    @Override
    public String toString() {
        if (Value != null) {
            return "'" + Value + "'";
        }
        return null;
    }

    @Override
    public MSQOperable add(MSQOperable other) {
        if (other.getType().equals("string")) {
            String res = Value + other.getOprValue();
            return new MSQString(res);
        }
        throw new RuntimeException("String can only add to string");
    }

    @Override
    public int compare (MSQOperable other) {
        try {
            return this.getOprValue().compareTo((String)other.getOprValue());
        } catch (RuntimeException e) {
            throw new RuntimeException("Bad comparison element types");
        }
    }
}
