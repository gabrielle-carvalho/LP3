package com.uneb.lp3.spring_project.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.uneb.lp3.spring_project.exception.RecordNotFoundException;

@RestControllerAdvice // Diz ao Spring que esta classe trata exceções de TODOS os controllers
public class ApplicationControllerAdvice {

    // Se qualquer controller lançar RecordNotFoundException, este método captura
    @ExceptionHandler(RecordNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND) // Retorna erro 404 automaticamente
    public String handleNotFoundException(RecordNotFoundException ex) {
        return ex.getMessage(); // Retorna a mensagem "Registro não encontrado..."
    }
}

// @RestControllerAdvice: É um tipo de AOP (Programação Orientada a Aspectos). Ele intercepta as exceções.
// @ExceptionHandler: Define qual classe de exceção esse método vai tratar.
// Benefício: Seu Controller fica limpo, focado apenas no sucesso. O erro é tratado num lugar só (centralizado).