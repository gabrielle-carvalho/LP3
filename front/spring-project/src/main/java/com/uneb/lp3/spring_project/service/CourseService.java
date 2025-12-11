package com.uneb.lp3.spring_project.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import com.uneb.lp3.spring_project.dto.CourseDTO;
import com.uneb.lp3.spring_project.dto.CoursePageDTO;
import com.uneb.lp3.spring_project.dto.mapper.CourseMapper;
import com.uneb.lp3.spring_project.exception.RecordNotFoundException;
import com.uneb.lp3.spring_project.model.Course;
import com.uneb.lp3.spring_project.repository.CourseRepository;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

@Validated
@Service
public class CourseService {

    private final CourseRepository courseRepository;
    private final CourseMapper courseMapper;

    public CourseService(CourseRepository courseRepository, CourseMapper courseMapper) {
        this.courseRepository = courseRepository;
        this.courseMapper = courseMapper;
    }

    // @PositiveOrZero: A página não pode ser negativa (começa em 0)
    // @Max(100): Segurança para ninguém pedir 1 milhão de registros e travar o banco
    public CoursePageDTO list(@PositiveOrZero int pageNumber, @Positive @Max(100) int pageSize) {
        // Cria o objeto de paginação do Spring Data
        Page<Course> page = courseRepository.findAll(PageRequest.of(pageNumber, pageSize));

        // Converte a lista de Entidades daquela página para DTOs
        List<CourseDTO> courses = page.get()
                .map(courseMapper::toDTO)
                .collect(Collectors.toList());

        // Retorna o DTO com a lista + totais
        return new CoursePageDTO(courses, page.getTotalElements(), page.getTotalPages());
    }

    /* ... Mantenha os outros métodos (findById, create, update, delete) iguais ... */
    // Apenas copiei os outros métodos para contexto, se você já os tem, mantenha-os.
    public CourseDTO findById(@NotNull @Positive Long id) {
        return courseRepository.findById(id).map(courseMapper::toDTO)
                .orElseThrow(() -> new RecordNotFoundException(id));
    }

    public CourseDTO create(@Valid @NotNull CourseDTO course) {
        return courseMapper.toDTO(courseRepository.save(courseMapper.toEntity(course)));
    }

    public CourseDTO update(@NotNull @Positive Long id, @Valid @NotNull CourseDTO course) {
        return courseRepository.findById(id)
                .map(recordFound -> {
                    Course mapper = courseMapper.toEntity(course);
                    recordFound.setName(course.name());
                    recordFound.setCategory(this.courseMapper.convertCategoryValue(course.category()));
                    // Limpa e adiciona aulas (se houver lógica de aulas)
                    recordFound.getLessons().clear();
                    mapper.getLessons().forEach(lesson -> recordFound.getLessons().add(lesson));
                    return courseMapper.toDTO(courseRepository.save(recordFound));
                })
                .orElseThrow(() -> new RecordNotFoundException(id));
    }

    public void delete(@NotNull @Positive Long id) {
        courseRepository.delete(courseRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException(id)));
    }
}