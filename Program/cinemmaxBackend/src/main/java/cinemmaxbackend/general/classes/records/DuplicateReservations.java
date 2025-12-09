package cinemmaxbackend.general.classes.records;

public record DuplicateReservations(Long reservationId, Long showTimeId, Long seatId, Character seatRow, Integer seatNumber) {
}
