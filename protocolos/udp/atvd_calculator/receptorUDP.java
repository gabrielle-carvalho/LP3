import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import javax.swing.JOptionPane;
//  javac receptorUDP.java
// java receptorUDP localhost 1234 10 10 +

public class receptorUDP {
    public static void main(String[] args) {
        if(args.length !=5){
            System.err.println("<nome da maquina> <Porta> <numero 1> <numero 2> <operacao>");
            System.exit(0);
        }

        try {
            InetAddress addr = InetAddress.getByName(args[0]);
            int port = Integer.parseInt(args[1]);
            int num1 = Integer.parseInt(args[2]);
            int num2 = Integer.parseInt(args[3]);
            char op = args[4].charAt(0);

            String mensagem = num1 + " " + num2 + " "+ op; // mensagem a ser passada para o servidor
            byte [] sendData = mensagem.getBytes();
            
            DatagramPacket pkg = new DatagramPacket(sendData, sendData.length, addr, port);
            DatagramSocket ds = new DatagramSocket();
            ds.send(pkg);
            System.out.println("Mensagem: " + "Mensagem enviada para: " + addr.getHostAddress() + "\n" + "Porta: " + port + "\n" + mensagem);

            byte[] buffer = new byte[256];
            DatagramPacket resposta = new DatagramPacket(buffer, buffer.length); //espera a resposta do pacote
            ds.receive(resposta);

            String resultado = new String(resposta.getData()).trim(); //tira espacos
            JOptionPane.showMessageDialog(null, resultado, "resultado da operacao", JOptionPane.INFORMATION_MESSAGE);
            ds.close();
        } 
        
        catch (IOException ioe) {
        }
    }
}
