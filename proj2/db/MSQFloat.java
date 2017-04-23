package db;

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
    public Object getOprValue() {
        return Value;
    }

    @Override
    public MSQOperable add(MSQOperable other) {
        if (other.getOprValue().equals(null)) {
            return other.add(this);
        }
        try {
            float res = Value + (float)other.getOprValue();
            return new MSQFloat(Float.toString(res));
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
            float res = Value - (float)other.getOprValue();
            return new MSQFloat(Float.toString(res));
        } catch (RuntimeException e) {
            throw new RuntimeException("malformed operation, incorrect types");
        }
    }
}
