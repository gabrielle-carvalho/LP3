import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

/**
 * Simula 500 "usuários" (threads) tentando se matricular em uma disciplina (LP-III),
 * mas limitando o atendimento a 3 usuários por vez.
 */
public class Semaphore_1 {

    // Criamos um Semáforo com 3 "permissões".
    // 3 "desks" de matrícula disponíveis.
    private static final Semaphore SEMAFORO = new Semaphore(3);

    public static void main(String[] args) {
        
        // Cria um pool de threads para simular muitos usuários chegando com base na quantidade de espaço disponivel
        ExecutorService executor = Executors.newCachedThreadPool();

        // tarefa (Runnable) que cada usuário (thread) vai executar
        Runnable r1 = () -> {
            String name = Thread.currentThread().getName();
            int usuario = new Random().nextInt(10000);

            // A thread tenta "pegar" uma das 3 permissões.
            // Se todas as 3 estiverem em uso, a thread "dorme" aqui e espera
            // na fila até que uma permissão seja liberada.
            acquire();
            
            System.out.println("Usuário " + usuario + " matriculou-se em LP-III! " 
                               + "Usando a thread " + name);
            
            sleep(); // Simula o tempo que o usuário leva no "desk" de matrícula


            System.out.println("Usuário " + usuario + " CONCLUIU a matrícula. " + name + " está livre.");

            // A thread "libera" a permissão (o "desk") para que a próxima thread
            // (usuário) que estava esperando na fila possa entrar.
            SEMAFORO.release();
            
        };

        // Simula 500 usuários tentando se matricular ao mesmo tempo.
        for (int i = 0; i < 500; i++) {
            executor.execute(r1);
        }

        executor.shutdown();
    }

    public static void acquire() {
        try {
            SEMAFORO.acquire();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            e.printStackTrace();
        }
    }

    public static void sleep() {
        try {
            int tempoEspera = new Random().nextInt(6) + 1;
            Thread.sleep(1000 * tempoEspera);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            e.printStackTrace();
        }
    }
}