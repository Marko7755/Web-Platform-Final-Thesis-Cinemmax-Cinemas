package cinemmaxbackend.general.classes.classic.director;

import cinemmaxbackend.general.classes.classic.actor.Actor;
import cinemmaxbackend.general.classes.classic.film.Film;
import cinemmaxbackend.general.classes.commands.actor.ActorCommand;
import cinemmaxbackend.general.classes.commands.director.DirectorCommand;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
@Table(name = "Directors")
public class Director {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "surname")
    private String surname;

    @ManyToMany(mappedBy = "directors")
    @JsonBackReference
    private List<Film> films;

    public static Director convertToDirector(DirectorCommand actorCommand) {
        Director director = new Director();
        director.setName(actorCommand.getName());
        director.setSurname(actorCommand.getSurname());
        return director;
    }
}
