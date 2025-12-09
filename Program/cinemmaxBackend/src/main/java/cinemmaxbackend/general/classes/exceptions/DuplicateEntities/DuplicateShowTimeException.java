package cinemmaxbackend.general.classes.exceptions.DuplicateEntities;

public class DuplicateShowTimeException extends RuntimeException {
    public DuplicateShowTimeException(String message) {
        super(message);
    }
}
