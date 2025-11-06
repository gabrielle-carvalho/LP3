
import java.util.*;
import java.util.concurrent.ScheduledFuture;

public class Leilao {
    private String leilaoId;
    private ItemLeilao item;
    private StatusLeilao status;
    private List<Lance> lances;
    private String compradorLiderAtual;
    private double lanceAtual;
    private ScheduledFuture<?> tarefaTimeout;
    
    public Leilao(String leilaoId, ItemLeilao item) {
        this.leilaoId = leilaoId;
        this.item = item;
        this.status = StatusLeilao.ATIVO;
        this.lances = new ArrayList<>();
        this.compradorLiderAtual = null;
        this.lanceAtual = item.getPrecoInicial();
        this.tarefaTimeout = null;
    }
    
    // Getters e Setters
    public String getLeilaoId() { 
        return leilaoId; 
    }

    public ItemLeilao getItem() { 
        return item; 
    }

    public StatusLeilao getStatus() { 
        return status; 
    }

    public void setStatus(StatusLeilao status) { 
        this.status = status; 
    }

    public List<Lance> getLances() { 
        return lances; 
    }

    public String getCompradorLiderAtual() { 
        return compradorLiderAtual; 
    }

    public void setCompradorLiderAtual(String id) { 
        this.compradorLiderAtual = id; 
    }

    public double getLanceAtual() { 
        return lanceAtual; 
    }

    public void setLanceAtual(double valor) { 
        this.lanceAtual = valor; 
    }

    public ScheduledFuture<?> getTarefaTimeout() { 
        return tarefaTimeout; 
    }

    public void setTarefaTimeout(ScheduledFuture<?> tarefa) { 
        this.tarefaTimeout = tarefa; 
    }
    
    public void adicionarLance(Lance lance) {
        lances.add(lance);
    }
    
    public long getTempoSemLances() {
        if (lances.isEmpty()) {
            return System.currentTimeMillis() - item.getTimestampCriacao();
        }
        Lance ultimo = lances.get(lances.size() - 1);
        return System.currentTimeMillis() - ultimo.getTimestamp();
    }
    
    public InfoLeilao toInfo() {
        long timestampUltimo = lances.isEmpty() ? 
            item.getTimestampCriacao() : 
            lances.get(lances.size() - 1).getTimestamp();
            
        return new InfoLeilao(
            leilaoId,
            item,
            status,
            lanceAtual,
            compradorLiderAtual,
            lances.size(),
            timestampUltimo
        );
    }
}