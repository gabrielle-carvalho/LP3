import java.io.Serializable;

public class ResultadoLance implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private boolean aceito;
    private String mensagem;
    private double lanceAtual;
    private double incrementoMinimo;
    
    public ResultadoLance(boolean aceito, String mensagem, double lanceAtual, double incrementoMinimo) {
        this.aceito = aceito;
        this.mensagem = mensagem;
        this.lanceAtual = lanceAtual;
        this.incrementoMinimo = incrementoMinimo;
    }
    
    public boolean isAceito() { 
        return aceito; 
    }

    public String getMensagem() { 
        return mensagem; 
    }

    public double getLanceAtual() { 
        return lanceAtual; 
    }

    public double getIncrementoMinimo() { 
        return incrementoMinimo; 
    }
    
    @Override
    public String toString() {
        return String.format("%s - %s (Atual: R$ %.2f)", 
            aceito ? "ACEITO" : "RECUSADO", mensagem, lanceAtual);
    }
}