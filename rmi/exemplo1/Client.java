import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Client {
    public static void main(String args[]){
        try {
            Registry registry = LocateRegistry.getRegistry("localhost");
            // Quando usar LocateRegistry.getRegistry()
            // O Cliente (Sempre): O cliente nunca cria a lista telefônica. Ele sempre parte do princípio que ela já existe em algum lugar.
            // O Servidor (em cenários avançados/profissionais):
            // Cenário de Uso 1: O Cliente (Obrigatório) O seu Client.java já usa isso corretamente. O cliente precisa primeiro obter a conexão com a "lista telefônica" (getRegistry()) para depois poder pedir um contato (lookup("Hello")).
            // Cenário de Uso 2: O Servidor (em um sistema maior) Imagine que sua empresa não tem só o serviço "Hello". Ela tem o "CalculatorServer", o "VotingServer" , etc.
            // Você não quer que cada um desses servidores tente criar a "lista telefônica" na mesma porta 1099. O primeiro conseguiria, e todos os outros falhariam com um erro de "Porta já em uso".
            // Nesse cenário profissional, o rmiregistry roda como um serviço separado e dedicado.
            //     Um administrador de sistemas inicia a "lista telefônica" uma única vez, usando o comando no terminal:
            // rmiregistry 1099 &
            // Agora, todos os seus programas servidores (Server.java, Calculator.java, etc.) vão usar getRegistry() para encontrar essa lista que já está rodando, e então usar rebind() para se registrarem nela.

            Hello stub = (Hello) registry.lookup("Hello");
            String response = stub.sayHello();
            System.out.println("response: " + response);
        } catch (Exception e) {
            System.out.println("Cliente exception: " + e.toString());
            e.printStackTrace();
        }
    }    
}
