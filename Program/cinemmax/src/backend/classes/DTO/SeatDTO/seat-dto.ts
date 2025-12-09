export class SeatDTO {
    /* public status: string = ''; */
    public selected: boolean = false;
    constructor(public id: number, public rowLetter: string, public seatNumber: number, public status: string) {
        status = '';
    }
}
