package com.uneb.lp3.spring_project.exception;

public class RecordNotFoundException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public RecordNotFoundException(Long id) {
        super("Registro não encontrado com o id: " + id);
    }
}

// Estendemos de RuntimeException (Unchecked Exception) para que não sejamos 
// obrigados a colocar throws RecordNotFoundException em todas as assinaturas 
// de método. O Spring sabe lidar muito bem com exceções de tempo de execução.

