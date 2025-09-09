import java.io.IOException;
import java.net.*;
import javax.imageio.IIOException;
import calculadores.Calculadora;

// java servidoreUDP localhost 1234

public class servidorUDP {
    public static void main(String[] args) throws UnknownHostException, SocketException, IOException{
        if (args.length!=2) {
            System.err.println("Uso correto: <nome da maquina> <Porta>");
            System.exit(0);
        }
        try {
            InetAddress addr = InetAddress.getByName(args[0]);
            int port = Integer.parseInt(args[1]);
            DatagramSocket ds = new DatagramSocket(port);
            byte[] msg = new byte[256];
            DatagramPacket pkg = new DatagramPacket(msg, msg.length);
            ds.receive(pkg);
            ds.close();

            String[] partes = (new String(pkg.getData())).trim().split(" ");
            int num1 = Integer.parseInt(partes[0]);
            int num2 = Integer.parseInt(partes[1]);
            byte[] op = partes[2].getBytes();

            Calculadora calc = new Calculadora(num1, num2, (char)op[0]);
            String resultado = "Resultado: " + calc.calcular();

            byte[] resultadoBytes = resultado.getBytes();
            DatagramPacket pacote = new DatagramPacket(resultado, resultado.length, resultadoBytes.length, addr, port);
            DatagramSocket dsocket = new DatagramSocket();
            dsocket.send(pacote);
            System.err.println("Mensagem: " + "Mensagem enviada para: " + addr.getHostAddress() + "\n" + "Porta: " + port + "\n" + resultado);
            dsocket.close(); 
        } 
        
        catch (IIOException ioe) {
        }
    }
}
