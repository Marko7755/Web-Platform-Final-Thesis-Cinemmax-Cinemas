package cinemmaxbackend.general.classes.commands.cinema;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CinemaCommand {
    @NotBlank(message = "Location is required")
    private String location;

    @NotBlank(message = "Name is required")
    private String name;
}

