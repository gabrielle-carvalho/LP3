import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;

/* ServerInitializer que:
a. Contém uma instância de CountDownLatch inicializada com o valor 4.
b. Possui um método waitForInitialization() que chama latch.await().
c. Possui um método startServer() que imprime a mensagem “Servidor 
Principal Online: Pronto para aceitar conexões (Socket.bind())” após o 
await() ser liberado. */

/*
 (main):
a. Crie um ExecutorService (e.g., Executors.newCachedThreadPool()).
b. Instancie o ServerInitializer com o CountDownLatch compartilhado.
c. Submeta 4 instâncias do ModuleLoader ao Executor. //cada uma vai estar numa thread diferente
d. Submeta a tarefa ServerInitializer.startServer() ao Executor imediatamente após a submissão dos ModuleLoaders.
e. A mensagem do servidor só pode aparecer após as 4 mensagens de 
módulo carregado.
 */

public class ServerInitializer {
    private static CountDownLatch latch = new CountDownLatch(4);

    public static void waitForInitialization() throws InterruptedException{
            latch.await(); //espera as 4 threads terminarem
    }
    
        public static void startServer() throws InterruptedException{
            waitForInitialization();
            System.out.println("Servidor Principal Online: Pronto para aceitar conexões (Socket.bind())");
    
        }

        public static void main(String[] args) {
            java.util.concurrent.ExecutorService executor = Executors.newCachedThreadPool();
            ServerInitializer serverInitializer = new ServerInitializer();
            executor.submit(new ModuleLoader("Configuração", (int)(Math.random() * 3000) + 1000, serverInitializer));
            executor.submit(new ModuleLoader("“Segurança”", 2000, serverInitializer));
            executor.submit(new ModuleLoader("Configuração", (int)(Math.random() * 2000) + 1000, serverInitializer));
            executor.submit(new ModuleLoader("Configuração",  3000, serverInitializer));
            executor.submit(() -> {
                try {
                    ServerInitializer.startServer(); //so vai rodar depois que os outros 4 terminarem

            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }

        });
    }
}




