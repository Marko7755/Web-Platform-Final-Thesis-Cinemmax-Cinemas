import { Component, ElementRef, OnInit, ViewChild } from '@angular/core';
import { ActivatedRoute, Router, ɵEmptyOutletComponent } from '@angular/router';
import { CommonModule } from '@angular/common';
import { ShowTimeDetailsDTO } from '../../../../../backend/classes/DTO/ShowTimeDetailsDTO/show-time-details-dto';
import { ShowTimeService } from '../../../../services/show-time/show-time.service';
import { SeatDTO } from '../../../../../backend/classes/DTO/SeatDTO/seat-dto';
import { SeatService } from '../../../../services/seat/seat.service';
import { take } from 'rxjs';



/* type SeatsPerRow = {
  row: string,
  seats: SeatDTO[]
}; */

type SeatMark = {
  row: string,
  number: number;
}

@Component({
  selector: 'app-seat-reservation',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './seat-reservation.component.html',
  styleUrl: './seat-reservation.component.css'
})

export class SeatReservationComponent implements OnInit {
  showTimeId: number | null = null;
  showTimeDetails: ShowTimeDetailsDTO | null = null;
  seatsForShowTime: SeatDTO[] = [];
  /*   seatsPerRow: SeatsPerRow[] = []; */
  seatsRowMap: Map<string, SeatDTO[]> = new Map<string, SeatDTO[]>();
  seatClicked: boolean = false;
  seatToBookNumber: number = 1;
  maxSeats: number = 10;
  selectedSeatsSum: number = 0;
  reservedSeats: SeatDTO[] = [];
  selectedSeats: SeatDTO[] = [];
  selectedReservedSeats: SeatMark[] = [];
  selectedReservedSeat: SeatMark | null = null;


  constructor(private route: ActivatedRoute, private showTimeService: ShowTimeService, private seatService: SeatService, private router: Router) { }

  ngOnInit(): void {
    const id = this.route.snapshot.paramMap.get('showTimeId');
    if (id) {
      this.showTimeId = +id;

      this.showTimeService.getDetails(this.showTimeId).subscribe({
        next: (details) => {
          this.showTimeDetails = details;
          console.log(this.showTimeDetails);
        },
        error: (err) => {
          console.error(err);
        }
      });

      this.showTimeService.getSeatsForShowTime(this.showTimeId).subscribe({
        next: (seats) => {
          this.seatsForShowTime = seats.map(s => new SeatDTO(s.id, s.rowLetter, s.seatNumber, s.status));
          /* console.log(this.seatsForShowTime); */
          this.groupSeatsPerRow();
          this.reservedSeats = seats.filter(seat => seat.status === 'reserved');
        },
        error: (err) => {
          console.error(err);
        }
      });
    }
  }


  keepOrder = () => 0;
  trackByKey = (_: number, e: { key: string }) => e.key;
  private groupSeatsPerRow(): void {
    //virtual seat is created because the last row in an array does not get saved since currentRow === s.rowLetter
    // (last seat does have the same row letter as currentRow, so the new seat forces the last row to be saved)
    let currentRow = '';
    let seats: SeatDTO[] = [];
    this.seatsRowMap = new Map<string, SeatDTO[]>();
    let a: SeatDTO[] = [...this.seatsForShowTime];
    a.push(new SeatDTO(0, '', 0, ''));

    for (const s of a) {
      if (currentRow !== s.rowLetter) {
        if (seats.length) {
          this.seatsRowMap.set(currentRow, seats);
          seats = [];
        }
        currentRow = s.rowLetter;
        seats.push(s);
      }
      else {
        seats.push(s);
      }
    }

    /* console.log(this.seatsRowMap); */
  }


  increaseSeatNumber() {
    if (this.seatToBookNumber < this.maxSeats) this.seatToBookNumber++;
  }

  decreaseSeatNumber() {
    if (this.seatToBookNumber > 1) this.seatToBookNumber--;
  }

  onSeatClick(seat: SeatDTO) {
    /* console.log(seat); */

    //extra check for duplicate seats - if already reserved seat is somehow selected, it does not get saved and the error message is displayed
    const selectedReservedSeat = this.reservedSeats.find(reservedSeat => reservedSeat.id === seat.id)
    const seatMark: SeatMark | null = selectedReservedSeat ? { row: selectedReservedSeat.rowLetter, number: selectedReservedSeat.seatNumber } : null;
    if (seatMark) {
      this.selectedReservedSeats.push(seatMark);
      return;
    }

    /* if (this.selectedSeats.findIndex(s => seat.id === s.id) === -1) {
      this.selectedSeats.push(seat);
      console.log("nema");
    }
    else {
      this.selectedSeats = this.selectedSeats.filter(s => s.id !== seat.id);
      console.log("del");
    } */

    seat.selected = !seat.selected;
    if (seat.selected) {
      if (this.selectedSeats.findIndex(s => seat.id === s.id) === -1) {
        this.selectedSeats.push(seat);
        this.selectedSeatsSum++;
      }


    }
    else {
      this.selectedSeats = this.selectedSeats.filter(s => s.id !== seat.id);
      this.selectedSeatsSum--;
    }
    /* if(!seat.selected) {
      this.selectedSeatsSum --;
    } */
    /*  console.log(this.selectedSeatsSum); */
  }

  refreshSeats() {
    this.showTimeService.getSeatsForShowTime(this.showTimeId!).subscribe({
      next: (seats) => {
        this.seatsForShowTime = seats.map(s => new SeatDTO(s.id, s.rowLetter, s.seatNumber, s.status));
        this.groupSeatsPerRow();
        this.reservedSeats = seats.filter(seat => seat.status === 'reserved');
      },
      error: (err) => {
        console.error(err);
      }
    });
    this.selectedSeats = [];
    this.seatToBookNumber = 0;
    this.selectedSeatsSum = 0;
  }




  next() {
    console.log("Selected: ", this.selectedSeats);

    const seatsIds = this.selectedSeats.map(seat => seat.id);

    /* this.seatService.selectSeatIdsBehavior.next(seatsIds); */

    this.router.navigate([`/selectionDetails/${this.showTimeId}`],
      { queryParams: { seatIds: seatsIds.join(',') } }
    );
  }

  // Dodajte ovu metodu u vašu SeatReservationComponent klasu
  calculateScreenWidth(): number {
    if (!this.seatsRowMap || this.seatsRowMap.size === 0) {
      return 100; // Default width
    }

    // Pronađi red s najviše sjedala
    let maxSeats = 0;
    for (const row of this.seatsRowMap.values()) {
      if (row.length > maxSeats) {
        maxSeats = row.length;
      }
    }

    if (maxSeats === 0) return 100;

    // Izračunaj širinu ekrana kao postotak maksimalnog broja sjedala (20)
    // Formula: (broj sjedala / maksimalni broj sjedala) * 100
    const screenWidthPercentage = (maxSeats / 20) * 100;

    // Ograniči na minimalno 30% i maksimalno 100%
    return Math.max(30, Math.min(100, screenWidthPercentage));
  }

}
