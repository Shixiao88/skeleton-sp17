import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Created by Administrator on 2017/5/21.
 */

public class TestQuadtree {

    @Test
    public void TestCreatQT() {
        String[] img_source = {"root", "1", "2","3","4"};
        MapQuadtree mqt = new MapQuadtree(img_source);
        System.out.println(mqt);

    }

}
