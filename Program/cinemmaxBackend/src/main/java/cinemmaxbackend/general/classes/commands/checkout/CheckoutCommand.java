package cinemmaxbackend.general.classes.commands.checkout;

import cinemmaxbackend.general.classes.commands.reservation.ReservationCommand;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CheckoutCommand {
    @NotNull(message = "Card number is required")
    @NotBlank(message = "Card number cannot be blank")
    @Size(min = 16, max = 19, message = "Card number must be between 16 and 19 digits")
    @Pattern(regexp = "^[0-9]+$", message = "Card number must contain only digits")
    private String cardNumber;

    @NotNull(message = "CVV is required")
    @NotBlank(message = "CVV cannot be blank")
    @Size(min = 3, max = 4, message = "CVV must be 3 or 4 digits")
    @Pattern(regexp = "^[0-9]+$", message = "CVV must contain only digits")
    private String cvv;

    @NotNull(message = "Reservation details are required")
    private ReservationCommand reservationCommand;
}
