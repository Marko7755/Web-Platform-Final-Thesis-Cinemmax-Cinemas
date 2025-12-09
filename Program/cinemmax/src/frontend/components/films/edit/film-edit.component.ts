import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { FilmService } from '../../../services/film/film.service';
import { Film } from '../../../../backend/classes/general/Film/film/film';
import { AbstractControl, FormBuilder, FormGroup, ValidationErrors, Validators } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { ActorService } from '../../../services/actor/actor.service';
import { DirectorService } from '../../../services/director/director.service';
import { NotificationService } from '../../../services/notification/notification.service';
import { ActorDTO } from '../../../../backend/classes/DTO/ActorDTO/actor-dto';
import { DirectorDTO } from '../../../../backend/classes/DTO/DirectorDTO/director-dto';
import { FormsModule } from '@angular/forms';
import { ReactiveFormsModule } from '@angular/forms';
import { FilmFormBase } from '../../../../backend/classes/general/Film/FilmFormBase/film-form-base';

@Component({
  selector: 'app-film-edit',
  standalone: true,
  imports: [CommonModule, FormsModule, ReactiveFormsModule],
  templateUrl: './film-edit.component.html',
  styleUrl: './film-edit.component.css'
})
export class FilmEditComponent extends FilmFormBase implements OnInit {
  filmToEdit: Film | null = null;
  filmId: number | null = null;

  constructor(
    fb: FormBuilder,
    actorService: ActorService,
    directorService: DirectorService,
    notificationService: NotificationService,
    private filmService: FilmService,
    private route: ActivatedRoute
  ) {
    super(fb, actorService, directorService, notificationService);
  }

  ngOnInit(): void {
    ['cinema_release_date', 'cinema_end_date'].forEach(name => {
      const c = this.filmForm.get(name);
      c?.setValidators([Validators.required]);
      c?.updateValueAndValidity({ emitEvent: false })
    })


    const filmId = this.route.snapshot.paramMap.get('filmId');
    if (!filmId) {
      this.notificationService.errorNotification("Error", "Film ID is missing in the route parameters.");
      return;
    }
    this.filmId = +filmId;
    if (filmId) {
      this.filmService.getById(+filmId).subscribe({
        next: (filmRes: Film) => {
          this.filmId = filmId ? +filmId : null;
          /*           this.filmToEdit = filmRes; */
          this.filmForm.patchValue({
            name: filmRes.name,
            genre: filmRes.genre,
            about: filmRes.about,
            imdb_rating: filmRes.imdbRating,
            age_rate: filmRes.ageRate,
            duration: filmRes.duration,
            image_url: filmRes.imageUrl,
            trailer_url: filmRes.trailerUrl,
            cinema_release_date: this.toDateInputValue(filmRes.cinemaReleaseDate),
            cinema_end_date: this.toDateInputValue(filmRes.cinemaEndDate),
          });
          this.selectedActors = filmRes.actors || [];
          this.selectedDirectors = filmRes.directors || [];
          this.filmForm.updateValueAndValidity();
        },
        error: (err) => {
          console.error('Error fetching film for edit:', err);
        }
      });
    }
  }

  toDateInputValue(dateStr: string | Date): string {
    const date = new Date(dateStr);
    const offset = date.getTimezoneOffset();
    date.setMinutes(date.getMinutes() - offset);
    return date.toISOString().split('T')[0];
  }


  removeDirector(index: number): void {
    this.selectedDirectors.splice(index, 1);
    this.filmForm.updateValueAndValidity();
  }

  removeActor(index: number): void {
    this.selectedActors.splice(index, 1);
    this.filmForm.updateValueAndValidity();
  }


  editFilm() {
    if (this.filmForm.valid) {
      const film = new Film(
        this.filmForm.get('name')?.value,
        this.filmForm.get('genre')?.value,
        this.filmForm.get('about')?.value
      );
      film.id = this.filmId!;
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

      this.filmService.edit(this.filmId!, film).subscribe({
        next: (res) => {
          this.notificationService.successNotification("Film edit", res.message);
          this.selectedActors = [];
          this.selectedDirectors = [];
          this.actorInput = '';
          this.directorInput = '';
        },
        error: (err) => {
          this.notificationService.errorNotification("Error editing film", err.error.error || "There was an error while editing the film!");
          console.error(err);
        }
      });
    }
  }

}
