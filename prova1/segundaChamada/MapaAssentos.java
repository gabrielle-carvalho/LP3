import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class MapaAssentos {
    private final Map<String, Assento> assentos;
    private final AtomicInteger assentosLivres;
    private final int fileiras;
    private final char[] colunas;
    private final int totalAssentos;
    
    public MapaAssentos(int fileiras, char[] colunas) {
        this.fileiras = fileiras;
        this.colunas = colunas;
        this.totalAssentos = fileiras * colunas.length;
        this.assentos = new ConcurrentHashMap<>();
        this.assentosLivres = new AtomicInteger(totalAssentos);
        
        inicializarAssentos();
    }
    
    private void inicializarAssentos() {
        for (int fileira = 1; fileira <= fileiras; fileira++) {
            for (char coluna : colunas) {
                String codigo = fileira + String.valueOf(coluna);
                assentos.put(codigo, new Assento(codigo));
            }
        }
    }
    
    public Assento getAssento(String codigo) {
        return assentos.get(codigo);
    }
    
    public List<String> getTodosCodigosAssentos() {
        return new ArrayList<>(assentos.keySet());
    }
    
    public int getQuantidadeAssentosLivres() {
        return assentosLivres.get();
    }
    
    public int getTotalAssentos() {
        return totalAssentos;
    }
    
    public void decrementarAssentosLivres() {
        assentosLivres.decrementAndGet();
    }
    
    public void incrementarAssentosLivres() {
        assentosLivres.incrementAndGet();
    }
    
    public double getTaxaOcupacao() {
        return ((totalAssentos - assentosLivres.get()) * 100.0) / totalAssentos;
    }
}