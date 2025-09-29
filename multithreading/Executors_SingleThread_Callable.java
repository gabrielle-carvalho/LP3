import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Executors_SingleThread_Callable {
    public static void main(String[] args){
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(new MeuRunnable());
        executor.shutdown(); //faz com que ele finalize o codigo e nao fique rodando indeterminadamente
    }

    public static class MeuRunnable implements Runnable{
        public void run(){
            String nome = Thread.currentThread().getName();
            System.out.println(nome + "LP-III");
        }
    }
}