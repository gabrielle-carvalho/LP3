public class Threads_1 {
    public static void main(String[] args){
        Thread t = Thread.currentThread();
        System.out.println(t.getName());
        
        MeuRunnable r = new MeuRunnable();
        Thread t0 = new Thread(r);
        Thread t1 = new Thread(r);
        Thread t2 = new Thread(r);
        Thread t3 = new Thread(r);

        // Runnable com lambda
        Thread t4 = new Thread(() -> System.out.println("LP-III"));
        
        t0.start(); //cria uma nova thread
        t2.start(); //cria uma nova thread
        t1.start();
        // t1.run(); //usa thread principal
        t3.start();
        t4.start();


    }
}
