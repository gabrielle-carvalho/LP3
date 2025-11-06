import java.io.*;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ClienteCheckIn {
    private static final String HOST_SERVIDOR = "localhost";
    private static final int PORTA_SERVIDOR = 5001;
    
    public static void main(String[] args) {
        try (
            Socket socket = new Socket(HOST_SERVIDOR, PORTA_SERVIDOR);
            BufferedReader entrada = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter saida = new PrintWriter(socket.getOutputStream(), true);
            Scanner scanner = new Scanner(System.in)
        ) {
            System.out.println("╔════════════════════════════════════════════════╗");
            System.out.println("║   Sistema Check-in - Cliente                   ║");
            System.out.println("╚════════════════════════════════════════════════╝");
            System.out.println("Conectado ao servidor " + HOST_SERVIDOR + ":" + PORTA_SERVIDOR);
            System.out.println();
            
            ExecutorService executorLeitura = Executors.newSingleThreadExecutor();
            executorLeitura.submit(() -> {
                try {
                    String resposta;
                    while ((resposta = entrada.readLine()) != null) {
                        System.out.println(resposta);
                    }
                } catch (IOException e) {
                }
            });
            
            System.out.println("Digite seus comandos:");
            System.out.println();
            
            while (scanner.hasNextLine()) {
                String comando = scanner.nextLine().trim();
                if (comando.isEmpty()) continue;
                
                System.out.println("→ " + comando);
                saida.println(comando);
                
                if (comando.toUpperCase().startsWith("QUIT")) {
                    break;
                }
                
                Thread.sleep(100);
            }
            
            executorLeitura.shutdownNow();
            
        } catch (Exception e) {
            System.err.println("Erro: " + e.getMessage());
            e.printStackTrace();
        }
    }
}