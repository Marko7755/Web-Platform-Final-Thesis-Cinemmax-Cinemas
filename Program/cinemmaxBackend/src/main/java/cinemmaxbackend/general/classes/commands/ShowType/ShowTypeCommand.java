package cinemmaxbackend.general.classes.commands.ShowType;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ShowTypeCommand {
    @NotBlank(message = "Type is required and cannot be blank")
    private String type;

    @DecimalMin(value = "0.00", inclusive = true, message = "Additional price cannot be negative")
    @Digits(integer = 5, fraction = 1, message = "Additional price can have at most 1 decimal place")
    private BigDecimal additionalPrice = BigDecimal.ZERO;
}
