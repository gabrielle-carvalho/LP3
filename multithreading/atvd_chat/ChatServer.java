import java.io.*;
import java.net.*;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class ChatServer {

private static final int PORT = 12345;

    private static final List<PrintWriter> clients = new CopyOnWriteArrayList<>();

    public static void main(String[] args) {
        System.out.println("[Servidor] Ouvindo na porta  " + PORT + "...");
        try (ServerSocket server = new ServerSocket(PORT)) {
            while (true) {
                Socket socket = server.accept();
                System.out.println("[Servidor] Conexão de: " + socket.getInetAddress().getHostAddress());
                new Thread(new ClientHandler(socket)).start();
            }
        } catch (IOException e) {
            System.err.println("[Servidor] Erro: " + e.getMessage());
        }
    }

    private static class ClientHandler implements Runnable {
        private final Socket socket;
        private PrintWriter out;

        ClientHandler(Socket socket) {
            this.socket = socket;
        }

        @Override public void run() {
            try (BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));) {
                out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
                clients.add(out); // 1) registrar cliente

                out.println("Bem-vindo! Digite mensagens. Use 'exit' para sair.");
                
                out.println("Digite seu nick: ");
                String nick = in.readLine();

                String txt = nick + ": entrou no seu chat!";
                broadcast("[" + nick + "] " + txt , out);

                String line;
                while ((line = in.readLine()) != null) { // 3) laço principal de leitura
                    if ("exit".equalsIgnoreCase(line.trim())) {
                        out.println("Você saiu do chat. Até logo!");
                        break;
                    }
                    broadcast("[" + nick + "] " + line, out);
                }
            } catch (IOException e) {
                System.out.println("Cliente desconectou: " + e.getMessage()); // queda de cliente é comum; logar e seguir
            } finally {
                try { clients.remove(out); socket.close(); } catch (IOException ignore) {}
            }
        }

        private void broadcast(String msg, PrintWriter sender) {
            for (PrintWriter pw : clients) {
                pw.println(msg);
            }
        }
    }
}
