import java.io.Serializable;

public class Candidato implements Serializable { // indica que para que o bojeto Candidato possa ser enviado pela rede (do Servidor para o Cliente, na listarCandidatos()), ele deve ser "serializável"
// isso significa que o Java sabe como "achatar" esse objeto em uma sequência de bytes para enviá-lo e como "remontá-lo" do outro lado.
    private static final long serialVersionUID = 1L;
    // serialVersionUID: É um controle de versão. Se você alterar esta classe 
    // (ex: adicionar um novo campo), você deveria mudar esse número. 
    // O RMI usa isso para garantir que o Cliente e o Servidor estão falando da mesma versão da classe.
    
    private int id;
    private String nome;
    private String partido;
    private int numero;
    
    public Candidato(int id, String nome, String partido, int numero) {
        this.id = id;
        this.nome = nome;
        this.partido = partido;
        this.numero = numero;
    }
    
    public int getId() { 
        return id; 
    }
    public String getNome() { 
        return nome; 
    }
    public String getPartido() { 
        return partido; 
    }
    public int getNumero() { 
        return numero; 
    }
    
    @Override
    public String toString() {
        return String.format("[%d] %s - %s (Número: %d)", id, nome, partido, numero);
    }
    // Sobrescreve o método toString padrão. Isso é para formatação. 
    // Quando você der um System.out.println(candidato), ele imprimirá uma string bonita 
    // (ex: [1] João Silva - Partido A (Número: 10)) em vez de algo como Candidato@1f4a7d6.
}
