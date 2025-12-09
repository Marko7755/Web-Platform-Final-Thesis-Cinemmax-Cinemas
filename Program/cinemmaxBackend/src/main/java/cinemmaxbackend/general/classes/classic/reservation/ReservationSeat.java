package cinemmaxbackend.general.classes.classic.reservation;

import cinemmaxbackend.general.classes.classic.ShowTime.ShowTime;
import cinemmaxbackend.general.classes.classic.seat.Seat;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Entity
@Table(name = "Reservation_Seats")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReservationSeat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "reservation_id")
    @JsonBackReference
    private Reservation reservation;

/*    @ManyToOne
    @JoinColumn(name = "show_time_id")
    private ShowTime showTime;*/

    @ManyToOne
    @JoinColumn(name = "seat_id")
    @JsonManagedReference
    private Seat seat;

    @Column(name="price", precision=10, scale=2, nullable=false)
    private BigDecimal price;

    @OneToOne(mappedBy = "reservationSeat", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private BookedSeat bookedSeat;

}
