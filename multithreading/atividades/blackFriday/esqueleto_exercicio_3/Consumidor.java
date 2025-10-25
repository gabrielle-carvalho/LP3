import java.util.Random;
import java.util.concurrent.*;

class Consumidor implements Runnable {
    private final BlockingQueue<Pedido> fila;
    private final GerenciadorEstoque estoque;
    private final GerenciadorEstatisticas stats;
    private final int id;
    private final Random random = new Random();
    
    public Consumidor(int id, BlockingQueue<Pedido> fila, GerenciadorEstoque estoque,
                     GerenciadorEstatisticas stats) {
        this.id = id;
        this.fila = fila;
        this.estoque = estoque;
        this.stats = stats;
    }
    
    @Override
    public void run() {
        try {
            while (true) {
                
                // TODO: Remover pedido da fila com timeout (poll com 5 segundos)
                // Se null, significa que não há mais pedidos, pode encerrar
                
                // TODO: Processar pedido
                // TODO: Se null, break do loop
            }
            System.out.println("[Consumidor-" + id + "] Finalizou processamento");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println("[Consumidor-" + id + "] Finalizou processamento");
        }
    }
    
/*************  ✨ Windsurf Command ⭐  *************/
    /**
     * Processa um pedido.
     * <p>
     * Verifica se h  estoque suficiente para processar o pedido,
     * reserva o estoque, simula o processamento do pedido (100-300ms)
     * e atualiza as estat sticas.
     * <p>
     * @param pedido o pedido a ser processado
     * @throws InterruptedException se o thread for interrompido
     */
/*******  ffc31858-a1a2-4522-a873-8bd2c3b96c66  *******/
    private void processarPedido(Pedido pedido) throws InterruptedException {
        // TODO: Implementar processamento
        // 1. Verificar estoque
        // 2. Reservar estoque
        // 3. Simular processamento (100-300ms)
        // 4. Atualizar estatísticas
    }
}