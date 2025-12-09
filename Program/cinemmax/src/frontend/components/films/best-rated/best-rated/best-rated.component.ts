import { Component, OnInit } from '@angular/core';
import { FilmService } from '../../../../services/film/film.service';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { AuthService } from '../../../../../backend/services/auth.service';
import { Observable } from 'rxjs';
import { DecodedToken } from '../../../../../backend/interfaces/decoded-token';
import { NotificationService } from '../../../../services/notification/notification.service';
import { BestRatedFilmDTO } from '../../../../../backend/classes/DTO/FilmDTO/BestRated/best-rated-film-dto';

@Component({
  selector: 'app-best-rated',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './best-rated.component.html',
  styleUrl: './best-rated.component.css'
})
export class BestRatedComponent implements OnInit {
  bestRatedFilms: BestRatedFilmDTO[] = [];
  currentUser: DecodedToken | null | undefined;
  constructor(private filmService: FilmService, private authService: AuthService, private notificationService: NotificationService) { }

  ngOnInit() {

    this.authService.currentUser$.subscribe(user => {
      this.currentUser = user;
      this.filmService.getBestRatedReleased().subscribe(bestRated => this.bestRatedFilms = bestRated);

      if (this.currentUser?.role === 'admin') {
        this.filmService.getBestRatedForAdmin().subscribe(bestRated => this.bestRatedFilms = bestRated);
      }
      else {
        this.filmService.getBestRatedReleased().subscribe(bestRated => this.bestRatedFilms = bestRated);
      }

    });


  }


  deleteFilm(filmId: number): void {
    this.notificationService.confirmCancelNotification("delete this film", () => {
      this.filmService.delete(filmId).subscribe({
        next: () => {
          this.filmService.getBestRatedReleased().subscribe({
            next: (films: BestRatedFilmDTO[]) => {
              this.bestRatedFilms = films;
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

}
