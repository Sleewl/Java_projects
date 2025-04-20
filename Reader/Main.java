import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class Main {
    public static void main(String[] args) {
        int numThreads = Integer.parseInt(args[0]);
        BlockingQueue<String> queue = new ArrayBlockingQueue<>(numThreads);

        for (int i = 1; i <= numThreads; i++) {
            Thread t = new Thread(new Writer(queue));
            t.setName("Писатель "+ i);
            t.start();
        }

        for (int i = 1; i <= numThreads; i++) {
            Thread readerThread = new Thread(new Reader(queue));
            readerThread.setName("Читатель " + i);
            readerThread.start();
        }
    }
}