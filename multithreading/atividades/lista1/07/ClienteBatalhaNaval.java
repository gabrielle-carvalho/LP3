import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class ClienteBatalhaNaval {
    public static void main(String[] args) {
        String host = "localhost";
        int porta = 1234;
        try (Socket socket = new Socket(host, porta)) {
            System.out.println("Conectado ao servidor de Batalha Naval");
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            Scanner sc = new Scanner(System.in);

            String mensagem;
            while ((mensagem = in.readLine()) != null) {
                System.out.println(mensagem);
                if (mensagem.toLowerCase().contains("digite linha e coluna")) {
                    String entrada = sc.nextLine();
                    out.println(entrada);
                }
                if (mensagem.toLowerCase().contains("venceu") || mensagem.toLowerCase().contains("perdeu")) {
                    System.out.println("Fim de jogo.");
                    break;
                }
            }

            sc.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
