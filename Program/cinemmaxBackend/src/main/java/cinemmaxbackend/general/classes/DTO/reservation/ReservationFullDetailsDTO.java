package cinemmaxbackend.general.classes.DTO.reservation;

import cinemmaxbackend.general.classes.DTO.cinema.CinemaDTO;
import cinemmaxbackend.general.classes.DTO.hall.HallDTO;
import cinemmaxbackend.general.classes.DTO.seat.EmailSeatDTO;
import cinemmaxbackend.general.classes.classic.ShowType.ShowType;
import cinemmaxbackend.general.classes.classic.reservation.Reservation;
import cinemmaxbackend.general.classes.classic.reservation.ReservationSeat;
import cinemmaxbackend.general.classes.classic.seat.Seat;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

@Data
@Builder
public class ReservationFullDetailsDTO {
    private Long id;
    private String filmName;
    private LocalDateTime showTime;
    private String showType;
    private CinemaDTO cinema;
    private HallDTO hall;
    private List<EmailSeatDTO> seats;
    private BigDecimal finalPrice;
    private LocalDateTime reservationTime;

    public static ReservationFullDetailsDTO fromEntity(Reservation r) {
        List<EmailSeatDTO> seats = r.getSeats().stream()
                .map(ReservationSeat::getSeat)
                .map(s -> new EmailSeatDTO(s.getRowLetter(), s.getSeatNumber()))
                .sorted(Comparator.comparing(EmailSeatDTO::getSeatRow).thenComparing(EmailSeatDTO::getSeatNumber))
                .toList();

        return ReservationFullDetailsDTO.builder()
                .id(r.getId())
                .filmName(r.getShowTime().getFilm().getName())
                .showTime(r.getShowTime().getShowTime())
                .showType(r.getShowTime().getShowType().getType())
                .cinema(new CinemaDTO(r.getShowTime().getHall().getCinema().getId(), r.getShowTime()
                        .getHall().getCinema().getName(), r.getShowTime().getHall().getCinema().getLocation()))
                .hall(new HallDTO(r.getShowTime().getHall().getId(), r.getShowTime().getHall().getNumber()))
                .seats(seats)
                .finalPrice(r.getFinalPrice())
                .reservationTime(r.getReservationTime())
                .build();
    }

}
