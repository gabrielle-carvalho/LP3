import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface ServicoLeilao extends Remote {
    
    // === VENDEDORES ===
    
    /**
     * Cadastra novo item para leilão.
     * @return ID do leilão criado
     */
    String cadastrarItem(ItemLeilao item) throws RemoteException;
    
    /**
     * Cancela leilão (apenas se ainda ATIVO e sem lances).
     */
    boolean cancelarLeilao(String leilaoId, String vendedorId) throws RemoteException;
    
    /**
     * Lista todos os leilões de um vendedor.
     */
    List<InfoLeilao> listarLeiloesVendedor(String vendedorId) throws RemoteException;
    
    // === COMPRADORES ===
    
    /**
     * Registra comprador no sistema para receber callbacks.
     */
    boolean registrarComprador(String compradorId, CallbackComprador callback) 
        throws RemoteException;
    
    /**
     * Faz lance em leilão ativo.
     * REGRAS:
     * - Lance deve ser >= lanceAtual * 1.05 (incremento mínimo de 5%)
     * - Leilão deve estar ATIVO
     * - Comprador não pode dar lance em seu próprio lance líder
     */
    ResultadoLance fazerLance(String leilaoId, String compradorId, double valor) 
        throws RemoteException;
    
    /**
     * Lista todos os leilões ativos.
     */
    List<InfoLeilao> listarLeiloesAtivos() throws RemoteException;
    
    /**
     * Consulta informações detalhadas de um leilão.
     */
    InfoLeilao consultarLeilao(String leilaoId) throws RemoteException;
    
    /**
     * Lista histórico de lances de um leilão.
     */
    List<Lance> listarLancesLeilao(String leilaoId) throws RemoteException;
}