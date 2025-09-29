import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class ClassesAtomicasLogico {
    static AtomicBoolean b = new AtomicBoolean(false);
    public static void main(String[] args){
        MeuRunnable runnable = new MeuRunnable();
        Thread t0 = new Thread(runnable);
        Thread t1 = new Thread(runnable);
        Thread t2 = new Thread(runnable);

        t0.start();
        t1.start();
        t2.start();
    }

    public static class MeuRunnable implements Runnable{
        @Override
        public void run(){
            String name = Thread.currentThread().getName();
            System.out.println(name + ": " + b.compareAndExchange(false, true));
            // Ele compara o valor atual do AtomicBoolean com o valor expect que foi passado
            // Se for igual, ele troca o valor atual para update
            // Sempre retorna o valor antigo que estava armazenado
        }
    }
}
