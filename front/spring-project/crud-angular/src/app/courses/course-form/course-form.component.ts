import { Component } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatSnackBar } from '@angular/material/snack-bar';
import { CoursesService } from '../services/courses.service';
import { CommonModule, Location } from '@angular/common';

@Component({
  selector: 'app-course-form',
  standalone: true, // Garante que é um componente standalone
  imports: [
    ReactiveFormsModule, // Resolve o erro [formGroup]
    MatFormFieldModule,  // Resolve mat-form-field e mat-label
    MatInputModule,      // Necessário para inputs de texto dentro do form-field
    MatCardModule,       // Resolve mat-card, mat-card-content, mat-card-actions
    MatToolbarModule,    // Resolve mat-toolbar
    MatButtonModule,     // Resolve botões com mat-raised-button
    MatSelectModule,      // Resolve mat-select e mat-option
    CommonModule,
  ],
  templateUrl: './course-form.component.html',
  styleUrl: './course-form.component.scss' // Ou .css, dependendo do seu projeto
})

export class CourseFormComponent {

  form: FormGroup;

  constructor(
    private formBuilder: FormBuilder,
    private service: CoursesService, // Seu serviço
    private snackBar: MatSnackBar,   // Para mensagens de erro/sucesso
    private location: Location       // Para voltar
  ) {
    this.form = this.formBuilder.group({
      name: [null, [Validators.required, Validators.minLength(5)]],
      category: [null, [Validators.required]]
      name: [null, [Validators.required, Validators.minLength(5)]],
      category: [null, [Validators.required]]
    });
  }

  onSubmit() {
    this.service.save(this.form.value)
      .subscribe({
        next: (result) => this.onSuccess(),
        error: (error) => {
          // Isso vai fazer o erro aparecer em vermelho no Console do navegador
          console.error('ERRO NO ANGULAR:', error);
          this.onError();
        }
      });
  }

  onCancel() {
    this.location.back();
  }

  private onSuccess() {
    this.snackBar.open('Curso salvo com sucesso!', '', { duration: 5000 });
    this.onCancel(); // Volta para a lista após salvar
  }

  private onError() {
    this.snackBar.open('Erro ao salvar curso.', '', { duration: 5000 });
  }
}
