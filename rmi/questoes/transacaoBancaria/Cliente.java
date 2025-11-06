import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Cliente {

    public static void main(String[] args) {
        try {
            Registry registry = LocateRegistry.getRegistry("localhost", 1099);
            ServicoBanco servico = (ServicoBanco) registry.lookup("BancoService");

            System.out.println("Cliente conectado. Iniciando simulação de deadlock...");
            System.out.println("-----------------------------------------------------");

            System.out.printf("Saldo Inicial C1001: R$ %.2f%n", servico.getSaldo(1001));
            System.out.printf("Saldo Inicial C1002: R$ %.2f%n", servico.getSaldo(1002));
            System.out.println("-----------------------------------------------------");

            ExecutorService executor = Executors.newFixedThreadPool(4); // pool de threads para simular acessos concorrentes

            // Tarefa 1: Transfere de 1001 para 1002
            Runnable clienteA = () -> {
                try {
                    for (int i = 0; i < 5; i++) {
                        servico.transferir(1001, 1002, 10.00);
                        Thread.sleep(100);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            };

            // Tarefa 2: Transfere de 1002 para 1001
            Runnable clienteB = () -> {
                try {
                    for (int i = 0; i < 5; i++) {
                        servico.transferir(1002, 1001, 5.00);
                        Thread.sleep(150);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            };

            executor.submit(clienteA);
            executor.submit(clienteB);
            executor.submit(clienteA);
            executor.submit(clienteB);

            executor.shutdown();
            if (executor.awaitTermination(30, TimeUnit.SECONDS)) {
                System.out.println("-----------------------------------------------------");
                System.out.println("Simulação concluída com SUCESSO (Sem Deadlock).");
                System.out.printf("Saldo Final C1001: R$ %.2f%n", servico.getSaldo(1001));
                System.out.printf("Saldo Final C1002: R$ %.2f%n", servico.getSaldo(1002));
            } else {
                System.err.println("!!! SIMULAÇÃO FALHOU (Timeout) !!!");
                executor.shutdownNow();
            }

        } catch (Exception e) {
            System.err.println("Erro no cliente: " + e.getMessage());
            e.printStackTrace();
        }
    }
}