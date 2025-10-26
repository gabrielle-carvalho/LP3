import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class ClienteSimulador {
    public static void main(String[] args) {
        String host = "localhost";
        int porta = 1234;

        try (
            Socket socket = new Socket(host, porta);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            Scanner sc = new Scanner(System.in)
        ) {

            String msgServidor = in.readLine();
            System.out.println("Servidor: " + msgServidor);
            
            String numero = sc.nextLine();
            out.println(numero);

            msgServidor = in.readLine();
            System.out.println("Servidor: " + msgServidor);

        } catch (Exception e) {
            System.err.println("Erro no cliente: " + e.getMessage());
        }
        System.out.println("Cliente desconectado.");
    }
}