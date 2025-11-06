import java.rmi.*;
import java.rmi.registry.*;
import java.util.List;
import java.util.Scanner;
import java.util.UUID;

public class ClienteVendedor {
    
    private ServicoLeilao servico;
    private String vendedorId;
    private String nomeVendedor;
    private Scanner scanner;
    
    // Cores para output
    private static final String RESET = "\u001B[0m";
    private static final String RED = "\u001B[31m";
    private static final String GREEN = "\u001B[32m";
    private static final String YELLOW = "\u001B[33m";
    private static final String BLUE = "\u001B[34m";
    private static final String PURPLE = "\u001B[35m";
    private static final String CYAN = "\u001B[36m";
    
    public ClienteVendedor() {
        this.scanner = new Scanner(System.in);
        // Gerar ID único automaticamente
        this.vendedorId = "V" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
    
    public void inicializar() {
        limparTela();
        System.out.println(CYAN + "╔════════════════════════════════════════════════════════╗" + RESET);
        System.out.println(CYAN + "║     BEM-VINDO AO SISTEMA DE LEILÃO - VENDEDOR         ║" + RESET);
        System.out.println(CYAN + "╚════════════════════════════════════════════════════════╝" + RESET);
        System.out.println();
        
        // Solicitar nome do vendedor
        System.out.print(YELLOW + "Digite seu nome: " + RESET);
        this.nomeVendedor = scanner.nextLine().trim();
        
        while (this.nomeVendedor.isEmpty()) {
            System.out.println(RED + "Nome não pode ser vazio!" + RESET);
            System.out.print(YELLOW + "Digite seu nome: " + RESET);
            this.nomeVendedor = scanner.nextLine().trim();
        }
        
        System.out.println();
        System.out.println(GREEN + "Perfil criado com sucesso!" + RESET);
        System.out.println("Nome: " + nomeVendedor);
        System.out.println("ID único: " + CYAN + vendedorId + RESET);
        System.out.println(YELLOW + "Anote seu ID para consultas futuras!" + RESET);
        
        pausar();
    }
    
    public void conectar() throws RemoteException, NotBoundException {
        System.out.println(CYAN + "\nConectando ao servidor..." + RESET);
        
        try {
            Registry registry = LocateRegistry.getRegistry("localhost", 1099);
            servico = (ServicoLeilao) registry.lookup("ServidorLeilao");
            System.out.println(GREEN + "Conectado ao servidor de leilões!" + RESET);
        } catch (Exception e) {
            System.out.println(RED + "Erro ao conectar ao servidor!" + RESET);
            System.out.println(YELLOW + "Certifique-se de que o servidor está rodando." + RESET);
            throw e;
        }
    }
    
    public void executarMenu() {
        limparTela();
        System.out.println(CYAN + "╔════════════════════════════════════════════════════════╗" + RESET);
        System.out.println(CYAN + "║         SISTEMA DE LEILÃO - PAINEL DO VENDEDOR        ║" + RESET);
        System.out.println(CYAN + "╚════════════════════════════════════════════════════════╝" + RESET);
        System.out.println(YELLOW + "Vendedor: " + RESET + nomeVendedor);
        System.out.println(YELLOW + "ID: " + RESET + CYAN + vendedorId + RESET);
        System.out.println();
        
        while (true) {
            try {
                exibirMenu();
                int opcao = lerOpcao();
                
                switch (opcao) {
                    case 1:
                        cadastrarNovoItem();
                        break;
                    case 2:
                        listarMeusLeiloes();
                        break;
                    case 3:
                        consultarLeilaoEspecifico();
                        break;
                    case 4:
                        visualizarLancesLeilao();
                        break;
                    case 5:
                        cancelarLeilao();
                        break;
                    case 6:
                        listarTodosLeiloesAtivos();
                        break;
                    case 7:
                        exibirMeuPerfil();
                        break;
                    case 0:
                        System.out.println(YELLOW + "\n╔════════════════════════════════════════════════════════╗" + RESET);
                        System.out.println(YELLOW + "║              Encerrando sessão...                     ║" + RESET);
                        System.out.println(YELLOW + "╚════════════════════════════════════════════════════════╝" + RESET);
                        System.out.println("Seu ID: " + CYAN + vendedorId + RESET);
                        System.out.println(GREEN + "Obrigado por usar o sistema!" + RESET);
                        return;
                    default:
                        System.out.println(RED + "Opção inválida!" + RESET);
                }
                
                pausar();
                
            } catch (Exception e) {
                System.out.println(RED + "Erro: " + e.getMessage() + RESET);
                e.printStackTrace();
                pausar();
            }
        }
    }
    
    private void exibirMenu() {
        System.out.println("\n" + PURPLE + "═══ MENU PRINCIPAL ═══" + RESET);
        System.out.println("1. " + GREEN + "Cadastrar novo item para leilão" + RESET);
        System.out.println("2. Listar meus leilões");
        System.out.println("3. Consultar leilão específico");
        System.out.println("4. Ver lances de um leilão");
        System.out.println("5. " + RED + "Cancelar leilão" + RESET);
        System.out.println("6. Ver todos os leilões ativos");
        System.out.println("7. Meu perfil");
        System.out.println("0. Sair");
        System.out.print(CYAN + "\nEscolha uma opção: " + RESET);
    }
    
    private int lerOpcao() {
        try {
            return Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            return -1;
        }
    }
    
    private void exibirMeuPerfil() {
        System.out.println("\n" + CYAN + "╔════════════════════════════════════════════════════════╗" + RESET);
        System.out.println(CYAN + "║                   MEU PERFIL                           ║" + RESET);
        System.out.println(CYAN + "╚════════════════════════════════════════════════════════╝" + RESET);
        System.out.println("Nome: " + YELLOW + nomeVendedor + RESET);
        System.out.println("ID único: " + CYAN + vendedorId + RESET);
        System.out.println("Tipo: " + GREEN + "VENDEDOR" + RESET);
        System.out.println("\n" + YELLOW + "Guarde seu ID para consultas futuras!" + RESET);
        System.out.println(CYAN + "Você pode compartilhar seus leilões usando o ID do leilão" + RESET);
    }
    
    private void cadastrarNovoItem() throws RemoteException {
        System.out.println("\n" + CYAN + "═══ CADASTRAR NOVO ITEM ═══" + RESET);
        
        System.out.print("Nome do item: ");
        String nome = scanner.nextLine().trim();
        
        if (nome.isEmpty()) {
            System.out.println(RED + "Nome não pode ser vazio!" + RESET);
            return;
        }
        
        System.out.print("Descrição: ");
        String descricao = scanner.nextLine().trim();
        
        System.out.print("Preço inicial (R$): ");
        double precoInicial;
        try {
            precoInicial = Double.parseDouble(scanner.nextLine().trim());
            if (precoInicial <= 0) {
                System.out.println(RED + "Preço deve ser maior que zero!" + RESET);
                return;
            }
        } catch (NumberFormatException e) {
            System.out.println(RED + "Preço inválido!" + RESET);
            return;
        }
        
        // Criar item e cadastrar
        ItemLeilao item = new ItemLeilao(null, nome, descricao, precoInicial, vendedorId);
        String leilaoId = servico.cadastrarItem(item);
        
        System.out.println("\n" + GREEN + "╔════════════════════════════════════════════════════════╗" + RESET);
        System.out.println(GREEN + "║           ITEM CADASTRADO COM SUCESSO!              ║" + RESET);
        System.out.println(GREEN + "╚════════════════════════════════════════════════════════╝" + RESET);
        System.out.println(YELLOW + "ID do Leilão: " + RESET + CYAN + leilaoId + RESET);
        System.out.println("Item: " + YELLOW + nome + RESET);
        System.out.println("Preço inicial: " + GREEN + "R$ " + String.format("%.2f", precoInicial) + RESET);
        System.out.println("\n" + CYAN + "O leilão está ativo e aguardando lances!" + RESET);
        System.out.println(YELLOW + "Timeout: 15 segundos sem lances" + RESET);
        System.out.println(PURPLE + "Compartilhe o ID do leilão: " + CYAN + leilaoId + RESET);
    }
    
    private void listarMeusLeiloes() throws RemoteException {
        System.out.println("\n" + CYAN + "═══ MEUS LEILÕES ═══" + RESET);
        
        List<InfoLeilao> leiloes = servico.listarLeiloesVendedor(vendedorId);
        
        if (leiloes.isEmpty()) {
            System.out.println(YELLOW + "Você ainda não tem leilões cadastrados." + RESET);
            System.out.println(CYAN + "Use a opção 1 para cadastrar um novo item!" + RESET);
            return;
        }
        
        System.out.println("\n" + GREEN + "Total: " + leiloes.size() + " leilão(ões)" + RESET + "\n");
        
        for (int i = 0; i < leiloes.size(); i++) {
            InfoLeilao info = leiloes.get(i);
            String corStatus = obterCorStatus(info.getStatus());
            String iconStatus = obterIconStatus(info.getStatus());
            
            System.out.println("┌─────────────────────────────────────────────────────");
            System.out.println("│ " + YELLOW + (i + 1) + ". " + RESET + info.getItem().getNome());
            System.out.println("│    " + BLUE + "ID: " + info.getLeilaoId() + RESET);
            System.out.println("│    " + iconStatus + " Status: " + corStatus + info.getStatus() + RESET);
            System.out.println("│    Preço inicial: R$ " + String.format("%.2f", info.getItem().getPrecoInicial()));
            System.out.println("│    " + GREEN + "Lance atual: R$ " + String.format("%.2f", info.getLanceAtual()) + RESET);
            System.out.println("│    Lances recebidos: " + info.getQuantidadeLances());
            
            if (info.getCompradorLiderAtual() != null) {
                System.out.println("│    " + PURPLE + "Líder: " + info.getCompradorLiderAtual() + RESET);
            } else {
                System.out.println("│    " + YELLOW + "Aguardando primeiro lance" + RESET);
            }
            
            if (info.getStatus() == StatusLeilao.ATIVO) {
                long tempoDecorrido = (System.currentTimeMillis() - info.getTimestampUltimoLance()) / 1000;
                long tempoRestante = 300 - tempoDecorrido;
                if (tempoRestante > 0) {
                    System.out.println("│    " + RED + "Finaliza em " + tempoRestante + "s (sem novos lances)" + RESET);
                }
            }
            
            System.out.println("└─────────────────────────────────────────────────────");
        }
    }
    
    private void consultarLeilaoEspecifico() throws RemoteException {
        System.out.println("\n" + CYAN + "═══ CONSULTAR LEILÃO ═══" + RESET);
        System.out.print("Digite o ID do leilão: ");
        String leilaoId = scanner.nextLine().trim();
        
        if (leilaoId.isEmpty()) {
            System.out.println(RED + "ID não pode ser vazio!" + RESET);
            return;
        }
        
        try {
            InfoLeilao info = servico.consultarLeilao(leilaoId);
            exibirDetalhesLeilao(info);
        } catch (RemoteException e) {
            System.out.println(RED + "Leilão não encontrado!" + RESET);
        }
    }
    
    private void exibirDetalhesLeilao(InfoLeilao info) {
        String corStatus = obterCorStatus(info.getStatus());
        String iconStatus = obterIconStatus(info.getStatus());
        boolean isMeu = info.getItem().getVendedorId().equals(vendedorId);
        
        System.out.println("\n╔════════════════════════════════════════════════════════╗");
        System.out.println("║              DETALHES DO LEILÃO                     ║");
        System.out.println("╚════════════════════════════════════════════════════════╝");
        System.out.println("ID: " + CYAN + info.getLeilaoId() + RESET);
        System.out.println("Item: " + YELLOW + info.getItem().getNome() + RESET);
        System.out.println("Descrição: " + info.getItem().getDescricao());
        System.out.println(iconStatus + " Status: " + corStatus + info.getStatus() + RESET);
        System.out.println("Vendedor: " + info.getItem().getVendedorId() + 
            (isMeu ? GREEN + " (VOCÊ)" + RESET : ""));
        System.out.println("Preço inicial: R$ " + String.format("%.2f", info.getItem().getPrecoInicial()));
        System.out.println(GREEN + "Lance atual: R$ " + String.format("%.2f", info.getLanceAtual()) + RESET);
        System.out.println("Quantidade de lances: " + info.getQuantidadeLances());
        
        if (info.getCompradorLiderAtual() != null) {
            System.out.println(PURPLE + "Líder atual: " + info.getCompradorLiderAtual() + RESET);
        } else {
            System.out.println(YELLOW + "Líder atual: Nenhum" + RESET);
        }
        
        if (info.getStatus() == StatusLeilao.ATIVO) {
            long tempoDecorrido = (System.currentTimeMillis() - info.getTimestampUltimoLance()) / 1000;
            long tempoRestante = 300 - tempoDecorrido;
            System.out.println("Tempo desde último lance: " + tempoDecorrido + " minutos");
            if (tempoRestante > 0) {
                System.out.println(RED + "Finaliza em " + tempoRestante + "m se não houver novos lances" + RESET);
            }
        }
    }
    
    private void visualizarLancesLeilao() throws RemoteException {
        System.out.println("\n" + CYAN + "═══ HISTÓRICO DE LANCES ═══" + RESET);
        System.out.print("Digite o ID do leilão: ");
        String leilaoId = scanner.nextLine().trim();
        
        if (leilaoId.isEmpty()) {
            System.out.println(RED + "ID não pode ser vazio!" + RESET);
            return;
        }
        
        try {
            List<Lance> lances = servico.listarLancesLeilao(leilaoId);
            InfoLeilao info = servico.consultarLeilao(leilaoId);
            
            if (lances.isEmpty()) {
                System.out.println(YELLOW + "Este leilão ainda não recebeu lances." + RESET);
                return;
            }
            
            System.out.println("\nLeilão: " + YELLOW + info.getItem().getNome() + RESET);
            System.out.println(GREEN + "Total: " + lances.size() + " lance(s)" + RESET + "\n");
            
            for (int i = 0; i < lances.size(); i++) {
                Lance lance = lances.get(i);
                boolean isVencedor = (i == lances.size() - 1);
                
                String prefixo = isVencedor ? GREEN + " " : "   ";
                String sufixo = isVencedor ? " (LÍDER ATUAL)" : "";
                
                System.out.println(prefixo + (i + 1) + ". " + 
                    GREEN + "R$ " + String.format("%.2f", lance.getValor()) + RESET + 
                    " - " + PURPLE + lance.getCompradorId() + RESET + 
                    sufixo);
            }
            
        } catch (RemoteException e) {
            System.out.println(RED + "Leilão não encontrado!" + RESET);
        }
    }
    
    private void cancelarLeilao() throws RemoteException {
        System.out.println("\n" + RED + "═══  CANCELAR LEILÃO ═══" + RESET);
        System.out.print("Digite o ID do leilão: ");
        String leilaoId = scanner.nextLine().trim();
        
        if (leilaoId.isEmpty()) {
            System.out.println(RED + "ID não pode ser vazio!" + RESET);
            return;
        }
        
        System.out.print(YELLOW + " Tem certeza que deseja cancelar? (S/N): " + RESET);
        String confirmacao = scanner.nextLine().trim().toUpperCase();
        
        if (!confirmacao.equals("S")) {
            System.out.println(CYAN + "Cancelamento abortado." + RESET);
            return;
        }
        
        boolean sucesso = servico.cancelarLeilao(leilaoId, vendedorId);
        
        if (sucesso) {
            System.out.println(GREEN + "Leilão cancelado com sucesso!" + RESET);
        } else {
            System.out.println(RED + "Não foi possível cancelar o leilão." + RESET);
            System.out.println(YELLOW + "\nMotivos possíveis:" + RESET);
            System.out.println("  Leilão não existe");
            System.out.println("  Você não é o dono deste leilão");
            System.out.println("  Leilão já tem lances (não pode ser cancelado)");
            System.out.println("  Leilão já foi finalizado");
        }
    }
    
    private void listarTodosLeiloesAtivos() throws RemoteException {
        System.out.println("\n" + CYAN + "═══ TODOS OS LEILÕES ATIVOS ═══" + RESET);
        
        List<InfoLeilao> leiloes = servico.listarLeiloesAtivos();
        
        if (leiloes.isEmpty()) {
            System.out.println(YELLOW + "Não há leilões ativos no momento." + RESET);
            return;
        }
        
        System.out.println("\n" + GREEN + "Total: " + leiloes.size() + " leilão(ões) ativo(s)" + RESET + "\n");
        
        for (int i = 0; i < leiloes.size(); i++) {
            InfoLeilao info = leiloes.get(i);
            boolean isMeu = info.getItem().getVendedorId().equals(vendedorId);
            
            System.out.println("┌─────────────────────────────────────────────────────");
            System.out.println("│ " + YELLOW + (i + 1) + ". " + RESET + info.getItem().getNome() + 
                (isMeu ? GREEN + " (MEU)" + RESET : ""));
            System.out.println("│    " + BLUE + "ID: " + info.getLeilaoId() + RESET);
            System.out.println("│    Vendedor: " + info.getItem().getVendedorId());
            System.out.println("│    " + GREEN + " Lance atual: R$ " + String.format("%.2f", info.getLanceAtual()) + RESET);
            System.out.println("│    Lances: " + info.getQuantidadeLances());
            
            if (info.getCompradorLiderAtual() != null) {
                System.out.println("│    " + PURPLE + " Líder: " + info.getCompradorLiderAtual() + RESET);
            }
            
            System.out.println("└─────────────────────────────────────────────────────");
        }
    }
    
    private String obterCorStatus(StatusLeilao status) {
        switch (status) {
            case ATIVO: return GREEN;
            case FINALIZADO: return BLUE;
            case CANCELADO: return RED;
            default: return RESET;
        }
    }
    
    private String obterIconStatus(StatusLeilao status) {
        switch (status) {
            case ATIVO: return "🟢";
            case FINALIZADO: return "🔵";
            case CANCELADO: return "🔴";
            default: return "⚪";
        }
    }
    
    private void limparTela() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }
    
    private void pausar() {
        System.out.print("\n" + YELLOW + "Pressione ENTER para continuar..." + RESET);
        scanner.nextLine();
        limparTela();
    }
    
    public static void main(String[] args) {
        try {
            ClienteVendedor cliente = new ClienteVendedor();
            cliente.inicializar();
            cliente.conectar();
            cliente.executarMenu();
            
        } catch (Exception e) {
            System.err.println("Erro: " + e.getMessage());
            e.printStackTrace();
        }
    }
}