import java.rmi.*;
import java.rmi.registry.*;
import java.rmi.server.UnicastRemoteObject;
public class CalculadoraServidor implements Calculadora{ // cria um serviço remoto criando o obj que implementa o serviço, no caso calculadora
    @Override // implementacao da interface
    public int soma(int x, int y) throws RemoteException{
        return x + y;
    }
    @Override
    public int subtracao(int x, int y) throws RemoteException{
        return x - y;
    }
   /** */
    @Override
    public int multiplicacao(int x, int y) throws RemoteException{
        return x * y;
    }
    /** */
    @Override
    public double divisao(int x, int y) throws RemoteException{
        return (double) x / y;
    }

    public static void main(String[] args) {
        try {
            CalculadoraServidor server = new CalculadoraServidor(); // cria serviço remoto, criando um novo objeto do server, pois ele implementa o serviço que queremos
            Calculadora stub = (Calculadora) UnicastRemoteObject.exportObject(server, 0); // exporta o objeto para o rmi, cria serviço que aguarda conexaoes dos clientes
            // o servidor registra o objeto no rmi registry com nome publico
            Registry registry = LocateRegistry.createRegistry(1099); // rmi registry local roda em cada maquina que hospeda o servico remoto geralmente na porta 1099
            registry.rebind("Calculadora", stub); // stub é registrado no RMI Registry, esse trcho associa o nome "Calculadora" ao stub dentro do registro RMI. esse nome pode ser acessado pelo cliente ao inves de passar endereco
            System.out.println("Servidor Pronto...");

        } catch (Exception e) {
            System.err.println("Server exception: " + e.toString());
            e.printStackTrace();
        }
    }
}
