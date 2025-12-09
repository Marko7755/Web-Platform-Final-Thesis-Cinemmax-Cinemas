import { FormBuilder, FormGroup, Validators, AbstractControl, ValidationErrors } from '@angular/forms';
import { ActorService } from '../../../../../frontend/services/actor/actor.service';
import { DirectorService } from '../../../../../frontend/services/director/director.service';
import { NotificationService } from '../../../../../frontend/services/notification/notification.service';
import { ActorDTO } from '../../../DTO/ActorDTO/actor-dto';
import { DirectorDTO } from '../../../DTO/DirectorDTO/director-dto';

export abstract class FilmFormBase {
  filmForm: FormGroup;

  actorInput: string = '';
  actorSuggestions: ActorDTO[] = [];
  selectedActors: ActorDTO[] = [];

  directorInput: string = '';
  directorSuggestions: DirectorDTO[] = [];
  selectedDirectors: DirectorDTO[] = [];

  constructor(
    protected fb: FormBuilder,
    protected actorService: ActorService,
    protected directorService: DirectorService,
    protected notificationService: NotificationService
  ) {
    this.filmForm = this.fb.group({
      name: ['', Validators.required],
      genre: ['', Validators.required],
      about: ['', Validators.required],
      age_rate: ['', [Validators.required, Validators.min(0)]],
      imdb_rating: ['', [Validators.required, Validators.min(0), Validators.max(10)]],
      duration: ['', [Validators.required, Validators.min(1)]],
      image_url: ['', [Validators.required, Validators.pattern(/https?:\/\/.+/)]],
      trailer_url: ['', [Validators.required, Validators.pattern(/https?:\/\/.+/)]],
      cinema_release_date: ['', [Validators.required, this.noPastDateValidator]],
      cinema_end_date: ['', [Validators.required, this.noPastDateValidator]]
    }, {
      validators: [
        this.endAfterStartValidator,
        this.atLeastOneActorAndDirectorValidator.bind(this)
      ]
    });

  }

  noPastDateValidator(control: AbstractControl): ValidationErrors | null {
    const value = control.value;
    if (!value) return null;
    const selectedDate = new Date(value);
    const today = new Date();
    today.setHours(0, 0, 0, 0);
    return selectedDate < today ? { pastDate: true } : null;
  }

  endAfterStartValidator(group: AbstractControl): ValidationErrors | null {
    const start = new Date(group.get('cinema_release_date')?.value);
    const end = new Date(group.get('cinema_end_date')?.value);
    if (!start || !end || isNaN(start.getDate()) || isNaN(end.getDate())) return null;
    return end >= start ? null : { beforeStart: true };
  }

  atLeastOneActorAndDirectorValidator(group: AbstractControl): ValidationErrors | null {
    const errors: ValidationErrors = {};

    if (this.selectedActors.length === 0) {
      errors['noActor'] = true;
    }

    if (this.selectedDirectors.length === 0) {
      errors['noDirector'] = true;
    }

    return Object.keys(errors).length ? errors : null;
  }




  searchActors() {
    if (this.actorInput.trim().length === 0) {
      this.actorSuggestions = [];
      return;
    }

    this.actorService.findByInput(this.actorInput).subscribe({
      next: (actors) => {
        this.actorSuggestions = actors.map(actor => new ActorDTO(actor.name, actor.surname));
        /* this.actorSuggestions = actors; -> this saves actors as default object, not ActorDTO, manual convert to ActorDTO is needed*/
      }

      ,
      error: (err) => {
        console.error('Error fetching actor suggestions:', err);
        this.actorSuggestions = [];
      }
    });
  }

  /* saveActor(actorToSave: ActorDTO): void {
    this.actorService.add(actorToSave).subscribe({
      next: (res) => {
        console.log('Actor added successfully:', res);
        this.selectedActors.push(actorToSave);
        this.filmForm.updateValueAndValidity();
      },
      error: (err) => {
        console.error(err);
        this.notificationService.errorNotification("Error saving a new Actor", "There was an error while saving a new Actor!");
      }
    })
  } */

