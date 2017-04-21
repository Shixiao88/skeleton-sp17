package db;

/**
 * Created by Xiao Shi on 2017/4/21.
 */
public class MSQString implements Primitives {

    private String type;

    MSQString() {
        type = "String";
    }

    @Override
    public String getType() {return "";}

    @Override
    public boolean checkType() {return false; }

    @Override
    public boolean checkForm() {return false;}

    @Override
    public Primitives MSQadd(Primitives other) {return null;}

    @Override
    public Primitives MSQminus(Primitives other) {return null;}

    @Override
    public Primitives MSQmultiply(Primitives other) {return null;}

    @Override
    public Primitives MSQdevide(Primitives other) {return null;}

    @Override
    public int MSQcompare(Primitives other) {return 0;}

}
