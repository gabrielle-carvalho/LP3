import java.util.Random;
import java.util.Scanner;
import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.time.LocalDate;

public class ClienteLeilao {
    private static Random random = new Random();
    private static Cliente cliente;

    public static void main(String[] args) {
     try {
        Scanner scanner = new Scanner(System.in);
        msgBoasVindas();
        System.out.println("insira seu nome ");
        String nome = scanner.nextLine();
        cliente = new Cliente(random.nextInt(100), nome);
        msgCadastro();

        String host = "localhost";
        int porta =1099;
        String nomeServico = "LeilaoService";
        Registry registry = LocateRegistry.getRegistry(host,porta);
        Servico servicoLeilao = (Servico) Naming.lookup(nomeServico);
        int escolha = 0;
        while (escolha != 3) {
            System.out.println("\n--- Menu do Leilão ---");
            System.out.println("1. Ofertar Lance");
            System.out.println("2. Consultar Maior Lance");
            System.out.println("3. Sair");
            System.out.print("Escolha: ");
            
            try {
                escolha = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Por favor, insira um número.");
                continue;
            }
            switch (escolha) {
                case 1:
                    System.out.print("Digite o valor do seu lance: ");
                    try {
                        double valor = Double.parseDouble(scanner.nextLine());
                        String resposta = servicoLeilao.ofertarLances(cliente, valor);
                        System.out.println("Servidor: " + resposta);
                    } catch (NumberFormatException e) {
                        System.out.println("Valor inválido.");
                    }
                    break;
                case 2:
                    Double maiorLance = servicoLeilao.consultarMaiorLance();
                    System.out.println("O maior lance atual é: " + maiorLance);
                    break;
                case 3:
                    sairLeilao();
                    break;
                default:
                    System.out.println("Opção inválida.");
            }
        }
        scanner.close();

     } catch (Exception e) {
        System.out.println("Erro de conexão!!!! " + e.getMessage());
        e.printStackTrace();
     }   
    }
    public static void sairLeilao(){
        System.out.println("saindo do leilao");
    }

    public static void msgBoasVindas(){
        System.out.println("bem vindo ao leilao!!");
    }

    public static void msgCadastro(){
        System.out.println("Cadastrando [" + cliente.getNome() + "] com o id " + cliente.getId());
    }
}
