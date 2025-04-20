public class Supervisor extends Thread {
    private final AbstractProgram abstract_program = new AbstractProgram();
    private String status;

    @Override
    public void run()
    {
        abstract_program.setDaemon(true);
        abstract_program.start();
        while (true)
        {
            synchronized (abstract_program)
            {
                status = abstract_program.getStatus();
                boolean equalsUnknown = "UNKNOWN".equals(status);
                if (equalsUnknown) {
                    abstract_program.print();
                    abstract_program.setStatus(2);
                }
                if (abstract_program.isChanged()) {
                    try {
                        sleep(100);
                    }
                    catch (InterruptedException e)
                    {
                        e.printStackTrace();
                        break;
                    }
                    boolean equalsFatal = "FATAL ERROR".equals(status);
                    boolean equalsStopping = "STOPPING".equals(status);
                    if (equalsStopping) {
                        abstract_program.print();
                        abstract_program.setStatus(2);
                        abstract_program.notifyAll();
                    } else if (equalsFatal) {
                        abstract_program.print();
                        break;
                    }
                    abstract_program.print();
                }
            }
        }
    }
}

