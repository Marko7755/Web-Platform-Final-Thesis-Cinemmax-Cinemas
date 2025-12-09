package cinemmaxbackend.general.classes.commands.seat;

import cinemmaxbackend.general.annotations.WithinHallCapacity;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
@WithinHallCapacity
public class SeatCommand {

    @NotNull(message = "Hall id is required")
    private Long hallId;

    @NotNull(message = "Start row letter is required")
    @Pattern(regexp = "^[A-Za-z]$", message = "startRow must be a single letter A-Z")
    private String startRow;

    @NotNull(message = "End row letter is required")
    @Pattern(regexp = "^[A-Za-z]$", message = "endRow must be a single letter A-Z")
    private String endRow;

    @NotNull(message = "Number of seats per row is required")
    @Min(1)
    @Max(20)
    private Integer seatsPerRow;
}
