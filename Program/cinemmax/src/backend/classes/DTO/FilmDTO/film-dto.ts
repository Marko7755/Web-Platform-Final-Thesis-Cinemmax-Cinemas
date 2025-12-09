export class FilmDTO {
    constructor(
        public id: number,
        public name: string,
        public imdbRating: number,
        public ageRate: number,
        public imageUrl: string
    ) {}
}
