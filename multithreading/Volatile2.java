public class Volatile2 {
    private static volatile int numero = 0;
    private static volatile boolean preparado = false;
    //sem volatile cada thread pode ter uma copia da variavel e mudanças feitas por outra thread não aparecem imediatamente
    private static class MeuRunnable implements Runnable {
    @Override
    public void run() {
        while (!preparado) {
            Thread.yield();
        }
        if (numero != 42) {
            throw new IllegalStateException("LP-III");
        }
    }
    }
    public static void main(String[] args) {
        while (true) {
            Thread t0 = new Thread(new MeuRunnable());
            t0.start();
            Thread t1 = new Thread(new MeuRunnable());
            t1.start();
            Thread t2 = new Thread(new MeuRunnable());
            t2.start();
            numero = 42;
            preparado = true;
            while(t0.getState() != State.TERMINATED
            || t1.getState() != State.TERMINATED
            || t2.getState() != State.TERMINATED) {
            //espera
            }
            numero = 0;
            preparado = false;
        }
    }

}