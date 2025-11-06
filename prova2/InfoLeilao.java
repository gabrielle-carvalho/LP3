import java.io.Serializable;

public class InfoLeilao implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private String leilaoId;
    private ItemLeilao item;
    private StatusLeilao status;
    private double lanceAtual;
    private String compradorLiderAtual; // null se sem lances
    private int quantidadeLances;
    private long timestampUltimoLance;
    
    public InfoLeilao(String leilaoId, ItemLeilao item, StatusLeilao status,
                      double lanceAtual, String compradorLiderAtual, 
                      int quantidadeLances, long timestampUltimoLance) {
        this.leilaoId = leilaoId;
        this.item = item;
        this.status = status;
        this.lanceAtual = lanceAtual;
        this.compradorLiderAtual = compradorLiderAtual;
        this.quantidadeLances = quantidadeLances;
        this.timestampUltimoLance = timestampUltimoLance;
    }
    
    // Getters
    public String getLeilaoId() { 
        return leilaoId; 
    }

    public ItemLeilao getItem() { 
        return item; 
    }

    public StatusLeilao getStatus() { 
        return status; 
    }

    public double getLanceAtual() { 
        return lanceAtual; 
    }

    public String getCompradorLiderAtual() { 
        return compradorLiderAtual; 
    }

    public int getQuantidadeLances() { 
        return quantidadeLances; 
    }

    public long getTimestampUltimoLance() { 
        return timestampUltimoLance; 
    }
    
    @Override
    public String toString() {
        return String.format("Leilao[%s] %s - Status:%s Lance:R$%.2f Lider:%s", 
            leilaoId, item.getNome(), status, lanceAtual, 
            compradorLiderAtual != null ? compradorLiderAtual : "Nenhum");
    }
}