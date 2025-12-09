package cinemmaxbackend.general.classes.exceptions.NotFound;

public class SeatNotFoundException extends RuntimeException {
    public SeatNotFoundException(String message) {
        super(message);
    }
}
