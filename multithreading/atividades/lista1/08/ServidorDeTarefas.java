import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.IOException; // Adicionado para exceções

public class ServidorDeTarefas {

    public static void main(String[] args) {
        int porta = 1234;

        ExecutorService poolDeProcessamento = Executors.newCachedThreadPool();

        List<Long> resultadosCombinados = Collections.synchronizedList(new ArrayList<>());

        try (ServerSocket serverSocket = new ServerSocket(porta)) {
            System.out.println("Servidor de Tarefas iniciado na porta: " + porta);
            System.out.println("Aguardando clientes para processar...");

            while (true) {
                Socket clienteTarefa = serverSocket.accept();
                System.out.printf("[Servidor] Nova tarefa recebida de: \n" + clienteTarefa.getRemoteSocketAddress());

                Runnable processador = new ProcessadorDeTarefa(clienteTarefa, resultadosCombinados);
                
                poolDeProcessamento.submit(processador);
            }

        } catch (Exception e) {
            System.err.println("Erro no Servidor de Tarefas: " + e.getMessage());
            poolDeProcessamento.shutdown();
        }
    }


    public static class ProcessadorDeTarefa implements Runnable {

        private Socket clienteSocket;
        private List<Long> resultadosCombinados;

        public ProcessadorDeTarefa(Socket socket, List<Long> resultados) {
            this.clienteSocket = socket;
            this.resultadosCombinados = resultados;
        }

        @Override
        public void run() {
            String threadNome = Thread.currentThread().getName();
            
            try (
                BufferedReader in = new BufferedReader(new InputStreamReader(clienteSocket.getInputStream()));
                PrintWriter out = new PrintWriter(clienteSocket.getOutputStream(), true)
            ) {
                
                out.println("Conectado. Envie um número para calcular o fatorial:");
                String entrada = in.readLine();
                
                int numero;
                try {
                    numero = Integer.parseInt(entrada.trim());
                } catch (NumberFormatException e) {
                    out.println("Erro: Entrada inválida. Não é um número.");
                    return; 
                }

                System.out.printf("\nIniciando fatorial de " + numero);
                Thread.sleep(1000 + (long)(Math.random() * 3000)); 
                
                long resultado = calcularFatorial(numero);
                System.out.printf("\nFatorial de "+ numero + " finalizado: \n");

                resultadosCombinados.add(resultado);
                
                System.out.printf("Resultado = " + resultado);

                out.println("\nTarefa concluída. Resultado: " + resultado);

            } catch (InterruptedException e) {
                System.err.printf(" Thread interrompida.\n");
                Thread.currentThread().interrupt();
            } catch (Exception e) {
                System.err.printf(" Erro ao processar tarefa: \n" + e.getMessage());
            }
            
            System.out.printf ("\nTarefa finalizada, cliente desconectado.\n");
        }

        private long calcularFatorial(int n) {
            if (n < 0) return -1;
            if (n == 0 || n == 1) return 1;
            long resultado = 1;
            for (int i = 2; i <= n; i++) {
                resultado *= i;
            }
            return resultado;
        }
    }
}
