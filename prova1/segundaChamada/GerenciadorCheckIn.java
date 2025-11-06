import java.util.*;
import java.util.concurrent.*;

public class GerenciadorCheckIn {
    private final Map<String, Passageiro> passageiros;
    private final MapaAssentos mapaAssentos;
    private final Map<String, OperacaoTroca> operacoesAtivas;
    private final Semaphore limitadorOperacoes;
    private final ScheduledExecutorService monitorTimeout;
    private static final long TIMEOUT_OPERACAO_MS = 20000;
    
    public GerenciadorCheckIn(MapaAssentos mapaAssentos, int maxOperacoesSimultaneas) {
        this.passageiros = new ConcurrentHashMap<>();
        this.mapaAssentos = mapaAssentos;
        this.operacoesAtivas = new ConcurrentHashMap<>();
        this.limitadorOperacoes = new Semaphore(maxOperacoesSimultaneas, true);
        this.monitorTimeout = Executors.newScheduledThreadPool(1);
        
        iniciarMonitorTimeout();
    }
    
    private void iniciarMonitorTimeout() {
        monitorTimeout.scheduleAtFixedRate(() -> {
            verificarOperacoesExpiradas();
        }, 5, 5, TimeUnit.SECONDS);
    }
    
    private void verificarOperacoesExpiradas() {
        for (Map.Entry<String, OperacaoTroca> entry : operacoesAtivas.entrySet()) {
            OperacaoTroca op = entry.getValue();
            if (op.isExpirada(TIMEOUT_OPERACAO_MS) && 
                op.getEstado() != OperacaoTroca.EstadoOperacao.CONCLUIDA &&
                op.getEstado() != OperacaoTroca.EstadoOperacao.CANCELADA) {
                System.out.println("[TIMEOUT] Cancelando operação: " + entry.getKey());
                cancelarOperacao(entry.getKey());
            }
        }
    }
    
    public void registrarPassageiro(Passageiro passageiro) {
        passageiros.put(passageiro.getCpf(), passageiro);
    }
    
    /**
     * ========================================================================
     * ATIVIDADE 1: Iniciar Operação de Troca (Bloqueio de Recursos)
     * ========================================================================
     * 
     * CENÁRIO: Dois passageiros querem trocar de assento durante check-in
     * - João (CPF 111, assento 5A) quer trocar com Maria (CPF 222, assento 8C)
     * - Sistema precisa BLOQUEAR ambos passageiros E ambos assentos
     * - Locks ficam ABERTOS até confirmar ou cancelar
     * 
     * VOCÊ DEVE IMPLEMENTAR:
     * 
     * 1. CONTROLE DE TAXA
     *    - tryAcquire no Semaphore com timeout 10 segundos
     *    - Se falhar: retornar erro "SISTEMA_SOBRECARREGADO"
     * 
     * 2. VALIDAÇÕES BÁSICAS
     *    - Verificar se ambos CPFs existem no mapa de passageiros
     *    - Verificar se são CPFs diferentes
     *    - Se inválido: liberar semáforo e retornar erro
     * 
     * 3. CRIAR E REGISTRAR OPERAÇÃO
     *    - Criar OperacaoTroca com ID único
     *    - Adicionar em operacoesAtivas (thread-safe)
     *    - Mudar estado para BLOQUEANDO_RECURSOS
     * 
     * 4. DETERMINAR ORDEM DE BLOQUEIO - ANTI-DEADLOCK
     *    CRÍTICO: Sempre bloquear na mesma ordem global!
     *    
     *    Exemplo de ordem:
     *    a) Ordenar passageiros por CPF (String comparison)
     *    b) Ordenar assentos por código (String comparison)
     *    
     *    Se João(CPF=222) troca com Ana(CPF=111):
     *    - Ordem: Ana(111) primeiro, depois João(222)
     *    
     *    Se trocam assentos 8C e 5A:
     *    - Ordem: 5A primeiro, depois 8C
     * 
     * 5. BLOQUEAR PASSAGEIRO 1 (com ordem)
     *    Passageiro primeiroPassageiro = (cpf1 < cpf2) ? p1 : p2;
     *    
     *    if (!primeiroPassageiro.getLockPassageiro().tryLock(5, SECONDS)) {
     *        → cancelar operação
     *        → retornar erro "TIMEOUT_PASSAGEIRO"
     *    }
     *    
     *    // Verificações após lock
     *    if (primeiroPassageiro.getFezCheckIn()) {
     *        → unlock e cancelar
     *        → retornar erro "JA_FEZ_CHECKIN"
     *    }
     *    
     *    operacao.adicionarRecursoBloqueado(primeiroPassageiro);
     *    // NÃO CHAME unlock() aqui!
     * 
     * 6. BLOQUEAR PASSAGEIRO 2 (com ordem)
     *    - Mesma lógica do passo 5
     *    - Se falhar: precisa fazer rollback do lock do passageiro 1
     * 
     * 7. BLOQUEAR ASSENTO 1 (com ordem)
     *    String primeiroAssento = (assento1 < assento2) ? assento1 : assento2;
     *    Assento a1 = mapaAssentos.getAssento(primeiroAssento);
     *    
     *    if (!a1.getTrava().tryLock(5, SECONDS)) {
     *        → cancelar operação (libera locks anteriores)
     *        → retornar erro "TIMEOUT_ASSENTO"
     *    }
     *    
     *    operacao.adicionarRecursoBloqueado(a1);
     *    // NÃO CHAME unlock() aqui!
     * 
     * 8. BLOQUEAR ASSENTO 2 (com ordem)
     *    - Mesma lógica do passo 7
     *    - Se falhar: rollback de TODOS os locks anteriores
     * 
     * 9. SUCESSO
     *    operacao.mudarEstado(RECURSOS_BLOQUEADOS);
     *    return RespostaReserva.sucesso(
     *        idOperacao, 
     *        "Recursos bloqueados. Use confirmarTroca() para efetivar"
     *    );
     * 
     * 10. ROLLBACK EM CASO DE FALHA
     *     - Em QUALQUER falha nos passos acima:
     *     - Chamar cancelarOperacao(idOperacao)
     *     - Esse método liberará TODOS os locks já adquiridos
     * 
     * EXEMPLO DE ORDEM CORRETA:
     * trocar(CPF="333", CPF="111", assento1="8C", assento2="5A")
     * 
     * Ordem de bloqueio:
     * 1. Passageiro CPF="111" (menor CPF)
     * 2. Passageiro CPF="333" (maior CPF)
     * 3. Assento "5A" (menor código)
     * 4. Assento "8C" (maior código)
     * 
     * TODOS sempre bloqueiam nessa ordem = SEM DEADLOCK!
     */
    public RespostaReserva iniciarTrocaAssentos(String cpf1, String cpf2) {
        // TODO: IMPLEMENTAR AQUI - ATIVIDADE 1
        throw new UnsupportedOperationException(
            "Método iniciarTrocaAssentos() não implementado - ATIVIDADE 1\n" +
            "DICA: Ordene os recursos antes de bloquear! CPFs e assentos em ordem alfabética."
        );        
    }
    
