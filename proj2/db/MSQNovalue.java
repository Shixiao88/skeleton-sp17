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

    MSQNovalue() {}

    @Override
    public void setType(String col_type){ Type = col_type; }

    @Override
    public String getType() {
        return Type;
    }

    @Override
    public MSQNovalue copy() {
        return new MSQNovalue();
    }

    @Override
    public String toString() {
        return "NONVALUE";
    }

    public Object getOprValue() {
        if (Type == "string") {
            return new MSQString("").getOprValue();
        } else if (Type == "int") {
            return new MSQInt("0").getOprValue();
        } else if (Type == "float") {
            return new MSQFloat("0.0").getOprValue();
        }
        return null;
    }

    @Override
    public MSQOperable add(MSQOperable other) {
        if (other instanceof MSQNovalue) {
            return new MSQNovalue();
        }
        try {
            return other.add(this);
        } catch (RuntimeException e) {
            throw new RuntimeException("malformed operation, incorrect types");
        }
    }

    @Override
    public MSQOperable minus (MSQOperable other) {
        if (other instanceof MSQNovalue) {
            return new MSQNovalue();
        } else if (Type.equals("float") || other.getType().equals("float")) {
            return new MSQFloat(Float.toString(-(float) other.getOprValue()));
        }
        try {
            return new MSQInt(Integer.toString(- (int)other.getOprValue()));
        } catch (RuntimeException e) {
            throw new RuntimeException("malformed operation, incorrect types");
        }

    }
}
