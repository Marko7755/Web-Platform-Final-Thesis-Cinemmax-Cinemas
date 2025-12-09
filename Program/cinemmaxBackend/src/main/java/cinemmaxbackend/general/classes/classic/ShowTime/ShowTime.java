package cinemmaxbackend.general.classes.classic.ShowTime;

import cinemmaxbackend.general.classes.classic.ShowType.ShowType;
import cinemmaxbackend.general.classes.classic.film.Film;
import cinemmaxbackend.general.classes.classic.hall.Hall;
import cinemmaxbackend.general.classes.classic.reservation.Reservation;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@Table(name = "Show_Time")
public class ShowTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "film_id")
    @JsonIgnore
    private Film film;

    @ManyToOne
    @JsonManagedReference
    @JoinColumn(name = "hall_id")
    private Hall hall;

    @Column(name = "show_time")
    private LocalDateTime showTime;

    @ManyToOne
    @JoinColumn(name = "type_id")
    private ShowType showType;

    @Column(name = "base_price", precision = 5, scale = 2)
    private BigDecimal basePrice;

    @OneToMany(mappedBy = "showTime", cascade = CascadeType.REMOVE)
    @JsonIgnore
    private List<Reservation> reservations;



}
