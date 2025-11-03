import java.rmi.Remote;
import java.rmi.RemoteException;
public interface ServicoLoja extends Remote {
    public RespostaPedido fazerPedido(String nomeCliente, String idProduto, int quantidade, Categoria categoria) throws RemoteException;
}
