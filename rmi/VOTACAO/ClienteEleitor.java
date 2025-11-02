import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.List;
import java.util.Scanner;

public class ClienteEleitor {
    private ServicoVotacao servico; // cliente nao sabe do servidor, ele so conhece a interface servico votacao
    private String token;
    private Scanner scanner;
    
    public ClienteEleitor() {
        scanner = new Scanner(System.in);
    }
    
    public void conectar(String host) {
        try {
            Registry registry = LocateRegistry.getRegistry(host, 1099); // obtem aquilo que foi criado pelo servidor 
            servico = (ServicoVotacao) registry.lookup("ServicoVotacao"); // O cliente pede: "Quem é 'ServicoVotacao'?". O Registry responde, enviando o Stub (o proxy) pela rede. A variável servico agora contém esse Stub.
            System.out.println("Conectado ao servidor de votação!");
        } catch (Exception e) {
            System.err.println("Erro ao conectar: " + e.getMessage());
            System.exit(1);
        }
    }
    
    public void executar() {
        System.out.println("\n========================================");
        System.out.println("  SISTEMA DE VOTAÇÃO ELETRÔNICA");
        System.out.println("========================================\n");
        
        // Autenticação
        if (!autenticar()) {
            System.out.println("Falha na autenticação. Encerrando...");
            return;
        }
        
        // Menu principal
        boolean continuar = true;
        while (continuar) {
            try {
                System.out.println("\n--- MENU ---");
                System.out.println("1. Ver candidatos");
                System.out.println("2. Votar");
                System.out.println("3. Verificar se já votou");
                System.out.println("4. Ver status da eleição");
                System.out.println("0. Sair");
                System.out.print("Escolha uma opção: ");
                
                int opcao = scanner.nextInt();
                scanner.nextLine(); // Limpar buffer
                
                switch (opcao) {
                    case 1:
                        listarCandidatos();
                        break;
                    case 2:
                        votar();
                        break;
                    case 3:
                        verificarVoto();
                        break;
                    case 4:
                        verificarStatus();
                        break;
                    case 0:
                        continuar = false;
                        System.out.println("Encerrando...");
                        break;
                    default:
                        System.out.println("Opção inválida!");
                }
            } catch (Exception e) {
                System.err.println("Erro: " + e.getMessage());
                scanner.nextLine(); // Limpar buffer em caso de erro
            }
        }
    }
    
    private boolean autenticar() {
        try {
            System.out.println("=== AUTENTICAÇÃO ===");
            System.out.print("CPF: ");
            String cpf = scanner.nextLine();
            System.out.print("Senha: ");
            String senha = scanner.nextLine();
            
            token = servico.autenticar(cpf, senha, false); // servico é um stub!
            // O Stub "empacota" (serializa) os argumentos cpf, senha, false.
            // O Stub abre uma conexão de rede com o servidor (ex: localhost:1099).
            // O Stub envia os dados para o Skeleton (o "assistente" do lado do servidor).
            // O Skeleton "desempacota" os dados e chama o método autenticar real no objeto ServidorVotacao (que está na memória do servidor).
            // O método real retorna o token.
            // O Skeleton "empacota" o token e o envia de volta ao Stub.
            // O Stub "desempacota" o token e o retorna.
            // O cliente armazena na variável token. ... tudo isso parecendo uma simples chamada de método. Isso é RMI.
            
            if (token != null) {
                System.out.println("\nAutenticação bem-sucedida!");
                return true;
            } else {
                System.out.println("\nCPF ou senha incorretos!");
                return false;
            }
        } catch (Exception e) {
            System.err.println("Erro na autenticação: " + e.getMessage());
            return false;
        }
    }
    
    private void listarCandidatos() {
        try {
            System.out.println("\n=== CANDIDATOS ===");
            List<Candidato> candidatos = servico.listarCandidatos();
            
            for (Candidato c : candidatos) {
                System.out.println(c);
            }
        } catch (Exception e) {
            System.err.println("Erro ao listar candidatos: " + e.getMessage());
        }
    }
    
    // chamadas remotas. O cliente passa seu token para se identificar em todas elas.

    private void votar() {
        try {
            // Verificar se já votou
            if (servico.jaVotou(token)) {
                System.out.println("\nVocê já votou nesta eleição!");
                return;
            }
            
            // Verificar status
            String status = servico.obterStatusEleicao();
            if (!status.equals("EM_ANDAMENTO")) {
                System.out.println("\nA eleição não está em andamento!");
                return;
            }
            
            // Mostrar candidatos
            listarCandidatos();
            
            System.out.print("\nDigite o ID do candidato: ");
            int idCandidato = scanner.nextInt();
            scanner.nextLine(); // Limpar buffer
            
            System.out.print("Confirma seu voto? (S/N): ");
            String confirmacao = scanner.nextLine().toUpperCase();
            
            if (confirmacao.equals("S")) {
                boolean sucesso = servico.registrarVoto(token, idCandidato);
                
                if (sucesso) {
                    System.out.println("\nVoto registrado com sucesso!");
                    System.out.println("Obrigado por participar!");
                } else {
                    System.out.println("\nNão foi possível registrar o voto.");
                }
            } else {
                System.out.println("Voto cancelado.");
            }
        } catch (Exception e) {
            System.err.println("Erro ao votar: " + e.getMessage());
            scanner.nextLine();
        }
    }
    
    private void verificarVoto() {
        try {
            boolean votou = servico.jaVotou(token);
            if (votou) {
                System.out.println("\nVocê já votou nesta eleição.");
            } else {
                System.out.println("\nVocê ainda não votou.");
            }
        } catch (Exception e) {
            System.err.println("Erro: " + e.getMessage());
        }
    }
    
    private void verificarStatus() {
        try {
            String status = servico.obterStatusEleicao();
            System.out.println("\nStatus da eleição: " + status);
        } catch (Exception e) {
            System.err.println("Erro: " + e.getMessage());
        }
    }
    
    public static void main(String[] args) {
        ClienteEleitor cliente = new ClienteEleitor();
        
        String host = "localhost";
        if (args.length > 0) {
            host = args[0];
        }
        
        cliente.conectar(host); // O cliente se conecta ao servidor rodando na "mesma máquina" (localhost).
        cliente.executar();
    }
}