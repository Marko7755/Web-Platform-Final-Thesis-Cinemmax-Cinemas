package cinemmaxbackend.general.classes.exceptions.PasswordReset;

public class PasswordResetTokenException extends RuntimeException {
    public PasswordResetTokenException(String message) {
        super(message);
    }

}
