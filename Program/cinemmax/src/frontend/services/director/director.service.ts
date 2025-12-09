import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { DirectorDTO } from '../../../backend/classes/DTO/DirectorDTO/director-dto';

@Injectable({
  providedIn: 'root'
})
export class DirectorService {

  private readonly apiUrl: string = 'http://localhost:8080/api/directors';
  
    constructor(private http: HttpClient) { }
  
    findByInput(input: string): Observable<DirectorDTO[]> {
      return this.http.get<DirectorDTO[]>(`${this.apiUrl}/findByInput`, {
        params: { input: input}
      });
    }
  
    add(directorToSave: DirectorDTO): Observable<any> {
      return this.http.post<DirectorDTO>(`${this.apiUrl}/save`, directorToSave);
    }
}
