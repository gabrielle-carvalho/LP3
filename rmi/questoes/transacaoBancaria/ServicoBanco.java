import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ServicoBanco extends Remote {

    double getSaldo(int idConta) throws RemoteException;
    void depositar(int idConta, double valor) throws RemoteException;
    boolean sacar(int idConta, double valor) throws RemoteException;
    boolean transferir(int idContaOrigem, int idContaDestino, double valor) throws RemoteException;
}