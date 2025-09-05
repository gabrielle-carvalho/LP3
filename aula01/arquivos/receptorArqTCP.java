import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class receptorArqTCP {
    public static void main(String[] args) {
        if (args.length != 1) {
            System.err.println("ReptorArqTCP <porta>");
            System.exit(1);
        }
        try {
            int porta = Integer.parseInt(args[0]);
            try (ServerSocket serverSocket = new ServerSocket(porta)) {
                System.out.println("aguardando conexão na porta " + porta);

                Socket socket = serverSocket.accept();
                System.out.println("conectado");

                InputStream input = socket.getInputStream();
                FileOutputStream fileOut = new FileOutputStream("recebido.txt");

                byte[] buffer = new byte[4096];
                int bytesRead;

                while ((bytesRead = input.read(buffer)) != -1) {
                    fileOut.write(buffer, 0, bytesRead);
                }

                fileOut.close();
                socket.close();
                System.out.println("recebido");
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }
}

