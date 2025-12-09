import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { ActorDTO } from '../../../backend/classes/DTO/ActorDTO/actor-dto';
import { HttpClient } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class ActorService {
  private readonly apiUrl: string = 'http://localhost:8080/api/actors';

  constructor(private http: HttpClient) { }

  findByInput(input: string): Observable<ActorDTO[]> {
    return this.http.get<ActorDTO[]>(`${this.apiUrl}/findByInput`, {
      params: { input: input}
    });
  }

  add(actorToSave: ActorDTO): Observable<any> {
    return this.http.post<ActorDTO>(`${this.apiUrl}/save`, actorToSave);
  }


}
