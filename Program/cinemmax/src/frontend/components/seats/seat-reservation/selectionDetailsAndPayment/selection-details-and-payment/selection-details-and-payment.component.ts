import { Component } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { SeatService } from '../../../../../services/seat/seat.service';
import { ShowTimeService } from '../../../../../services/show-time/show-time.service';
import { ShowTimeDetailsDTO } from '../../../../../../backend/classes/DTO/ShowTimeDetailsDTO/show-time-details-dto';
import { SeatDTO } from '../../../../../../backend/classes/DTO/SeatDTO/seat-dto';
import { DatePipe } from '@angular/common';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { NotificationService } from '../../../../../services/notification/notification.service';

@Component({
  selector: 'app-selection-details-and-payment',
  standalone: true,
  imports: [DatePipe, CommonModule, RouterLink],
  templateUrl: './selection-details-and-payment.component.html',
  styleUrl: './selection-details-and-payment.component.css'
})
export class SelectionDetailsAndPaymentComponent {
  showTimeId: number | null = null;
  showTimeDetails: ShowTimeDetailsDTO | null = null;
  seatsToReserve: SeatDTO[] = [];

  constructor(private route: ActivatedRoute, private showTimeService: ShowTimeService, private notificationService: NotificationService, private router: Router) { }

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


      const urlParams = this.route.snapshot.queryParamMap.get('seatIds') ?? '';
      const seatIds = urlParams ? urlParams.split(',').map(string => +string) : [];
      console.log(seatIds);

      if (!urlParams) {
        this.notificationService.errorNotificationWithConfirmation("Error", "There was an error with urlParams.", () => this.router.navigate(['']));
        return;
      }
      this.showTimeService.getSeatsForShowTime(this.showTimeId).subscribe({
        next: (seats) => {
          const seatsForShowTime = seats.map(s => new SeatDTO(s.id, s.rowLetter, s.seatNumber, s.status)).filter(s => s.status !== 'reserved');
          /* this.seatsToReserve = seatIds.forEach(seatId => this.seatsForShowTime.find(seat => seatId === seat.id)); */

          this.seatsToReserve = seatsForShowTime.filter(seat => seatIds.includes(seat.id));
          console.log(this.seatsToReserve);
        },
        error: (err) => {
          console.error(err);
        }
      });

      /* this.seatService.selectedSeatsObs$.pipe(take(1)).subscribe(seatIds => console.log(seatIds)); */
    }
  }


  get seatIds(): string {
    const a = this.seatsToReserve ?? [];
    return a.map(s => s.id).join(',');
  }



}
