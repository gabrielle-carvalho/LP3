import java.util.ArrayList;
import java.util.List;

public class OperacaoTroca {
    private final String idOperacao;
    private final String cpfPassageiro1;
    private final String cpfPassageiro2;
    private final List<Object> recursosBloquados;
    private final long timestampInicio;
    private volatile EstadoOperacao estado;
    
    public enum EstadoOperacao {
        INICIADA,
        BLOQUEANDO_RECURSOS,
        RECURSOS_BLOQUEADOS,
        EXECUTANDO,
        CONCLUIDA,
        CANCELADA,
        FALHOU
    }
    
    public OperacaoTroca(String idOperacao, String cpf1, String cpf2) {
        this.idOperacao = idOperacao;
        this.cpfPassageiro1 = cpf1;
        this.cpfPassageiro2 = cpf2;
        this.recursosBloquados = new ArrayList<>();
        this.timestampInicio = System.currentTimeMillis();
        this.estado = EstadoOperacao.INICIADA;
    }
    
    public void adicionarRecursoBloqueado(Object recurso) {
        recursosBloquados.add(recurso);
    }
    
    public List<Object> getRecursosBloqueados() {
        return new ArrayList<>(recursosBloquados);
    }
    
    public void mudarEstado(EstadoOperacao novoEstado) {
        this.estado = novoEstado;
    }
    
    public boolean isExpirada(long timeoutMs) {
        return System.currentTimeMillis() - timestampInicio > timeoutMs;
    }
    
    public String getIdOperacao() { 
        return idOperacao; 
    }

    public String getCpfPassageiro1() { 
        return cpfPassageiro1; 
    }

    public String getCpfPassageiro2() { 
        return cpfPassageiro2; 
    }

    public EstadoOperacao getEstado() { 
        return estado; 
    }
}