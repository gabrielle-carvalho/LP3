import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ArrayBlockingQueue;

public class ProdutorConsumidor {

    public static class Producer implements Runnable {
        private Buffer buffer;
        private int id;

        public Producer(Buffer buffer, int id) {
            this.buffer = buffer;
            this.id = id;
        }

        @Override
        public void run() {
            try {
                int itemCounter = 0;
                while (true) {
                    Object item = "Item " + id + "-" + (itemCounter++);
                    
                    Thread.sleep((long) (Math.random() * 500));
                    
                    buffer.put(item);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.out.println("Produtor " + id + " interrompido");
            }
        }
    }

    public static class Consumer implements Runnable {
        private Buffer buffer;
        private int id;

        public Consumer(Buffer buffer, int id) {
            this.buffer = buffer;
            this.id = id;
        }

        @Override
        public void run() {
            try {
                while (true) {
                    Object item = buffer.get();
                    
                    Thread.sleep((long) (Math.random() * 1000));
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.out.println("Consumidor " + id + " interrompido");
            }
        }
    }

    public static class Buffer {
        
        private BlockingQueue<Object> queue;

        public Buffer(int capacity) {
            this.queue = new ArrayBlockingQueue<>(capacity);
        }

        public void put(Object item) throws InterruptedException {
            queue.put(item);
            System.out.println("Produtor " + Thread.currentThread().getName() + " produziu: " + item);
        }

        public Object get() throws InterruptedException {
            Object item = queue.take();
            System.out.println("Consumidor " + Thread.currentThread().getName() + " consumiu: " + item);
            return item;
        }
    }

    public static void main(String[] args) {
        int bufferSize = 5;

        Buffer buffer = new Buffer(bufferSize);
        Producer producerTask = new Producer(buffer, 1);
        Consumer consumerTask1 = new Consumer(buffer, 1);
        Consumer consumerTask2 = new Consumer(buffer, 2);

        System.out.println("Iniciando simulação Produtor-Consumidor");
        System.out.println("Tamanho do Buffer: " + bufferSize);

        Thread p1 = new Thread(producerTask, "produtor 1");
        Thread c1 = new Thread(consumerTask1, "consumidor 1");
        Thread c2 = new Thread(consumerTask2, "consumidor 2");

        p1.start();
        c1.start();
        c2.start();

        try {
            Thread.sleep(15000);
        } catch (InterruptedException e) {
             Thread.currentThread().interrupt();
        }

        System.out.println("encerrado a simulação");
        p1.interrupt();
        c1.interrupt();
        c2.interrupt();
        System.out.println("Simulação parada");
    }

}