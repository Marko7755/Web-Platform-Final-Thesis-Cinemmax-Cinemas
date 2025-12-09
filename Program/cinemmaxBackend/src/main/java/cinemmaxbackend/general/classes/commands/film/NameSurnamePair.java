package cinemmaxbackend.general.classes.commands.film;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class NameSurnamePair {
    @NotBlank(message = "Name is required")
    private String name;

    @NotBlank(message = "Surname is required")
    private String surname;
}
