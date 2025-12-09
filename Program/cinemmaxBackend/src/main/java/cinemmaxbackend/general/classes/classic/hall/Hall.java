package cinemmaxbackend.general.classes.classic.hall;

import cinemmaxbackend.general.classes.classic.ShowTime.ShowTime;
import cinemmaxbackend.general.classes.classic.cinema.Cinema;
import cinemmaxbackend.general.classes.classic.seat.Seat;
import cinemmaxbackend.general.classes.commands.hall.HallCommand;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Data
@Table(name = "Halls")
public class Hall {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "Cinema_id")
    @JsonManagedReference
    private Cinema cinema;

    @Column(name = "hall_number")
    private Integer number;

    @Column(name = "capacity")
    private Integer capacity;

    @OneToMany(mappedBy = "hall", cascade = CascadeType.REMOVE, orphanRemoval = true)
    @JsonBackReference
    private List<ShowTime> showTimes;


    @OneToMany(mappedBy = "hall", cascade = CascadeType.REMOVE, orphanRemoval = true)
    @JsonManagedReference
    private List<Seat> seats;

    public static Hall convertToHall(HallCommand hallCommand) {
        Hall hall = new Hall();
        hall.setNumber(hallCommand.getNumber());
        hall.setCapacity(hallCommand.getCapacity());
        return hall;
    }
}
