import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.*;

public class ServidorReservaVoo {
    private static final int PORTA = 27301;
    private static final int TAMANHO_POOL_THREADS = 50;
    // CONCEITO: TAMANHO DO POOL DE THREADS
    // Define quantas threads trabalhadoras ficarão ativas no pool
    // limita o número de clientes que podem ser tratados SIMULTANEAMENTE
    // Se 50 clientes se conectarem, todos serão aceitos, mas apenas 50 por vez tera seu loop de `TratadorCliente` executando

    private static final int MAX_RESERVAS_SIMULTANEAS = 10;
    // CONCEITO: SEMAPHORE (Limite de Taxa)
    // Este é o número de *reservas* ativas (não clientes) que podem
    // ser processadas ao mesmo tempo pelo AlocadorAssentos

    private final ServerSocket socketServidor;
    private final ExecutorService servicoExecutor;
    // CONCEITO: ExecutorService (Pool de Threads)
    // Esta é a forma moderna de gerenciar threads.
    // Em vez de `new Thread(tarefa).start()` para cada cliente,
    // nós entregamos a "tarefa" (o TratadorCliente) para o Executor.
    // O Executor tem um número FIXO de threads (50, neste caso)
    // que ele reutiliza, economizando recursos do sistema.

    private final PriorityBlockingQueue<RequisicaoReserva> filaRequisicoes;
    // CONCEITO: PriorityBlockingQueue (Padrão Produtor-Consumidor)
    // Esta é a "fila de tarefas" entre os TratadorCliente (Produtores)
    // e o AlocadorAssentos (Consumidor).
    // É "Blocking" (bloqueante): se o Consumidor tentar pegar um item e a
    // fila estiver vazia, ele "dorme" (bloqueia) até um item chegar.
    // É "Priority" (prioridade): os itens são ordenados automaticamente
    // (neste caso, pela Categoria do cliente).
    private final MapaAssentos mapaAssentos;
    private final Semaphore limitadorTaxa;
    // CONCEITO: Semaphore (Semáforo)
    // Um semáforo é um "porteiro" que só deixa N threads passarem.
    // Ele será usado pelo AlocadorAssentos para garantir que,
    // das milhares de requisições na fila, apenas 10 sejam
    // PROCESSADAS ativamente de cada vez.

    private final AlocadorAssentos alocador;
    private volatile boolean executando = true;
    // CONCEITO: 'volatile'
    // 'volatile' garante VISIBILIDADE.
    // Quando 'executando' for definido como 'false' pela thread principal
    // (no método 'encerrar()'), esta flag garante que as outras threads
    // (como a thread do `while(executando)`) VEJAM a mudança
    // imediatamente e parem o loop.
    
    public ServidorReservaVoo(int porta) throws IOException {
        this.socketServidor = new ServerSocket(porta);
        this.servicoExecutor = Executors.newFixedThreadPool(TAMANHO_POOL_THREADS);
        // CONCEITO: Criação do Pool de Threads
        // Executors.newFixedThreadPool(N) cria um pool que mantém
        // exatamente N threads vivas, prontas para o trabalho.

        this.filaRequisicoes = new PriorityBlockingQueue<>();
        
        this.mapaAssentos = new MapaAssentos(30, new char[]{'A', 'B', 'C', 'D', 'E', 'F'});
        
        this.limitadorTaxa = new Semaphore(MAX_RESERVAS_SIMULTANEAS, true);
        // O semáforo é iniciado com 10 "permissões".
        
        this.alocador = new AlocadorAssentos(filaRequisicoes, mapaAssentos, limitadorTaxa);
        // O Alocador (Consumidor) é criado e recebe as ferramentas

        System.out.println("╔════════════════════════════════════════════════╗");
        System.out.println("║   Sistema de Reserva de Voo - Servidor        ║");
        System.out.println("╚════════════════════════════════════════════════╝");
        System.out.println("Porta: " + porta);
        System.out.println("Assentos: " + mapaAssentos.getTotalAssentos());
        System.out.println("Pool de threads: " + TAMANHO_POOL_THREADS);
        System.out.println("Máx. reservas simultâneas: " + MAX_RESERVAS_SIMULTANEAS);
        System.out.println();
    }
    
    public void iniciar() {
        servicoExecutor.submit(alocador);
        // CONCEITO: Iniciando o Consumidor
        // O AlocadorAssentos é um Runnable. Nós o submetemos ao pool.
        // O pool designará UMA thread (das 50) para rodar o
        // `alocador.run()` para sempre.
        
        System.out.println("[Servidor] Aguardando conexões...\n");
        
        while (executando) {
            try {
                Socket socketCliente = socketServidor.accept(); // O .accept() bloqueia a thread principal até um cliente chegar
                TratadorCliente tratador = new TratadorCliente(socketCliente, filaRequisicoes, mapaAssentos);
                // CONCEITO: Submetendo Tarefas (Produtor)
                // O TratadorCliente é um Runnable. Em vez de `new Thread(tratador).start()`,
                // nós o entregamos ao pool. O pool encontrará uma thread
                // livre (das 50) para executar o `tratador.run()`.

                servicoExecutor.submit(tratador);
                
            } catch (IOException e) {
                if (executando) {
                    System.err.println("[Servidor] Erro ao aceitar conexão: " + e.getMessage());
                }
            }
        }
    }
    
    public void encerrar() {
        System.out.println("\n[Servidor] Iniciando encerramento...");
        executando = false;// Sinaliza para o loop principal parar
        
        try {
            socketServidor.close();// Força o .accept() a parar
        } catch (IOException e) {
            System.err.println("Erro ao fechar ServerSocket: " + e.getMessage());
        }
        
        alocador.encerrar(); // Sinaliza para o alocador parar (volatile = false)
        // CONCEITO: Encerramento do Consumidor

        servicoExecutor.shutdown();
        // CONCEITO: Encerramento do Pool (Graceful Shutdown)
        // 1. shutdown(): Pára de aceitar NOVAS tarefas.
        
        try {
            if (!servicoExecutor.awaitTermination(30, TimeUnit.SECONDS)) {
            // 2. awaitTermination(): Bloqueia a thread principal e espera
            // até 30 segundos para que as tarefas ATUAIS terminem.

                servicoExecutor.shutdownNow();
                // 3. shutdownNow(): Se o tempo estourou, força o cancelamento
                // de todas as tarefas restantes (interrompendo-as).
            }
        } catch (InterruptedException e) {
            servicoExecutor.shutdownNow();
            Thread.currentThread().interrupt();
        }
        
        System.out.println("[Servidor] Encerrado");
    }
    
    public static void main(String[] args) {
        try {
            ServidorReservaVoo servidor = new ServidorReservaVoo(PORTA);
            
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