    /**
     * ========================================================================
     * ATIVIDADE 2: Confirmar Troca (Efetivar e Liberar Locks)
     * ========================================================================
     * 
     * CENÁRIO: Cliente confirmou a troca. Agora efetive e libere os locks.
     * 
     * VOCÊ DEVE IMPLEMENTAR:
     * 
     * 1. VALIDAÇÕES
     *    - Obter operação de operacoesAtivas
     *    - Se não existe: retornar erro "OPERACAO_INVALIDA"
     *    - Se estado != RECURSOS_BLOQUEADOS: retornar erro "ESTADO_INVALIDO"
     * 
     * 2. OBTER RECURSOS BLOQUEADOS
     *    List<Object> recursos = operacao.getRecursosBloqueados();
     *    
     *    Separar em:
     *    - List<Passageiro> passageirosBloqueados
     *    - List<Assento> assentosBloqueados
     *    
     *    (use instanceof para identificar tipo)
     * 
     * 3. EXTRAIR INFORMAÇÕES ANTES DE TROCAR
     *    Passageiro p1 = passageirosBloqueados.get(0);
     *    Passageiro p2 = passageirosBloqueados.get(1);
     *    
     *    String assentoAntigoP1 = p1.getAssentoAtual();
     *    String assentoAntigoP2 = p2.getAssentoAtual();
     * 
     * 4. ATUALIZAR ESTADO DOS ASSENTOS NO MAPA
     *    - Trocar passageiros nos assentos
     *    - Usar compareAndSet se necessário para atomicidade
     *    
     *    Assento a1 = mapaAssentos.getAssento(assentoAntigoP1);
     *    Assento a2 = mapaAssentos.getAssento(assentoAntigoP2);
     *    
     *    // Trocar ocupantes
     *    a1.ocupar(p2.getNome(), p2.getCategoria(), p2.getCpf());
     *    a2.ocupar(p1.getNome(), p1.getCategoria(), p1.getCpf());
     * 
     * 5. ATUALIZAR PASSAGEIROS
     *    p1.trocarAssento(assentoAntigoP2);
     *    p2.trocarAssento(assentoAntigoP1);
     * 
     * 6. LIBERAR LOCKS DOS PASSAGEIROS
     *    for (Passageiro p : passageirosBloqueados) {
     *        try {
     *            p.getLockPassageiro().unlock();
     *            // Lock adquirido em OUTRO método!
     *        } catch (Exception e) {
     *            // Logar mas continuar liberando outros
     *        }
     *    }
     * 
     * 7. LIBERAR LOCKS DOS ASSENTOS
     *    for (Assento a : assentosBloqueados) {
     *        try {
     *            a.getTrava().unlock();
     *            // Lock adquirido em OUTRO método!
     *        } catch (Exception e) {
     *            // Logar mas continuar
     *        }
     *    }
     * 
     * 8. FINALIZAR
     *    operacao.mudarEstado(CONCLUIDA);
     *    limitadorOperacoes.release();
     *    operacoesAtivas.remove(idOperacao);
     *    
     *    return RespostaReserva.sucesso(
     *        "Troca realizada: " + p1.getNome() + " ↔ " + p2.getNome()
     *    );
     * 
     */
    public RespostaReserva confirmarTroca(String idOperacao) {
        // TODO: IMPLEMENTAR AQUI - ATIVIDADE 2
        throw new UnsupportedOperationException(
            "Método confirmarTroca() não implementado - ATIVIDADE 2\n" +
            "DICA: Use try-catch em CADA unlock para garantir que todos sejam liberados."
        );
    }

