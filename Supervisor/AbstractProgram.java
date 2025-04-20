import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class AbstractProgram extends Thread {
    private volatile Integer status = 0;
    private boolean changed = false;
    private Map<Integer, String> program_status = new HashMap<>();
    public AbstractProgram()
    {
        program_status.put(0, "UNKNOWN");
        program_status.put(1, "STOPPING");
        program_status.put(2, "RUNNING");
        program_status.put(3, "FATAL ERROR");
    }
    @Override
    public void run()
    {
        while (true)
        {
            try
            {
                synchronized (this)
                {
                    changeStatus();
                    if (status == 1) {
                        wait();
                    }
                }
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
                break;
            }
        }
    }
    public void print()
    {
        System.out.println(program_status.get(status));
    }
    public void setStatus(Integer status)
    {
        this.status = status;
        changed = true;
    }
    private void changeStatus()
    {
        Random random = new Random();
        int temp = random.nextInt(3) + 1;
        if (this.status == temp)
        {
            changed = false;
        }
        else
        {
            this.status = temp;
            changed = true;
        }
    }
    public boolean isChanged()
    {
        return changed;
    }
    public String getStatus()
    {
        return program_status.get(status);
    }
}

