package com.uneb.lp3.spring_project.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Data // O Lombok gera Getters, Setters e toString automaticamente
@Entity // Diz ao Spring que isso aqui vai virar uma tabela no banco de dados
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO) // Gera o ID automaticamente (1, 2, 3...)
    private Long id;

    @Column(length = 200, nullable = false) // Coluna 'name' obrigatória, max 200 caracteres
    private String name;

    @Column(length = 30, nullable = false) // Coluna 'category' obrigatória
    private String category;
}