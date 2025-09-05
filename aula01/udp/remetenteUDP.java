import java.io.IOException;
import java.net.*;
import javax.imageio.IIOException;
// java remetenteUDP localhost 1234 mensagem!

public class remetenteUDP {
    public static void main(String[] args) throws UnknownHostException, SocketException, IOException{
        if (args.length!=3) {
            System.err.println("Uso correto: <nome da maquina> <Porta> <mensagem>");
            System.exit(0);
        }
        try {
            InetAddress addr = InetAddress.getByName(args[0]);
            int port = Integer.parseInt(args[1]);
            byte[] msg = args[2].getBytes();
            DatagramPacket pkg = new DatagramPacket(msg, msg.length, addr, port);
            DatagramSocket ds = new DatagramSocket();
            ds.send(pkg);
            System.err.println("Mensagem: " + "Mensagem enviada para: " + addr.getHostAddress() + "\n" + "Porta: " + port + "\n" + args[2]);
            ds.close();
        } 
        
        catch (IIOException ioe) {
        }
    }
}
