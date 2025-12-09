package com.uneb.lp3.spring_project;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.uneb.lp3.spring_project.model.Course;
import com.uneb.lp3.spring_project.repository.CourseRepository;

@SpringBootApplication
public class SpringProjectApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringProjectApplication.class, args);
    }

    @Bean
    CommandLineRunner initDatabase(CourseRepository courseRepository) {
        return args -> {
            courseRepository.deleteAll(); // Limpa o banco antes de começar

            Course c = new Course();
            c.setName("Curso de Angular com Spring");
            c.setCategory("Front-end");

            courseRepository.save(c); // Salva no banco de dados
        };
    }
}