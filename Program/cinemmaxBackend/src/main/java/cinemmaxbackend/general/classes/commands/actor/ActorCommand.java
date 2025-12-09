package cinemmaxbackend.general.classes.commands.actor;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ActorCommand {
    @NotBlank(message = "Name is required")
    private String name;

    @NotBlank(message = "Surname is required")
    private String surname;
}
