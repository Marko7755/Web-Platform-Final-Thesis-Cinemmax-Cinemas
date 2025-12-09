import { Component, OnInit } from '@angular/core';
import { CinemaService } from '../../../services/cinema/cinema.service';
import { HallService } from '../../../services/hall/hall.service';
import { NotificationService } from '../../../services/notification/notification.service';
import { FormGroup, FormBuilder, Validators, AbstractControl } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule } from '@angular/forms';
import { groupCinemasByLocation, CinemaGroup } from '../../../functions/group-cinemas-by-function';
import { Cinema } from '../../../../backend/classes/general/Cinema/cinema/cinema';
import { FormsModule } from '@angular/forms';
import { catchError, distinctUntilChanged, filter, map, Observable, of, shareReplay, startWith, switchMap, tap } from 'rxjs';
import { Hall } from '../../../../backend/classes/general/Hall/hall';
import { SeatPayload } from '../../../../backend/classes/Payload/seat/seatPayload';
import { SeatService } from '../../../services/seat/seat.service';
import { SeatDTO } from '../../../../backend/classes/DTO/SeatDTO/seat-dto';
import { start } from 'repl';

@Component({
  selector: 'app-seat-input',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, FormsModule],
  templateUrl: './seat-input.component.html',
  styleUrl: './seat-input.component.css'
})
export class SeatInputComponent implements OnInit {
  public seatForm: FormGroup;
  public allCinemas: Cinema[] = [];
  public groupedCinemas: CinemaGroup[] = [];

  public selectedCinemaId: number | null = null;
  public halls$: Observable<Hall[]> = of([]);
  existingSeats$!: Observable<{ rows: string[]; cols: number; set: Set<string> }>;

  public totalRows: string[] = [];
  public totalSeats: number[] = [];

  private hallsCache: Hall[] = [];

  public seatsPerRow: number = 0;

  constructor(private fb: FormBuilder, private cinemaService: CinemaService,
    private hallService: HallService, private notificationService: NotificationService,
    private seatService: SeatService) {

    this.seatForm = this.fb.group({
      cinemaId: [null, [Validators.required]],
      hallId: [{ value: null, disabled: true }, Validators.required],
      startRow: ['', [Validators.required, Validators.pattern(/^[A-Za-z]$/)]],
      endRow: ['', [Validators.required, Validators.pattern(/^[A-Za-z]$/)]],
      seatsPerRow: [null, [Validators.required, Validators.min(1), Validators.max(20), Validators.pattern(/^[0-9]+$/)]]
    },
      {
        validators: [this.capacityWithinHall()],
      }
    );
  }

  ngOnInit(): void {
    const cinemaIdCtrl = this.seatForm.get('cinemaId')!;
    const hallCtrl = this.seatForm.get('hallId')!;

    // cinemas (unchanged)
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
        this.hallsCache = [];
        this.seatForm.updateValueAndValidity();
      }),
      switchMap((id) =>
        this.hallService.getAllByCinemaId(+id).pipe(
          tap((halls) => {
            this.hallsCache = halls;
            if (halls.length > 0) {
              hallCtrl.enable({ emitEvent: false }); // enable, but don't emit
            } else {
              hallCtrl.disable({ emitEvent: false });
            }
            this.seatForm.updateValueAndValidity();
          })
        )
      )
    );

    // Seats for selected hall
    this.existingSeats$ = hallCtrl.valueChanges.pipe(
      startWith(hallCtrl.value),
      // coerce to number; ignore null/undefined/empty string
      map((v) => (v === '' || v == null ? null : +v)),
      filter((hallId): hallId is number => typeof hallId === 'number' && !Number.isNaN(hallId)),
      distinctUntilChanged(),
      switchMap((hallId) =>
        this.seatService.getByHallId(hallId).pipe(
          catchError((err) => {
            console.error('Error fetching seats for hall', err);
            return of<SeatDTO[]>([]);
          })
        )
      ),
      map((seats: SeatDTO[]) => {
        const rows = Array.from(new Set(
          seats.map(s => (s.rowLetter ?? '').toString().toUpperCase())
        )).filter(Boolean).sort(); // ['A','B','C']

        const cols = seats.reduce((m, s) => Math.max(m, s.seatNumber || 0), 0);

        const set = new Set(
          seats.map(s => `${(s.rowLetter ?? '').toString().toUpperCase()}-${s.seatNumber}`)
        );
        return { rows, cols, set }; // used in template
      }),
      shareReplay(1)
    );
  }







  private capacityWithinHall() {
    return (group: AbstractControl) => {
      const hallId = group.get('hallId')?.value as number | null;
      const startRowRaw = group.get('startRow')?.value as string | null;
      const endRowRaw = group.get('endRow')?.value as string | null;
      const perRow = group.get('seatsPerRow')?.value as number | null;
      this.seatsPerRow = perRow || 0;

      if (!hallId || !startRowRaw || !endRowRaw || perRow == null) return null;
      if (!group.get('startRow')?.valid || !group.get('endRow')?.valid || !group.get('seatsPerRow')?.valid) return null;

      const hall = this.hallsCache.find(h => h.id === hallId);
      if (!hall) return null;

      const s = startRowRaw.toUpperCase().charCodeAt(0);
      const e = endRowRaw.toUpperCase().charCodeAt(0);
      if (s > e) return { rowOrder: true };

      const rows = (e - s + 1);
      const totalSeats = rows * perRow;

      this.totalRows = Array.from({ length: rows }, (_, i) => String.fromCharCode(s + i));
      /* console.log('Total Rows:', this.totalRows); */
      this.totalSeats = Array.from({ length: perRow }, (_, i) => i + 1);
      /* console.log('Total Rows:', this.totalSeats); */

      return totalSeats > hall.capacity
        ? { capacityExceeded: { totalSeats, capacity: hall.capacity } } : null;
    }
  }



  submitSeat() {
    if (this.seatForm.valid) {
      const seatToAdd = new SeatPayload(
        this.seatForm.get('hallId')?.value,
        this.seatForm.get('startRow')?.value.toUpperCase(),
        this.seatForm.get('endRow')?.value.toUpperCase(),
        this.seatForm.get('seatsPerRow')?.value
      );
      console.log('Seat to add:', seatToAdd);
      this.seatService.add(seatToAdd).subscribe({
        next: (res) => {
          this.notificationService.successNotification("Seat added", res.message || "Seat successfully added!");
          this.seatForm.reset();
        },
        error: (err) => {
          if (err.status === 409) {
            this.notificationService.errorNotification("Duplicate seats found", "Some of the seats you are trying to add already exist in the hall!");
            return;
          }
          this.notificationService.errorNotification("Seat adding error", err.error?.error || "There was an error while adding the seat!");
          console.error('Error adding seat:', err);
        }
      })
    }
  }

}
