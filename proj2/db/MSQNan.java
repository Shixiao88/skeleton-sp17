package db;

/**
 * Created by Administrator on 2017/4/22.
 */
public class MSQNan extends MSQOperable {

    private String Type;
    private Object Value;

    MSQNan() {
        Value = null;
    }

    MSQNan(String col_type){
        Type = col_type;
        Value = null;
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
        MSQNan copy = new MSQNan();
        copy.setType(Type);
        return new MSQNan();
    }

    @Override
    public String toString() {
        return "NAN";
    }

    @Override
    public String getOprValue() {
        return (String)null;
    }

    @Override
    public MSQOperable add (MSQOperable other) {
        return new MSQNan();
    }

    @Override
    public MSQOperable minus (MSQOperable other) {
        return new MSQNan();
    }

    @Override
    public MSQOperable mul (MSQOperable other) {
        return new MSQNan();
    }

    @Override
    public MSQOperable divide (MSQOperable other) {
        return new MSQNan();
    }

    @Override
    public int compare (MSQOperable other) {
        if (other instanceof MSQNan) {
            return 0;
        }
        return 1;
    }

}
