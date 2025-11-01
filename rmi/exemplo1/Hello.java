import java.rmi.Remote;
import java.rmi.RemoteException;

public class Hello implements Remote {    
    String sayHello() throws RemoteException;
}
