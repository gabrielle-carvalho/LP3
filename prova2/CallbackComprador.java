import java.rmi.Remote;
import java.rmi.RemoteException;

public interface CallbackComprador extends Remote {
    
    /**
     * Notifica comprador que foi superado por outro lance.
     * @param leilaoId ID do leilão
     * @param novoLance Valor do novo lance que superou o seu
     * @param novoLider ID do comprador que fez o novo lance
     */
    void aoSerSuperado(String leilaoId, double novoLance, String novoLider) 
        throws RemoteException;
    
    /**
     * Notifica comprador que venceu o leilão.
     * @param leilaoId ID do leilão
     * @param valorFinal Valor final do lance vencedor
     */
    void aoVencerLeilao(String leilaoId, double valorFinal) throws RemoteException;
}