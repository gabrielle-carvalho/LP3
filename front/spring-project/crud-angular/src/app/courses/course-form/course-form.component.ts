import { Component, OnInit } from '@angular/core';
import { CommonModule, Location } from '@angular/common';
import { FormGroup, NonNullableFormBuilder, ReactiveFormsModule, Validators, FormArray } from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatIconModule } from '@angular/material/icon';
import { ActivatedRoute } from '@angular/router';

import { CoursesService } from '../services/courses.service';
import { Course } from '../model/course';
import { Lesson } from '../model/lesson';

@Component({
  selector: 'app-course-form',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    MatFormFieldModule,
    MatInputModule,
    MatCardModule,
    MatToolbarModule,
    MatButtonModule,
    MatSelectModule,
    MatSnackBarModule,
    MatIconModule
  ],
  templateUrl: './course-form.component.html',
  styleUrls: ['./course-form.component.scss']
})
export class CourseFormComponent implements OnInit {

  form!: FormGroup;

  constructor(
    private formBuilder: NonNullableFormBuilder,
    private service: CoursesService,
    private snackBar: MatSnackBar,
    private location: Location,
    private route: ActivatedRoute
  ) { }

  ngOnInit(): void {
    // Pega o curso que veio do Resolver (se for edição)
    const course: Course = this.route.snapshot.data['course'];

    this.form = this.formBuilder.group({
      _id: [course._id],
      name: [course.name, [Validators.required, Validators.minLength(5), Validators.maxLength(100)]],
      category: [course.category, [Validators.required]],
      lessons: this.formBuilder.array(this.retrieveLessons(course), Validators.required)
    });
  }

  // Prepara o array de aulas (existentes ou uma vazia)
  private retrieveLessons(course: Course) {
    const lessons = [];
    if (course?.lessons) {
      course.lessons.forEach(lesson => lessons.push(this.createLesson(lesson)));
    } else {
      lessons.push(this.createLesson());
    }
    return lessons;
  }

  // Cria um grupo de form para uma aula
  private createLesson(lesson: Lesson = { id: '', name: '', linkLesson: '' }) {
    return this.formBuilder.group({
      id: [lesson.id],
      name: [lesson.name, [Validators.required, Validators.minLength(5), Validators.maxLength(100)]],
      linkLesson: [lesson.linkLesson, [Validators.required, Validators.minLength(5), Validators.maxLength(100)]]
    });
  }

  // Helper para o HTML acessar o array
  getLessonsFormArray() {
    return (<FormArray>this.form.get('lessons')).controls;
  }

  addNewLesson() {
    const lessons = this.form.get('lessons') as FormArray;
    lessons.push(this.createLesson());
  }

  removeLesson(index: number) {
    const lessons = this.form.get('lessons') as FormArray;
    lessons.removeAt(index);
  }

  onSubmit() {
    if (this.form.valid) {
      this.service.save(this.form.value)
        .subscribe({
          next: () => this.onSuccess(),
          error: () => this.onError()
        });
    } else {
      alert('Formulário inválido');
    }
  }

  onCancel() {
    this.location.back();
  }

  private onSuccess() {
    this.snackBar.open('Curso salvo com sucesso!', '', { duration: 5000 });
    this.onCancel();
  }

  private onError() {
    this.snackBar.open('Erro ao salvar curso.', '', { duration: 5000 });
  }
}
