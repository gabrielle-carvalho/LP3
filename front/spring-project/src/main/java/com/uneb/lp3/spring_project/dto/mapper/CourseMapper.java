package com.uneb.lp3.spring_project.dto.mapper;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.uneb.lp3.spring_project.dto.CourseDTO;
import com.uneb.lp3.spring_project.dto.LessonDTO;
import com.uneb.lp3.spring_project.enums.Category;
import com.uneb.lp3.spring_project.model.Course;
import com.uneb.lp3.spring_project.model.Lesson;

@Component
public class CourseMapper {

    public CourseDTO toDTO(Course course) {
        if (course == null) {
            return null;
        }

        // Convertendo a lista de Lessons para LessonDTOs
        List<LessonDTO> lessons = course.getLessons()
                .stream()
                .map(lesson -> new LessonDTO(lesson.getId(), lesson.getName(), lesson.getLinkLesson()))
                .collect(Collectors.toList());

        return new CourseDTO(
                course.getId(),
                course.getName(),
                course.getCategory().getValue(),
                lessons // Passando a lista convertida
        );
    }

    public Course toEntity(CourseDTO courseDTO) {
        if (courseDTO == null) {
            return null;
        }

        Course course = new Course();
        if (courseDTO.id() != null) {
            course.setId(courseDTO.id());
        }
        course.setName(courseDTO.name());
        course.setCategory(convertCategoryValue(courseDTO.category()));

        // Convertendo a lista de LessonDTOs para Lessons (Entidades)
        // Importante: Precisamos setar o curso na aula para o relacionamento funcionar
        List<Lesson> lessons = courseDTO.lessons().stream().map(lessonDTO -> {
            var lesson = new Lesson();
            lesson.setId(lessonDTO.id());
            lesson.setName(lessonDTO.name());
            lesson.setLinkLesson(lessonDTO.linkLesson());
            lesson.setCourse(course); // 💡 Vínculo pai-filho obrigatório no JPA
            return lesson;
        }).collect(Collectors.toList());

        course.setLessons(lessons);

        return course;
    }

    public Category convertCategoryValue(String value) {
        if (value == null) {
            return null;
        }
        return switch (value) {
            case "Front-end" -> Category.FRONT_END;
            case "Back-end" -> Category.BACK_END;
            default -> throw new IllegalArgumentException("Categoria inválida: " + value);
        };
    }
}