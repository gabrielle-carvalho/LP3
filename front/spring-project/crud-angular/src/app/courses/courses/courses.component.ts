import { Component, OnInit } from '@angular/core';
import { MatTableModule } from '@angular/material/table';
import { MatCardModule } from '@angular/material/card';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { CommonModule } from '@angular/common';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';
import { Observable, catchError, of } from 'rxjs';
import { ActivatedRoute, Router } from '@angular/router';
import { Location } from '@angular/common';
import { FormBuilder } from '@angular/forms';
import { Course } from '../model/course';
import { CoursesService } from '../services/courses.service';

@Component({
  selector: 'app-courses',
  standalone: true,
  imports: [
    MatTableModule,
    MatCardModule,
    MatToolbarModule,
    MatProgressSpinnerModule,
    CommonModule,
    MatIconModule,
    MatButtonModule
  ],
  templateUrl: './courses.component.html',
  styleUrl: './courses.component.scss'
})
export class CoursesComponent {

  courses$: Observable<Course[]>;
  displayedColumns = ['name', 'category', 'actions'];

  constructor(
    private coursesService: CoursesService,
    public router: Router,
    public route: ActivatedRoute
  ) {
    this.courses$ = this.coursesService.list()
      .pipe(
        catchError(error => {
          console.log('Erro ao carregar cursos.');
          return of([]);
        })
      );
  }

  ngOnInit(): void { }

  onAdd() {
    console.log('Botão clicado! Navegando...'); // Isso vai aparecer no F12 se o clique funcionar
    this.router.navigate(['/new']);
  }
}
export class CourseFormComponent {
  constructor(
    private formBuilder: FormBuilder,
    private location: Location
  ) { }
  onCancel() {
    this.location.back(); // Volta para a página anterior
  }
}
