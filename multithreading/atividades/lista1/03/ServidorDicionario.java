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

public class ServidorDicionario {
    private static final Map<String, String> DICIONARIO = new HashMap<>();    // Map para armazenar as palavras e seus significados.

    static {    // Bloco estático para popular o dicionário quando a classe é carregada
        DICIONARIO.put("java", "Uma linguagem de POO");
        DICIONARIO.put("socket", "Um ponto final em uma comunicação de rede");
        DICIONARIO.put("tcp", "Protocolo de Controle de Transmissão. Garante a entrega ordenada e confiável de dados");
        DICIONARIO.put("uneb", "Universidade do Estado da Bahia, instituição de ensino superior pública"); 
        DICIONARIO.put("gabi", "Estudante universitaria de 19 anos"); 
    }

    public static void main(String[] args) {
        int porta = 12346;

        try (ServerSocket serverSocket = new ServerSocket(porta)) {
            System.out.println("Servidor de Dicionário ouvindo na porta " + porta);

            while (true) {
                System.out.println("Aguardando cliente...");
                try (Socket clientSocket = serverSocket.accept()) {
                    System.out.println("Cliente conectado: " + clientSocket.getInetAddress());
                    
                    handleClient(clientSocket);

                } catch (IOException e) {
                    System.err.println("Erro ao lidar com o cliente: " + e.getMessage());
                }
            }
        } catch (IOException e) {
            System.err.println("Erro ao iniciar o servidor: " + e.getMessage());
        }
    }

    private static void handleClient(Socket clientSocket) throws IOException {
        // BufferedReader le linhas de texto do cliente
        InputStream in = clientSocket.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));

        // PrintWriter para envia LINHAS de texto para o cliente
        // O 'true' no construtor ativa o 'autoFlush', que envia a mensagem
        // imediatamente após cada .println()
        OutputStream out = clientSocket.getOutputStream();
        PrintWriter writer = new PrintWriter(out, true);

        String palavra;
        
        // Fica lendo palavras do cliente até que o cliente desconecte
        while ((palavra = reader.readLine()) != null) {
            
            if (palavra.equalsIgnoreCase("sair")) {
                System.out.println("Cliente " + clientSocket.getInetAddress() + " desconectou.");
                break;
            }

            System.out.println("Cliente " + clientSocket.getInetAddress() + " buscou por: " + palavra);

            // Busca a palavra no Map
            // .getOrDefault() é uma forma segura de buscar:
            // - Se achar, retorna o significado.
            // - Se não achar, retorna a mensagem padrão.
            String significado = DICIONARIO.getOrDefault(
                palavra.toLowerCase(), 
                "Palavra não esta no dicionário"
            );

            writer.println(significado);
        }
    }
}