package cinemmaxbackend.general.classes.exceptions.NotFound;

public class ReservationNotFoundException extends RuntimeException {
    public ReservationNotFoundException(String message) {
        super(message);
    }
}
