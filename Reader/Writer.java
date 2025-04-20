import java.util.concurrent.BlockingQueue;

class Writer implements Runnable {
    private final BlockingQueue<String> queue;


    Writer(BlockingQueue<String> queue) {
        this.queue = queue;
    }

    @Override
        public void run() {
            try {
                String message = Thread.currentThread().getName() + " написал сообщение ";
                //synchronized (queue) {
                    queue.put(message);
                    System.out.println(message);
               // }
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
    }
}

