import java.util.Random;
import java.util.concurrent.*;

class Produtor implements Runnable {
    private final BlockingQueue<Pedido> fila;
    private final String fonte; // API, Web, Mobile
    private final int quantidadePedidos;
    private final GerenciadorEstatisticas stats;
    private final Random random = new Random();
    
    private static final String[] CLIENTES = {"João", "Maria", "Pedro", "Ana", "Carlos"};
    private static final String[] PRODUTOS = {"Notebook", "Mouse", "Teclado", "Monitor", "Headset"};
    
    public Produtor(BlockingQueue<Pedido> fila, String fonte, int quantidadePedidos, 
                    GerenciadorEstatisticas stats) {
        this.fila = fila;
        this.fonte = fonte;
        this.quantidadePedidos = quantidadePedidos;
        this.stats = stats;
    }
    
    @Override
    public void run() {
        try {
            for (int i = 0; i < quantidadePedidos; i++) {
                // TODO: Gerar pedido aleatório
                // TODO: Adicionar na fila usando put()
                // TODO: Registrar nas estatísticas
                // TODO: Simular delay entre pedidos (50-200ms)
                
                Thread.sleep(random.nextInt(150) + 50);
            }
            System.out.println("[" + fonte + "] Finalizou geração de pedidos");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
    
    private Pedido gerarPedidoAleatorio() {
        // TODO: Implementar geração aleatória
        return null;
    }
}