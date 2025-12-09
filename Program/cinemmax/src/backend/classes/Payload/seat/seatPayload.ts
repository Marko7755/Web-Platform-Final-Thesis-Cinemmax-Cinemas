export class SeatPayload {
    public id?: number;

    constructor(public hallId: number, public startRow: string, public endRow: string, public seatsPerRow: number) {}
}
