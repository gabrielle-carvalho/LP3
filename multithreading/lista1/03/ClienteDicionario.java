import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

public class ClienteDicionario {

    public static void main(String[] args) {
        String host = "localhost";
        int porta = 12346;

        try (
            Socket socket = new Socket(host, porta);
            
            // 2. FLUXOS DE TEXTO (PARA O SERVIDOR):
            OutputStream out = socket.getOutputStream();
            PrintWriter writer = new PrintWriter(out, true); // Envia para o servidor
            InputStream in = socket.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(in)); // Lê do servidor

            // 3. FLUXO DE LEITURA (DO TECLADO DO USUÁRIO):
            BufferedReader consoleReader = new BufferedReader(new InputStreamReader(System.in))
        ) {
            
            System.out.println("Conectado ao Servidor de Dicionário.");
            System.out.println("Digite uma palavra para buscar o significado (ou 'sair' para desconectar).");

            String userInput;
            
            while (true) {
                System.out.print("> "); // Prompt para o usuário
                userInput = consoleReader.readLine(); // Lê do teclado

                if (userInput == null) {
                    break;
                }

                writer.println(userInput); // ENVIA A PALAVRA PARA O SERVIDOR:

                if (userInput.equalsIgnoreCase("sair")) {
                    System.out.println("Desconectando...");
                    break;
                }

                // 6. RECEBE A RESPOSTA DO SERVIDOR:
                String respostaDoServidor = reader.readLine();
                System.out.println("Servidor: " + respostaDoServidor);
            }

        } catch (IOException e) {
            System.err.println("Erro no cliente: " + e.getMessage());
        }
    }
}