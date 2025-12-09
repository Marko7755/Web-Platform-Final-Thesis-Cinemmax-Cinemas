package cinemmaxbackend.general.classes.classic.reservation;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Embeddable
@Data
@NoArgsConstructor
public class BookedSeatId implements Serializable {
    private Long showTimeId;
    private Long seatId;
}
