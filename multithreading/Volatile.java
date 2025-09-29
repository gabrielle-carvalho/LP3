public class Volatile {
    private static int numero = 0;
    private static boolean preparado = false;

    private static class MeuRunnable implements Runnable {
        @Override
        public void run() {
            while (!preparado) {
            Thread.yield(); // permite que outras threads executem, nao monopolizando a cpu
            }
            System.out.println("Número: " + numero);
        }
    }
    public static void main(String[] args) {
        Thread t0 = new Thread(new MeuRunnable());
        t0.start();
        numero = 42;
        preparado = true;
    }
}
