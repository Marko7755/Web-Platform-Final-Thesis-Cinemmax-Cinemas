package cinemmaxbackend.general.classes.commands.director;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class DirectorCommand {
    @NotBlank(message = "Name is required")
    private String name;

    @NotBlank(message = "Surname is required")
    private String surname;
}
