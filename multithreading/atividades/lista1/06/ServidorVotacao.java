// Implemente um servidor TCP para um sistema de votação onde os clientes podem votar
// em opções específicas. O servidor deve contar os votos e fornecer resultados atualizados
// aos clientes.

import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import java.io.*;
import java.util.concurrent.CopyOnWriteArrayList;

public class ServidorVotacao {
    private static String opcao1 = "Bill";
    private static String opcao2 = "Boris";
    private static String opcao3 = "Hera";
    private static int votos1=0;
    private static int votos2=0;
    private static int votos3=0;

    private static final Object lockVoto = new Object(); 
    private static final List<PrintWriter> clientesConectados = new CopyOnWriteArrayList<>();

    public static void main(String[] args){
        int porta = 1234;
        try (ServerSocket serverSocket = new ServerSocket(porta)){
            System.out.println("Servidor de sistema de votacao iniciado");
            System.out.println("Ouvindo na porta " + porta);
            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Novo cliente conectado: " + clientSocket.getInetAddress());
                new Thread(new ClientHandler(clientSocket)).start();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static class ClientHandler implements Runnable {
        private final Socket socket;
        private PrintWriter writer;
        private boolean votou = false;

        public ClientHandler(Socket socket){
            this.socket = socket;
        }

        @Override
        public void run(){
            try(OutputStream out = socket.getOutputStream();
                PrintWriter w = new PrintWriter(out, true);
                BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()))
            ){
                this.writer = w;
                clientesConectados.add(writer);
                writer.println("Bem-vindo ao sistema de votos");


                writer.println("\nDigite o código da opção que deseja votar:");
                writer.println("001 - "+opcao1);
                writer.println("002 - "+opcao2);
                writer.println("003 - "+opcao3);
                
                String linhaDoCliente;
                while ((linhaDoCliente = reader.readLine()) != null) {
                    processarVoto(linhaDoCliente.trim(), this);
                }

            } catch (IOException e) {
                System.err.println("Cliente desconectado: " + socket.getInetAddress());
            } finally {
                if (writer != null) {
                    clientesConectados.remove(writer);
                }
                                
                try {
                    socket.close();
                } catch (IOException e) { /* ... */ }
            }
        }
    }


    private static void processarVoto(String voto, ClientHandler cliente) {
        synchronized (lockVoto) {
            if (!cliente.votou) {
                switch (voto) {
                        case "001":
                            votos1++;
                            cliente.writer.println("\nVoto em " + opcao1 + " registrado com sucesso!");
                            break;
                        case "002":
                            votos2++;
                            cliente.writer.println("nVoto em " + opcao2 + " registrado com sucesso!");
                            break;
                        case "003":
                            votos3++;
                            cliente.writer.println("\nVoto em " + opcao3 + " registrado com sucesso!");
                            break;
                        default:
                            cliente.writer.println("\nCódigo inválido. Tente novamente.");
                            return;
                    }
                cliente.votou=true;
                enviarAtualizacaoTodos(); 
            } else {
                cliente.writer.println("voce ja votou anteriormente");
            }
        }
    }

    private static void enviarAtualizacaoTodos() {
        String resultado = "\nResultado Parcial\n"
                + opcao1 + ": " + votos1 + " votos\n"
                + opcao2 + ": " + votos2 + " votos\n"
                + opcao3 + ": " + votos3 + " votos\n";

        for (PrintWriter w : clientesConectados) {
            w.println(resultado);
        }
    }

}
