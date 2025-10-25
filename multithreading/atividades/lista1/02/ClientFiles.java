import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

public class ClientFiles {
    public static void main(String[] args) {
        String host = "localhost";
        int port = 12345;
        String nomeArquivoEnviar = "meu_arquivo_para_enviar.txt";

        try (
            // Abre conexão com o servidor
            Socket socket = new Socket(host, port);
            // Para onde vamos escrever os bytes (a conexão com o servidor)
            OutputStream out = socket.getOutputStream();
            // De onde vamos ler os bytes (o arquivo no disco)
            FileInputStream in = new FileInputStream(nomeArquivoEnviar);
        ) {
            System.out.println("Conectado ao servidor. Enviando arquivo: " + nomeArquivoEnviar);

            // Mesmo loop do servidor, mas com origem e destino invertidos
            byte[] buffer = new byte[8192];
            int bytesRead;

            // Lê do arquivo (in) e escreve para o servidor (out)
            while ((bytesRead = in.read(buffer)) != -1) {
                out.write(buffer, 0, bytesRead);
            }

            System.out.println("Arquivo enviado com sucesso!");

        } catch (IOException e) {
            System.err.println("Erro no cliente: " + e.getMessage());
        }
    }
}
