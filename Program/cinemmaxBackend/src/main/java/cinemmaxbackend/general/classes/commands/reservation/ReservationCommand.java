package cinemmaxbackend.general.classes.commands.reservation;

import cinemmaxbackend.general.classes.enums.Status;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Set;

@Data
public class ReservationCommand {
    @NotNull(message = "ShowTimeId is required")
    private Long showTimeId;

    @NotNull(message = "SeatId is required")
    private Set<Long> seatsIds;

    @NotNull(message = "UserId is required")
    private Long userId;

    @NotNull(message = "Status is required")
    private Status status;
}
