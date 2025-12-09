import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Hall } from '../../../backend/classes/general/Hall/hall';

@Injectable({
  providedIn: 'root'
})
export class HallService {

  private readonly apiUrl: string = 'http://localhost:8080/api/halls';
    constructor(private http: HttpClient) { }
  
  
    add(hallToSave: Hall): Observable<any> {
      return this.http.post<Hall>(`${this.apiUrl}/save`, hallToSave);
    }
  
    getAll(): Observable<Hall[]> {
      return this.http.get<Hall[]>(`${this.apiUrl}/getAll`);
    }
  
    getById(id: number): Observable<Hall> {
      return this.http.get<Hall>(`${this.apiUrl}/getById/${id}`);
    }

    getAllByCinemaId(cinemaId: number): Observable<Hall[]> {
      return this.http.get<Hall[]>(`${this.apiUrl}/getAllByCinemaId/${cinemaId}`);
    }
  
  
}
