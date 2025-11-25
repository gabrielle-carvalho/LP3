import java.io.ObjectInputFilter;
import java.rmi.*;
import java.rmi.registry.*;
import java.rmi.server.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.locks.*;

public class ServidorLeiloeiro extends UnicastRemoteObject implements ServicoLeilao {
    
    // Estruturas de dados compartilhadas
    private final ConcurrentHashMap<String, Leilao> leiloes;
    private final ConcurrentHashMap<String, Comprador> compradores;
    
    // Controle de concorrência
    private final ReentrantLock lockLance;
    
    // Executores
    private final ExecutorService executorLances;
    private final ScheduledExecutorService executorTimeout;
    
    // Constantes
    private static final long TIMEOUT_INATIVIDADE_MS = 300000; // 5 minutos
    private static final double INCREMENTO_MINIMO_PERCENTUAL = 0.05; // 5%
    
    public ServidorLeiloeiro() throws RemoteException {
        super();
        this.leiloes = new ConcurrentHashMap<>();
        this.compradores = new ConcurrentHashMap<>();
        this.lockLance = new ReentrantLock();
        this.executorLances = Executors.newFixedThreadPool(10);
        this.executorTimeout = Executors.newScheduledThreadPool(5);
        
        log("Servidor Leiloeiro iniciado");
    }
    
    // =========================================================================
    // MÉTODOS DE VENDEDOR
    // =========================================================================
    
    @Override
    public String cadastrarItem(ItemLeilao item) throws RemoteException {
        String leilaoId = "L" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        
        Leilao leilao = new Leilao(leilaoId, item);
        leiloes.put(leilaoId, leilao);
        
        log(String.format("ITEM CADASTRADO - Leilao=%s Item=%s PrecoInicial=R$%.2f Vendedor=%s",
            leilaoId, item.getNome(), item.getPrecoInicial(), item.getVendedorId()));
        
        // Agendar timeout inicial (15s sem lances)
        agendarTimeoutInatividade(leilaoId);
        
        return leilaoId;
    }
    
    @Override
    public boolean cancelarLeilao(String leilaoId, String vendedorId) throws RemoteException {
        Leilao leilao = leiloes.get(leilaoId);
        
        if (leilao == null) {
            return false;
        }
        
        // Verificar se vendedor é o dono
        if (!leilao.getItem().getVendedorId().equals(vendedorId)) {
            log("CANCELAMENTO NEGADO - Vendedor não é o dono: " + leilaoId);
            return false;
        }
        
        // Só pode cancelar se ATIVO e sem lances
        if (leilao.getStatus() != StatusLeilao.ATIVO || !leilao.getLances().isEmpty()) {
            log("CANCELAMENTO NEGADO - Leilao tem lances ou não está ativo: " + leilaoId);
            return false;
        }
        
        leilao.setStatus(StatusLeilao.CANCELADO);
        
        // Cancelar timeout agendado
        if (leilao.getTarefaTimeout() != null) {
            leilao.getTarefaTimeout().cancel(false);
        }
        
        log("LEILAO CANCELADO - " + leilaoId);
        return true;
    }
    
    @Override
    public List<InfoLeilao> listarLeiloesVendedor(String vendedorId) throws RemoteException {
        List<InfoLeilao> resultado = new ArrayList<>();
        
        for (Leilao leilao : leiloes.values()) {
            if (leilao.getItem().getVendedorId().equals(vendedorId)) {
                resultado.add(leilao.toInfo());
            }
        }
        
        return resultado;
    }
    
    // =========================================================================
    // MÉTODOS DE COMPRADOR
    // =========================================================================
    
    @Override
    public boolean registrarComprador(String compradorId, CallbackComprador callback) 
            throws RemoteException {
        
        if (compradores.containsKey(compradorId)) {
            log("REGISTRO COMPRADOR FALHOU - Já existe: " + compradorId);
            return false;
        }
        
        Comprador comprador = new Comprador(compradorId, callback);
        compradores.put(compradorId, comprador);
        
        log("COMPRADOR REGISTRADO - " + compradorId);
        return true;
    }
    
    @Override
    public ResultadoLance fazerLance(String leilaoId, String compradorId, double valor) 
            throws RemoteException {
        
        // Processar lance de forma assíncrona
        executorLances.submit(() -> processarLance(leilaoId, compradorId, valor));
        
        // Retornar resultado preliminar
        return new ResultadoLance(true, "Lance em processamento", valor, 0.0);
    }
    
