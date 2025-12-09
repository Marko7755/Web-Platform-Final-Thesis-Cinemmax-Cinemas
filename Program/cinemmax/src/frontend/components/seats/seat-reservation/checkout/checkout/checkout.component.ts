import { Component, OnInit } from '@angular/core';
import { ShowTimeDetailsDTO } from '../../../../../../backend/classes/DTO/ShowTimeDetailsDTO/show-time-details-dto';
import { ActivatedRoute, Router } from '@angular/router';
import { ShowTimeService } from '../../../../../services/show-time/show-time.service';
import { SeatDTO } from '../../../../../../backend/classes/DTO/SeatDTO/seat-dto';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ReactiveFormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { ReservationPayload } from '../../../../../../backend/classes/Payload/reservation/reservation-payload';
import { AuthService } from '../../../../../../backend/services/auth.service';
import { UserInfo } from '../../../../../../backend/classes/general/User/UserInfo/user-info';
import { CheckoutPayload } from '../../../../../../backend/classes/Payload/checkout/checkout-payload';
import { CheckoutService } from '../../../../../services/checkout/checkout.service';
import { NotificationService } from '../../../../../services/notification/notification.service';

const DIGITS_ONLY = /^[0-9]+$/;
const EXP_MMYY = /^(0[1-9]|1[0-2])\/\d{2}$/;

@Component({
  selector: 'app-checkout',
  standalone: true,
  imports: [ReactiveFormsModule, CommonModule],
  templateUrl: './checkout.component.html',
  styleUrl: './checkout.component.css'
})
export class CheckoutComponent implements OnInit {
  showTimeId: number | null = null;
  showTimeDetails: ShowTimeDetailsDTO | null = null;
  paymentForm: FormGroup;
  user: UserInfo | null = null;
  seatsIdsToReserve: number[] = [];
  isLoading: boolean = false;

  constructor(private route: ActivatedRoute, private showTimeService: ShowTimeService, private fb: FormBuilder,
    private authService: AuthService, private router: Router, private checkoutService: CheckoutService,
    private notificationService: NotificationService) {
    this.paymentForm = this.fb.group({
      cardNumber: [
        '',
        [Validators.required, Validators.minLength(16), Validators.maxLength(19), Validators.pattern(DIGITS_ONLY)]
      ],
      cvv: [
        '',
        [Validators.required, Validators.minLength(3), Validators.maxLength(4), Validators.pattern(DIGITS_ONLY)]
      ],
      exp: [
        '',
        [Validators.required, Validators.pattern(EXP_MMYY)]
      ]
    });
  }


  ngOnInit() {
    const id = this.route.snapshot.paramMap.get('showTimeId');
    if (id) {
      this.showTimeId = +id;
      console.log(this.showTimeId);

      const urlParams = this.route.snapshot.queryParamMap.get('seatIds');
      const paramSeatIds = urlParams ? urlParams.split(',').map(s => +s) : [];
      console.log(paramSeatIds);


      if(!urlParams) {
        this.notificationService.errorNotificationWithConfirmation("Error", "There was an error with urlParams.", () => this.router.navigate(['']));
        return;
      } 

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
          seats = seats.map(s => new SeatDTO(s.id, s.rowLetter, s.seatNumber, s.status)).filter(s => s.status !== 'reserved');
          this.seatsIdsToReserve = seats.filter(s => paramSeatIds.includes(s.id)).map(s => s.id);
        }
      })
    };

    this.authService.getCurrentUser().subscribe({
      next: (user: UserInfo) => {
        this.user = user;
      },
      error: (err) => {
        console.error('Error fetching current user:', err);
      }
    });

  }

  payAndReserve(): void {
    //even number => success
    //odd number => fail
    if (this.paymentForm.valid) {
      if (!this.user) {
        this.router.navigate(['/login']);
        return;
      }
      this.isLoading = true;
      const cardNumber = this.paymentForm.get('cardNumber')?.value;
      const cvv = this.paymentForm.get('cvv')?.value;
      const reservationPayload = new ReservationPayload(this.showTimeId!, this.seatsIdsToReserve, this.user.id, "reserved");
      const checkoutPayload = new CheckoutPayload(cardNumber, cvv, reservationPayload);
      console.log(checkoutPayload);

      this.checkoutService.payAndReserve(checkoutPayload).subscribe({
        next: (res) => {
          this.isLoading = false;
          console.log(res);
          this.notificationService.successNotificationWithConfirmation("Reservation created",
            res.message, () => this.router.navigate(['']));
        },
        error: (err) => {
          this.isLoading = false;
          console.error(err);
          if (err.status === 409) {
            const duplicates = err.error.duplicates
              .map((d: any) => `${d.seatRow}${d.seatNumber}`)
              .join(", ");
            this.notificationService.errorNotificationWithConfirmation("Failed to make a reservation", `These seats are already taken: ${duplicates}`,
              () => this.router.navigate([`/seatReservation/${this.showTimeId}`]));
          }
          else if (err.status === 400) {
            this.notificationService.errorNotification("Payement error", err.error.message)
          }
          else {
            this.notificationService.errorNotification("Reservation Error", "There was an error while reserving seats, please try again later...");
          }
        }
      })

    }

  }


  cancel() {
    this.router.navigate(['']);
  }








  /** Keep only digits, auto-insert slash, clamp month to 01–12, and cap length at 5. */
  onExpiryInput(ev: Event) {
    const input = ev.target as HTMLInputElement;
    const raw = (input.value || '').replace(/\D+/g, '').slice(0, 4); // digits only, max 4
    let mm = raw.slice(0, 2);
    const yy = raw.slice(2, 4);

    // If user types a single digit > 1, make it 0X (e.g., "3" -> "03")
    if (mm.length === 1 && parseInt(mm, 10) > 1) mm = '0' + mm;

    // Clamp month to 01..12 when two digits entered
    if (mm.length === 2) {
      const m = Math.min(12, Math.max(1, parseInt(mm, 10)));
      mm = m.toString().padStart(2, '0');
    }

    const formatted = yy ? `${mm}/${yy}` : mm;
    // Paint into the input and update form without re-emitting to avoid cursor jumps
    input.value = formatted;
    this.paymentForm.get('exp')!.setValue(formatted, { emitEvent: false });
  }


}
