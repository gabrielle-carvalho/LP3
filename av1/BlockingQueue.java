// BlockingQueue com capacidade 5.
// Ela cuida de TODA a lógica de "estar cheia" ou "estar vazia". 
private static final BlockingQueue<Integer> fila = new LinkedBlockingDeque<>(5);

public static void main(String[] args) {
    
    Runnable produtor = () -> {
        simulaProcessamento();
        System.out.println("Produzindo...");
        int numero = new Random().nextInt(10000);
        try {
            // PUT (Bloqueante)
            // O .put() INSERE o item. 
            // Se a fila estiver cheia (tamanho 5), a thread do produtor
            // vai AUTOMATICAMENTE "dormir" (ficar 'Blocked') até que um consumidor libere espaço.
            fila.put(numero); 
            System.out.println("Produzido: " + numero);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    };

=    Runnable consumidor = () -> {
        simulaProcessamento();
        System.out.println("Consumindo...");
        try {
            // TAKE (Bloqueante)
            // .take() REMOVE o item
            // Se a fila estiver vazia, a thread do consumidor
            // vai AUTOMATICAMENTE dormir aqui
            // até que um produtor adicione um item
            Integer item = fila.take(); 
            System.out.println("Consumido: " + item);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    };
    
    // Código para rodar os dois de forma agendada
    ScheduledExecutorService executor = Executors.newScheduledThreadPool(2);
    executor.scheduleWithFixedDelay(produtor, 0, 10, TimeUnit.MILLISECONDS); [cite: 1455]
    executor.scheduleWithFixedDelay(consumidor, 0, 10, TimeUnit.MILLISECONDS); [cite: 1455]
}