    @Override
    public List<InfoLeilao> listarLeiloesAtivos() throws RemoteException {
        List<InfoLeilao> resultado = new ArrayList<>();
        
        for (Leilao leilao : leiloes.values()) {
            if (leilao.getStatus() == StatusLeilao.ATIVO) {
                resultado.add(leilao.toInfo());
            }
        }
        
        return resultado;
    }
    
    @Override
    public InfoLeilao consultarLeilao(String leilaoId) throws RemoteException {
        Leilao leilao = leiloes.get(leilaoId);
        
        if (leilao == null) {
            throw new RemoteException("Leilão não encontrado: " + leilaoId);
        }
        
        return leilao.toInfo();
    }
    
    @Override
    public List<Lance> listarLancesLeilao(String leilaoId) throws RemoteException {
        Leilao leilao = leiloes.get(leilaoId);
        
        if (leilao == null) {
            throw new RemoteException("Leilão não encontrado: " + leilaoId);
        }
        
        return new ArrayList<>(leilao.getLances());
    }
    
    /**
     * MÉTODO 1 - PARA IMPLEMENTAR (3,0 pontos)
     * 
     * Processa um lance feito por um comprador.
     * 
     * REQUISITOS:
     * 1. Usar lockLance.lock() para garantir exclusão mútua
     * 2. Dentro do lock (try-finally):
     *    a) Buscar leilão pelo ID, se null retornar
     *    b) Verificar se leilão está ATIVO, se não logar e retornar
     *    c) Chamar validarLance(leilao, compradorId, valor)
     *    d) Se não válido, logar motivo e retornar
     *    e) Se válido:
     *       - Calcular liderAnterior = leilao.getCompradorLiderAtual()
     *       - Criar Lance com UUID: new Lance(lanceId, leilaoId, compradorId, valor)
     *       - leilao.adicionarLance(lance)
     *       - leilao.setLanceAtual(valor)
     *       - leilao.setCompradorLiderAtual(compradorId)
     *       - Cancelar timeout antigo: leilao.getTarefaTimeout().cancel(false)
     *       - Logar: "[LANCE ACEITO] Leilao=X Comprador=Y Valor=R$Z"
     * 3. SEMPRE liberar lock no finally
     * 4. FORA do lock:
     *    - Se havia líder anterior diferente, chamar notificarOutbid()
     *    - Agendar novo timeout: agendarTimeoutInatividade(leilaoId)
     * 
     * ATENÇÃO:
     * - Lock evita dois lances simultâneos serem aceitos incorretamente
     * - Notificação DEVE ser fora do lock (RMI bloqueante)
     * - Sempre cancele timeout antigo antes de agendar novo
     */
    private void processarLance(String leilaoId, String compradorId, double valor)
    {
        lockLance.lock();
        String liderAnterior = "";

        try
        {
            Leilao leilao = leiloes.get(leilaoId);

            if (leilao == null)
            {
                return;
            }

            if (leilao.getStatus() != StatusLeilao.ATIVO)
            {
                return;
            }
            
            boolean validado = validarLance(leilao, compradorId, valor);

            if (!validado)
            {
                log("Lance inválido");
                return;
            }
            else
            {
                liderAnterior = leilao.getCompradorLiderAtual();
                String lanceId = "LC" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
                Lance lance = new Lance(lanceId, leilaoId, compradorId, valor);
                leilao.adicionarLance(lance);
                leilao.setLanceAtual(valor);
                leilao.setCompradorLiderAtual(compradorId);

                leilao.getTarefaTimeout().cancel(false);
                String str = String.format("[LANCE ACEITO] Leilao=%s Comprador=%s Valor=R$%.2f", leilaoId, compradorId, lance.getValor());
                log(str);
            }

        } catch (Exception e)
        {
            System.out.println(e);
        }
        finally
        {
            lockLance.unlock();

            if (!liderAnterior.equals(compradorId))
            {
                notificarOutbid(liderAnterior, leilaoId, valor, compradorId);
            }

            agendarTimeoutInatividade(leilaoId);
            
        }
    }
    
