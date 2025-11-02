import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;
import java.util.Map;

public interface ServicoVotacao extends Remote { //diz ao Java que esta interface será usada para RMI
    // Autenticação
    String autenticar(String cpf, String senha, boolean admin) throws RemoteException;
    boolean validarToken(String token) throws RemoteException;
    
    // Votação
    boolean registrarVoto(String token, int idCandidato) throws RemoteException;
    boolean jaVotou(String token) throws RemoteException;
    
    // Consulta (apenas para administradores)
    Map<Integer, Integer> obterResultadoParcial(String tokenAdmin) throws RemoteException;
    Map<Integer, Integer> obterResultadoFinal(String tokenAdmin) throws RemoteException;
    List<Candidato> listarCandidatos() throws RemoteException; // retornará uma lista de objetos Candidato. É por isso que Candidato precisava ser Serializable. O RMI irá "achatar" cada Candidato na lista, enviar pela rede, e o cliente irá "remontá-los"
    
    // Administração
    boolean iniciarEleicao(String tokenAdmin) throws RemoteException;
    boolean encerrarEleicao(String tokenAdmin) throws RemoteException;
    String obterStatusEleicao() throws RemoteException;
}