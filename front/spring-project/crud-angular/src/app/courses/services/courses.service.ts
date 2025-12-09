import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Course } from '../model/course';
import { first, tap } from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class CoursesService {

  private readonly API = '/api/courses';

  constructor(private httpClient: HttpClient) { }

  list() {
    return this.httpClient.get<Course[]>(this.API)
      .pipe(
        first(), // Pega a primeira resposta e encerra a inscrição
        tap(courses => console.log(courses)) // Mostra no console para debug
      );
  }

  save(record: Partial<Course>) {
    return this.httpClient.post<Course>(this.API, record);
  }
}
