package cinemmaxbackend.general.classes.exceptions.NotFound;

public class ShowTimeNotFoundException extends RuntimeException {
    public ShowTimeNotFoundException(String message) {
        super(message);
    }
}
