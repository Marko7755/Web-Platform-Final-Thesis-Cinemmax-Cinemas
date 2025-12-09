import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { SeatPayload } from '../../../backend/classes/Payload/seat/seatPayload';
import { BehaviorSubject, Observable } from 'rxjs';
import { Seat } from '../../../backend/classes/general/Seat/seat';
import { SeatDTO } from '../../../backend/classes/DTO/SeatDTO/seat-dto';

@Injectable({
  providedIn: 'root'
})
export class SeatService {

  private readonly apiUrl: string = 'http://localhost:8080/api/seats';

  /* public selectSeatIdsBehavior: BehaviorSubject<number[]> = new BehaviorSubject<number[]>([]);
  public selectedSeatsObs$ = this.selectSeatIdsBehavior.asObservable(); */

  constructor(private http: HttpClient) { }

  add(seatToSave: SeatPayload): Observable<any> {
    return this.http.post<SeatPayload>(`${this.apiUrl}/save`, seatToSave);
  }

  getAll(): Observable<Seat[]> {
    return this.http.get<Seat[]>(`${this.apiUrl}/getAll`);
  }

  getByHallId(hallId: number): Observable<SeatDTO[]> {
    return this.http.get<SeatDTO[]>(`${this.apiUrl}/getAllByHallId/${hallId}`);
  }

  getById(id: number): Observable<Seat> {
    return this.http.get<Seat>(`${this.apiUrl}/getById/${id}`);
  }
}
