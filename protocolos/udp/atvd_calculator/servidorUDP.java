import calculadores.Calculadora;
import java.io.IOException;
import java.net.*;
import javax.imageio.IIOException;

//  javac servidorUDP.java 
// java servidorUDP localhost 1234

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
            while (true) {

                byte[] msg = new byte[256];
                DatagramPacket pkg = new DatagramPacket(msg, msg.length);
                ds.receive(pkg);

                String recebido = new String(pkg.getData()).trim();
                String[] partes = recebido.split(" ");

                if (partes[0].equalsIgnoreCase("exit")) {
                    System.out.println("Encerrando servidor...");
                    break;
                }

                if (partes.length < 3) {
                    System.out.println("Mensagem inválida recebida: " + recebido);
                    continue;
                }

                int num1 = Integer.parseInt(partes[0]);
                int num2 = Integer.parseInt(partes[1]);
                char op = partes[2].charAt(0);

                Calculadora calc = new Calculadora(num1, num2, op);
                String resultado = "Resultado: " + calc.calcular();

                byte[] resultadoBytes = resultado.getBytes();
                DatagramPacket pacote = new DatagramPacket(resultadoBytes, resultadoBytes.length, pkg.getAddress(), pkg.getPort());
                ds.send(pacote);
                System.err.println("Mensagem enviada para: " + pkg.getAddress().getHostAddress() + "Porta: " + pkg.getPort() + "\n" + resultado);
                
            }
            ds.close();
        } 
        catch (IIOException ioe) {
        }
    }
}