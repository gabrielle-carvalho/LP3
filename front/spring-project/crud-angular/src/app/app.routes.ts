import { Routes } from '@angular/router';
import { CoursesComponent } from './courses/courses/courses.component';
import { CourseFormComponent } from './courses/course-form/course-form.component'; // <--- Importe isso

export const routes: Routes = [
  { path: '', component: CoursesComponent },
  { path: 'new', component: CourseFormComponent }
];
