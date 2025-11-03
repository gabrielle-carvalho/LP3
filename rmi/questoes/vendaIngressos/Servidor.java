// Sistema de Venda de Ingressos (Show/Cinema)

// Este é o cenário mais parecido com o seu, mas com uma pequena variação.

//     O Desafio: Múltiplos clientes tentam comprar ingressos para um evento (show, cinema). Os melhores assentos (ex: centro, primeiras fileiras) são os mais disputados.

//     Conceitos-Chave (Foco):

//         Mapa: Use um ConcurrentHashMap<String, Ingresso> para o mapa do local.

//         Trava: Cada Ingresso deve ter seu próprio ReentrantLock ou AtomicBoolean (como no seu Assento.java).

//         Produtor-Consumidor: Mantenha o padrão de TratadorCliente -> BlockingQueue -> ProcessadorIngresso (similar ao AlocadorAssentos).

//     Desafio Extra (Avançado): Implemente um "carrinho de compras". Quando um cliente seleciona um ingresso, ele fica "travado" (reservado) por 5 minutos. Se o pagamento (simulado) não ocorrer, o ingresso é automaticamente liberado.

//         Dica: Para isso, você usaria um ScheduledExecutorService para agendar uma tarefa de "liberação" 5 minutos no futuro, que só será executada se o ingresso ainda estiver no estado "reservado".