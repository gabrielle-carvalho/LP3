import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ExecutionException;

public class ContadorPalavrasParalelo {

    public static class WordCounterTask implements Callable<Integer> {
        
        private String fileName;

        public WordCounterTask(String fileName) {
            this.fileName = fileName;
        }

        @Override
        public Integer call() throws IOException {
            int wordCount = 0;
            String threadName = Thread.currentThread().getName();
            System.out.println("Thread " + threadName + " COMEÇOU a processar: " + fileName);

            try (BufferedReader reader = Files.newBufferedReader(Paths.get(fileName))) {
                String line;
                while ((line = reader.readLine()) != null) { // Remove espaços em branco extras e divide a linha por um ou mais espaços
                    String[] words = line.trim().split("\\s+");
                    
                    if (!line.trim().isEmpty()) {
                        wordCount += words.length;
                    }
                }
            } catch (IOException e) {
                System.err.println("Erro ao ler o arquivo " + fileName + " na thread " + threadName);
                throw e;
            }

            System.out.println("Thread " + threadName + " TERMINOU: " + fileName + " Palavras: " + wordCount);
            return wordCount;
        }
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        
        List<String> fileNames = List.of("arquivo1.txt","arquivo2.txt","arquivo3.txt");
        System.out.println("Iniciando processamento de " + fileNames.size() + " arquivos...");
        int numThreads = fileNames.size();
        ExecutorService executor = Executors.newFixedThreadPool(numThreads); // 

        List<Callable<Integer>> tasks = new ArrayList<>();
        for (String fileName : fileNames) {
            tasks.add(new WordCounterTask(fileName));
        }

        int totalWordCount = 0;
        try {
            // submeter todas as tarefas para execução e obter lista de futures
            // invokeAll espera que todas as tarefas terminem
            List<Future<Integer>> futures = executor.invokeAll(tasks);

            System.out.println("Todas as tarefas de contagem foram concluídas");
            for (Future<Integer> future : futures) {
                // future.get() pega o valor retornado por call()
                totalWordCount += future.get();
            }

            System.out.println("CONTAGEM TOTAL FINAL: " + totalWordCount + " palavras");

        } catch (ExecutionException e) {
            System.err.println("tarefa falhou");
            e.printStackTrace();
        } finally {
            executor.shutdown(); 
        }
    }

}
