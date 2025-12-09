package cinemmaxbackend.general.classes.DTO.actor;

import cinemmaxbackend.general.classes.classic.actor.Actor;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ActorDTO {
    private String name;
    private String surname;

    public static ActorDTO fromEntity(Actor actor) {
        return new ActorDTO(actor.getName(), actor.getSurname());
    }

}
