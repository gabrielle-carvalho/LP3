public class ProdutorConsumidor_1 {
    
    // BUFFER: ArrayList comum, com limite 5 
    private static final List<Integer> lista = new ArrayList<>(5);
    
    private static boolean produzindo = true;
    private static boolean consumindo = true;

    public static void main(String[] args) {
        
        Thread produtor = new Thread(() -> {
            while (true) {
                simulaProcessamento(); // Simula um tempo de trabalho
                
                // LÓGICA DO PRODUTOR: Só produz se "produzindo" for true
                if (produzindo) {
                    System.out.println("Produzindo");
                    lista.add(new Random().nextInt(10000)); 
                    
                    // Se a lista encheu (size 5), ele para de produzir
                    if (lista.size() == 5) {
                        System.out.println("Pausando produtor.");
                        produzindo = false; 
                    }
                    // ...e avisa o consumidor para começar.
                    if (lista.size() == 1) {
                        System.out.println("Iniciando consumidor.");
                        consumindo = true; 
                    }
                } else {
                    System.out.println("!!! Produtor dormindo!");
                }
            }
        });

        Thread consumidor = new Thread(() -> {
            while (true) {
                simulaProcessamento(); // Simula um tempo de trabalho
                
                // Só consome se "consumindo" for true
                if (consumindo) {
                    System.out.println("Consumindo");
                    // Pega o primeiro item e remove
                    lista.stream().findFirst().ifPresent(n -> {
                        lista.remove(n);
                    });

                    // Se a lista esvaziou, para de consumir
                    if (lista.size() == 0) {
                        System.out.println("Pausando consumidor.");
                        consumindo = false;
                    }
                    // avisa o produtor para (re)começar.
                    if (lista.size() == 4) {
                        System.out.println("Iniciando produtor.");
                        produzindo = true; 
                    }
                } else {
                    System.out.println("??? Consumidor dormindo!"); 
                }
            }
        });

        produtor.start(); 
        consumidor.start();
    }
    
    private static final void simulaProcessamento() {
         try {
             Thread.sleep(new Random().nextInt(40));
         } catch (InterruptedException e) { /* ... */ }
    }
}