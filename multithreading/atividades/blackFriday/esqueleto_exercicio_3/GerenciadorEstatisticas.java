import java.util.concurrent.atomic.*;

class GerenciadorEstatisticas {
    private final AtomicInteger pedidosGerados = new AtomicInteger(0);
    private final AtomicInteger pedidosProcessados = new AtomicInteger(0);
    private final AtomicInteger pedidosRejeitados = new AtomicInteger(0);
    private final long tempoInicio;
    
    public GerenciadorEstatisticas() {
        this.tempoInicio = System.currentTimeMillis();
    }
    
    public void registrarPedidoGerado() {
        pedidosGerados.incrementAndGet();
    }
    
    public void registrarPedidoProcessado() {
        pedidosProcessados.incrementAndGet();
    }
    
    public void registrarPedidoRejeitado() {
        pedidosRejeitados.incrementAndGet();
    }
    
    public void exibirEstatisticas(int tamanhoFila) {
        // TODO: Implementar exibição formatada
        long tempoDecorrido = (System.currentTimeMillis() - tempoInicio) / 1000;
        int processados = pedidosProcessados.get();
        double taxa = 0.0;
        if (tempoDecorrido> 0){
            taxa = (double) processados / tempoDecorrido;
        }
        System.out.println("\n=== ESTATÍSTICAS ===");
        System.out.println("Fila: " + tamanhoFila + " pedidos");
        System.out.println("Gerados: " + pedidosGerados.get());
        System.out.println("Processados: " + pedidosProcessados.get());
        System.out.println("Rejeitados: " + pedidosRejeitados.get());
        System.out.println("Taxa de processamento: " + taxa);
        System.out.println("===================\n");
    }
    
    public void exibirRelatorioFinal() {
        // TODO: Implementar relatório final completo
        long tempoTotal = System.currentTimeMillis() - tempoInicio;
        int gerados = pedidosGerados.get();
        int processados = pedidosProcessados.get();
        int rejeitados = pedidosRejeitados.get();
        int total = processados + rejeitados;

        double taxaMedia = 0.0;
        double taxaSucesso = 0.0;

        if(tempoTotal>0){
            taxaMedia = (double) processados / tempoTotal*100;
        }

        if(gerados > 0){
            taxaSucesso = (double) processados / gerados * 100;
        }

        System.out.println("\n╔════════════════════════════════════════╗");
        System.out.println("║          RELATÓRIO FINAL               ║");
        System.out.println("╚════════════════════════════════════════╝");
        // TODO: Completar
        System.out.println("tempo total execução = " + tempoTotal);
        System.out.println("----------------------------------------");
        System.out.println("total gerado = " + gerados);
        System.out.println("rejeitados (sem estoque) = " + rejeitados);
        System.out.println("total tratado = " + total);
        System.out.println("----------------------------------------");
        System.out.println("taxa de sucesso = " + taxaSucesso);
        System.out.println("taxa media = " + taxaMedia);
        if (gerados==total){
            System.out.println("todos os pedidos gerados foram tratados!!!!!!");
        }
        else {
            System.out.printf("ERRO DE VALIDAÇÃO: %d Gerados != %d Tratados (%d Processados + %d Rejeitados)\n",
                gerados, total, processados, rejeitados);
        }
        System.out.println("══════════════════════════════════════════");
    }
}