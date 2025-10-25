import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

public class Simulador {

    private static final int numVagasRegulares = 5;
    private static final int numVagasPrioritarias = 2;
    private static final int totalVeiculos = 20;
    private static final double percentualPrioritarios = 0.3;

    public static void main(String[] args) {
        System.out.println("Sistema de Estacionamento Inteligente");
        Estacionamento estacionamento = new Estacionamento(numVagasRegulares, numVagasPrioritarias);
        ExecutorService executor = Executors.newFixedThreadPool(4);
        Random random = new Random();

        System.out.println("GERANDO E INICIANDO VEÍCULOS");
        for (int i = 1; i <= totalVeiculos; i++) {
            final int id = i;
            final TipoVeiculo tipo = random.nextDouble() < percentualPrioritarios ? TipoVeiculo.PRIORITARIO : TipoVeiculo.NORMAL;
            System.out.printf("Gerado: Veículo #%d (%s)\n", id, tipo);
            executor.execute(() -> cicloDeVidaDoVeiculo(id, tipo, estacionamento, random));
        }

        finalizarSimulacao(executor);
        exibirEstatisticas(estacionamento);
        System.out.println("Simulação Concluída");
    }

    private static void cicloDeVidaDoVeiculo(int id, TipoVeiculo tipo, Estacionamento estacionamento, Random random) {
        try {
            System.out.printf("Veículo #%d (%s) chegou ao portão de entrada\n", id, tipo);
            estacionamento.getPortaoEntrada().acquire();

            Semaphore vagaOcupada = null;
            String tipoVaga = "";

            if (tipo == TipoVeiculo.PRIORITARIO && estacionamento.getVagasPrioritarias().tryAcquire()) {
                vagaOcupada = estacionamento.getVagasPrioritarias();
                tipoVaga = "PRIORITÁRIA";
            } else if (tipo == TipoVeiculo.NORMAL && estacionamento.getVagasRegulares().tryAcquire()) {
                vagaOcupada = estacionamento.getVagasRegulares();
                tipoVaga = "REGULAR";
            }
            
            estacionamento.getPortaoEntrada().release();

            if (vagaOcupada != null) {
                estacionamento.registrarEntrada();
                System.out.printf("Veículo #%d (%s) conseguiu vaga %s\n", id, tipo, tipoVaga);

                long tempoPermanencia = random.nextInt(4001) + 1000;
                System.out.printf("Veículo #%d (%s) está estacionado por %dms\n", id, tipo, tempoPermanencia);
                Thread.sleep(tempoPermanencia);

                System.out.printf("Veículo #%d (%s) está saindo...\n", id, tipo);
                estacionamento.getPortaoSaida().acquire();
                vagaOcupada.release();
                estacionamento.getPortaoSaida().release();
                System.out.printf("Veículo #%d (%s) saiu com sucesso!\n", id, tipo);
            } else {
                System.out.printf("!!! Veículo #%d (%s) não encontrou vaga e desistiu.\n", id, tipo);
                estacionamento.registrarDesistencia();
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.printf("Veículo #%d foi interrompido.\n", id);
        }
    }

    private static void finalizarSimulacao(ExecutorService executor) {
        executor.shutdown();
        try {
            if (!executor.awaitTermination(10, TimeUnit.MINUTES)) {
                executor.shutdownNow();
            }
        } catch (InterruptedException e) {
            executor.shutdownNow();
        }
    }

    private static void exibirEstatisticas(Estacionamento est) {
        System.out.println("\n ESTATÍSTICAS");
        System.out.printf("Vagas Regulares Livres: %d/%d\n", est.getVagasRegulares().availablePermits(), numVagasRegulares);
        System.out.printf("Vagas Prioritárias Livres: %d/%d\n", est.getVagasPrioritarias().availablePermits(), numVagasPrioritarias);
        System.out.printf("Total de entradas: %d\n", est.getTotalEntradas());
        System.out.printf("Total de desistências: %d\n", est.getTotalDesistencias());
        double taxaSucesso = (double) est.getTotalEntradas() / totalVeiculos * 100;
        System.out.printf("Taxa de sucesso: %.1f%%\n", taxaSucesso);
    }
}