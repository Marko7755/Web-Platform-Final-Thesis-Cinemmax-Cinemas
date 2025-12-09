package cinemmaxbackend.general.classes.exceptions.NotFound;

public class DirectorNotFoundException extends RuntimeException {
    public DirectorNotFoundException(String message) {
        super(message);
    }
}
