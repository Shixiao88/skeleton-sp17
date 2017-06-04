import lab14lib.Generator;

/**
 * Created by Xiao Shi on 2017/6/2.
 */
import lab14lib.Generator;

public class StrangeBitwiseGenerator implements Generator{
    private int period;
    private int state;

    public StrangeBitwiseGenerator (int period) {
        this.state = 0;
        this.period = period;
    }

    private double nomilize(double state) {
        return (state * (2.0) - 1.0);
    }

    @Override
    public double next() {
        state = (state + 1);
        int weirdState = state & (state >> 7) % period;
        return nomilize(((weirdState + period) % period) / (double)period);
    }
}