  addActor() {
    const input = this.actorInput.trim();
    if (!input) return;
    const foundActor = this.actorSuggestions.find(actor => `${actor.name} ${actor.surname}`.toLowerCase() === input.toLowerCase());
    //console.log(foundActor);

    if (!foundActor) {
      const actorInput = this.actorInput;
      const parts = actorInput.split(' ').filter(part => part.length > 0);

      if (parts.length < 2) {
        this.notificationService.warningNotification("Wrong actor format", "Actor has to have a name and a surname!");
        return;
      }
      const name = parts[0];
      const surname = parts[1];
      const actorToSave = new ActorDTO(name, surname);

      /* console.log(this.selectedActors);
      console.log(this.actorSuggestions); */

      if (!this.checkIfAlreadyAdded(actorToSave)) {
        this.selectedActors.push(actorToSave);
        this.filmForm.updateValueAndValidity();
        /* this.notificationService.confirmCancelNotification("add this actor to DB", () => { this.saveActor(actorToSave) }
        ); */
      }
      return;
    }

    if (!this.checkIfAlreadyAdded(foundActor)) {
      this.selectedActors.push(foundActor);
      this.filmForm.updateValueAndValidity();
    }

  }

  checkIfAlreadyAdded(valueToCheck: ActorDTO | DirectorDTO): boolean {
    if (valueToCheck instanceof ActorDTO) {
      const alreadyAdded = this.selectedActors.some(
        actor => actor.name === valueToCheck.name && actor.surname === valueToCheck.surname
      );
      if (alreadyAdded) {
        this.notificationService.warningNotification("Duplicate Actor", "This actor for this film already exists!");
        return true;
      }
    }
    else {
      const alreadyAdded = this.selectedDirectors.some(
        director => director.name === valueToCheck.name && director.surname === valueToCheck.surname
      );
      if (alreadyAdded) {
        this.notificationService.warningNotification("Duplicate Director", "This director for this film already exists!");
        return true;
      }
    }
    return false;
  }

  searchDirectors() {
    if (this.directorInput.trim().length === 0) {
      this.directorSuggestions = [];
      return;
    }

    this.directorService.findByInput(this.directorInput).subscribe({
      next: (directors) => {
        this.directorSuggestions = directors;
        /* console.log(directors); */
      }

      ,
      error: (err) => {
        console.error('Error fetching director suggestions:', err);
        this.directorSuggestions = [];
      }
    });
  }

  /*  saveDirector(directorToSave: DirectorDTO): void {
     this.directorService.add(directorToSave).subscribe({
       next: (res) => {
         console.log('Director added successfully:', res);
         this.selectedDirectors.push(directorToSave);
         this.filmForm.updateValueAndValidity();
       },
       error: (err) => {
         console.error(err);
         this.notificationService.errorNotification("Error saving a new Director", "There was an error while saving a new Director!");
       }
     })
   } */

  addDirector() {
    const input = this.directorInput.trim();
    if (!input) return;

    const foundDirector = this.directorSuggestions.find(director => `${director.name} ${director.surname}`.toLowerCase() === input.toLowerCase());
    //console.log(foundActor);

    if (!foundDirector) {
      const directorInput = this.directorInput;
      const parts = directorInput.split(' ').filter(part => part.length > 0);

      if (parts.length < 2) {
        this.notificationService.warningNotification("Wrong director format", "Director has to have a name and a surname!");
        return;
      }
      const name = parts[0];
      const surname = parts[1];
      const directorToSave = new DirectorDTO(name, surname);

      /* console.log(this.selectedActors);
      console.log(this.actorSuggestions); */

      if (!this.checkIfAlreadyAdded(directorToSave)) {
        this.selectedDirectors.push(directorToSave);
        this.filmForm.updateValueAndValidity();
        /* this.notificationService.confirmCancelNotification("add this director to DB", () => { this.saveDirector(directorToSave) }
        ); */
      }

      return;
    }

    if (!this.checkIfAlreadyAdded(foundDirector)) {
      this.selectedDirectors.push(foundDirector);
      this.filmForm.updateValueAndValidity();
    }
  }
}
