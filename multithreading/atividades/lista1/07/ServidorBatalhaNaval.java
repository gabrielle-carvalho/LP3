import java.io.*;
import java.net.*;
import java.util.Random;

public class ServidorBatalhaNaval {
    private static final int TAM = 5;
    private static int[][] tabuleiro1 = new int[TAM][TAM];
    private static int[][] tabuleiro2 = new int[TAM][TAM];

    public static void main(String[] args) {
        int porta = 1234;
        try (ServerSocket serverSocket = new ServerSocket(porta)) {
            System.out.println("Servidor de Batalha Naval iniciado na porta: " + porta);
            System.out.println("Aguardando jogador 1...");
            Socket jogador1 = serverSocket.accept();
            System.out.println("Jogador 1 conectado.");

            System.out.println("Aguardando jogador 2...");
            Socket jogador2 = serverSocket.accept();
            System.out.println("Jogador 2 conectado.");

            posicionarNavios(tabuleiro1);
            posicionarNavios(tabuleiro2);

            PrintWriter out1 = new PrintWriter(jogador1.getOutputStream(), true);
            BufferedReader in1 = new BufferedReader(new InputStreamReader(jogador1.getInputStream()));

            PrintWriter out2 = new PrintWriter(jogador2.getOutputStream(), true);
            BufferedReader in2 = new BufferedReader(new InputStreamReader(jogador2.getInputStream()));

            boolean vezJogador1 = true;
            boolean ativo = true;

            while (ativo) {
                if (vezJogador1) {
                    ativo = jogarTurno(out1, in1, out2, tabuleiro2, "Jogador 1");
                } else {
                    ativo = jogarTurno(out2, in2, out1, tabuleiro1, "Jogador 2");
                }
                vezJogador1 = !vezJogador1; // alterna os turnos dps de jogada válida
            }

            jogador1.close();
            jogador2.close();
            System.out.println("Jogo encerrado.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static boolean jogarTurno(PrintWriter outAtivo, BufferedReader inAtivo, PrintWriter outOponente, int[][] tabuleiroOponente, String nomeJogador) throws IOException {

        while (true) {
            outAtivo.println("Tabuleiro do Oponente");
            mostrarTabuleiro(outAtivo, tabuleiroOponente);
            outAtivo.println("Sua vez! Digite linha e coluna (ex: 2 3):");

            String entrada = inAtivo.readLine();
            if (entrada == null) {
                outOponente.println("Oponente desconectou");
                return false;
            }

            String[] coords = entrada.trim().split(" ");
            if (coords.length != 2) {
                outAtivo.println("Entrada inválida. Tente novamente.");
                continue;
            }

            int linha, coluna;
            try {
                linha = Integer.parseInt(coords[0]);
                coluna = Integer.parseInt(coords[1]);
            } catch (NumberFormatException e) {
                outAtivo.println("Coordenadas inválidas. Digite números de 0 a " + (TAM - 1) + ". Tente novamente.");
                continue;
            }

            if (linha < 0 || linha >= TAM || coluna < 0 || coluna >= TAM) {
                outAtivo.println("Coordenadas fora do tabuleiro (0 a " + (TAM - 1) + "). Tente novamente.");
                continue;
            }

            if (tabuleiroOponente[linha][coluna] == 1) {
                outAtivo.println("Acertou!");
                outOponente.println("Seu navio foi acertado em: " + linha + ", " + coluna);
                tabuleiroOponente[linha][coluna] = -1;
                break;
            } else if (tabuleiroOponente[linha][coluna] == 0) {
                outAtivo.println("Água!");
                outOponente.println("Oponente atirou em " + linha + ", " + coluna + " e errou.");
                break; 
            } else {
                outAtivo.println("Você já atirou nesse lugar. Tente outra posicao");
                continue;
            }
        } 

        if (checarVitoria(tabuleiroOponente)) {
            outAtivo.println("venceu");
            outOponente.println("perdeu");
            return false; 
        }
        return true; 
    }

    private static void mostrarTabuleiro(PrintWriter out, int[][] tabuleiro) {
        for (int i = 0; i < TAM; i++) {
            StringBuilder linhaStr = new StringBuilder();
            for (int j = 0; j < TAM; j++) {
                if (tabuleiro[i][j] == -1) {
                    linhaStr.append("X "); // Acerto
                } else {
                    linhaStr.append("- "); // Mostra '-' para água (0) e navio não atingido (1)
                }
            }
            out.println(linhaStr.toString().trim());
        }
    }

    private static void posicionarNavios(int[][] tabuleiro) {
        Random rand = new Random();
        int navios = 5;
        while (navios > 0) {
            int linha = rand.nextInt(TAM);
            int coluna = rand.nextInt(TAM);
            if (tabuleiro[linha][coluna] == 0) {
                tabuleiro[linha][coluna] = 1;
                navios--;
            }
        }
    }

    private static boolean checarVitoria(int[][] tabuleiro) {
        for (int i = 0; i < TAM; i++) {
            for (int j = 0; j < TAM; j++) {
                if (tabuleiro[i][j] == 1) {
                    return false;
                }
            }
        }
        return true; 
    }
}