export class ReservationPayload {
    constructor(public showTimeId: number, public seatsIds: number[], public userId: number, public status: string) {}
}
