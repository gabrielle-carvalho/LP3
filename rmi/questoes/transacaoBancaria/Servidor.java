import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

public class Servidor extends UnicastRemoteObject implements ServicoBanco {

    private final ConcurrentHashMap<Integer, ContaBancaria> contas;

    protected Servidor() throws RemoteException {
        super();
        this.contas = new ConcurrentHashMap<>();
        
        contas.put(1001, new ContaBancaria(1001, 1000.00));
        contas.put(1002, new ContaBancaria(1002, 1000.00));
        System.out.println("Contas 1001 e 1002 criadas, cada uma com R$ 1000.00");
    }

    @Override
    public double getSaldo(int idConta) throws RemoteException {
        ContaBancaria conta = contas.get(idConta);
        if (conta != null) {
            return conta.getSaldo();
        }
        throw new RemoteException("Conta não encontrada: " + idConta);
    }

    @Override
    public void depositar(int idConta, double valor) throws RemoteException {
        ContaBancaria conta = contas.get(idConta);
        if (conta != null) {
            conta.depositar(valor);
        } else {
            throw new RemoteException("Conta não encontrada: " + idConta);
        }
    }

    @Override
    public boolean sacar(int idConta, double valor) throws RemoteException {
        ContaBancaria conta = contas.get(idConta);
        if (conta != null) {
            return conta.sacar(valor);
        }
        throw new RemoteException("Conta não encontrada: " + idConta);
    }

    @Override
    public boolean transferir(int idContaOrigem, int idContaDestino, double valor) throws RemoteException {
        if (idContaOrigem == idContaDestino) {
            throw new RemoteException("Conta de origem e destino não podem ser as mesmas.");
        }

        ContaBancaria contaOrigem = contas.get(idContaOrigem);
        ContaBancaria contaDestino = contas.get(idContaDestino);

        if (contaOrigem == null || contaDestino == null) {
            throw new RemoteException("Uma ou ambas as contas não foram encontradas.");
        }

        // Garantir que as travas sejam adquiridas SEMPRE na mesma ordem
        // (ex: da conta com ID menor para a com ID maior).
        
        ContaBancaria primeiraTrava = (contaOrigem.getId() < contaDestino.getId()) ? contaOrigem : contaDestino;
        ContaBancaria segundaTrava = (contaOrigem.getId() < contaDestino.getId()) ? contaDestino : contaOrigem;

        try { // Tenta adquirir as travas com timeout para evitar bloqueio infinito
            System.out.printf("[TENTATIVA] %s: Tentando travar %d...%n", 
                Thread.currentThread().getName(), primeiraTrava.getId());
                
            if (!primeiraTrava.getTrava().tryLock(5, TimeUnit.SECONDS)) {
                System.out.printf("[FALHA] %s: Timeout ao travar %d.%n", 
                    Thread.currentThread().getName(), primeiraTrava.getId());
                return false; // Falha por timeout
            }
            
            System.out.printf("[OK] %s: TRAVOU %d.%n", 
                Thread.currentThread().getName(), primeiraTrava.getId());

            try {
                System.out.printf("[TENTATIVA] %s: Tentando travar %d...%n", 
                    Thread.currentThread().getName(), segundaTrava.getId());
                    
                if (!segundaTrava.getTrava().tryLock(5, TimeUnit.SECONDS)) {
                    System.out.printf("[FALHA] %s: Timeout ao travar %d.%n", 
                        Thread.currentThread().getName(), segundaTrava.getId());
                    return false; // Falha por timeout
                }
                
                System.out.printf("[OK] %s: TRAVOU %d.%n", 
                    Thread.currentThread().getName(), segundaTrava.getId());

                try {
                    // --- SEÇÃO CRÍTICA (Ambas as travas adquiridas) ---
                    System.out.printf("[PROCESSANDO] %s: Transferindo R$%.2f de %d para %d...%n",
                        Thread.currentThread().getName(), valor, idContaOrigem, idContaDestino);
                        
                    if (contaOrigem.sacar(valor)) {
                        contaDestino.depositar(valor);
                        System.out.printf("[SUCESSO] %s: Transferência concluída.%n", 
                            Thread.currentThread().getName());
                        return true;
                    } else {
                        System.out.printf("[FALHA] %s: Saldo insuficiente na conta %d.%n", 
                            Thread.currentThread().getName(), idContaOrigem);
                        return false;
                    }

                } finally {
                    // Libera a segunda trava
                    System.out.printf("[LIBERANDO] %s: Liberando trava %d...%n", 
                        Thread.currentThread().getName(), segundaTrava.getId());
                    segundaTrava.getTrava().unlock();
                }
            } finally {
                // Libera a primeira trava
                System.out.printf("[LIBERANDO] %s: Liberando trava %d...%n", 
                    Thread.currentThread().getName(), primeiraTrava.getId());
                primeiraTrava.getTrava().unlock();
            }

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RemoteException("Transferência interrompida", e);
        }
    }
    
    public static void main(String[] args) {
        try {
            Servidor servidor = new Servidor();
            
            Registry registry = LocateRegistry.createRegistry(1099);
            registry.rebind("BancoService", servidor);
            
            System.out.println("==================================");
            System.out.println(" Servidor do Banco (RMI) no ar!");
            System.out.println("==================================");
            
        } catch (Exception e) {
            System.err.println("Erro no servidor: " + e.getMessage());
            e.printStackTrace();
        }
    }
}