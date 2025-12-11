package com.uneb.lp3.spring_project.controller;

import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.uneb.lp3.spring_project.dto.CourseDTO;
import com.uneb.lp3.spring_project.dto.CoursePageDTO;
import com.uneb.lp3.spring_project.service.CourseService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;

@Validated //habilita validacao de objetos e seus campos
@RestController // marca essa classe como controller
@RequestMapping("/api/courses") // map web requests to a specific controller class or handler method. 
@AllArgsConstructor //cria um construtor que recebe como parâmetros todos os atributos da classe
@CrossOrigin(origins = "*") // habilita o compartilhamento de recursos entre origens (CORS) para permitir que um backend
public class CourseController {

    private final CourseService courseService;

    // Captura parâmetros da URL (ex: /api/courses?page=0&pageSize=5)
    @GetMapping // funciona como um atalho para mapear requisições HTTP do tipo GET para métodos específicos em um controller
    public CoursePageDTO list(
            @RequestParam(defaultValue = "0") @PositiveOrZero int page,
            @RequestParam(defaultValue = "10") @Positive @Max(100) int pageSize) {
        return courseService.list(page, pageSize);
    }

    // Método para obter um curso pelo ID. O ID é validado como não-nulo e positivo.
    @GetMapping("/{id}")
    public CourseDTO findById(@PathVariable @NotNull @Positive Long id) {
        return courseService.findById(id); // Chama o serviço para buscar o curso
    }

    // Método para criar um novo curso. O corpo da requisição é validado e o status HTTP é '201 Created'.
    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    public CourseDTO create(@RequestBody @Valid @NotNull CourseDTO course) {
        return courseService.create(course); // Chama o serviço para criar o curso
    }

    // Método para atualizar um curso existente. Valida o ID e o corpo da requisição.
    @PutMapping("/{id}")
    public CourseDTO update(@PathVariable @NotNull @Positive Long id,
            @RequestBody @Valid @NotNull CourseDTO course) {
        return courseService.update(id, course); // Chama o serviço para atualizar o curso
    }

    // Método para deletar um curso. O ID é validado como não-nulo e positivo. Retorna 204 (sem conteúdo) após sucesso.
    @DeleteMapping("/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void delete(@PathVariable @NotNull @Positive Long id) {
        courseService.delete(id); // Chama o serviço para deletar o curso
    }

}