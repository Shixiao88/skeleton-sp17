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

    @Override
    public MSQInt reverseAdd() {
        int res = 0 - Value;
        return new MSQInt(Integer.toString(res));
    }

    @Override
    public MSQOperable reverseMul () {
        if (Value == 0.0) {
            MSQNan res = new MSQNan();
            res.setType("float");
            return res;
        } else {
            float res = 1/Value;
            return new MSQFloat(Float.toString(res));
        }
    }

    @Override
    public MSQOperable add(MSQOperable other) {
        if (other instanceof MSQNan) {
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
        if (other instanceof MSQNan) {
            return other.minus(this);
        } else if (other.getType().equals("float")) {
            if (other instanceof MSQNovalue) {
                return new MSQFloat(getOprValue() + ".00");
            }
            MSQFloat flt_rvsd = ((MSQFloat) other).reverseAdd();
            return flt_rvsd.add(this);
        } else if (other.getType().equals("int")) {
            if (other instanceof MSQNovalue) {
                return copy();
            }
            MSQInt int_rvsd = ((MSQInt) other).reverseAdd();
            return int_rvsd.add(this);
        } else {
            throw new RuntimeException("\"malformed operation, incorrect types\"");
        }
    }


    @Override
    public MSQOperable mul (MSQOperable other) {
        if (other instanceof MSQNan) {
            return other.mul(this);
        } else if (other.getType().equals("float")) {
            float res = Value * Float.parseFloat(other.getOprValue());
            return new MSQFloat(Float.toString(res));
        } else if (other.getType().equals("int")) {
            int res = Value * Integer.parseInt(other.getOprValue());
            return new MSQInt(Integer.toString(res));
        } else {
            throw new RuntimeException("malformed operation, incorrect types");
        }
    }

    public MSQOperable divide (MSQOperable other) {
        if (other instanceof MSQNovalue) {
            MSQNan res = new MSQNan();
            res.setType(other.getType());
            return res;
        } else if (other.getType().equals("float")) {
            // might be NAN or a reversed float
            MSQOperable flt_rvsd = other.reverseMul();
            return mul(flt_rvsd);
        } else if (other.getType().equals("int")) {
            // might be NAN or a reversed float
            int op1 = Integer.parseInt(getOprValue());
            int op2 = Integer.parseInt(other.getOprValue());
            if (op2 == 0) {
                MSQNan res = new MSQNan();
                res.setType("int");
                return res;
            }
            int res = op1/op2;
            return new MSQInt(Integer.toString(res));
        } else {
            throw new RuntimeException("malformed operation, incorrect types");
        }
    }

    public int compare (MSQOperable other) {
        String other_value = other.getOprValue();
        Float this_value = Float.parseFloat(getOprValue());
        return this_value.compareTo(Float.parseFloat(other_value));
    }
}