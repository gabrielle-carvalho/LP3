import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class ServidorLeilao {

    private static String itemEmLeilao = "chaveirinho do bill";
    private static volatile double maiorLance = 0.0;
    private static volatile String clienteComMaiorLance = "Sem lances";

    private static final Object lockLeilao = new Object(); // Objeto usado como "chave" para sincronizar o acesso ao lance

    // CopyOnWriteArrayList para permitir o broadcast seguro
    private static final List<PrintWriter> clientesConectados = new CopyOnWriteArrayList<>();

    public static void main(String[] args) {
        int porta = 1234;
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
        private String nomeCliente;

        public ClientHandler(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            try (
                OutputStream out = socket.getOutputStream();
                PrintWriter w = new PrintWriter(out, true); // true = autoFlush
                // printwriter transforma textos em byes e envia em out, o true manda a msg na hr pra n perder o fluxo do leilao
                // OutputStream out bytes para SAÍDA.
                // InputStream in bytes de ENTRADA.
                //InputStreamReader traduz os bytes que chegam em caracteres
                // BufferedReader reader junta os caracteres em linhas de texto (com readLine()).

                BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()))
            ) {
                this.writer = w;
                writer.println("Bem-vindo ao leilão! Por favor, digite seu nome:");
                this.nomeCliente = reader.readLine();

                if (this.nomeCliente == null || this.nomeCliente.trim().isEmpty()) {
                    System.out.println("Cliente desconectou antes de se identificar.");
                    return;
                }
                System.out.println("Cliente " + this.nomeCliente + " (" + socket.getInetAddress() + ") entrou.");

                clientesConectados.add(writer); // ADICIONA O CLIENTE À LISTA DE BROADCAST

                // Envia o status atual para o novo cliente
                writer.println("Olá, " + this.nomeCliente + "! Você pode começar a dar lances.");
                writer.println("Item: " + itemEmLeilao);
                writer.println("Lance atual: R$ " + maiorLance + " (por: " + clienteComMaiorLance + ")");
                
                broadcast(this.nomeCliente + " entrou no leilão.");

                String linhaDoCliente;
                while ((linhaDoCliente = reader.readLine()) != null) {
                    try {
                        double lance = Double.parseDouble(linhaDoCliente);
                        
                        processarLance(lance, this.nomeCliente, writer);
                        
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
                
                String nomeParaAnuncio = (this.nomeCliente != null) ? this.nomeCliente : "Um cliente";
                broadcast(nomeParaAnuncio + " saiu do leilão.");
                
                try {
                    socket.close();
                } catch (IOException e) { /* ... */ }
            }
        }
    }

    private static void processarLance(double lance, String nomeDoCliente, PrintWriter clienteQueFezOLance) {
        // Só uma thread por vez pode estar aqui
        // evita que dois lances cheguem ao mesmo tempo e causem uma condição de corrida
        synchronized (lockLeilao) {
            if (lance > maiorLance) {
                maiorLance = lance;
                clienteComMaiorLance = nomeDoCliente;

                // Notifica a todos sobre o novo lance maior 
                broadcast("NOVO LANCE MAIOR: R$ " + maiorLance + " por " + clienteComMaiorLance);
                
            } else {
                clienteQueFezOLance.println("Seu lance de R$ " + lance + " é muito baixo. " + "O lance atual é R$ " + maiorLance);
            }
        }
    }
}
