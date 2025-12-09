package cinemmaxbackend.general.classes.commands.ShowTime;

import cinemmaxbackend.general.annotations.ShowTimeNotBeforeRelease;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Component
@Data
@ShowTimeNotBeforeRelease
public class ShowTimeCommand {

    @NotNull(message = "filmId is required")
    private Long filmId;

    @NotNull(message = "hallId is required")
    private Long hallId;

    @NotNull(message = "showTime is required")
    @FutureOrPresent(message = "showTime cannot be in the past")
    private LocalDateTime showTime;

    @NotNull(message = "typeId is required")
    private Long typeId;

    @Digits(integer = 5, fraction = 2)
    @DecimalMin(value = "0.00", inclusive = true) //inclusive = true - 0.00 can  be included as well
    private BigDecimal basePrice = new BigDecimal("5.00");
}
