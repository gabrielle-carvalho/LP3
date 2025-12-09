package com.uneb.lp3.spring_project.controller;

import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.uneb.lp3.spring_project.model.Course;
import com.uneb.lp3.spring_project.repository.CourseRepository;

import lombok.AllArgsConstructor;

@RestController // Avisa ao Spring que esta classe contem endpoints (URLs)
@RequestMapping("/api/courses") // Define que o endereço base será http://localhost:8080/api/courses
@AllArgsConstructor // Cria automaticamente o construtor para injetar o Repository (obrigado Lombok!)
@CrossOrigin(origins = "*")
public class CourseController {
    private final CourseRepository courseRepository;

    // Método GET: Quando alguém acessar a URL, retorna a lista de cursos do banco
    @GetMapping
    public List<Course> list() {
        return courseRepository.findAll();
    }
    @PostMapping
    // @ResponseStatus(code = HttpStatus.CREATED) // Opcional: retorna status 201 (Created) em vez de 200
    public Course create(@RequestBody Course course) {
        return courseRepository.save(course);
    }
}