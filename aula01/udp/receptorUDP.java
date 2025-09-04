import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import javax.swing.JOptionPane;
// java .\remetenteUDP.java localhost 1234 olaio

public class receptorUDP {
    public static void main(String[] args) {
        if(args.length !=1){
            System.err.println("Informe a porta a ser ouvida: ");
            System.exit(0);
        }
        try {
            int port = Integer.parseInt(args[0]);
            DatagramSocket ds = new DatagramSocket(port);
            byte[] msg = new byte[256];
            DatagramPacket pkg = new DatagramPacket(msg, msg.length);
            ds.receive(pkg);
            JOptionPane.showMessageDialog(null, ((String) new String(pkg.getData())).trim(), "Mensagem recebida ", 1);
            ds.close();
        } 
        catch (IOException ioe) {
        }
    }
}
