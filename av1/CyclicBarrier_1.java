import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Demonstra como 3 threads podem "se encontrar" em um ponto
 * de barreira antes de continuarem.
 */
public class CyclicBarrier_1 {

    // O problema a ser resolvido em paralelo: 432*3 + 3^14 + 45*127/12 = ?
    
    public static void main(String[] args) {

        // Criamos uma barreira para 3 threads. um "ponto de encontro", faz threads esperarem umas pelas outras
        CyclicBarrier barrier = new CyclicBarrier(3);

        // Um pool com 3 threads, uma para cada parte do cálculo
        ExecutorService executor = Executors.newFixedThreadPool(3);

        // Tarefa 1: Calcula 432 * 3
        Runnable r1 = () -> {
            System.out.println("Thread 1 (Início): Calculando 432 * 3...");
            double result = 432d * 3d;
            System.out.println("Thread 1 (Resultado): " + result);
            
            // CHEGAR AO PONTO DE ENCONTRO (await): A thread 1 "chega" à barreira e espera
            await(barrier); 
            
            System.out.println("Thread 1 (Continuando): Todas as threads terminaram a primeira parte.");
        };

        // Tarefa 2: Calcula 3^14
        Runnable r2 = () -> {
            System.out.println("Thread 2 (Início): Calculando 3^14...");
            double result = Math.pow(3, 14);
            System.out.println("Thread 2 (Resultado): " + result);

            // A thread 2 "chega" à barreira e espera.
            await(barrier); 
            
            System.out.println("Thread 2 (Continuando): Todas as threads terminaram a primeira parte.");
        };

        // Tarefa 3: Calcula 45*127 / 12
        Runnable r3 = () -> {
            System.out.println("Thread 3 (Início): Calculando 45*127 / 12...");
            double result = 45d * 127d / 12d;
            System.out.println("Thread 3 (Resultado): " + result);

            // A thread 3 "chega" à barreira.
            // Como esta é a 3ª thread, a barreira "quebra" e
            // TODAS AS 3 threads são liberadas para continuar.
            await(barrier); 
            
            System.out.println("Thread 3 (Continuando): Todas as threads terminaram a primeira parte.");
        };

        // Submete as 3 tarefas para execução
        executor.submit(r1);
        executor.submit(r2);
        executor.submit(r3);

        executor.shutdown();
    }

    private static void await(CyclicBarrier barrier) {
        try {
            // A thread fica "dormindo" aqui (BLOCKED) até que o número necessário de threads (3) também chegue neste ponto.
            barrier.await();
        } catch (InterruptedException | BrokenBarrierException e) {
            Thread.currentThread().interrupt();
            e.printStackTrace();
        }
    }
}