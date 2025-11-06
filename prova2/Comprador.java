public class Comprador {
    private String compradorId;
    private CallbackComprador callback;
    
    public Comprador(String compradorId, CallbackComprador callback) {
        this.compradorId = compradorId;
        this.callback = callback;
    }
    
    public String getCompradorId() { 
        return compradorId; 
    }

    public CallbackComprador getCallback() { 
        return callback; 
    }
}
