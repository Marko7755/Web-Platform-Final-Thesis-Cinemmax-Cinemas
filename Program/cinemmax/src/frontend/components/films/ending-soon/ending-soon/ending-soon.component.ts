import { Component, OnInit } from '@angular/core';
import { FilmService } from '../../../../services/film/film.service';
import { FilmPreviewDTO } from '../../../../../backend/classes/DTO/FilmDTO/Preview/film-preview-dto';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { AuthService } from '../../../../../backend/services/auth.service';
import { Observable } from 'rxjs';
import { DecodedToken } from '../../../../../backend/interfaces/decoded-token';
import { NotificationService } from '../../../../services/notification/notification.service';


@Component({
  selector: 'app-ending-soon',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './ending-soon.component.html',
  styleUrl: './ending-soon.component.css'
})
export class EndingSoonComponent implements OnInit {
  endingSoonFilms: FilmPreviewDTO[] = [];
  currentUser: Observable<DecodedToken | null> | undefined;
  constructor(private authService: AuthService, private filmService: FilmService, private notificationService: NotificationService) { }

  ngOnInit(): void {
    this.currentUser = this.authService.currentUser$;

    this.filmService.getEndingIn10Days().subscribe({
      next: (films: FilmPreviewDTO[]) => {
        this.endingSoonFilms = films;
      }
    });
  }

  deleteFilm(filmId: number): void {
    this.notificationService.confirmCancelNotification("delete this film", () => {
      this.filmService.delete(filmId).subscribe({
        next: () => {
          this.filmService.getUpcoming().subscribe({
            next: (films: FilmPreviewDTO[]) => {
              this.endingSoonFilms = films;
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
