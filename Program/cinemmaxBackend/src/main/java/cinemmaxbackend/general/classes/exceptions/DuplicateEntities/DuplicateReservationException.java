package cinemmaxbackend.general.classes.exceptions.DuplicateEntities;

import cinemmaxbackend.general.classes.classic.reservation.Reservation;
import cinemmaxbackend.general.classes.records.DuplicateReservations;
import lombok.Getter;

import java.util.List;

@Getter
public class DuplicateReservationException extends RuntimeException {
    List<DuplicateReservations> duplicates;
    public DuplicateReservationException(String message, List<DuplicateReservations> duplicates) {
        super(message);
        this.duplicates = List.copyOf(duplicates);

    }
}
