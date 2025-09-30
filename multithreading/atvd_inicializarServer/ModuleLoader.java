 /* 
ModuleLoader que implemente Runnable. 
Cada instância desta classe representa o carregamento de um módulo (e.g., “Configuração”, 
“Segurança”, “Logs”, “Cache”).
a. O run() deve simular um atraso no carregamento (e.g., Thread.sleep(1000) 
a 4000 milissegundos) passado como parametro.
b. Após o atraso, deve imprimir a mensagem “Módulo [Nome do Módulo] 
carregado.” E, em seguida, chamar latch.countDown() para sinalizar sua 
conclusão.
 */

public class ModuleLoader implements Runnable{
    private String moduleName;
    private int loadTime;
    private ServerInitializer serverInitializer;

    public ModuleLoader(String moduleName, int loadTime, ServerInitializer serverInitializer){
        this.moduleName = moduleName;
        this.loadTime = loadTime;
        this.serverInitializer = serverInitializer;
    }

    @Override
    public void run(){
        try{
            Thread.sleep(loadTime);
            System.out.println(moduleName + "carregado");
            serverInitializer.latch.countDown();
        } catch (InterruptedException e){
            e.printStackTrace();
        }
    }
}