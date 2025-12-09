package cinemmaxbackend.general.classes.exceptions.NotFound;

public class ActorNotFoundException extends RuntimeException {
    public ActorNotFoundException(String message) {
        super(message);
    }
}
