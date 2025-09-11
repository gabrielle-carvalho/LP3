public class Threads_1 {
    public static void main(String[] args){
        Thread t = Thread.currentThread();
        System.out.println(t.getName());
        
        // MeuRunnable r = new MeuRunnable();
        Thread t0 = new Thread(new MeuRunnable());
        // t0.run(); //usa thread principal
        t0.start();//cira uma nova thread
        // System.out.println(t0.getName());
    }
}
