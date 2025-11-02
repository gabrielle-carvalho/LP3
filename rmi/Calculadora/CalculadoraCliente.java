
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
//a partir do naming os clientes acham os serviços remotos

// rmi://[hostname]:[porta]/[nome do servico]

public class CalculadoraCliente {
    public static void main(String[] args) {
        try {
            Registry registry = LocateRegistry.getRegistry("localhost"); // Ela cria um objeto local do tipo Registry que sabe onde (host e porta padrão 1099) o registro está. É uma referência local ao registro remoto. acessa registry ja existente e disponivel
            // quando temos registry.lookup("Calculadora") o cliente envia uma requisição de rede para o RMI Registry que está rodando no servidor (em localhost:1099);
            // O Registry no servidor procura uma entrada com o nome "Calculadora" (que foi registrada antes com rebind()).
            // Ele retorna uma referência ao stub remoto correspondente a esse nome.
            // ➡️ O objeto retornado não é a CalculadoraServidor real, mas sim o stub (proxy gerado automaticamente) que:
            // Implementa a interface Calculadora;
            // Contém as informações de conexão necessárias para chamar o objeto real no servidor.
            Calculadora stub = (Calculadora) registry.lookup("Calculadora"); // acessa o rmiregistry atraves da classe estatica naming, ela provem do metodo lookup(), que o cliente usa para requisitar o registro, esse metodo aceita a url que especifica o nome do servidor e do servico desejado
            // Quando o cliente chama lookup, o RMI Registry retorna uma cópia serializada do stub para o cliente.
            // O cliente, então, tem uma referência remota (o stub) que aponta para o objeto real no servidor.
            // como se estivesse chamando métodos locais, mas todas essas chamadas são enviadas pela rede ao servidor.
            int a = 30;
            int b = 5;
            System.out.println("Soma: " + stub.soma(a, b));
            System.out.println("Subtracao: " + stub.subtracao(a, b));
            System.out.println("Multiplicacao: " + stub.multiplicacao(a, b));
            System.out.println("Divisao: " + stub.divisao(a, b));
            // Quando o cliente chama stub.funcao(3, 5), o stub:
            // Serializa os parâmetros (3 e 5);
            // Envia pela rede para o servidor;
            // O servidor executa funcao.soma(3, 5) e devolve o resultado;
            // O stub no cliente retorna o valor.
        } catch (Exception e) {
        }
    }
}
