import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class ClienteLoja {
    public static void main(String[] args) {
        try {
            Registry registry = LocateRegistry.getRegistry("localhost", 1099); // Conecte-se ao registro RMI que está rodando no computador local, na porta 1099
            ServicoLoja servico = (ServicoLoja) registry.lookup("LojaService"); // Procure o serviço chamado LojaService no registro RMI e use-o como um objeto remoto do tipo ServicoLoja

            System.out.println("Tentando comprar 10 unidades do ProdutoA (Cliente BASIC)...");
            RespostaPedido resposta = servico.fazerPedido("Clara", "ProdutoA", 10, Categoria.BASIC);

            if (resposta.getSucesso()) {
                System.out.println("Compra OK! Pedido: " + resposta.getCodigoPedido());
            } else {
                System.out.println("Compra Falhou: " + resposta.getMensagemErro());
            }
            
            System.out.println("\nTentando comprar 5 unidades do ProdutoB (Cliente PLATINUM)...");
            RespostaPedido resposta2 = servico.fazerPedido("Mateus", "ProdutoB", 5, Categoria.PLATINUM);

            if (resposta2.getSucesso()) {
                System.out.println("Compra OK! Pedido: " + resposta2.getCodigoPedido());
            } else {
                System.out.println("Compra Falhou: " + resposta2.getMensagemErro());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}