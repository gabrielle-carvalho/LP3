import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import javax.swing.JOptionPane;
// java receptorUDP 1234

public class receptorUDP {
    public static void main(String[] args) {
        if(args.length !=4){
            System.err.println("<nome da maquina> <Porta> <numero 1> <numero 2> <operacao>");
            System.exit(0);
        }
        try {
            InetAddress addr = InetAddress.getByName(args[0]);
            int port = Integer.parseInt(args[1]);
            int num1 = Integer.parseInt(args[2]);
            int num2 = Integer.parseInt(args[3]);
            char op = args[4].charAt(0);
            // byte[] op = args[4].getBytes();
            

            DatagramPacket pkg = new DatagramPacket(num1, num1.length, num2, num2.length, op, op.length, addr, port);
            DatagramSocket ds = new DatagramSocket();
            ds.send(pkg);
            System.err.println("Mensagem: " + "Mensagem enviada para: " + addr.getHostAddress() + "\n" + "Porta: " + port + "\n" + num1 + " " + num2 + " " + op);
            ds.close();

            /*System.out.print("\nDigite uma mensagem:  ");
            sendMsg = inputBufferedReader.readLine();
            sendData = sendMsg.getBytes();

            DatagramPacket sendPacket = new DatagramPacket(sendData, sendMsg.length(), addr, port);
            clientSocket.send(sendPacket); */
            
            int port = Integer.parseInt(args[0]);
            DatagramSocket ds = new DatagramSocket(port);
            byte[] msg = new byte[256];
            DatagramPacket pkg = new DatagramPacket(msg, msg.length);
            ds.receive(pkg);
            JOptionPane.showMessageDialog(null, ((String) new String(pkg.getData())).trim(), "Resultado da operação ", 1);
            ds.close();
        } 
        catch (IOException ioe) {
        }
    }
}
