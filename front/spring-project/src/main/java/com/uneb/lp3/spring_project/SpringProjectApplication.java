package com.uneb.lp3.spring_project;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.uneb.lp3.spring_project.enums.Category;
import com.uneb.lp3.spring_project.model.Course;
import com.uneb.lp3.spring_project.model.Lesson;
import com.uneb.lp3.spring_project.repository.CourseRepository;

@SpringBootApplication
public class SpringProjectApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringProjectApplication.class, args);
    }

    @Bean
    CommandLineRunner initDatabase(CourseRepository courseRepository) {
        return args -> {
            courseRepository.deleteAll();

            // Cria 20 cursos para testar a paginação
            for (int i = 0; i < 20; i++) {
                Course c = new Course();
                c.setName("Curso Exemplo Paginação " + i);
                c.setCategory(Category.BACK_END);

                Lesson l = new Lesson();
                l.setName("Aula Intro " + i);
                l.setLinkLesson("https://youtu.be/" + i);
                l.setCourse(c);
                c.getLessons().add(l);

                courseRepository.save(c);
            }
        };
    }
}