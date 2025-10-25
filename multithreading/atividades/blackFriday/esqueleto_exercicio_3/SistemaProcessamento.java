import java.util.concurrent.*;

public class SistemaProcessamento {
    
    public static void main(String[] args) throws InterruptedException {
        System.out.println("╔════════════════════════════════════════╗");
        System.out.println("║   SISTEMA DE PROCESSAMENTO DE PEDIDOS  ║");
        System.out.println("╚════════════════════════════════════════╝\n");
        
        // TODO: Criar BlockingQueue (PriorityBlockingQueue com capacidade 50)
        BlockingQueue<Pedido> fila = null;
        
        // TODO: Criar GerenciadorEstoque
        GerenciadorEstoque estoque = null;
        
        // TODO: Criar GerenciadorEstatisticas
        GerenciadorEstatisticas stats = null;
        
        // TODO: Criar e iniciar Monitor
        
        // TODO: Criar ExecutorService para produtores (3 threads)
        ExecutorService produtores = null;
        
        // TODO: Criar 3 produtores (API, Web, Mobile) - cada um gera 20 pedidos
        
        // TODO: Criar ExecutorService para consumidores (5 threads)
        ExecutorService consumidores = null;
        
        // TODO: Criar 5 consumidores
        
        // TODO: Aguardar produtores finalizarem
        
        // TODO: Aguardar consumidores finalizarem
        
        // TODO: Parar monitor
        
        // TODO: Exibir relatório final
        
        // TODO: Exibir estoque final
        
        System.out.println("\nSistema finalizado com sucesso!");
    }
}