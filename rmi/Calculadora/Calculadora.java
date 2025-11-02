
import java.rmi.*;
//a interface remota deve estender java.rmi.Remote

public interface Calculadora extends Remote{ //interface remota que vai ser usada pelo cliente e servidor para eles interagirem
    int soma(int x, int y) throws RemoteException;
    int subtracao(int x, int y) throws RemoteException;
    int multiplicacao(int x, int y) throws RemoteException;
    double divisao(int x, int y) throws RemoteException; // todo metodo deve ter RemoteException
}
