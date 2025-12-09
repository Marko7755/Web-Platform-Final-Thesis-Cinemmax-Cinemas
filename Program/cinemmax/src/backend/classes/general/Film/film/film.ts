export class Film {
  public id?: number;
  public ageRate: number = 0;
  public duration: number = 0;
  public imdbRating: number = 0;
  public imageUrl: string = '';
  public trailerUrl: string = '';
  public cinemaReleaseDate: string = '';
  public cinemaEndDate: string = '';
  public actors: { name: string; surname: string }[] = [];
  public directors: { name: string; surname: string }[] = [];

  constructor(
    public name: string,
    public genre: string,
    public about: string
  ) {}
}
