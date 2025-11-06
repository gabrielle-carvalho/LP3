import java.util.concurrent.locks.ReentrantLock;

/* Cada conta individual tem sua própria trava (ReentrantLock).
 * Isso permite que duas threads façam saques em contas DIFERENTES ao mesmo tempo, melhorando muito a performance */

 public class ContaBancaria {
    private final int id;
    private double saldo;
    private final ReentrantLock trava = new ReentrantLock();

    public ContaBancaria(int id, double saldoInicial) {
        this.id = id;
        this.saldo = saldoInicial;
    }

    public int getId() {
        return id;
    }

    public ReentrantLock getTrava() {
        return trava;
    }

    public double getSaldo() { // Trava a conta, lê o saldo, destrava a conta
        trava.lock();
        try {
            return this.saldo;
        } finally {
            trava.unlock();
        }
    }

    public void depositar(double valor) { // Trava a conta, adiciona o valor, destrava a conta
        trava.lock();
        try {
            if (valor > 0) {
                this.saldo += valor;
            }
        } finally {
            trava.unlock();
        }
    }

    public boolean sacar(double valor) { // Trava a conta, verifica o saldo, saca, destrava a conta
        trava.lock();
        try {
            if (valor > 0 && this.saldo >= valor) {
                this.saldo -= valor;
                return true; // Saque bem-sucedido
            }
            return false; // Saldo insuficiente
        } finally {
            trava.unlock();
        }
    }
}