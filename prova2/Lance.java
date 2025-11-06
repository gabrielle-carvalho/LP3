import java.io.Serializable;

public class Lance implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private String lanceId;
    private String leilaoId;
    private String compradorId;
    private double valor;
    private long timestamp;
    
    public Lance(String lanceId, String leilaoId, String compradorId, double valor) {
        this.lanceId = lanceId;
        this.leilaoId = leilaoId;
        this.compradorId = compradorId;
        this.valor = valor;
        this.timestamp = System.currentTimeMillis();
    }
    
    // Getters
    public String getLanceId() { 
        return lanceId; 
    }

    public String getLeilaoId() { 
        return leilaoId; 
    }

    public String getCompradorId() { 
        return compradorId; 
    }

    public double getValor() { 
        return valor; 
    }

    public long getTimestamp() { 
        return timestamp; 
    }
    
    @Override
    public String toString() {
        return String.format("Lance[%s] R$ %.2f por %s", lanceId, valor, compradorId);
    }
}