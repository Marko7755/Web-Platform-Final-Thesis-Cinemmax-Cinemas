package cinemmaxbackend.general.classes.exceptions.DuplicateEntities;

public class DuplicateEmailException extends RuntimeException {
    public DuplicateEmailException(String message) {
        super(message);
    }
}
