package db;

import sun.invoke.util.VerifyAccess;

import java.util.Comparator;

/**
 * Created by Xiao Shi on 2017/4/21.
 */
public class MSQString extends MSQOperable {

    private String Value_quotes;
    private String Value;
    private String Type;

    MSQString(String value) {
        this.Value_quotes = value;
        this.Value = "";
        this.Type = "string";
        for (int i = 1; i < Value_quotes.length() - 1; i += 1){
            this.Value += Value_quotes.charAt(i);
        }
    }

    void add_space(String[] lst_no_space) {
        this.Value = String.join(" ", lst_no_space);
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
            String res = "'" + getOprValue() + " " + other.getOprValue() + "'";
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
