import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class ServidorLeilao extends UnicastRemoteObject implements Servico {
    private double maiorLance = 0.0;
    private Cliente clienteMaiorLance = null;

    public ServidorLeilao() throws RemoteException{
        super(); //construtor unicast
    }

    @Override
    public String receberNotificacaoMaioresLances(int idCadastro) throws RemoteException{
        if (clienteMaiorLance!=null) {
            return "maior lance atual é: " + maiorLance + "feito por: " + clienteMaiorLance.getNome();
        }
        return "nenhum lance ainda";
    }

    @Override
    public double consultarMaiorLance() throws RemoteException {
        System.out.println("maior lance consultaod: " + maiorLance);
        return maiorLance;
    }

    @Override
    public String ofertarLances(Cliente cliente, double valor) throws RemoteException {
        if(valor>maiorLance){
            this.maiorLance=valor;
            this.clienteMaiorLance = cliente;
            System.out.println("nova lance recorde = " +valor+ "feito por: " + cliente.getNome());
            return "seu lance de " + valor + "é o maior!!!";
        }
        else{
        System.out.println("o baixo lance de "+valor+" recebido por " + cliente.getNome());
        return "seu lance de = "+ valor + "é menor que o maior lance atual: " + this.maiorLance;
        }
    }

    public static void main(String[] args) {
        try {
            int porta =1099;
            String nomeServico = "LeilaoService";
            Registry registry = LocateRegistry.createRegistry(porta);
            ServidorLeilao servidor = new ServidorLeilao();
            registry.rebind(nomeServico, servidor);
            System.out.println("servidor de leilao está no ar");
        } catch (Exception e) {
            System.out.println("erro no servidor: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
