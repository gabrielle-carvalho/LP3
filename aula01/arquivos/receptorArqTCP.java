import java.io.ObjectInputStream;
import java.net.Socket;


public class receptorArqTCP {
    public static void main(String[] args) {
        public static void main (String[] args){
            try {
                Socket cliente = new Socket("localhost", 12345);
                ObjectInputStream entrada = new ObjectInputStream(cliente.getInputStream());
                
            } catch (Exception e) {
            }
        }
    }
}
