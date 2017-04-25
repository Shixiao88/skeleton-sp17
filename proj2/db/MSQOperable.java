package db;

/**
 * Created by Administrator on 2017/4/22.
 */
public abstract class MSQOperable implements Primitives{

    public String getType() {
        return "";
    }

    public String getOprValue() { return null; }

    public void setType(String col_type) {
    }

    public MSQOperable copy(){ return null; }

    MSQOperable reverseAdd() {return null; }

    MSQOperable reverseMul() {return null; }

    // allow two instance do operation
    // return string, wrap to be MSQOperable later in ColName add method, to check if the type is match also.
    public MSQOperable add(MSQOperable other) {
        throw new RuntimeException("Malformed add elements");
    }

    public MSQOperable minus(MSQOperable other) {
        throw new RuntimeException("Malformed minus elements");
    }

    public MSQOperable mul(MSQOperable other) {
        throw new RuntimeException("Malformed multiply elements");
    }

    public MSQOperable divide(MSQOperable other) {
        throw new RuntimeException("Malformed multiply elements");
    }

    public int compare(MSQOperable other) {
        throw new RuntimeException("Malformed comparison elements");
    }


}
