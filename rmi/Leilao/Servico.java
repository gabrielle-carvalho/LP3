import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Servico extends Remote{
    public String receberNotificacaoMaioresLances(int idCadastro) throws RemoteException;
    public double consultarMaiorLance() throws RemoteException;
    public String ofertarLances(Cliente cliente, double valor) throws RemoteException;
}
