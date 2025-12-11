package br.uneb.lp3.av3.exceptions;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Trata exceção quando tenta criar uma sala com nome duplicado
     * Usado na Questão 01
     */
    @ExceptionHandler(SalaDuplicadaException.class)
    public ResponseEntity<Map<String, Object>> handleSalaDuplicadaException(SalaDuplicadaException ex) {
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("error", ex.getMessage());
        errorResponse.put("status", HttpStatus.BAD_REQUEST.value());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    /**
     * Trata exceção quando a sala não é encontrada
     * Usado na Questão 02 e outras buscas por sala
     */
    @ExceptionHandler(SalaNaoEncontradaException.class)
    public ResponseEntity<Map<String, Object>> handleSalaNaoEncontradaException(SalaNaoEncontradaException ex) {
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("error", ex.getMessage());
        errorResponse.put("status", HttpStatus.NOT_FOUND.value());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    /**
     * Trata exceção quando há conflito de horário em reservas
     * Usado nas Questões 03 e 04
     */
    @ExceptionHandler(HorarioIndisponivelException.class)
    public ResponseEntity<Map<String, Object>> handleHorarioIndisponivelException(HorarioIndisponivelException ex) {
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("error", ex.getMessage());
        errorResponse.put("status", HttpStatus.NOT_FOUND.value());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    /**
     * Trata exceções genéricas de IllegalArgumentException
     * Usado para validações gerais
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, Object>> handleIllegalArgumentException(IllegalArgumentException ex) {
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("error", ex.getMessage());
        errorResponse.put("status", HttpStatus.NOT_FOUND.value());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    /**
     * Handler genérico para exceções não tratadas (opcional)
     * Descomente se quiser capturar todas as exceções
     */
    /*
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGenericException(Exception ex) {
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("error", "Ocorreu um erro inesperado.");
        errorResponse.put("details", ex.getMessage());
        errorResponse.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }
    */
}
