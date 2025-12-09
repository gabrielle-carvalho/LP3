package com.uneb.lp3.spring_project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.uneb.lp3.spring_project.model.Course;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {
    // Só de estender JpaRepository, já ganhamos métodos como findAll(), save(), delete() de graça!
}