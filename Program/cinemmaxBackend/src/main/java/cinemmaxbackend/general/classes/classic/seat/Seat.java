package cinemmaxbackend.general.classes.classic.seat;

import cinemmaxbackend.general.classes.classic.hall.Hall;
import cinemmaxbackend.general.classes.classic.reservation.BookedSeat;
import cinemmaxbackend.general.classes.classic.reservation.ReservationSeat;
import cinemmaxbackend.general.classes.commands.seat.SeatCommand;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Data
@Table(name = "Seats")
public class Seat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "hall_id")
    @JsonBackReference
    public Hall hall;

    @Column(name = "row_letter")
    private Character rowLetter;

    @Column(name = "seat_number")
    private Integer seatNumber;

    /*@OneToMany(mappedBy = "seat", cascade = CascadeType.REMOVE, orphanRemoval = true)
    @JsonBackReference
    private List<BookedSeat> bookedSeats;*/

    @OneToMany(mappedBy = "seat", cascade = CascadeType.REMOVE, orphanRemoval = true)
    @JsonBackReference
    private List<ReservationSeat> reservationSeats;


}
