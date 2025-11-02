import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class Server extends UnicastRemoteObject implements Hello {
    public Server() throws Exception{
    }    
    public String sayHello() throws RemoteException {
        return "Hello world";
    }
    public static void main(String args[]){
        try {
            LocateRegistry.createRegistry(1099);
            // Quem usa createRegistry: O Servidor (em cenários simples).
            // Crie a "lista telefônica".
            // E depois se registre (rebind) nela.
            Server obj = new Server();
            Registry registry = LocateRegistry.getRegistry();
            registry.rebind("Hello", obj); // publicando objeto
            System.out.println("Server ready");
        } catch (Exception e) {
            System.out.println("Server exception: " + e.toString());
            e.printStackTrace();
        }
    }
}
