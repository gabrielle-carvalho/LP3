public class CyclicBarrier_1 {
    //432*3 + 3^14 + 45*127/12 = ?
    public static void main(String[] args) {
        CyclicBarrier cycleBarrier = new CyclicBarrier(3);
        ExecutorService executor = Executors.newFixedThreadPool(3);
        
        Runnable r1 = () ->{
            System.out.println(432d*3d);
            await(cycleBarrier); //ESPERA A EXECUCAO
            System.out.println("Terminei a execução");
        };
        Runnable r2 = () ->{
            System.out.println(Math.pow(3, 14));
            await(cycleBarrier);
            System.out.println("Terminei a execução");
        };
        Runnable r3 = () ->{
            System.out.println(45d*127d/12d);
            await(cycleBarrier);
            System.out.println("Terminei a execução");
        };

        executor.submit(r1);
        executor.submit(r2);
        executor.submit(r3);
        executor.shutdown();
        }
        private static void await(CyclicBarrier cycleBarrier) {
            try {
                cycleBarrier.await();
            } catch (InterruptedException | BrokenBarrierException e) {
                Thread.currentThread().interrupt();
                e.printStackTrace();
            }
        }
}