    /**
     * MÉTODO 2 - PARA IMPLEMENTAR (2,0 pontos)
     * 
     * Valida se um lance pode ser aceito.
     * 
     * REGRAS DE VALIDAÇÃO:
     * 1. Lance deve ser >= lanceAtual * 1.05 (incremento mínimo 5%)
     * 2. Comprador não pode dar lance se já é o líder atual
     * 3. Comprador deve estar registrado no sistema
     * 
     * ALGORITMO:
     * 1. double lanceAtual = leilao.getLanceAtual()
     * 2. double incrementoMinimo = lanceAtual * INCREMENTO_MINIMO_PERCENTUAL
     * 3. double lanceMinimo = lanceAtual + incrementoMinimo
     * 
     * 4. Se valor < lanceMinimo:
     *    - Retornar false (lance insuficiente)
     * 
     * 5. String liderAtual = leilao.getCompradorLiderAtual()
     * 6. Se liderAtual != null && liderAtual.equals(compradorId):
     *    - Retornar false (comprador já é o líder)
     * 
     * 7. Se !compradores.containsKey(compradorId):
     *    - Retornar false (comprador não registrado)
     * 
     * 8. Retornar true (lance válido)
     * 
     * EXEMPLO:
     * Lance atual: R$ 100,00
     * Incremento mínimo: R$ 100,00 * 0.05 = R$ 5,00
     * Lance mínimo: R$ 105,00
     * - Lance de R$ 104,00 → INVÁLIDO (menor que mínimo)
     * - Lance de R$ 105,00 → VÁLIDO
     * - Lance de R$ 110,00 → VÁLIDO
     */
    private boolean validarLance(Leilao leilao, String compradorId, double valor) 
    {
        double lanceAtual = leilao.getLanceAtual();
        double incrementoMinimo = lanceAtual * INCREMENTO_MINIMO_PERCENTUAL;
        double lanceMinimo = lanceAtual + incrementoMinimo;

        String liderAtual = leilao.getCompradorLiderAtual();

        if (liderAtual != null)
        {
            if (leilao.getCompradorLiderAtual().equals(compradorId)) return false;
        }

        if (valor < lanceAtual * 1.05 || compradores.get(compradorId) == null) return false;

        if (valor < lanceMinimo) return false;
        

        if (liderAtual != null && liderAtual.equals(compradorId)) return false;

        return compradores.containsKey(compradorId);
    }
    
    /**
     * MÉTODO 3 - PARA IMPLEMENTAR (3,0 pontos)
     * 
     *
     * Notifica comprador que foi superado por outro lance (outbid).
     * 
     * REQUISITOS:
     * 1. Buscar comprador antigo no mapa compradores
     * 2. Se comprador == null, apenas logar e retornar (desconectou)
     * 3. Tentar callback RMI (dentro de try-catch RemoteException):
     *    try {
     *        comprador.getCallback().aoSerSuperado(leilaoId, novoLance, novoLider)
     *        log("NOTIFICACAO OUTBID - Comprador=%s Leilao=%s NovoLance=R$%.2f")
     *    } catch (RemoteException e) {
     *        log("FALHA CALLBACK OUTBID - Comprador=%s desconectado")
     *        // Não remover comprador (pode voltar depois)
     *    }
     * 
     * IMPORTANTE:
     * - Este método é chamado FORA do lock para não bloquear
     * - RemoteException é normal (comprador pode ter desconectado)
     * - Não é crítico se falhar (apenas notificação informativa)
     */
    private void notificarOutbid(String compradorAnterior, String leilaoId, double novoLance, String novoLider)
    {
        Comprador comprador = compradores.get(compradorAnterior);
        
        if (comprador == null)
        {
            log("Comprador anterior é nulo");
            return;
        }

        try
        {
            comprador.getCallback().aoSerSuperado(leilaoId, novoLance, novoLider);
            String str = String.format("NOTIFICACAO OUTBID - Comprador=%s Leilao=%s NovoLance=R$%.2f", compradorAnterior, leilaoId, novoLance);
            log(str);
        } catch (RemoteException e)
        {
            String str = String.format("FALHA CALLBACK OUTBID - Comprador=%s desconectado", compradorAnterior);
            log(str);
        }

    }
    
