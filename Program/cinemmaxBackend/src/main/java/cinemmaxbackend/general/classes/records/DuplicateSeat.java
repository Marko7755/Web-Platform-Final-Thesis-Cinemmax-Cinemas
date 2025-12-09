package cinemmaxbackend.general.classes.records;

public record DuplicateSeat(String cinemaName, long hallNumber, char row, int seat) {
}
