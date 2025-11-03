import java.io.Serializable;
import java.util.concurrent.SynchronousQueue;

public class RequisicaoPedido implements Comparable<RequisicaoPedido>, Serializable {
    private final String nomeCliente;
    private final String idProduto;
    private final int quantidade;
    private final Categoria categoria;
        private final SynchronousQueue<RespostaPedido> filaResposta;
    private final long marcaTempo;
    
    public RequisicaoPedido(String nomeCliente, String idProduto, int quantidade, Categoria categoria) {
        this.nomeCliente = nomeCliente;
        this.idProduto = idProduto;
        this.quantidade = quantidade;
        this.categoria = categoria;
        this.filaResposta = new SynchronousQueue<RespostaPedido>();
        this.marcaTempo = System.nanoTime();
    }
    
    @Override
    public int compareTo(RequisicaoPedido outra) {
        int comparacaoCategoria = Integer.compare(
            this.categoria.getPrioridade(), 
            outra.categoria.getPrioridade()
        );
        
        if (comparacaoCategoria != 0) {
            return comparacaoCategoria;
        }
        
        return Long.compare(this.marcaTempo, outra.marcaTempo);
    }
    
    public String getNomeCliente() { 
        return nomeCliente; 
    }

    public String getIdProduto() {
        return idProduto;
    }

    public int getQuantidade() {
        return quantidade;
    }
    
    public Categoria getCategoria() { 
        return categoria; 
    }
    
    public SynchronousQueue<RespostaPedido> getFilaResposta() { 
        return filaResposta; 
    }
}