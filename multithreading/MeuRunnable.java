public class MeuRunnable implements Runnable {
    static Object lock1 = new Object();
    static Object lock2 = new Object();
    static int i = -1;

    @Override
    public void run(){ // synchronized faz com q o run espere as outras threads finalizarem para continuar
        i++;
        String nome = Thread.currentThread().getName();
        System.out.println(nome + ":" + i);
        synchronized(lock1){
            i++;
            String nome = Thread.currentThread().getName();
            System.out.println(nome + ":" + i);
        }
        synchronized(lock2){
            i++;
            String nome = Thread.currentThread().getName();
            System.out.println(nome + ":" + i);
        }
    }
}