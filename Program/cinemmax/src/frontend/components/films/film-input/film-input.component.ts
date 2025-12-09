import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule, AbstractControl, ValidationErrors } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ActorService } from '../../../services/actor/actor.service';
import { Film } from '../../../../backend/classes/general/Film/film/film';
import { NotificationService } from '../../../services/notification/notification.service';
import { DirectorService } from '../../../services/director/director.service';
import { FilmService } from '../../../services/film/film.service';
import { FilmFormBase } from '../../../../backend/classes/general/Film/FilmFormBase/film-form-base';

@Component({
  selector: 'app-film-input',
  standalone: true,
  imports: [ReactiveFormsModule, CommonModule, FormsModule],
  templateUrl: './film-input.component.html',
  styleUrl: './film-input.component.css'
})
export class FilmInputComponent extends FilmFormBase {

  constructor(
    fb: FormBuilder,
    actorService: ActorService,
    directorService: DirectorService,
    notificationService: NotificationService,
    private filmService: FilmService
  ) {
    super(fb, actorService, directorService, notificationService);
  }



  submitFilm() {
    if (this.filmForm.valid) {
      const film = new Film(
        this.filmForm.get('name')?.value,
        this.filmForm.get('genre')?.value,
        this.filmForm.get('about')?.value
      );
      film.imdbRating = this.filmForm.get('imdb_rating')?.value;
      film.ageRate = this.filmForm.get('age_rate')?.value;
      film.duration = this.filmForm.get('duration')?.value;
      film.trailerUrl = this.filmForm.get('trailer_url')?.value;
      film.cinemaReleaseDate = this.filmForm.get('cinema_release_date')?.value;
      film.cinemaEndDate = this.filmForm.get('cinema_end_date')?.value;
      film.actors = this.selectedActors;
      film.directors = this.selectedDirectors;
      film.imageUrl = this.filmForm.get('image_url')?.value;

      console.log('Created Film object:', film);

      this.filmService.add(film).subscribe({
        next: (res) => {
          console.log('Film saved successfully:', res);
          this.notificationService.successNotification("Film saved", res.message);
          this.selectedActors = [];
          this.selectedDirectors = [];
          this.actorInput = '';
          this.directorInput = '';
        },
        error: (err) => {
          if(err.status === 409) {
            this.notificationService.errorNotification("Conflict", err.error?.error);
            return;
          }
          this.notificationService.errorNotification("Error saving film", "There was an error while saving the film!");
          console.error(err);
        }
      });
    }
  }

}
