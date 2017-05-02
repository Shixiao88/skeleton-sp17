package db;

import edu.princeton.cs.algs4.In;
import jdk.internal.org.objectweb.asm.tree.analysis.Value;

import java.lang.management.OperatingSystemMXBean;
import java.sql.SQLClientInfoException;
import java.time.temporal.ValueRange;

/**
 * Created by Administrator on 2017/4/22.
 */
public class MSQNovalue extends MSQOperable{

    private String Type;

    @Override
    public void setType(String col_type){ Type = col_type; }

    @Override
    public String getType() {
        return Type;
    }

    @Override
    public MSQNovalue copy() {
        MSQNovalue copy = new MSQNovalue();
        copy.setType(Type);
        return copy;
    }

    @Override
    public String toString() {
        return "NOVALUE";
    }

    public String getOprValue() {
        if (Type.equals("string")) {
            return "";
        } else if (Type.equals("int")) {
            return "0";
        } else if (Type.equals("float")) {
            return "0.0";
        }
        return null;
    }

    @Override
    public MSQNovalue reverseAdd() {
        return this;
    }

    @Override
    public MSQOperable add(MSQOperable other) {
        try {
            if (Type.equals("float")) {
                MSQFloat f = new MSQFloat("0.000");
                return f.add(other);
            }
            return other.add(this);
        } catch (RuntimeException e) {
            throw new RuntimeException("malformed operation, incorrect types");
        }
    }

    @Override
    public MSQOperable minus (MSQOperable other) {
        try {
            return other.reverseAdd();
        } catch (RuntimeException e) {
            throw new RuntimeException("malformed operation, incorrect types");
        }
    }

    @Override
    public int compare (MSQOperable other) {
        try {
            if (other instanceof MSQNovalue) {
                return 0;
            } else if (other instanceof MSQNan) {
                return -1;
            } else if (other.getType().equals("string")) {
                return this.getOprValue().compareTo(other.getOprValue());
            } else if (other.getType().equals("int") || other.getType().equals("float")) {
                Float this_value = Float.parseFloat(this.getOprValue());
                Float other_value = Float.parseFloat(other.getOprValue());
                return this_value.compareTo(other_value);
            }
        } catch (RuntimeException e) {
            throw new RuntimeException("Bad formed comparison expression.");
        } return (Integer)null;
    }
}
