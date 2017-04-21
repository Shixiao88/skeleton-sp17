package db;

/**
 * Created by Xiao Shi on 2017/4/21.
 */
public class MSQName implements Primitives{

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
