import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { ShowTime } from '../../../backend/classes/general/ShowTime/show-time';
import { Observable } from 'rxjs';
import { ShowTimeInterface } from '../../../backend/interfaces/show-time';
import { ShowTimeDetailsDTO } from '../../../backend/classes/DTO/ShowTimeDetailsDTO/show-time-details-dto';
import { SeatDTO } from '../../../backend/classes/DTO/SeatDTO/seat-dto';

@Injectable({
  providedIn: 'root'
})
export class ShowTimeService {

  constructor(private http: HttpClient) { }

  private readonly apiUrl: string = 'http://localhost:8080/api/showTimes';

  add(showTimeToSave: ShowTime): Observable<any> {
    return this.http.post<ShowTime>(`${this.apiUrl}/save`, showTimeToSave);
  }

  getAllByFilmId(filmId: number): Observable<ShowTimeInterface[]> {
    return this.http.get<ShowTimeInterface[]>(`${this.apiUrl}/getAllByFilmId/${filmId}`);
  }

  getAllByCinemaLocation(location: string): Observable<ShowTime[]> {
    return this.http.get<ShowTime[]>(`${this.apiUrl}/getAllByLocation`, { params: { location } });
  }

  getAllByFilmIdAndCinemaLocation(id: number, location: string): Observable<ShowTimeInterface[]> {
    return this.http.get<ShowTimeInterface[]>(`${this.apiUrl}/getAllByFilmIdAndLocation/${id}`,
       { params: { location } });
  }

  getDetails(id: number): Observable<ShowTimeDetailsDTO> {
    return this.http.get<ShowTimeDetailsDTO>(`${this.apiUrl}/getShowTimeDetails/${id}`);
  }

  getSeatsForShowTime(id: number): Observable<SeatDTO[]> {
    return this.http.get<SeatDTO[]>(`${this.apiUrl}/getSeatsForShowTime/${id}`);
  }

}
