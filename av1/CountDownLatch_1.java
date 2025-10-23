import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Demonstra como uma thread pode esperar por
 * um número específico de eventos (3 execuções de 'r1')
 * antes de continuar.
 */
public class CountDownLatch_1 {

    private static volatile int i = 0;

    // "Trava de Contagem Regressiva"
    // Começamos o contador em 3.
    private static CountDownLatch LATCH = new CountDownLatch(3);

    public static void main(String[] args) {

        // Um executor agendado para rodar a thread r1
        ScheduledExecutorService executor = Executors.newScheduledThreadPool(3);

        // A tarefa que será executada 3 vezes
        Runnable r1 = () -> {
            int j = new Random().nextInt();
            int x = i * j;
            System.out.println(i + " x " + j + " = " + x);

            // A thread sinaliza que terminou seu trabalho.
            // Isso diminui o contador do LATCH
            LATCH.countDown();
        };

        // Agenda r1 para rodar 1 vez por segundo
        executor.scheduleAtFixedRate(r1, 0, 1, TimeUnit.SECONDS);
        // r1: Esta é a tarefa que queremos executar.
        // 0 (Atraso Inicial): Quanto tempo o executor deve esperar antes de rodar a tarefa pela primeira vez
        // 1 (Período): O intervalo de tempo entre o início de uma execução e o início da próxima
        // TimeUnit.SECONDS: A unidade de tempo para os números 0 e 1

        // Loop principal da nossa 'main' thread
        while (true) {
            
            // A thread "dorme" aqui (BLOCKED).
            // Ela só vai acordar e continuar quando o contador do LATCH
            // chegar a zero (ou seja, quando r1 tiver rodado 3 vezes)
            await(); // Método auxiliar que chama LATCH.await()
            
            // Zera o 'i' e reinicia o LATCH para um novo ciclo de 3
            System.out.println(">>> 3 execuções concluídas. Reiniciando o ciclo. <<<");
            i = new Random().nextInt();
            LATCH = new CountDownLatch(3);
        }
    }

    public static void await() {
        try {
            LATCH.await(); 
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}