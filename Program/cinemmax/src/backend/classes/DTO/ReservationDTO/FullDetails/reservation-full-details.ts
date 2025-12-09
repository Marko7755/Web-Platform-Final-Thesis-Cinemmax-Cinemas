import { CinemaDTO } from "../../CinemaDTO/cinema-dto";
import { HallDTO } from "../../HallDTO/hall-dto";
import { SeatMinDetailsDTO } from "../../SeatDTO/MinDetails/seat-min-details-dto";

export class ReservationFullDetails {
    constructor(public id: number, public filmName: string, public showTime: string, 
        public showType: string,  public cinema: CinemaDTO, public hall: HallDTO, 
        public seats: SeatMinDetailsDTO[], public finalPrice: number, public reservationTime: string) { }
}
