import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.*;

public class ServidorCheckIn {
    private static final int PORTA = 5001;
    private static final int TAMANHO_POOL_THREADS = 30;
    
    private final ServerSocket socketServidor;
    private final ExecutorService servicoExecutor;
    private final GerenciadorCheckIn gerenciadorCheckIn;
    private volatile boolean executando = true;
    
    public ServidorCheckIn(int porta) throws IOException {
        this.socketServidor = new ServerSocket(porta);
        this.servicoExecutor = Executors.newFixedThreadPool(TAMANHO_POOL_THREADS);
        
        MapaAssentos mapaAssentos = new MapaAssentos(33, new char[]{'A', 'B', 'C', 'D', 'E', 'F'});
        this.gerenciadorCheckIn = new GerenciadorCheckIn(mapaAssentos, 5);
        
        inicializarPassageiros();
        
        System.out.println("╔════════════════════════════════════════════════╗");
        System.out.println("║   Sistema Check-in - Servidor                  ║");
        System.out.println("╚════════════════════════════════════════════════╝");
        System.out.println("Porta: " + porta);
        System.out.println("Pool de threads: " + TAMANHO_POOL_THREADS);
        System.out.println();
    }
    
    private void inicializarPassageiros() {
        Passageiro[] passageiros = {
            new Passageiro("11111", "João Silva", Categoria.GOLD, "5A"),
            new Passageiro("22222", "Maria Santos", Categoria.PLATINUM, "8C"),
            new Passageiro("33333", "Pedro Costa", Categoria.SILVER, "12B"),
            new Passageiro("44444", "Ana Lima", Categoria.BASIC, "15D"),
            new Passageiro("55555", "Carlos Souza", Categoria.GOLD, "20A"),
            new Passageiro("66666", "Julia Ferreira", Categoria.PLATINUM, "25F")
        };
        
        for (Passageiro p : passageiros) {
            gerenciadorCheckIn.registrarPassageiro(p);
            MapaAssentos mapa = gerenciadorCheckIn.getMapaAssentos();
            mapa.getAssento(p.getAssentoAtual()).ocupar(
                p.getNome(), p.getCategoria(), p.getCpf()
            );
            mapa.decrementarAssentosLivres();
        }
        
        System.out.println("✓ " + passageiros.length + " passageiros cadastrados");
    }
    
    public void iniciar() {
        System.out.println("[Servidor] Aguardando conexões...\n");
        
        while (executando) {
            try {
                Socket socketCliente = socketServidor.accept();
                TratadorClienteCheckIn tratador = new TratadorClienteCheckIn(
                    socketCliente, gerenciadorCheckIn
                );
                servicoExecutor.submit(tratador);
                
            } catch (IOException e) {
                if (executando) {
                    System.err.println("Erro ao aceitar conexão: " + e.getMessage());
                }
            }
        }
    }
    
    public void encerrar() {
        System.out.println("\n[Servidor] Encerrando...");
        executando = false;
        
        try {
            socketServidor.close();
        } catch (IOException e) {
            System.err.println("Erro ao fechar socket: " + e.getMessage());
        }
        
        gerenciadorCheckIn.encerrar();
        servicoExecutor.shutdown();
        
        try {
            if (!servicoExecutor.awaitTermination(30, TimeUnit.SECONDS)) {
                servicoExecutor.shutdownNow();
            }
        } catch (InterruptedException e) {
            servicoExecutor.shutdownNow();
            Thread.currentThread().interrupt();
        }
        
        System.out.println("[Servidor] Encerrado");
    }
    
    public static void main(String[] args) {
        try {
            ServidorCheckIn servidor = new ServidorCheckIn(PORTA);
            
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                servidor.encerrar();
            }));
            
            servidor.iniciar();
            
        } catch (IOException e) {
            System.err.println("Erro ao iniciar servidor: " + e.getMessage());
            e.printStackTrace();
        }
    }
}