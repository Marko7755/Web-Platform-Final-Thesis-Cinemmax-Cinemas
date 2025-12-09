import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { ReservationMinDetails } from '../../../backend/classes/DTO/ReservationDTO/MinDetails/reservation-min-details';
import { ReservationFullDetails } from '../../../backend/classes/DTO/ReservationDTO/FullDetails/reservation-full-details';

@Injectable({
  providedIn: 'root'
})
export class ReservationService {
  private apiUrl = 'http://localhost:8080/api/reservations';

  constructor(private http: HttpClient) { }


  getMinDetails(): Observable<ReservationMinDetails[]> {
    return this.http.get<ReservationMinDetails[]>(`${this.apiUrl}/getMinDetails`);
  }

  getFullDetails(reservationId: number): Observable<ReservationFullDetails> {
    return this.http.get<ReservationFullDetails>(`${this.apiUrl}/getFullDetails/${reservationId}`);
  }



}
