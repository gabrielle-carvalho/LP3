import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Executors_Scheduled1 {
    public static void main(String[] args) throws InterruptedException, ExecutionException {
        ScheduledExecutorService executor = Executors.newScheduledThreadPool(4);
        executor.scheduleAtFixedRate(new Tarefa(), 0, 1, TimeUnit.SECONDS);
    }
    public static class Tarefa implements Runnable {
        @Override
        public void run() {
            try {
                Thread.sleep(1000);
            } catch (Exception e) {
                e.printStackTrace();
            }
            String nome = Thread.currentThread().getName();
            int nextInt = new Random().nextInt();
            System.out.println(nome + ": LP-III " + nextInt);
        }
    }
}