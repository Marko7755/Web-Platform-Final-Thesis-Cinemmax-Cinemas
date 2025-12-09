import { Routes } from '@angular/router';
import { MainPageComponent } from '../frontend/components/main-page/main-page.component';
import { authGuard } from '../backend/security/AuthGuard/auth.guard';
import { LoginComponent } from '../frontend/components/auth/login/login.component';
import { RegisterComponent } from '../frontend/components/auth/register/register.component';
import { PasswordResetComponent } from '../frontend/components/auth/PasswordReset/password-reset/password-reset.component';
import { EmailInputComponent } from '../frontend/components/auth/PasswordReset/email-input/email-input.component';
import { FilmInputComponent } from '../frontend/components/films/film-input/film-input.component';
import { FilmInfoComponent } from '../frontend/components/films/film-info/film-info.component';
import { MyProfileComponent } from '../frontend/components/my-profile/my-profile.component';
import { FilmEditComponent } from '../frontend/components/films/edit/film-edit.component';
import { CinemaInputComponent } from '../frontend/components/cinemas/cinema-input/cinema-input.component';
import { CinemaManageComponent } from '../frontend/components/cinemas/manage/cinema-manage.component';
import { CinemaEditComponent } from '../frontend/components/cinemas/cinemaEdit/cinema-edit.component';
import { HallInputComponent } from '../frontend/components/halls/hall-input/hall-input.component';
import { SeatInputComponent } from '../frontend/components/seats/seat-input/seat-input.component';
import { ShowTypeInputComponent } from '../frontend/components/show-types/show-type-input/show-type-input.component';
import { ShowTimeInputComponent } from '../frontend/components/show-time-input/show-time-input.component';
import { SeatReservationComponent } from '../frontend/components/seats/seat-reservation/seat-reservation/seat-reservation.component';
import { SelectionDetailsAndPaymentComponent } from '../frontend/components/seats/seat-reservation/selectionDetailsAndPayment/selection-details-and-payment/selection-details-and-payment.component';
import { CheckoutComponent } from '../frontend/components/seats/seat-reservation/checkout/checkout/checkout.component';
import { ReservationMinDetailsComponent } from '../frontend/components/reservations/Details/MinDetails/reservation-min-details/reservation-min-details.component';
import { ReservationFullDetails } from '../backend/classes/DTO/ReservationDTO/FullDetails/reservation-full-details';
import { ReservationFullDetailsComponent } from '../frontend/components/reservations/Details/FullDetails/reservation-full-details/reservation-full-details.component';
import { ComingSoonComponent } from '../frontend/components/films/coming-soon/coming-soon/coming-soon.component';
import { EndingSoonComponent } from '../frontend/components/films/ending-soon/ending-soon/ending-soon.component';
import { BestRatedComponent } from '../frontend/components/films/best-rated/best-rated/best-rated.component';
import { AboutUsComponent } from '../frontend/components/about-us/about-us.component';


export const routes: Routes = [
    { path: '', component: MainPageComponent },
    { path: 'login', component: LoginComponent },
    { path: 'register', component: RegisterComponent },
    { path: 'emailInput', component: EmailInputComponent },
    { path: 'passwordReset', component: PasswordResetComponent },
    { path: 'filmInput', component: FilmInputComponent, canActivate: [authGuard] },
    { path: 'filmInfo/:filmId', component: FilmInfoComponent },
    { path: 'myProfile', component: MyProfileComponent, canActivate: [authGuard] },
    { path: 'filmEdit/:filmId', component: FilmEditComponent, canActivate: [authGuard] },
    { path: 'cinemaInput', component: CinemaInputComponent, canActivate: [authGuard] },
    { path: 'cinemaManage', component: CinemaManageComponent, canActivate: [authGuard] },
    { path: 'cinemaEdit/:cinemaId', component: CinemaEditComponent, canActivate: [authGuard] },
    { path: 'hallInput', component: HallInputComponent, canActivate: [authGuard] },
    { path: 'seatInput', component: SeatInputComponent, canActivate: [authGuard] },
    { path: 'showTypeInput', component: ShowTypeInputComponent, canActivate: [authGuard] },
    { path: 'showTimeInput/:filmId', component: ShowTimeInputComponent, canActivate: [authGuard] },
    { path: 'seatReservation/:showTimeId', component: SeatReservationComponent/* , canActivate: [authGuard] */ },
    { path: 'selectionDetails/:showTimeId', component: SelectionDetailsAndPaymentComponent/* , canActivate: [authGuard] */ },
    { path: 'checkout/:showTimeId', component: CheckoutComponent, canActivate: [authGuard] },
    { path: 'reservationMinDetails', component: ReservationMinDetailsComponent, canActivate: [authGuard] },
    { path: 'reservationFullDetails/:reservationId', component: ReservationFullDetailsComponent, canActivate: [authGuard] },
    { path: 'films/comingSoon', component: ComingSoonComponent },
    { path: 'films/endingSoon', component: EndingSoonComponent },
    { path: 'films/topRated', component: BestRatedComponent },
    { path: 'aboutUs', component: AboutUsComponent }
];
