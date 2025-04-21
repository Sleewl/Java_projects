import java.util.concurrent.BlockingQueue;
class Reader implements Runnable {
    private final BlockingQueue<String> queue;
    Reader(BlockingQueue<String> queue) {
        this.queue = queue;
    }

    @Override
    public void run() {
            try {
                //synchronized (queue) {
                    String message = queue.take();
                    System.out.println(Thread.currentThread().getName() + " прочитал: " + message);
                //}

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
    }
}
