import java.util.List;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Executors_MultiThread1 {
    public static void main(String[] args) throws Exception {
        ExecutorService executor = null;
        try {
            //executor = Executors.newFixedThreadPool(4); //num de threads que serao usados por outras tarefas que ficam no aguardo
            executor = Executors.newCachedThreadPool(); // cria threads com numero indefinido, a partir de quanto de recurso computacional esta disponivel para ser utilizados
            Future<String> f1 = new Tarefa9();
            Future<String> f2 = new Tarefa(());
            Future<String> f4 = new Tarefa();
            Future<String> f5 = new Tarefa();
            Future<String> f6 = new Tarefa();
            Future<String> f7 = new Tarefa();
            Future<String> f8 = new Tarefa();
            List<Future<String>> list = executor.invokeAll(List.of(f1, f2, f3, f4, f5, f6,f7, f8));
            for(Future<String>future:list){
                System.out.println(future.get());
            }
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
