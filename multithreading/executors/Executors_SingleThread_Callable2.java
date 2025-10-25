import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class Executors_SingleThread_Callable2 {
    public static void main(String[] args) throws InterruptedException {
        ExecutorService executor = null;
        try {
            executor = Executors.newSingleThreadExecutor();
            executor.execute(new MeuRunnable());
            executor.execute(new MeuRunnable());
            executor.execute(new MeuRunnable());
            Future<String> future = executor.submit(new MeuCallable());
            System.out.println(future.isDone());
            executor.shutdown();
            executor.awaitTermination(10, TimeUnit.SECONDS);
            System.out.println(future.isDone());
        } catch (Exception e) {
            throw e;
        } finally {
        if (executor != null) {
            executor.shutdownNow();
        } 
        }
    }

    public static class MeuCallable implements Callable<String> {
        public String call() throws Exception {
            // Thread.sleep(1000);
            String nome = Thread.currentThread().getName();
            int nextInt = new Random().nextInt();
            return nome + ": LP-III " + nextInt;
        }
    }
}
