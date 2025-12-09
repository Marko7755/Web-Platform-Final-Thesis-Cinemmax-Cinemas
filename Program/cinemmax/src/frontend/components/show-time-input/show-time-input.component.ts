import { Component, OnInit } from '@angular/core';
import { AbstractControl, FormBuilder, FormGroup, ValidationErrors, Validators } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule } from '@angular/forms';
import { startWith, filter, tap, switchMap, Observable, of } from 'rxjs';
import { Cinema } from '../../../backend/classes/general/Cinema/cinema/cinema';
import { CinemaGroup, groupCinemasByLocation } from '../../functions/group-cinemas-by-function';
import { CinemaService } from '../../services/cinema/cinema.service';
import { HallService } from '../../services/hall/hall.service';
import { NotificationService } from '../../services/notification/notification.service';
import { Hall } from '../../../backend/classes/general/Hall/hall';
import { ShowTypeService } from '../../services/show-type/show-type.service';
import { ShowType } from '../../../backend/classes/general/ShowType/show-type';
import { ActivatedRoute } from '@angular/router';
import { ShowTime } from '../../../backend/classes/general/ShowTime/show-time';
import { ShowTimeService } from '../../services/show-time/show-time.service';

@Component({
  selector: 'app-show-time-input',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './show-time-input.component.html',
  styleUrl: './show-time-input.component.css'
})
export class ShowTimeInputComponent implements OnInit {

  showTimeForm: FormGroup;

  public allCinemas: Cinema[] = [];
  public groupedCinemas: CinemaGroup[] = [];

  public selectedCinemaId: number | null = null;
  public halls$: Observable<Hall[]> = of([]);
  public showTypes: ShowType[] = [];

  private filmId: number | null = null;

  constructor(private fb: FormBuilder, private cinemaService: CinemaService,
    private hallService: HallService, private notificationService: NotificationService,
    private showTypeService: ShowTypeService, private route: ActivatedRoute,
    private showTimeService: ShowTimeService) {

    this.showTimeForm = this.fb.group({
      cinemaId: [null, Validators.required],
      hallId: ['', Validators.required],
      typeId: ['', Validators.required],
      showTime: ['', [Validators.required, this.noPastDateTimeValidator]],
    })
  }
  ngOnInit(): void {
    const id = this.route.snapshot.paramMap.get('filmId');
    if (id) {
      this.filmId = +id;

      const cinemaIdCtrl = this.showTimeForm.get('cinemaId')!;
      const hallCtrl = this.showTimeForm.get('hallId')!;

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

      // Halls load lifecycle: disable while loading; enable only after we have data
      this.halls$ = cinemaIdCtrl.valueChanges.pipe(
        startWith(cinemaIdCtrl.value),
        filter((id) => id != null),
        tap(() => {
          // reset without emitting to avoid a spurious valueChanges while disabled
          hallCtrl.disable({ emitEvent: false });
          hallCtrl.setValue(null, { emitEvent: false });
          this.showTimeForm.updateValueAndValidity();
        }),
        switchMap((id) =>
          this.hallService.getAllByCinemaId(+id).pipe(
            tap((halls) => {
              if (halls.length > 0) {
                hallCtrl.enable({ emitEvent: false }); // enable, but don't emit
              } else {
                hallCtrl.disable({ emitEvent: false });
              }
              this.showTimeForm.updateValueAndValidity();
            })
          )
        )
      );


      this.showTypeService.getAll().subscribe({
        next: (types: ShowType[]) => {
          this.showTypes = types;
        },
        error: (err) => {
          this.notificationService.errorNotification("Error fetching show types", err.error?.error || "There was an error while fetching the show types!");
          console.error('Error fetching show types:', err);
        }
      })
    }





  }

  noPastDateTimeValidator(control: AbstractControl): ValidationErrors | null {
    const v = control.value;
    if (!v) return null;

    // input type="datetime-local" vraća string "YYYY-MM-DDTHH:mm"
    const selected = v instanceof Date ? v : new Date(v);
    if (isNaN(selected.getTime())) return { invalidDateTime: true };

    // Zaokruži "sada" na minutu (isti “granularity” kao input)
    const now = new Date();
    now.setSeconds(0, 0);

    return selected.getTime() < now.getTime() ? { pastDateTime: true } : null; // FutureOrPresent
  }


  submitShowTime() {
    if (this.showTimeForm.valid) {

      const showTimeToAdd: ShowTime = new ShowTime(
        this.filmId!,
        this.showTimeForm.get('hallId')?.value,
        this.showTimeForm.get('showTime')?.value,
        this.showTimeForm.get('typeId')?.value
      );


      console.log('Submitting show time:', showTimeToAdd);

      this.showTimeService.add(showTimeToAdd).subscribe({
        next: (res) => {
          this.notificationService.successNotification("Show time saved", res.message);
          this.showTimeForm.reset();
        },
        error: (err) => {
          if (err.status === 409) {
            this.notificationService.errorNotification("Conflict", err.error?.error);
            return;
          }
          this.notificationService.errorNotification("Error saving show time", err.error?.error || "There was an error while saving the show time!");
          console.error(err);
        }
      })
    }
  }

}
