package cinemmaxbackend.general.classes.classic.reservation;

import cinemmaxbackend.general.classes.classic.ShowTime.ShowTime;
import cinemmaxbackend.general.classes.classic.seat.Seat;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "Booked_Seats")
public class BookedSeat {
    /*@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;*/

    @EmbeddedId
    private BookedSeatId id = new BookedSeatId();

    @ManyToOne
    @MapsId("showTimeId")
    @JoinColumn(name = "show_time_id")
    private ShowTime showTime;

    @ManyToOne
    @MapsId("seatId")
    @JoinColumn(name = "seat_id")
    private Seat seat;

    @OneToOne
    @JoinColumn(name = "reservation_seat_id")
    private ReservationSeat reservationSeat;

}
