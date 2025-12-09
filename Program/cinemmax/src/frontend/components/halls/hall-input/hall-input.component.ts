import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { NotificationService } from '../../../services/notification/notification.service';
import { CommonModule } from '@angular/common';
import { CinemaService } from '../../../services/cinema/cinema.service';
import { Cinema } from '../../../../backend/classes/general/Cinema/cinema/cinema';
import { ReactiveFormsModule } from '@angular/forms';
import { Hall } from '../../../../backend/classes/general/Hall/hall';
import { HallService } from '../../../services/hall/hall.service';
import { groupCinemasByLocation, CinemaGroup } from '../../../functions/group-cinemas-by-function';


@Component({
  selector: 'app-hall-input',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './hall-input.component.html',
  styleUrl: './hall-input.component.css'
})
export class HallInputComponent implements OnInit {
  public allCinemas: Cinema[] = [];
  public groupedCinemas: CinemaGroup[] = [];
  public hallForm: FormGroup;


  constructor(private fb: FormBuilder, private cinemaService: CinemaService, private notificationService: NotificationService, private hallService: HallService) {
    this.hallForm = this.fb.group({
      cinemaId: [null, [Validators.required]],
      hallNumber: [
        null as number | null,
        [Validators.required, Validators.min(1), Validators.pattern(/^[0-9]+$/)],
      ],
      capacity: [
        null as number | null,
        [
          Validators.required,
          Validators.min(1),
          Validators.max(1000),
          Validators.pattern(/^[0-9]+$/),
        ],
      ],
    });
  }
  ngOnInit(): void {
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



  public submitHall() {
    if (this.hallForm.valid) {
      const hallToAdd: Hall = new Hall(
        this.hallForm.get('cinemaId')?.value,
        this.hallForm.get('hallNumber')?.value,
        this.hallForm.get('capacity')?.value
      );
      this.hallService.add(hallToAdd).subscribe({
         next: (res) => {
          this.notificationService.successNotification("Hall saved", res.message);
          this.hallForm.reset();
        },
        error: (err) => {
          if(err.status === 409) {
            this.notificationService.errorNotification("Conflict", err.error?.error);
            return;
          }
          this.notificationService.errorNotification("Error saving hall", "There was an error while saving the hall!");
          console.error(err);
        }
      });
    }
  }



}
