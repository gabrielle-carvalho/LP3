import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class ProcessadorPedidos implements Runnable {
    private final PriorityBlockingQueue<RequisicaoPedido> filaRequisicoes; //fila d prioridades bloqueantes, 'take()' faz com que a thread espere ate aparecer novo pedido, bloqueia se a fila ta vazia
    private final ConcurrentHashMap<String, AtomicInteger> estoque; // versão thread-safe do HashMap. atomicinteger permite manipulacao atomica sem precisar de syncrhonized. chave = nome do produto e valor = quantidade disponível. Como é ConcurrentHashMap, várias threads podem atualizar o estoque ao mesmo tempo, sem corromper os dados.
    private final Semaphore apiPagamento;
    private volatile boolean executando = true;

    public ProcessadorPedidos(PriorityBlockingQueue<RequisicaoPedido> filaRequisicoes, ConcurrentHashMap<String, AtomicInteger> estoque,Semaphore apiPagamento) {
        this.filaRequisicoes = filaRequisicoes;
        this.estoque = estoque;
        this.apiPagamento = apiPagamento;
    }

    @Override
    public void run() {
        System.out.println("[ProcessadorPedidos] Iniciado.");
        while (executando) {
            try {
                RequisicaoPedido requisicao = filaRequisicoes.take(); //pega o de maior prioridade, o remove da fila
                processar(requisicao);

            } catch (InterruptedException e) {
                if (executando) Thread.currentThread().interrupt();
                break;
            }
        }
        System.out.println("[ProcessadorPedidos] Encerrado.");
    }

    private void processar(RequisicaoPedido requisicao) {
        RespostaPedido resposta;
        try {
            if (!apiPagamento.tryAcquire(10, TimeUnit.SECONDS)) { // Tenta adquirir o semáforo da API Pagamento wm ate 10 segundos
                enviarResposta(requisicao, RespostaPedido.falha("TIMEOUT", "API de pagamento sobrecarregada"));
                return;
            }
            
            Thread.sleep(50); // Simula 50ms de processamento

            resposta = darBaixaEstoque(requisicao); // Lógica de verificação e baixa de estoque

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            resposta = RespostaPedido.falha("ERRO_SERVIDOR", "Processamento interrompido");
        } finally {//entra aq todas as vezes
            apiPagamento.release(); //libera o semáforo
        }

        enviarResposta(requisicao, resposta);
    }

    private RespostaPedido darBaixaEstoque(RequisicaoPedido requisicao) {
        String idProduto = requisicao.getIdProduto();
        int quantidadePedida = requisicao.getQuantidade();
        
        AtomicInteger estoqueAtual = estoque.get(idProduto);

        if (estoqueAtual == null) {
            return RespostaPedido.falha("PRODUTO_INEXISTENTE", "Produto " + idProduto + " não encontrado");
        }

        while (true) { // Compare-And-Set para atomicidade
            int valorAtual = estoqueAtual.get();
            if (valorAtual < quantidadePedida) {
                return RespostaPedido.falha("ESTOQUE_INSUFICIENTE", "Estoque insuficiente para " + idProduto);
            }
            
            int novoValor = valorAtual - quantidadePedida;
            
            if (estoqueAtual.compareAndSet(valorAtual, novoValor)) {  // tenta atualizar atomicamente
                String codigoPedido = UUID.randomUUID().toString().substring(0, 8);
                System.out.printf("[PEDIDO] %s (%s) comprou %d de %s. Estoque restante: %d%n",
                        requisicao.getNomeCliente(), requisicao.getCategoria(), quantidadePedida, idProduto, novoValor);
                return RespostaPedido.sucesso(codigoPedido);
            }
            // Se falhou (outra thread atualizou o estoque), o loop tenta novamente
        }
    }

    private void enviarResposta(RequisicaoPedido requisicao, RespostaPedido resposta) {
        try {
            requisicao.getFilaResposta().put(resposta); //add nova requisicao a fila
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println("[ProcessadorPedidos] Erro ao enviar resposta: " + e.getMessage());
        }
    }

    public void encerrar() {
        executando = false;
        Thread.currentThread().interrupt(); // Interrompe o take()
    }
}