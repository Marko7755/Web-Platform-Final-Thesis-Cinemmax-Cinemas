import { Component, OnInit } from '@angular/core';
import { ReservationService } from '../../../../../services/reservation/reservation.service';
import { AuthService } from '../../../../../../backend/services/auth.service';
import { ReservationMinDetails } from '../../../../../../backend/classes/DTO/ReservationDTO/MinDetails/reservation-min-details';
import { RouterLink } from '@angular/router';
import { DatePipe } from '@angular/common';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-reservation-min-details',
  standalone: true,
  imports: [RouterLink, DatePipe, CommonModule],
  templateUrl: './reservation-min-details.component.html',
  styleUrl: './reservation-min-details.component.css'
})
export class ReservationMinDetailsComponent implements OnInit {
  reservations: ReservationMinDetails[] = [];

  constructor(private reservationService: ReservationService, private authService: AuthService) { }

  ngOnInit() {
    if (this.authService.isLoggedIn()) {
      this.reservationService.getMinDetails().subscribe({
        next: (reservations) => {
          this.reservations = reservations;
          console.log(reservations);
        }
      })
    }

  }


  get activeReservations(): ReservationMinDetails[] {
    const now = new Date();
    return (this.reservations ?? []).filter(r => new Date(r.showTime) >= now);
  }

  get pastReservations(): ReservationMinDetails[] {
    const now = new Date();
    return (this.reservations ?? []).filter(r => new Date(r.showTime) < now);
  }


}
