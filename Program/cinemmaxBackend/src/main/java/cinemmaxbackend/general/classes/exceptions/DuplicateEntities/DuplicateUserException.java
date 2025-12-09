package cinemmaxbackend.general.classes.exceptions.DuplicateEntities;

public class DuplicateUserException extends RuntimeException {
    public DuplicateUserException(String message) {
        super(message);
    }
}
