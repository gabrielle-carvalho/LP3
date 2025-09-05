import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.net.Socket;

public class remetenteArqTCP {
    public static void main(String[] args) {
        if (args.length != 3) {
            System.err.println("coloque <servidor> <porta> <arquivo>");
            System.exit(1);
        }

        String servidor = args[0];
        int porta = Integer.parseInt(args[1]);
        String caminho = args[2];

        try (Socket socket = new Socket(servidor, porta)) {
            File file = new File(caminho);
            FileInputStream fileIn = new FileInputStream(file);

            OutputStream output = socket.getOutputStream();

            byte[] buffer = new byte[4096];
            int bytesRead;

            while ((bytesRead = fileIn.read(buffer)) != -1) {
                output.write(buffer, 0, bytesRead);
            }

            fileIn.close();
            socket.close();
            System.out.println("enviado");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
