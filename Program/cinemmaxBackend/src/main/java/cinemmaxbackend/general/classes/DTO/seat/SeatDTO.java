package cinemmaxbackend.general.classes.DTO.seat;

import cinemmaxbackend.general.classes.classic.seat.Seat;
import cinemmaxbackend.general.classes.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SeatDTO {
    public Long id;
    public Character rowLetter;
    public Integer seatNumber;
    public Status status;

    public static SeatDTO fromSeat(Seat seat) {
        SeatDTO dto = new SeatDTO();
        dto.id = seat.getId();
        dto.rowLetter = seat.getRowLetter();
        dto.seatNumber = seat.getSeatNumber();
        return dto;
    }
}
