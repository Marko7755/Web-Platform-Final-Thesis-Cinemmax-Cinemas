import { CinemaDTO } from "../CinemaDTO/cinema-dto";
import { FilmDTO } from "../FilmDTO/film-dto";
import { HallDTO } from "../HallDTO/hall-dto";
import { PricingDTO } from "../PricingDTO/pricing-dto";
import { ShowTypeDTO } from "../ShowTypeDTO/show-type-dto";

export class ShowTimeDetailsDTO {
    constructor(
        public id: number,
        public showTime: string,
        public film: FilmDTO,
        public showType: ShowTypeDTO,
        public hall: HallDTO,
        public cinema: CinemaDTO,
        public pricing: PricingDTO
    ) {}
}
