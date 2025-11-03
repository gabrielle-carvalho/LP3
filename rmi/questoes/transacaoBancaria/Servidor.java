// Simulação de Transações Bancárias (Deadlock)

// Este é um problema clássico de concorrência que força você a lidar com travas múltiplas.

//     O Desafio: Múltiplos clientes (em "caixas eletrônicos") fazem depósitos, saques e, o mais importante, transferências entre contas simultaneamente.

//     Conceitos-Chave (Foco):

//         Contas: Use um ConcurrentHashMap<Integer, ContaBancaria>.

//         Trava: Cada objeto ContaBancaria deve ter seu próprio ReentrantLock.

//         Operações: depositar(valor) e sacar(valor) precisam travar (lock()) a conta, alterar o saldo e destravar (unlock()).

//     Desafio Extra (Avançado - Risco de Deadlock):

//         A operação transferir(contaOrigem, contaDestino, valor) é o ponto crítico. Você precisa travar ambas as contas para fazer a transferência com segurança.

//         O Risco: Se o Cliente A tenta transferir de Conta 1 para Conta 2 (travando C1 e depois C2) ao mesmo tempo que o Cliente B tenta transferir de Conta 2 para Conta 1 (travando C2 e depois C1), você terá um Deadlock.

//         A Solução: Para evitar o deadlock, você deve implementar uma regra de ordenação de travas: sempre trave as contas na mesma ordem (por exemplo, pela ordem crescente do ID da conta), não importa qual é a origem e qual é o destino.