import java.io.*;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

public class TratadorClienteCheckIn implements Runnable {
    private static final AtomicLong geradorIdCliente = new AtomicLong(0);
    
    private final Socket socket;
    private final String idCliente;
    private final GerenciadorCheckIn gerenciador;
    
    public TratadorClienteCheckIn(Socket socket, GerenciadorCheckIn gerenciador) {
        this.socket = socket;
        this.idCliente = "Cliente-" + geradorIdCliente.incrementAndGet();
        this.gerenciador = gerenciador;
    }
    
    @Override
    public void run() {
        System.out.printf("[%s] Conectado%n", idCliente);
        
        try (
            BufferedReader entrada = new BufferedReader(
                new InputStreamReader(socket.getInputStream()));
            PrintWriter saida = new PrintWriter(socket.getOutputStream(), true)
        ) {
            enviarMenu(saida);
            
            String linha;
            while ((linha = entrada.readLine()) != null) {
                linha = linha.trim();
                if (linha.isEmpty()) continue;
                
                System.out.printf("[%s] Comando: %s%n", idCliente, linha);
                
                String resposta = processarComando(linha);
                saida.println(resposta);
                
                if (linha.toUpperCase().startsWith("QUIT")) {
                    break;
                }
                
                saida.println();
                enviarMenu(saida);
            }
            
        } catch (IOException e) {
            System.err.printf("[%s] Erro: %s%n", idCliente, e.getMessage());
        } finally {
            fecharSocket();
            System.out.printf("[%s] Desconectado%n", idCliente);
        }
    }
    
    private void enviarMenu(PrintWriter saida) {
        saida.println("╔════════════════════════════════════════════════╗");
        saida.println("║   COMANDOS DISPONÍVEIS                         ║");
        saida.println("╚════════════════════════════════════════════════╝");
        saida.println("  TROCAR <cpf1> <cpf2>      - Iniciar troca");
        saida.println("  CONFIRMAR <idOperacao>    - Confirmar troca");
        saida.println("  CANCELAR <idOperacao>     - Cancelar operação");
        saida.println("  PASSAGEIROS               - Listar passageiros");
        saida.println("  QUIT                      - Sair");
        saida.println("───────────────────────────────────────────────────");
        saida.println("Digite seu comando: ");
        saida.println();
        saida.flush();
    }
    
    private String processarComando(String comando) {
        String[] partes = comando.split("\\s+");
        String cmd = partes[0].toUpperCase();
        
        try {
            switch (cmd) {
                case "TROCAR":
                    return tratarTrocar(partes);
                case "CONFIRMAR":
                    return tratarConfirmar(partes);
                case "CANCELAR":
                    return tratarCancelar(partes);
                case "PASSAGEIROS":
                    return tratarListarPassageiros();
                case "QUIT":
                    return "Até logo!";
                default:
                    return "ERRO: Comando desconhecido: " + cmd;
            }
        } catch (Exception e) {
            return "ERRO: " + e.getMessage();
        }
    }
    
    private String tratarTrocar(String[] partes) {
        if (partes.length < 3) {
            return "ERRO: Uso correto: TROCAR <cpf1> <cpf2>";
        }
        
        String cpf1 = partes[1];
        String cpf2 = partes[2];
        
        RespostaReserva resposta = gerenciador.iniciarTrocaAssentos(cpf1, cpf2);
        
        if (resposta.getSucesso()) {
            return String.format(
                "╔════════════════════════════════════════════════╗\n" +
                "║   TROCA PREPARADA COM SUCESSO               ║\n" +
                "╚════════════════════════════════════════════════╝\n" +
                "  ID da Operação: %s\n" +
                "  Status: Recursos bloqueados\n" +
                "  \n" +
                "  -> Para confirmar: CONFIRMAR %s\n" +
                "  -> Para cancelar:  CANCELAR %s", 
                resposta.getCodigoReserva(),
                resposta.getCodigoReserva(),
                resposta.getCodigoReserva()
            );
        } else {
            return String.format(
                "╔════════════════════════════════════════════════╗\n" +
                "║    FALHA AO PREPARAR TROCA                   ║\n" +
                "╚════════════════════════════════════════════════╝\n" +
                "  Código: %s\n" +
                "  Motivo: %s",
                resposta.getCodigoErro(),
                resposta.getMensagemErro()
            );
        }
    }
    
    private String tratarConfirmar(String[] partes) {
        if (partes.length < 2) {
            return " ERRO: Uso correto: CONFIRMAR <idOperacao>";
        }
        
        String idOperacao = partes[1];
        
        RespostaReserva resposta = gerenciador.confirmarTroca(idOperacao);
        
        if (resposta.getSucesso()) {
            return String.format(
                "╔════════════════════════════════════════════════╗\n" +
                "║    TROCA CONFIRMADA!                          ║\n" +
                "╚════════════════════════════════════════════════╝\n" +
                "  %s\n" +
                "  \n" +
                "  A troca foi efetivada com sucesso!",
                resposta.getCodigoAssento()
            );
        } else {
            return String.format(
                "╔════════════════════════════════════════════════╗\n" +
                "║    FALHA AO CONFIRMAR                         ║\n" +
                "╚════════════════════════════════════════════════╝\n" +
                "  Código: %s\n" +
                "  Motivo: %s",
                resposta.getCodigoErro(),
                resposta.getMensagemErro()
            );
        }
    }
    
    private String tratarCancelar(String[] partes) {
        if (partes.length < 2) {
            return " ERRO: Uso correto: CANCELAR <idOperacao>";
        }
        
        String idOperacao = partes[1];
        gerenciador.cancelarOperacao(idOperacao);
        
        return String.format(
            "╔════════════════════════════════════════════════╗\n" +
            "║    OPERAÇÃO CANCELADA                         ║\n" +
            "╚════════════════════════════════════════════════╝\n" +
            "  Operação %s foi cancelada.\n" +
            "  Todos os recursos foram liberados.",
            idOperacao
        );
    }
    
    private String tratarListarPassageiros() {
        StringBuilder sb = new StringBuilder();
        sb.append("╔════════════════════════════════════════════════╗\n");
        sb.append("║   PASSAGEIROS CADASTRADOS                      ║\n");
        sb.append("╚════════════════════════════════════════════════╝\n");
        sb.append("CPF     | Nome              | Categoria | Assento | Check-in\n");
        sb.append("--------|-------------------|-----------|---------|----------\n");
        
        Map<String, Passageiro> todosPassageiros = gerenciador.getTodosPassageiros();
        
        List<String> cpfsOrdenados = new ArrayList<>(todosPassageiros.keySet());
        Collections.sort(cpfsOrdenados);
        
        for (String cpf : cpfsOrdenados) {
            Passageiro p = todosPassageiros.get(cpf);
            sb.append(String.format("%-7s | %-17s | %-9s | %-7s | %s%n",
                p.getCpf(), 
                p.getNome(), 
                p.getCategoria(), 
                p.getAssentoAtual(),
                p.getFezCheckIn() ? "Sim" : "Não"
            ));
        }
        
        return sb.toString();
    }
    
    private void fecharSocket() {
        try {
            if (!socket.isClosed()) {
                socket.close();
            }
        } catch (IOException e) {
            System.err.printf("[%s] Erro ao fechar socket: %s%n", 
                idCliente, e.getMessage());
        }
    }
}