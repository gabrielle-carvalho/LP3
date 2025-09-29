public class Sychronized2 {
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

    public static class MeuRunnable implements Runnable {
        @Override
        public void run(){
            int j;
            synchronized(this){
                i++;
                j = i*2;
            }
            double jElevadoA100 = Math.pow(j, 100);
            double sqrt = Math.sqrt(jElevadoA100);
            System.out.println(sqrt);
        }
    }
}