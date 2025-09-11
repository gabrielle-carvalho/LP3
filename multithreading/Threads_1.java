public class Threads_1 {
    public static void main(String[] args){
        Thread t = Thread.currentThread();
        System.out.println(t.getName());
        
        MeuRunnable r = new MeuRunnable();
        Thread t0 = new Thread(r);
        Thread t1 = new Thread(r);
        Thread t2 = new Thread(new MeuRunnable());
        Thread t3 = new Thread(r);
        t0.start(); //cria uma nova thread
        t2.start(); //cria uma nova thread
        t1.run(); //usa thread principal
        t3.start();

        // System.out.println(t0.getName());
    }
}
