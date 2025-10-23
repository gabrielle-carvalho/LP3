import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class ServidorLeilao {

    private static String itemEmLeilao = "Quadro Raro de Van Gogh";
    private static volatile double maiorLance = 0.0;
    private static volatile String clienteComMaiorLance = "Ninguém";

    private static final Object lockLeilao = new Object(); // Objeto usado como "chave" para sincronizar o acesso ao lance

    // CopyOnWriteArrayList para permitir o broadcast seguro
    // (iterar na lista) enquanto novos clientes se conectam (modificar a lista).
    private static final List<PrintWriter> clientesConectados = new CopyOnWriteArrayList<>();

    public static void main(String[] args) {
        int porta = 12347;

        try (ServerSocket serverSocket = new ServerSocket(porta)) {
            System.out.println("Servidor de Leilão iniciado para o item: " + itemEmLeilao);
            System.out.println("Ouvindo na porta " + porta);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Novo cliente conectado: " + clientSocket.getInetAddress());

                // Cria uma nova Thread para este cliente e a inicia
                new Thread(new ClientHandler(clientSocket)).start();
            }
        } catch (IOException e) {
            System.err.println("Erro no servidor: " + e.getMessage());
        }
    }

    private static void broadcast(String mensagem) {
        System.out.println("BROADCAST: " + mensagem);
        // Itera sobre a lista thread-safe e envia a mensagem para cada cliente
        for (PrintWriter writer : clientesConectados) {
            writer.println(mensagem);
        }
    }

    private static class ClientHandler implements Runnable {
        private final Socket socket;
        private PrintWriter writer;

        public ClientHandler(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            try (
                OutputStream out = socket.getOutputStream();
                PrintWriter w = new PrintWriter(out, true); // true = autoFlush
                BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()))
            ) {
                this.writer = w;

                clientesConectados.add(writer); // 4a. ADICIONA O CLIENTE À LISTA DE BROADCAST

                // Envia o status atual para o novo cliente
                writer.println("Bem-vindo ao leilão! Digite seu lance");
                writer.println("Item: " + itemEmLeilao);
                writer.println("Lance atual: R$ " + maiorLance + " (por: " + clienteComMaiorLance + ")");
                
                broadcast("Novo cliente entrou no leilão.");

                String linhaDoCliente;
                while ((linhaDoCliente = reader.readLine()) != null) {
                    try {
                        double lance = Double.parseDouble(linhaDoCliente);
                        
                        processarLance(lance, writer);
                        
                    } catch (NumberFormatException e) {
                        writer.println("Erro: Envie apenas números. (Ex: 150.50)");
                    }
                }

            } catch (IOException e) {
                System.err.println("Cliente desconectado: " + socket.getInetAddress());
            } finally {
                if (writer != null) {
                    clientesConectados.remove(writer);
                }
                broadcast("Um cliente saiu do leilão.");
                try {
                    socket.close();
                } catch (IOException e) { /* ... */ }
            }
        }
    }

    private static void processarLance(double lance, PrintWriter clienteQueFezOLance) {
        // Só uma thread por vez pode estar aqui
        // evita que dois lances cheguem ao mesmo tempo e causem uma condição de corrida
        synchronized (lockLeilao) {
            if (lance > maiorLance) {
                maiorLance = lance;
                // NOTA: Em um sistema real, pegaríamos o nome do cliente
                clienteComMaiorLance = "Cliente " + clienteQueFezOLance.hashCode(); // Simulação

                // 6. NOTIFICA TODOS OS CLIENTES
                // Notifica a todos sobre o novo lance maior 
                broadcast("NOVO LANCE MAIOR: R$ " + maiorLance + " por " + clienteComMaiorLance);
                
            } else {
                clienteQueFezOLance.println("Seu lance de R$ " + lance + " é muito baixo. " + "O lance atual é R$ " + maiorLance);
            }
        }
    }
}