    /**
     * MÉTODO 4 - PARA IMPLEMENTAR (2,0 pontos)
     * 
     * Agenda timeout de inatividade (5 minutos sem novos lances).
     * Quando timeout expira, leilão é finalizado automaticamente.
     * 
     * REQUISITOS:
     * 1. Buscar leilão pelo ID
     * 2. Se leilão == null ou status != ATIVO, retornar
     * 3. Agendar tarefa com executorTimeout.schedule():
     *    ScheduledFuture<?> tarefa = executorTimeout.schedule(() -> {
     *        // Código a executar após 5 minutos
     *        finalizarLilaoPorInatividade(leilaoId);
     *    }, TIMEOUT_INATIVIDADE_MS, TimeUnit.MILLISECONDS);
     * 4. Armazenar tarefa no leilão: leilao.setTarefaTimeout(tarefa)
     * 
     * IMPORTANTE:
     * - Este método é chamado após cada lance aceito
     * - Sempre cancele timeout antigo antes de chamar este método
     * - Se leilão não receber lance em 5 minutos, será finalizado automaticamente
     * 
     * NOTA: O método finalizarLeilaoPorInatividade() já está implementado
     */
    private void agendarTimeoutInatividade(String leilaoId)
    {
        Leilao leilao = leiloes.get(leilaoId);

        if (leilao == null || leilao.getStatus() != StatusLeilao.ATIVO)
        {
            return;
        }

        ScheduledFuture<?> tarefa = executorTimeout.schedule(() -> {
            finalizarLeilaoPorInatividade(leilaoId);
        }, TIMEOUT_INATIVIDADE_MS, TimeUnit.MILLISECONDS);

        leilao.setTarefaTimeout(tarefa);
    }
    
    /**
     * Finaliza leilão por inatividade (chamado pelo timeout).
     */
    private void finalizarLeilaoPorInatividade(String leilaoId) {
        Leilao leilao = leiloes.get(leilaoId);
        
        if (leilao == null || leilao.getStatus() != StatusLeilao.ATIVO) {
            return;
        }
        
        leilao.setStatus(StatusLeilao.FINALIZADO);
        
        String vencedor = leilao.getCompradorLiderAtual();
        double valorFinal = leilao.getLanceAtual();
        
        if (vencedor != null) {
            log(String.format("LEILAO FINALIZADO - Leilao=%s Vencedor=%s ValorFinal=R$%.2f Lances=%d",
                leilaoId, vencedor, valorFinal, leilao.getLances().size()));
            
            // Notificar vencedor
            notificarVencedor(vencedor, leilaoId, valorFinal);
        } else {
            log(String.format("LEILAO FINALIZADO SEM LANCES - Leilao=%s", leilaoId));
        }
    }
    
    /**
     * Notifica comprador vencedor.
     */
    private void notificarVencedor(String compradorId, String leilaoId, double valorFinal) {
        Comprador comprador = compradores.get(compradorId);
        
        if (comprador == null) {
            log("Vencedor desconectado: " + compradorId);
            return;
        }
        
        try {
            comprador.getCallback().aoVencerLeilao(leilaoId, valorFinal);
            log(String.format("NOTIFICACAO VITORIA - Comprador=%s Leilao=%s Valor=R$%.2f",
                compradorId, leilaoId, valorFinal));
        } catch (RemoteException e) {
            log("Falha ao notificar vencedor: " + compradorId);
        }
    }
    
    /**
     * Log auxiliar.
     */
    private void log(String mensagem) {
        String timestamp = String.format("[%tT]", System.currentTimeMillis());
        System.out.println(timestamp + " " + mensagem);
    }
    
    /**
     * Shutdown gracioso.
     */
    public void shutdown() {
        executorLances.shutdown();
        executorTimeout.shutdown();
        try {
            executorLances.awaitTermination(5, TimeUnit.SECONDS);
            executorTimeout.awaitTermination(5, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            log("Erro no shutdown: " + e.getMessage());
        }
    }
    
    public static void main(String[] args) {
        try {
            ServidorLeiloeiro servidor = new ServidorLeiloeiro();
            Registry registry = LocateRegistry.createRegistry(1099);
            registry.rebind("ServidorLeilao", servidor);
            
            System.out.println("=== SERVIDOR LEILÃO INICIADO ===");
            System.out.println("Registry: localhost:1099");
            System.out.println("Serviço: ServidorLeilao");
            System.out.println("Aguardando conexões...\n");
            
            Thread.currentThread().join();
            
        } catch (Exception e) {
            System.err.println("Erro no servidor: " + e.getMessage());
            e.printStackTrace();
        }
    }
}