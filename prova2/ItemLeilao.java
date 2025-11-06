import java.io.Serializable;

public class ItemLeilao implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private String itemId;
    private String nome;
    private String descricao;
    private double precoInicial;
    private String vendedorId;
    private long timestampCriacao;
    
    public ItemLeilao(String itemId, String nome, String descricao, 
                      double precoInicial, String vendedorId) {
        this.itemId = itemId;
        this.nome = nome;
        this.descricao = descricao;
        this.precoInicial = precoInicial;
        this.vendedorId = vendedorId;
        this.timestampCriacao = System.currentTimeMillis();
    }
    
    // Getters
    public String getItemId() { 
        return itemId; 
    }

    public String getNome() { 
        return nome; 
    }

    public String getDescricao() { 
        return descricao; 
    }

    public double getPrecoInicial() { 
        return precoInicial; 
    }

    public String getVendedorId() { 
        return vendedorId; 
    }

    public long getTimestampCriacao() { 
        return timestampCriacao; 
    }
    
    @Override
    public String toString() {
        return String.format("%s - %s (R$ %.2f)", itemId, nome, precoInicial);
    }
}