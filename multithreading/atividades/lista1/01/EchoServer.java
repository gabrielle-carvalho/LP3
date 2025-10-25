import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class EchoServer {
    public static void main(String[] args) {
        int port = 12345;
        System.out.println("Servidor de Eco iniciado na porta " + port);

        try (ServerSocket serverSocket = new ServerSocket(port)) {
            while (true) {
                try (Socket clientSocket = serverSocket.accept()) {
                    System.out.println("Cliente conectado: " + clientSocket.getInetAddress().getHostAddress());

                    BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

                    PrintWriter writer = new PrintWriter(clientSocket.getOutputStream(), true); 

                    String line;
                    if ((line = reader.readLine()) != null) { 
                        System.out.println("Recebido: " + line);
                        writer.println("Eco: " + line);
                        System.out.println("Ecoado: " + line);
                    }
                } catch (Exception e) {
                    System.out.println("Erro ao comunicar com o cliente: " + e.getMessage());
                }
            }
        } catch (Exception e) {
            System.out.println("Erro ao iniciar o servidor: " + e.getMessage());
        }
    }
}