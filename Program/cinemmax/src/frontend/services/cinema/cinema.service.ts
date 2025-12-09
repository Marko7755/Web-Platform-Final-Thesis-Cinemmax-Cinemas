import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Cinema } from '../../../backend/classes/general/Cinema/cinema/cinema';
import { BehaviorSubject, Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class CinemaService {

  private readonly apiUrl: string = 'http://localhost:8080/api/cinemas';

  private selectedCinemmaxNameSubject = new BehaviorSubject<string | null>(null);
  selectedCinemmaxName$ = this.selectedCinemmaxNameSubject.asObservable();

  constructor(private http: HttpClient) { }


  setSelectedCinemaName(name: string | null): void {
    this.selectedCinemmaxNameSubject.next(name);
  }

  getSelectedCinemaName(): string | null {
    return this.selectedCinemmaxNameSubject.value;
  }

  add(cinemaToSave: Cinema): Observable<any> {
    return this.http.post<any>(`${this.apiUrl}/save`, cinemaToSave);
  }

  getAll(): Observable<Cinema[]> {
    return this.http.get<Cinema[]>(`${this.apiUrl}/getAll`);
  }

  getById(id: number): Observable<Cinema> {
    return this.http.get<Cinema>(`${this.apiUrl}/getById/${id}`);
  }

  edit(id: number, cinemaToEdit: Cinema): Observable<any> {
    return this.http.put<any>(`${this.apiUrl}/edit/${id}`, cinemaToEdit);
  }

  delete(id: number): Observable<any> {
    return this.http.delete<Cinema>(`${this.apiUrl}/delete/${id}`);
  }

}
