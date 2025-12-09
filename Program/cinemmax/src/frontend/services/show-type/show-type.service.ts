import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { ShowType } from '../../../backend/classes/general/ShowType/show-type';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ShowTypeService {
  private readonly apiUrl: string = 'http://localhost:8080/api/showTypes';
  constructor(private http: HttpClient) { }

  add(showTypeToSave: ShowType): Observable<any> {
    return this.http.post<ShowType>(`${this.apiUrl}/save`, showTypeToSave);
  }

  getAll(): Observable<ShowType[]> {
    return this.http.get<ShowType[]>(`${this.apiUrl}/getAll`);
  }

  getById(id: number): Observable<ShowType> {
    return this.http.get<ShowType>(`${this.apiUrl}/getById/${id}`);
  }

}
