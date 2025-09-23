import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class Executors_SingleThread_Callable1 {
    public static void main(String[] args) throws InterruptedException {
        ExecutorService executor = null;
        try {
            executor = Executors.newSingleThreadExecutor();
            Future<String> future = executor.submit(new MeuCallable());
            System.out.println(future.isDone());
            System.out.println(future.get());
            // executor.awaitTermination(5, TimeUnit.SECONDS);
            System.out.println(future.isDone());
        } catch (Exception e) {
            throw e;
        } finally {
        if (executor != null) {
            executor.shutdown();
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
