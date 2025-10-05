// Estacionamento.java
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;

public class Estacionamento {

    // Contadores para estatísticas
    private final AtomicInteger totalEntradas = new AtomicInteger(0);
    private final AtomicInteger totalDesistencias = new AtomicInteger(0);

    // controle de recursos
    private final Semaphore vagasRegulares;
    private final Semaphore vagasPrioritarias;
    private final Semaphore portaoEntrada;
    private final Semaphore portaoSaida;

    public Estacionamento(int numVagasRegulares, int numVagasPrioritarias) {
        this.vagasRegulares = new Semaphore(numVagasRegulares, true);
        this.vagasPrioritarias = new Semaphore(numVagasPrioritarias, true);
        this.portaoEntrada = new Semaphore(1, true); // 1 veículo por vez
        this.portaoSaida = new Semaphore(1, true);   // 1 veículo por vez
    }

    // Métodos para acesso aos semáforos
    public Semaphore getVagasRegulares() {
        return vagasRegulares;
    }

    public Semaphore getVagasPrioritarias() {
        return vagasPrioritarias;
    }

    public Semaphore getPortaoEntrada() {
        return portaoEntrada;
    }

    public Semaphore getPortaoSaida() {
        return portaoSaida;
    }

    // Métodos para estatísticas
    public void registrarEntrada() {
        totalEntradas.incrementAndGet();
    }

    public void registrarDesistencia() {
        totalDesistencias.incrementAndGet();
    }

    public int getTotalEntradas() {
        return totalEntradas.get();
    }

    public int getTotalDesistencias() {
        return totalDesistencias.get();
    }
}