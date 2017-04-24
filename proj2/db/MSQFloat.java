package db;

import edu.princeton.cs.algs4.In;

/**
 * Created by Administrator on 2017/4/21.
 */
public class MSQFloat extends MSQOperable {

    private float Value;
    private String Type;

    MSQFloat(String value) {
        Value = Float.parseFloat(value);
        Type = "float";
    }

    @Override
    public String getType() {
        return Type;
    }

    @Override
    public MSQFloat copy() {
        return new MSQFloat(Float.toString(Value));
    }

    @Override
    public String toString() {
        return String.format("%.3f", Value);
    }

    @Override
    public String getOprValue() {
        return Float.toString(Value);
    }

    // turn postive to negative, or the other way around to transfer minus to add
    public MSQFloat reverseAdd() {
        float res = 0 - Value;
        return new MSQFloat(Float.toString(res));
    }

    @Override
    public MSQOperable add(MSQOperable other) {
        if (other.getOprValue().equals(null)) {
            return other.add(this);
        } else if (other.getType().equals("float")) {
            float res = Value + Float.parseFloat(other.getOprValue());
            return new MSQFloat(Float.toString(res));
        } else if (other.getType().equals("int")) {
            float res = Value + Integer.parseInt(other.getOprValue());
            return new MSQFloat(Float.toString(res));
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
            throw new RuntimeException("malformed operation, incorrect types");
        }
    }
}
