import java.util.concurrent.*;

public class SistemaProcessamento {
    
    public static void main(String[] args) throws InterruptedException {
        System.out.println("╔════════════════════════════════════════╗");
        System.out.println("║   SISTEMA DE PROCESSAMENTO DE PEDIDOS  ║");
        System.out.println("╚════════════════════════════════════════╝\n");
        
        // TODO: Criar BlockingQueue (PriorityBlockingQueue com capacidade 50)
        BlockingQueue<Pedido> fila = new PriorityBlockingQueue<>(50);
        
        // TODO: Criar GerenciadorEstoque
        GerenciadorEstoque estoque = new GerenciadorEstoque();
        
        // TODO: Criar GerenciadorEstatisticas
        GerenciadorEstatisticas stats = new GerenciadorEstatisticas();
        
        // TODO: Criar e iniciar Monitor
        Monitor monitor = new Monitor(fila, stats);
        Thread threadMonitor = new Thread(monitor, "monitor");
        threadMonitor.start();
        
        // TODO: Criar ExecutorService para produtores (3 threads)
        ExecutorService produtores = Executors.newFixedThreadPool(3);
        
        // TODO: Criar 3 produtores (API, Web, Mobile) - cada um gera 20 pedidos
        produtores.submit(new Produtor(fila, "API", 20, stats));
        produtores.submit(new Produtor(fila, "Web", 20, stats));
        produtores.submit(new Produtor(fila, "Mobile", 20, stats));

        // TODO: Criar ExecutorService para consumidores (5 threads)
        ExecutorService consumidores = Executors.newFixedThreadPool(5);
        
        // TODO: Criar 5 consumidores
        for(int i=0; i<5;i++){
            consumidores.submit(new Consumidor(i, fila, estoque, stats));
        }
        
        // TODO: Aguardar produtores finalizarem
        produtores.shutdown();
        produtores.awaitTermination(1, TimeUnit.MINUTES);
        System.out.println("todos produtores finalizaram");
        
        // TODO: Aguardar consumidores finalizarem
        consumidores.shutdown();
        consumidores.awaitTermination(2, TimeUnit.MINUTES);
        System.out.println("todos consumidores finalizaram");
        
        // TODO: Parar monitor
        monitor.parar();
        threadMonitor.interrupt();
        threadMonitor.join();
        System.out.println("todos monitores finalizaram");
        
        // TODO: Exibir relatório final
        stats.exibirRelatorioFinal();
        
        // TODO: Exibir estoque final
        estoque.exibirEstoque();
        
        System.out.println("\nSistema finalizado com sucesso!");
    }
}