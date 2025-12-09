import { ReservationPayload } from "../reservation/reservation-payload";

export class CheckoutPayload {
    constructor(public cardNumber: string, public cvv: string, public reservationCommand: ReservationPayload) {}
}
