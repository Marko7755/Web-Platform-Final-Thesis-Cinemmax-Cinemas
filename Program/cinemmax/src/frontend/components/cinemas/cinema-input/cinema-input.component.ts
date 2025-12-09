import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators, AbstractControl, ValidationErrors } from '@angular/forms';
import { Cinema } from '../../../../backend/classes/general/Cinema/cinema/cinema';
import { CinemaService } from '../../../services/cinema/cinema.service';
import { NotificationService } from '../../../services/notification/notification.service';
import { CinemaFormBase } from '../../../../backend/classes/general/Cinema/CinemaFormBase/cinema-form-base';

@Component({
  selector: 'app-cinema-input',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './cinema-input.component.html',
  styleUrl: './cinema-input.component.css'
})
export class CinemaInputComponent extends CinemaFormBase {

  constructor(fb: FormBuilder, private cinemaService: CinemaService, private notificationService: NotificationService) {
    super(fb);
  }


  submitCinema() {
    if (this.cinemaForm.valid) {
      const location = this.cinemaForm.get('location')?.value.trim();
      const name = this.cinemaForm.get('name')?.value.trim();
      const cinemaToAdd = new Cinema(location, name);
      this.cinemaService.add(cinemaToAdd).subscribe({
        next: (res) => {
          this.notificationService.successNotification("Cinema saved", res.message);
          this.cinemaForm.reset();
        },
        error: (err) => {
          if(err.status === 409) {
            this.notificationService.errorNotification("Conflict", err.error?.error);
            return;
          }
          this.notificationService.errorNotification("Error saving film", "There was an error while saving the cinema!");
          console.error(err);
        }
      })
    }
  }
}
