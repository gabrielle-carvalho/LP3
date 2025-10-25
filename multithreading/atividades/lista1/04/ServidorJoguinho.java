import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class ServidorJoguinho{

    public static void main(String[] args) {
        int porta = 12346;

        try (ServerSocket serverSocket = new ServerSocket(porta)) {
            System.out.println("Servidor de jogo ouvindo na porta " + porta);
            int numeroSecreto = new Random().nextInt(100) + 1;
            System.out.println("Número secreto gerado! (para teste: " + numeroSecreto + ")");

            while (true) {
                System.out.println("Aguardando cliente...");
                try (Socket clientSocket = serverSocket.accept()) {
                    System.out.println("Cliente conectado: " + clientSocket.getInetAddress());
                    handleClient(clientSocket, numeroSecreto);
                } catch (IOException e) {
                    System.err.println("Erro ao lidar com o cliente: " + e.getMessage());
                }
            }
        } catch (IOException e) {
            System.err.println("Erro ao iniciar o servidor: " + e.getMessage());
        }
    }

    private static void handleClient(Socket clientSocket, int numeroSecreto) throws IOException {
        InputStream in = clientSocket.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));

        OutputStream out = clientSocket.getOutputStream();
        PrintWriter writer = new PrintWriter(out, true);

        writer.println("Tente adivinhar o número entre 1 e 100:");

        String linha;
        
        // Fica lendo nums do cliente até que o cliente desconecte
        while ((linha = reader.readLine()) != null) {
            try {
                int tentativa = Integer.parseInt(linha);
                if (tentativa==numeroSecreto){
                    System.out.println("GANHOU");
                }
                else if(tentativa>numeroSecreto){
                    System.out.println("o numeroSecreto correto é menor");
                }
                else {
                    System.out.println("o numeroSecreto correto é maior");
                }

            } catch (NumberFormatException e) {
                writer.println("Por favor, digite um número válido.");
            }
        }

        System.out.println("Cliente desconectado: " + clientSocket.getInetAddress());
    }
}