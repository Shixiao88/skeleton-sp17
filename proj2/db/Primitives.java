package db;
import java.util.HashMap;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.util.StringJoiner;

/**
 * Created by Xiao Shi on 2017/4/21.
 */
public interface Primitives {

    String getType();

    boolean checkType();

    boolean checkForm();

    Primitives MSQadd(Primitives other);

    Primitives MSQminus(Primitives other);

    Primitives MSQmultiply(Primitives other);

    Primitives MSQdevide(Primitives other);

    int MSQcompare(Primitives other);
}
