package cinemmaxbackend.general.classes.exceptions.NotFound;

public class HallNotFoundException extends RuntimeException {
    public HallNotFoundException(String message) {
        super(message);
    }
}
