export class FilmPreviewDTO {
    public startDate: string = '';
    public endDate: string = '';
    constructor(public id: number, public name: string, public imageUrl: string) {}
}
