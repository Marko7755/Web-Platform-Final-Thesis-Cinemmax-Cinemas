package cinemmaxbackend.general.classes.classic.actor;

import cinemmaxbackend.general.classes.classic.film.Film;
import cinemmaxbackend.general.classes.commands.actor.ActorCommand;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
@Table(name = "Actors")
public class Actor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "surname")
    private String surname;

    @ManyToMany(mappedBy = "actors")
    @JsonBackReference
    private List<Film> films;

    public static Actor convertToActor(ActorCommand actorCommand) {
        Actor actor = new Actor();
        actor.setName(actorCommand.getName());
        actor.setSurname(actorCommand.getSurname());
        return actor;
    }

}
