package cinemmaxbackend.general.classes.DTO.seat;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class EmailSeatDTO {
    private Character seatRow;
    private Integer seatNumber;
}
