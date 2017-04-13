import java.util.ArrayList;
import java.lang.Math;

/**
 * Created by Xiao Shi on 2017/4/13.
 */
public class GuitarHero {

    private static String keyboard = "q2we4r5ty7u8i9op-[=zxdcfvgbnjmk,.;/' ";
    //private static ArrayList<Character> keys = new ArrayList<>();
    private static int len = 37;
    private static ArrayList<synthesizer.GuitarString> guitar_keys = new ArrayList<>();

    public static void main(String[] args) {
        for (int i=0; i<len; i+=1) {
            double frequency = 440 * Math.pow(2, ((i - 440)/220));
            //keys.add(keyboard.charAt(i));
            guitar_keys.add(new synthesizer.GuitarString(frequency));
        }

        while (true) {
            if (StdDraw.hasNextKeyTyped()) {
                char key = StdDraw.nextKeyTyped();
                int key_index = keyboard.indexOf(key);
                try {
                    synthesizer.GuitarString hit = guitar_keys.get(key_index);
                    hit.pluck();
                    double sample = hit.sample();
                    StdAudio.play(sample);
                    hit.tic();
                } catch (IndexOutOfBoundsException e) {
                    return;
                }
            }
        }
    }
}
