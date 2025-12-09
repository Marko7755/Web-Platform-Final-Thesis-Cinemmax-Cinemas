package cinemmaxbackend.general.classes.commands.hall;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class HallCommand {
    @NotNull(message = "Cinema id is required")
    private Long cinemaId;

    @NotNull(message = "Hall number is required")
    @Positive(message = "Hall number must not be negative")
    private Integer number;

    @NotNull(message = "Capacity is required")
    @Min(value = 0, message = "Hall must have at least one seat")
    @Max(value = 1000, message = "Hall must not have more than 1000 seats")
    private Integer capacity;
}
