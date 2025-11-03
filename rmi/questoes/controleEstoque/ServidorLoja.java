import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class ServidorLoja extends UnicastRemoteObject implements ServicoLoja { // UnicastRemoteObject transforma sua classe em um servidor que pode escutar na rede
    private final PriorityBlockingQueue<RequisicaoPedido> filaPedidos; 
    private final ProcessadorPedidos processador;

    public ServidorLoja() throws RemoteException {
        super();
        this.filaPedidos = new PriorityBlockingQueue<>();

        ConcurrentHashMap<String, AtomicInteger> estoque = new ConcurrentHashMap<>();
        estoque.put("ProdutoA", new AtomicInteger(100));
        estoque.put("ProdutoB", new AtomicInteger(50));

        Semaphore apiPagamento = new Semaphore(10); //permite 10 pagamnetos simultaneos
        this.processador = new ProcessadorPedidos(filaPedidos, estoque, apiPagamento);
        new Thread(processador).start();
    }

    @Override
    public RespostaPedido fazerPedido(String nomeCliente, String idProduto, int quantidade, Categoria categoria) 
            throws RemoteException {
        
        RequisicaoPedido requisicao = new RequisicaoPedido(nomeCliente, idProduto, quantidade, categoria);
        
        try {
            filaPedidos.put(requisicao);
            RespostaPedido resposta = requisicao.getFilaResposta().poll(30, TimeUnit.SECONDS);
            
            if (resposta == null) {
                return RespostaPedido.falha("TIMEOUT", "Servidor sobrecarregado");
            }
            return resposta;
        } catch (InterruptedException e) { 
            Thread.currentThread().interrupt();
            throw new RemoteException("Servidor interrompido", e);
        }
    }

    public static void main(String[] args) {
        try {
            Registry registry = LocateRegistry.createRegistry(1099);
            registry.rebind("LojaService", new ServidorLoja());
            System.out.println("Servidor da Loja (RMI) no ar!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
