import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { ReservationService } from '../../../../../services/reservation/reservation.service';
import { NotificationService } from '../../../../../services/notification/notification.service';
import { ReservationFullDetails } from '../../../../../../backend/classes/DTO/ReservationDTO/FullDetails/reservation-full-details';
import { CommonModule } from '@angular/common';
import { SeatDTO } from '../../../../../../backend/classes/DTO/SeatDTO/seat-dto';
import { SeatMinDetailsDTO } from '../../../../../../backend/classes/DTO/SeatDTO/MinDetails/seat-min-details-dto';

type SeatGroup = { row: string; numbers: number[] };

@Component({
  selector: 'app-reservation-full-details',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './reservation-full-details.component.html',
  styleUrl: './reservation-full-details.component.css'
})
export class ReservationFullDetailsComponent implements OnInit {
  reservationId: number | null = null;
  reservationPosition: number | null = null;
  reservationDetails: ReservationFullDetails | null = null;

  seatsRowMap: Map<string, SeatMinDetailsDTO[]> = new Map<string, SeatMinDetailsDTO[]>();

  constructor(
    private route: ActivatedRoute,
    private reservationService: ReservationService,
    private notificationService: NotificationService
  ) { }

  ngOnInit(): void {
    const id = this.route.snapshot.paramMap.get('reservationId');
    if (!id) {
      this.notificationService.errorNotification('Error', 'Reservation ID is missing in the route parameters.');
      return;
    }
    this.reservationId = +id;

    const queryParam = this.route.snapshot.queryParamMap.get('pos');
    if (queryParam) {
      this.reservationPosition = +queryParam;
    }

    this.reservationService.getFullDetails(this.reservationId).subscribe({
      next: (details) => {
        this.reservationDetails = details;
        this.groupSeatsPerRow();
        // console.log(details, this.seatGroups);
      },
      error: () => {
        this.notificationService.errorNotification('Error', 'Failed to load reservation details.');
      }
    });
  }

  keepOrder = () => 0;
  trackByKey = (_: number, e: { key: string }) => e.key;
  private groupSeatsPerRow(): void {
    //virtual seat is created because the last row in an array does not get saved since currentRow === s.rowLetter
    // (last seat does have the same row letter as currentRow, so the new seat forces the last row to be saved)
    let currentRow = '';
    let seats: SeatMinDetailsDTO[] = [];
    this.seatsRowMap = new Map<string, SeatMinDetailsDTO[]>();
    let a: SeatMinDetailsDTO[] = [...this.reservationDetails?.seats!];
    a.push(new SeatMinDetailsDTO('', 0));
    console.log(a);

    for (const s of a) {
      if (currentRow !== s.seatRow) {
        if (seats.length) {
          this.seatsRowMap.set(currentRow, seats);
          seats = [];
        }
        currentRow = s.seatRow;
        seats.push(s);
      }
      else {
        seats.push(s);
      }
    }

    console.log(this.seatsRowMap);
  }
}
