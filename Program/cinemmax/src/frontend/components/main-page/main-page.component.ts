import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FilmService } from '../../services/film/film.service';
import { Film } from '../../../backend/classes/general/Film/film/film';
import { RouterLink } from '@angular/router';
import { AuthService } from '../../../backend/services/auth.service';
import { Observable, of } from 'rxjs';
import { DecodedToken } from '../../../backend/interfaces/decoded-token';
import { NotificationService } from '../../services/notification/notification.service';
import { Cinema } from '../../../backend/classes/general/Cinema/cinema/cinema';
import { CinemaService } from '../../services/cinema/cinema.service';
import { FormsModule } from '@angular/forms';
import { CinemaGroup, groupCinemasByLocation } from '../../functions/group-cinemas-by-function';
import { FilmPreviewDTO } from '../../../backend/classes/DTO/FilmDTO/Preview/film-preview-dto';

@Component({
  selector: 'app-main-page',
  standalone: true,
  imports: [CommonModule, RouterLink, FormsModule],
  templateUrl: './main-page.component.html',
  styleUrl: './main-page.component.css'
})
export class MainPageComponent implements OnInit {
  upcomingFilms: FilmPreviewDTO[] = [];
  /* currentUser: Observable<DecodedToken | null> | undefined; */
  currentUser: DecodedToken | null | undefined;
  allCinemas: Cinema[] = [];
  groupedCinemas: CinemaGroup[] = [];
  selectedCinemmaxName: string | null = null;
  allGenres: string[] = [];
  selectedGenre: string | null = null;
  currentDate: Date = new Date();

  constructor(private filmService: FilmService, private authService: AuthService,
    private notificationService: NotificationService, public cinemaService: CinemaService) { }

  parseToDate(date: string) {
    return this.filmService.parseDate(date);
  }

  public allFilms: FilmPreviewDTO[] = [];
  ngOnInit(): void {
    this.authService.currentUser$.subscribe(user => {
      this.currentUser = user;
      if (user?.role === 'admin') {
        this.filmService.getAllForAdmin().subscribe(films => this.allFilms = films);
      }
      else {
        this.filmService.getAllReleased().subscribe(films => this.allFilms = films);
      }
    })


    this.filmService.getUpcoming().subscribe(upcomingFilms => {
      this.upcomingFilms = upcomingFilms;

      if (typeof window !== 'undefined') {
        for (const film of this.upcomingFilms) {
          const img = new Image();
          img.src = film.imageUrl;
        }
      }
    });
    this.filmService.getAllGenres().subscribe(genres => this.allGenres = genres);


    this.cinemaService.getAll().subscribe({
      next: (cinemas: Cinema[]) => {
        this.allCinemas = cinemas;
      },
      error: (err) => {
        this.notificationService.errorNotification("Error fetching cinemas", err.error?.error || "There was an error while fetching the cinemas!");
        console.error('Error fetching cinemas:', err);
      }
    })

    this.cinemaService.getAll().subscribe({
      next: (cinemas: Cinema[]) => {
        this.allCinemas = cinemas;
        this.groupedCinemas = groupCinemasByLocation(cinemas);
        this.groupedCinemas.sort((a, b) => a.location.localeCompare(b.location));
        this.groupedCinemas.forEach(group => group.cinemas.sort((a, b) => a.name.localeCompare(b.name)));
      },
      error: (err) => {
        this.notificationService.errorNotification("Error fetching cinemas", err.error?.error || "There was an error while fetching the cinemas!");
        console.error('Error fetching cinemas:', err);
      }
    });


  }


  compareCinema = (a: Cinema | null, b: Cinema | null) => !!a && !!b && a.id === b.id;
  trackById = (_: number, c: Cinema) => c.id;

  onCinemaChange(val: string | null) {
    if (val === null) return;
    this.cinemaService.setSelectedCinemaName(val);
  }


  currentIndex: number = 0;

  isFading = false;

  prevSlide(): void {
    this.fadeToSlide((this.currentIndex - 1 + this.upcomingFilms.length) % this.upcomingFilms.length);
  }

  nextSlide(): void {
    this.fadeToSlide((this.currentIndex + 1) % this.upcomingFilms.length);
  }

  fadeToSlide(newIndex: number): void {
    this.isFading = true;
    setTimeout(() => {
      this.currentIndex = newIndex;
    }, 150);
  }

  onImageLoad(): void {
    setTimeout(() => {
      this.isFading = false;
    }, 50);
  }


  get currentMovie(): FilmPreviewDTO {
    return this.upcomingFilms[this.currentIndex];
  }


  deleteFilm(filmId: number): void {
    this.notificationService.confirmCancelNotification("delete this film", () => {
      this.filmService.delete(filmId).subscribe({
        next: () => {
          this.filmService.getAllReleased().subscribe({
            next: (films: FilmPreviewDTO[]) => {
              this.allFilms = films;
              this.filmService.getUpcoming().subscribe(upcomingFilms => {
                this.upcomingFilms = upcomingFilms;
              })
            }
          });
        },
        error: (err) => {
          this.notificationService.errorNotification("Error deleting film", "There was an error while deleting the film!");
          console.error('Error deleting film:', err);
        }
      })
    })

  }


  onGenreChange(genre: string | null): void {
    if (genre != 'All genres') {
      if(this.currentUser?.role === 'admin') {
        this.filmService.getAllByGenreForAdmin(genre || '').subscribe(films => this.allFilms = films);
      }
      else {
        this.filmService.getAllByGenre(genre || '').subscribe(films => this.allFilms = films);
      }
      
    }
    else {
      if (this.currentUser?.role === 'admin') {
        this.filmService.getAllForAdmin().subscribe(films => this.allFilms = films);
      }
      else {
        this.filmService.getAllReleased().subscribe(films => this.allFilms = films);
      }

    }

  }


}
