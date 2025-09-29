package synchronization;
public class Sychronized {
    private static int i = 0;
    public static void main(String[] args){
        MeuRunnable r = new MeuRunnable();
        Thread t0 = new Thread(r);
        Thread t1 = new Thread(r);
        Thread t2 = new Thread(r);
        Thread t3 = new Thread(r);

        t0.start();
        t1.start();
        t2.start();
        t3.start();
    }

    public void static void imprime(){
        synchronized (Sychronized.class){
            i++;
            String name = Thread.currentThread().getName();
            System.out.println(name + ":" + i);
        }
    }
    public class MeuRunnable implements Runnable {
        @Override
        public void run(){
            imprime();
        }
    }
}