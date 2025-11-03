import java.io.Serializable;

public class RespostaPedido implements Serializable { //serializable para pegar a repsosta transformar em bytes e enviar pra rede
    
    private final boolean sucesso;
    private final String codigoPedido;
    private final String codigoErro;
    private final String mensagemErro;

    private RespostaPedido(boolean sucesso, String codigoPedido, String codigoErro, String mensagemErro) {
        this.sucesso = sucesso;
        this.codigoPedido = codigoPedido;
        this.codigoErro = codigoErro;
        this.mensagemErro = mensagemErro;
    }

    public static RespostaPedido sucesso(String codigoPedido) {
        return new RespostaPedido(true, codigoPedido, null, null);
    }

    public static RespostaPedido falha(String codigoErro, String mensagemErro) {
        return new RespostaPedido(false, null, codigoErro, mensagemErro);
    }

    public boolean getSucesso() {
        return sucesso;
    }

    public String getCodigoPedido() {
        return codigoPedido;
    }
    
    public String getCodigoErro() {
        return codigoErro;
    }

    public String getMensagemErro() {
        return mensagemErro;
    }
}