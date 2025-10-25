import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class ClienteJoguinho {

    public static void main(String[] args) {
        String servidor = "localhost";
        int porta = 12346;

        try (Socket socket = new Socket(servidor, porta);
             BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
             Scanner teclado = new Scanner(System.in)) {

            System.out.println("Conectado ao servidor " + servidor + ":" + porta);

            Thread ouvinte = new Thread(() -> {
                try {
                    String linha;
                    while ((linha = reader.readLine()) != null) {
                        System.out.println("Servidor: " + linha);
                    }
                } catch (IOException e) {
                    System.out.println("Conexão encerrada pelo servidor.");
                }
            });
            ouvinte.start();

            while (true) {
                System.out.print("Seu palpite: ");
                String tentativa = teclado.nextLine();

                if (tentativa.equalsIgnoreCase("sair")) {
                    System.out.println("Encerrando conexão...");
                    break;
                }

                writer.println(tentativa);
            }

        } catch (IOException e) {
            System.err.println("Erro ao conectar ao servidor: " + e.getMessage());
        }
    }
}
