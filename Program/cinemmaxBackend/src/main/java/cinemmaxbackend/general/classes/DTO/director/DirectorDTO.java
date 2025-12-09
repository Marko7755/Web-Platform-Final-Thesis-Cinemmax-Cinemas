package cinemmaxbackend.general.classes.DTO.director;

import cinemmaxbackend.general.classes.DTO.actor.ActorDTO;
import cinemmaxbackend.general.classes.classic.actor.Actor;
import cinemmaxbackend.general.classes.classic.director.Director;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DirectorDTO {
    private String name;
    private String surname;

    public static DirectorDTO fromEntity(Director director) {
        return new DirectorDTO(director.getName(), director.getSurname());
    }
}
