package cinemmaxbackend.general.classes.exceptions.NotFound;

public class ShowTypeNotFoundException extends RuntimeException {
    public ShowTypeNotFoundException(String message) {
        super(message);
    }
}
