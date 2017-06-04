/**
 * Created by Xiao Shi on 2017/6/2.
 */

import lab14lib.Generator;

public class AcceleratingSawToothGenerator implements Generator {
    private int period;
    private int state;
    private double acceleration;

    public AcceleratingSawToothGenerator(int period, double acceleration) {
        this.state = 0;
        this.period = period;
        this.acceleration = acceleration;
    }

    private double nomilize(double state) {
        return (state * (2.0) - 1.0);
    }

    @Override
    public double next() {
        state = (state + 1);
        double next = nomilize(((state + period) % period) / (double)period);
        if (next == -1.0) {
            period = (int)Math.round(period * acceleration);
            state = 0;
        }
        return next;
    }
}
