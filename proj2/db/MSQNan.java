package db;

/**
 * Created by Administrator on 2017/4/22.
 */
public class MSQNan extends MSQOperable {

    private String Type;

    MSQNan() {
    }

    @Override
    public void setType(String col_type) {
        Type = col_type;
    }

    @Override
    public String getType() {
        return Type;
    }

    @Override
    public MSQNan copy() {
        return new MSQNan();
    }

    @Override
    public String toString() {
        return "NAN";
    }

    @Override
    public Object getOprValue() {
        return null;
    }

    @Override
    public MSQOperable add (MSQOperable other) {
        return new MSQNan();
    }

    @Override
    public MSQOperable minus (MSQOperable other) {
        return new MSQNan();
    }

}
