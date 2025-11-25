import { MatCardModule } from '@angular/material/card';
import { MatTableModule } from '@angular/material/table';
import { MatToolbarModule } from '@angular/material/toolbar';
import { Component, OnInit } from '@angular/core';
import { Course } from ‘../model/course';
import { CoursesService } from '../services/courses.service';

@Component({
  selector: 'app-courses',
  standalone: true,
  imports: [ MatTableModule, MatCardModule, MatToolbarModule ],
  templateUrl: './courses.html',
  styleUrl: './courses.css',
})
export class Courses {

}

export interface Course {
  _id: string;
  name: string;
  category: string;
 }

/*export class CoursesComponent implements OnInit {*/
export class CoursesComponent {
courses: Course[] = [{_id: "1", name: "Curso Angular", category: "Front-end"}];
  displayedColumns: string[] = ['name', 'category’];
  dataSource = [];
  constructor(){}
  ngOnInit(): void {}
 }

 export class CoursesService {
  list(): Course[] {
  return [
  {_id: "1", name: "Curso Angular", category: "Front-end"}
  ];
  }
  constructor() { }
 }

 export class CoursesComponent implements OnInit {
  courses: Course[] = [];
  displayedColumns: string[] = ['name', 'category’];
  courseService: CoursesService;
  constructor(){
  this.courseService = new CoursesService();
  this.courses = this.courseService.list();
  }

