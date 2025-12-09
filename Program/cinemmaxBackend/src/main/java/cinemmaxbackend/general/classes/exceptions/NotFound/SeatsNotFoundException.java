package cinemmaxbackend.general.classes.exceptions.NotFound;

import cinemmaxbackend.general.classes.classic.seat.Seat;
import lombok.Getter;

import java.util.List;

@Getter
public class SeatsNotFoundException extends RuntimeException {
    List<Long> notFound;
    public SeatsNotFoundException(String message, List<Long> notFound) {
        super(message);
        this.notFound = List.copyOf(notFound);
    }
}
