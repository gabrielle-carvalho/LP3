package com.uneb.lp3.spring_project.dto;

import java.util.List;

public record CoursePageDTO(
    List<CourseDTO> courses,
    long totalElements,
    int totalPages) {
}