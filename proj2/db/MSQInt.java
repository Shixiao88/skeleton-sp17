package db;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Interval1D;

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
    public String getOprValue() {
        return Integer.toString(Value);
    }

    public MSQInt reverseAdd() {
        int res = 0 - Value;
        return new MSQInt(Integer.toString(res));
    }

    @Override
    public MSQOperable add(MSQOperable other) {
        if (other.getOprValue().equals(null)) {
            return other.add(this);
        } else if (other.getType().equals("float")) {
            return other.add(this);
        } else if (other.getType().equals("int")) {
            int res = Integer.parseInt(getOprValue()) + Integer.parseInt(other.getOprValue());
            return new MSQInt(Integer.toString(res));
        } else {
            throw new RuntimeException("malformed operation, incorrect types");
        }
    }


    @Override
    public MSQOperable minus (MSQOperable other) {
        if (other.getOprValue().equals(null)) {
            return other.minus(this);
        } else if (other.getType().equals("float")) {
            MSQFloat flt_rvsd = ((MSQFloat) other).reverseAdd();
            return flt_rvsd.add(this);
        } else if (other.getType().equals("int")) {
            MSQInt int_rvsd = ((MSQInt) other).reverseAdd();
            return int_rvsd.add(this);
        } else {
            throw new RuntimeException("\"malformed operation, incorrect types\"");
        }
    }

}