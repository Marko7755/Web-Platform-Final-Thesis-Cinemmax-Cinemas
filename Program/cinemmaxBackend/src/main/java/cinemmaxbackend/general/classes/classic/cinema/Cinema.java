package cinemmaxbackend.general.classes.classic.cinema;

import cinemmaxbackend.general.classes.classic.hall.Hall;
import cinemmaxbackend.general.classes.commands.cinema.CinemaCommand;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
@Table(name = "Cinemas")
public class Cinema {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "location")
    private String location;

    @Column(name = "name")
    private String name;

    @OneToMany(mappedBy = "cinema", cascade = CascadeType.REMOVE, orphanRemoval = true)
    @JsonBackReference
    private List<Hall> halls;

    public static Cinema convertToCinema(CinemaCommand cinemaCommand) {
        Cinema cinema = new Cinema();
        cinema.setName(cinemaCommand.getName());
        cinema.setLocation(cinemaCommand.getLocation());
        return cinema;
    }
}
