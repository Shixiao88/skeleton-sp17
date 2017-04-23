package db;

import java.util.IntSummaryStatistics;

/**
 * Created by Administrator on 2017/4/22.
 */
public class MSQInt extends MSQOperable {

    private int Value;
    private String Type;

    MSQInt(String value) {
        Value = Integer.parseInt(value);
        Type = "int";
    }

    @Override
    public String getType() {
        return Type;
    }

    @Override
    public MSQInt copy () {
        return new MSQInt(Integer.toString(Value));
    }

    @Override
    public String toString() {
        return Integer.toString(Value);
    }

    @Override
    public Integer getOprValue() {
        return Value;
    }

    @Override
    public MSQOperable add(MSQOperable other) {
        if (other.getOprValue().equals(null)) {
            return other.add(this);
        }
        try {
            int res = Value + (int)other.getOprValue();
            return new MSQInt(Integer.toString(res));
        } catch (RuntimeException e) {
            throw new RuntimeException("malformed operation, incorrect types");
        }
    }

    @Override
    public MSQOperable minus (MSQOperable other) {
        if (other.getOprValue().equals(null)) {
            return other.minus(this);
        }
        try {
            int res = Value - (int)other.getOprValue();
            return new MSQInt(Integer.toString(res));
        } catch (RuntimeException e) {
            throw new RuntimeException("malformed operation, incorrect types");
        }
    }

}