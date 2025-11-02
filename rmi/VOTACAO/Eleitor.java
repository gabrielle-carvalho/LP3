public class Eleitor { // nao é serializable pq o objeto eleitor nunca deve sair do servidor. ele contem o hash da senha
    // essa classe é usada internamente pelo servidor para checar as credenciais mas nunca cai pra rede
    private String cpf;
    private String senhaHash;
    private boolean admin;
    
    public Eleitor(String cpf, String senhaHash, boolean admin) {
        this.cpf = cpf;
        this.senhaHash = senhaHash;
        this.admin = admin;
    }
    
    public String getCpf() { 
        return cpf; 
    }
    public String getSenhaHash() { 
        return senhaHash; 
    }
    public boolean isAdmin() { 
        return admin; 
    }
}
