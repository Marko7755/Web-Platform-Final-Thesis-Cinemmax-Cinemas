package cinemmaxbackend.general.classes.exceptions.NotFound;

public class FilmNotFoundException extends RuntimeException {
    public FilmNotFoundException(String message) {
        super(message);
    }
}
