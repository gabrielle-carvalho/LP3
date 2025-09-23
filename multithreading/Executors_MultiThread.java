import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Executors_MultiThread {
    public static void main(String[] args) throws Exception {
        ExecutorService executor = null;
        try {
            //executor = Executors.newFixedThreadPool(4); //num de threads que serao usados por outras tarefas que ficam no aguardo
            executor = Executors.newCachedThreadPool(); // cria threads com numero indefinido, a partir de quanto de recurso computacional esta disponivel para ser utilizados
            Future<String> f1 = executor.submit(new Tarefa());
            Future<String> f2 = executor.submit(new Tarefa());
            Future<String> f3 = executor.submit(new Tarefa());
            Future<String> f4 = executor.submit(new Tarefa());
            Future<String> f5 = executor.submit(new Tarefa());
            Future<String> f6 = executor.submit(new Tarefa());
            Future<String> f7 = executor.submit(new Tarefa());
            Future<String> f8 = executor.submit(new Tarefa());
            System.out.println(f1.get());
            System.out.println(f2.get());
            System.out.println(f3.get());
            System.out.println(f4.get());
            System.out.println(f5.get());
            System.out.println(f6.get());
            System.out.println(f7.get());
            System.out.println(f8.get());
            executor.shutdown();
        } catch (Exception e) {
            throw e;
        } finally {
            if (executor != null) {
                executor.shutdownNow();
            }
        }
    }

    public static class Tarefa implements Callable<String> {
        @Override
        public String call() throws Exception {
            String nome = Thread.currentThread().getName();
            int nextInt = new Random().nextInt();
            return nome + ": LP-III " + nextInt;
        }
    }


}
