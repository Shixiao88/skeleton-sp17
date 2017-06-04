import lab14lib.*;
import java.util.ArrayList;

public class Main {
	public static void main(String[] args) {

		Generator generator = new StrangeBitwiseGenerator(1024);
		GeneratorAudioVisualizer gav = new GeneratorAudioVisualizer(generator);
		gav.drawAndPlay(128000, 1000000);

//		Generator g1 = new SineWaveGenerator(200);
//		Generator g2 = new SineWaveGenerator(201);
//		Generator g3 = new AcceleratingSawToothGenerator(300, 1.1);
//		Generator g4 = new AcceleratingSawToothGenerator(301, 0.9);
//
//		ArrayList<Generator> generators = new ArrayList<Generator>();
//		generators.add(g1);
//		generators.add(g2);
//		generators.add(g3);
//		generators.add(g4);
//		MultiGenerator mg = new MultiGenerator(generators);
//
//		GeneratorAudioVisualizer gav = new GeneratorAudioVisualizer(mg);
//		gav.drawAndPlay(500000, 1000000);
	}
}