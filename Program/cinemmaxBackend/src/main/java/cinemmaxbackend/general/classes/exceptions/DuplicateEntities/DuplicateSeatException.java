package cinemmaxbackend.general.classes.exceptions.DuplicateEntities;

import cinemmaxbackend.general.classes.records.DuplicateSeat;
import lombok.Getter;

import java.util.List;

@Getter
public class DuplicateSeatException extends RuntimeException {
    private final List<DuplicateSeat> duplicateSeats;

    public DuplicateSeatException(String message, List<DuplicateSeat> duplicateSeats) {
        super(message);
        this.duplicateSeats = List.copyOf(duplicateSeats);
    }
}
