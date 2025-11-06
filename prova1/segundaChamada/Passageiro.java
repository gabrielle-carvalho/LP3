import java.util.concurrent.locks.ReentrantLock;

public class Passageiro {
    private final String cpf;
    private final String nome;
    private volatile Categoria categoria;
    private volatile String assentoAtual;
    private final ReentrantLock lockPassageiro;
    private volatile boolean fezCheckIn;
    
    public Passageiro(String cpf, String nome, Categoria categoria, String assentoInicial) {
        this.cpf = cpf;
        this.nome = nome;
        this.categoria = categoria;
        this.assentoAtual = assentoInicial;
        this.lockPassageiro = new ReentrantLock(true);
        this.fezCheckIn = false;
    }
    
    public ReentrantLock getLockPassageiro() {
        return lockPassageiro;
    }
    
    public void trocarAssento(String novoAssento) {
        this.assentoAtual = novoAssento;
    }
    
    public void fazerCheckIn() {
        this.fezCheckIn = true;
    }
    
    public void fazerUpgrade(Categoria novaCategoria) {
        this.categoria = novaCategoria;
    }
    
    public String getCpf() { 
        return cpf; 
    }

    public String getNome() { 
        return nome; 
    }

    public Categoria getCategoria() { 
        return categoria; 
    }

    public String getAssentoAtual() { 
        return assentoAtual; 
    }

    public boolean getFezCheckIn() { 
        return fezCheckIn; 
    }
}