    /**
     * ========================================================================
     * ATIVIDADE 3: Cancelar Operação (Rollback Total)
     * ========================================================================
     * 
     * CENÁRIO: Operação falhou, timeout, ou cliente desistiu.
     * Precisa liberar TODOS os locks, mesmo os parcialmente adquiridos.
     * 
     * VOCÊ DEVE IMPLEMENTAR:
     * 
     * 1. OBTER OPERAÇÃO
     *    OperacaoTroca op = operacoesAtivas.get(idOperacao);
     *    if (op == null) {
     *        return; // Já foi cancelada, tudo bem
     *    }
     * 
     * 2. MARCAR COMO CANCELADA
     *    op.mudarEstado(EstadoOperacao.CANCELADA);
     * 
     * 3. OBTER RECURSOS BLOQUEADOS
     *    List<Object> recursos = op.getRecursosBloqueados();
     *    
     *    IMPORTANTE: Lista pode estar parcialmente preenchida!
     *    Se falhou no meio do bloqueio, nem todos os recursos foram adquiridos.
     * 
     * 4. LIBERAR LOCKS DE PASSAGEIROS
     *    for (Object recurso : recursos) {
     *        if (recurso instanceof Passageiro) {
     *            Passageiro p = (Passageiro) recurso;
     *            try {
     *                // Verificar se lock está realmente adquirido
     *                // Tentar unlock mesmo assim (IllegalMonitorStateException se não tiver)
     *                p.getLockPassageiro().unlock();
     *                
     *            } catch (IllegalMonitorStateException e) {
     *                // Lock não estava adquirido, tudo bem
     *                
     *            } catch (Exception e) {
     *                // Qualquer outro erro: LOGAR mas CONTINUAR
     *                System.err.println("Erro ao liberar lock passageiro: " + e);
     *            }
     *        }
     *    }
     * 
     * 5. LIBERAR LOCKS DE ASSENTOS
     *    for (Object recurso : recursos) {
     *        if (recurso instanceof Assento) {
     *            Assento a = (Assento) recurso;
     *            try {
     *                a.getTrava().unlock();
     *                
     *            } catch (IllegalMonitorStateException e) {
     *                // Lock não estava adquirido, ok
     *                
     *            } catch (Exception e) {
     *                // Logar mas continuar
     *                System.err.println("Erro ao liberar lock assento: " + e);
     *            }
     *        }
     *    }
     * 
     * 6. LIBERAR SEMAPHORE
     *    try {
     *        limitadorOperacoes.release();
     *    } catch (Exception e) {
     *        // Se já foi liberado, tudo bem
     *    }
     * 
     * 7. LIMPAR OPERAÇÃO
     *    operacoesAtivas.remove(idOperacao);
     *    System.out.println("[CANCELADO] Operação " + idOperacao);
     * 
     * REGRAS CRÍTICAS:
     * - Este método NUNCA deve lançar exceção
     * - Deve liberar TODOS os locks, mesmo se alguns falharem
     * - Deve ser IDEMPOTENTE (pode ser chamado múltiplas vezes)
     * - Precisa lidar com lista parcialmente preenchida
     * - Catch IllegalMonitorStateException é esperado e OK
     * 
     */
    public void cancelarOperacao(String idOperacao) {
        // TODO: IMPLEMENTAR AQUI - ATIVIDADE 3
        throw new UnsupportedOperationException(
            "Método cancelarOperacao() não implementado - ATIVIDADE 3\n" +
            "DICA: Use try-catch em CADA unlock. Capture IllegalMonitorStateException separadamente."
        );
    }
    
    public void encerrar() {
        monitorTimeout.shutdownNow();
        for (String id : new ArrayList<>(operacoesAtivas.keySet())) {
            cancelarOperacao(id);
        }
    }
    
    public MapaAssentos getMapaAssentos() {
        return mapaAssentos;
    }
    
    public Map<String, Passageiro> getTodosPassageiros() {
        return new ConcurrentHashMap<>(passageiros);
    }
}