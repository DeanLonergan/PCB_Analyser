package PCB_Analyser;

import org.openjdk.jmh.Main;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.RunnerException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.concurrent.TimeUnit;

@Measurement(iterations = 10)
@Warmup(iterations = 10)
@Fork(value = 1)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MICROSECONDS)
@State(Scope.Thread)
public class Benchmarks {
    public static Hashtable<Integer, ArrayList<Integer>> pixelTable = new Hashtable<>();
    public static ArrayList<Integer> pixelArray = new ArrayList<>();
    public static int[] order = new int[512];

    @Setup(Level.Invocation)
    public static void setup() {
        for (int i = 0; i < 512; i++) {
            if (i % 7 == 0) {
                pixelArray.add(i);
                order[i] = i * -1;
            } else {
                pixelArray.add(-1);
                order[i] = i;
            }
        }
        for (int j = 0; j < 512; j++) {
            pixelTable.put(j,pixelArray);
        }
    }

    public static void main(String[] args) throws Exception {
        org.openjdk.jmh.Main.main(args);
    }

    @Benchmark
    public static void sort() {
        MainController mc = new MainController();
        mc.sort(order,pixelTable);
    }
}
