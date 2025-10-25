import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class ClienteLeilao {

    public static void main(String[] args) {
        String host = "localhost";
        int porta = 1234;

        try (
            Socket socket = new Socket(host, porta);

            PrintWriter writer = new PrintWriter(socket.getOutputStream(), true); // Envia com autoFlush
            BufferedReader serverReader = new BufferedReader(new InputStreamReader(socket.getInputStream())); // Recebe do Servidor

            BufferedReader consoleReader = new BufferedReader(new InputStreamReader(System.in))
        ) {

            String promptNome = serverReader.readLine();
            System.out.println(promptNome);

            String nomeCliente = consoleReader.readLine();

            writer.println(nomeCliente);

            // ouvindo o servidor em segundo plano e imprimindo qualquer mensagem que ele mandar
            RecebedorServidor recebedor = new RecebedorServidor(serverReader);
            new Thread(recebedor).start();

            System.out.println("Você pode começar a digitar seus lances.");
            String userInput;
            
            while ((userInput = consoleReader.readLine()) != null) {
                writer.println(userInput);
            }

        } catch (UnknownHostException e) {
            System.err.println("Servidor não encontrado: " + host);
        } catch (IOException e) {
            System.err.println("Erro ao conectar com o servidor: " + e.getMessage());
        }
    }
}

class RecebedorServidor implements Runnable {

    private BufferedReader serverReader;

    public RecebedorServidor(BufferedReader serverReader) {
        this.serverReader = serverReader;
    }

    @Override
    public void run() {
        try {
            String linhaDoServidor;
            // loop das mensagens do servidor
            while ((linhaDoServidor = serverReader.readLine()) != null) {
                System.out.println(linhaDoServidor);
            }
        } catch (IOException e) {
            System.out.println("Conexão com o servidor perdida.");
        }
    }
}