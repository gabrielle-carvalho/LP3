import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerFiles {
    public static void main(String[] args) {
        int port = 12345;
        String nomeArquivoRecebido = "arquivo_recebido_do_cliente.dat";

        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Servidor de arquivos iniciado na porta " + port);
            while (true) {
                System.out.println("Aguardando conexão");

                try (Socket clientSocket = serverSocket.accept()) {
                    System.out.println("Cliente conectado: " + clientSocket.getInetAddress());

                    // Pega o fluxo de bytes que vem do cliente
                    InputStream in = clientSocket.getInputStream();

                    // Cria um arquivo no disco para salvar os bytes recebidos
                    try (FileOutputStream out = new FileOutputStream(nomeArquivoRecebido)) {
                        System.out.println("Recebendo arquivo e salvando como: " + nomeArquivoRecebido);

                        byte[] buffer = new byte[8192];
                        int bytesRead;

                        // Lê do cliente e escreve no arquivo
                        while ((bytesRead = in.read(buffer)) != -1) {
                            out.write(buffer, 0, bytesRead);
                        }

                        System.out.println("Arquivo recebido com sucesso!");
                    }

                } catch (IOException e) {
                    System.err.println("Erro " + e.getMessage());
                }
            }

        } catch (IOException e) {
            System.err.println("Erro ao iniciar o servidor: " + e.getMessage());
        }
    }
}
