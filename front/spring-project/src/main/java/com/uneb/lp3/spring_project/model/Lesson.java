package com.uneb.lp3.spring_project.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Data
@Entity
public class Lesson {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(length = 100, nullable = false)
    private String name;

    @Column(length = 100, nullable = false)
    private String linkLesson;

    // Relacionamento ManyToOne (Muitas Aulas para Um Curso)
    // fetch = FetchType.LAZY: Só carrega o curso do banco se dermos um .getCourse()
    // optional = false: Uma aula NÃO pode existir sem curso
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "course_id", nullable = false) // Nome da coluna FK no banco
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY) // Evita loop infinito no JSON (Aula -> Curso -> Aula...)
    private Course course;
}