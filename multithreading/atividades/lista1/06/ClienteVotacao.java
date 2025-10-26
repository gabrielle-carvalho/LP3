import java.io.*;
import java.net.*;
import java.util.Scanner;

public class ClienteVotacao {
    public static void main(String[] args) {
        String servidor = "localhost";
        int porta = 1234;

        try (
            Socket socket = new Socket(servidor, porta);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            Scanner teclado = new Scanner(System.in)
        ) {
            System.out.println("conectado ao servidor de votação em " + servidor + ":" + porta);

            Thread leitorServidor = new Thread(() -> {
                try {
                    String linha;
                    while ((linha = in.readLine()) != null) {
                        System.out.println(linha);
                    }
                } catch (IOException e) {
                    System.out.println("conexao encerrada pelo servidor");
                }
            });
            leitorServidor.start();

            while (true) {
                System.out.print("digite o codigo do seu voto ou sair: ");
                String msg = teclado.nextLine();
                if (msg.equalsIgnoreCase("sair")) {
                    break;
                }
                out.println(msg);
            }

            socket.close();
            System.out.println("Cliente encerrado.");

        } catch (IOException e) {
            System.out.println("Erro ao conectar ao servidor: " + e.getMessage());
        }
    }
}
