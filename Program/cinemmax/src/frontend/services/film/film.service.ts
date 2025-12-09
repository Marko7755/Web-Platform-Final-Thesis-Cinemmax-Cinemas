import { Injectable } from '@angular/core';
import { Film } from '../../../backend/classes/general/Film/film/film';
import { Observable } from 'rxjs';
import { HttpClient, HttpParams } from '@angular/common/http';
import { FilmPreviewDTO } from '../../../backend/classes/DTO/FilmDTO/Preview/film-preview-dto';
import { BestRatedFilmDTO } from '../../../backend/classes/DTO/FilmDTO/BestRated/best-rated-film-dto';

@Injectable({
  providedIn: 'root'
})
export class FilmService {

  constructor(private http: HttpClient) { }

  private readonly apiUrl: string = 'http://localhost:8080/api/film';

  parseDate(dateString: string): Date {
    return new Date(dateString);
  }


  add(filmToSave: Film): Observable<any> {
    return this.http.post<Film>(`${this.apiUrl}/save`, filmToSave);
  }

  getAllForAdmin(): Observable<FilmPreviewDTO[]> {
    return this.http.get<FilmPreviewDTO[]>(`${this.apiUrl}/getAllForAdmin`);
  }

  getAllReleased(): Observable<FilmPreviewDTO[]> {
    return this.http.get<FilmPreviewDTO[]>(`${this.apiUrl}/getAllReleased`);
  }

  getUpcoming(): Observable<FilmPreviewDTO[]> {
    return this.http.get<FilmPreviewDTO[]>(`${this.apiUrl}/getUpcoming`);
  }

  getById(id: number): Observable<Film> {
    return this.http.get<Film>(`${this.apiUrl}/getById/${id}`);
  }

  edit(id: number, filmToEdit: Film): Observable<any> {
    return this.http.put<Film>(`${this.apiUrl}/edit/${id}`, filmToEdit);
  }

  delete(id: number): Observable<any> {
    return this.http.delete<Film>(`${this.apiUrl}/delete/${id}`);
  }

  getEndingIn10Days(): Observable<FilmPreviewDTO[]> {
    return this.http.get<FilmPreviewDTO[]>(`${this.apiUrl}/getEndingIn10Days`);
  }

  getBestRatedForAdmin(): Observable<BestRatedFilmDTO[]> {
    return this.http.get<BestRatedFilmDTO[]>(`${this.apiUrl}/getBestRatedForAdmin`);
  }

  getBestRatedReleased(): Observable<BestRatedFilmDTO[]> {
    return this.http.get<BestRatedFilmDTO[]>(`${this.apiUrl}/getBestRatedReleased`);
  }

  getAllGenres(): Observable<string[]> {
    return this.http.get<string[]>(`${this.apiUrl}/getAllGenres`);
  }

  getAllByGenreForAdmin(genre: string): Observable<FilmPreviewDTO[]> {
    return this.http.get<FilmPreviewDTO[]>(`${this.apiUrl}/getAllByGenreForAdmin`, {
      params: new HttpParams().set('genre', genre)
    });
  }

  getAllByGenre(genre: string): Observable<FilmPreviewDTO[]> {
    return this.http.get<FilmPreviewDTO[]>(`${this.apiUrl}/getAllByGenre`, {
      params: new HttpParams().set('genre', genre)
    });

  }

}
