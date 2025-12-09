package cinemmaxbackend.general.classes.DTO.reservation;

import cinemmaxbackend.general.classes.classic.reservation.Reservation;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class ReservationMinDetailsDTO {
    private Long id;
    private String filmName;
    private LocalDateTime showTime;
    private Integer numberOfTickets;

    public static ReservationMinDetailsDTO fromEntity(Reservation reservation) {
        return new ReservationMinDetailsDTO(reservation.getId(), reservation.getShowTime().getFilm().getName(),
                reservation.getShowTime().getShowTime(), reservation.getSeats().size());
    }

}
