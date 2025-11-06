public class Motorista {
    private InfoMotorista info;
    private StatusMotorista status;
    private CallbackMotorista callback;
    private String atribuicaoAtual;
    private volatile long timestampDisponivel;
    
    public Motorista(InfoMotorista info, CallbackMotorista callback) {
        this.info = info;
        this.status = StatusMotorista.DISPONIVEL;
        this.callback = callback;
        this.atribuicaoAtual = null;
        this.timestampDisponivel=System.currentTimeMillis();
    }
    
    public InfoMotorista getInfo() { 
        return info; 
    }

    public StatusMotorista getStatus() { 
        return status; 
    }

    public CallbackMotorista getCallback() { 
        return callback; 
    }

    public String getAtribuicaoAtual() { 
        return atribuicaoAtual; 
    }
    
    public void setStatus(StatusMotorista status) { 
        this.status = status; 
        if (status == StatusMotorista.DISPONIVEL) {
            this.timestampDisponivel = System.currentTimeMillis();
        }
    }

    public void setAtribuicaoAtual(String atribuicaoId) { 
        this.atribuicaoAtual = atribuicaoId; 
    }
    
    public boolean isDisponivel() {
        return status == StatusMotorista.DISPONIVEL && atribuicaoAtual == null;
    }

    public long getTimestampDisponivel() {
        return timestampDisponivel;
    }

    public void setTimestampDisponivel(long ts) {
        this.timestampDisponivel = ts;
    }
}
