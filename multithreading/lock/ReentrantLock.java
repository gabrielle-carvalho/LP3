
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ReentrantLock {
    private static int = -1;
    private static lock = new ReentrantLock();
    public static void main(String[] args) {
        ExecutorService executor = Executors.newCachedThreadPool();
        Runnable r1 = () -> {
            lock.lock();
            String name = Thread.currentThread().getName();
            i++;
            System.out.println(name + "lendo incremento" + i);
            lock.unlock();
        };
        for (int i=0; i<6; i++){
            executor.execute(r1);
        }
        executor.shutdown();
    }
}
