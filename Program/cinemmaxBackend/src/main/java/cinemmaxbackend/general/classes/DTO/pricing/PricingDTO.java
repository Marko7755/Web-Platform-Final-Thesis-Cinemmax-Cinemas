package cinemmaxbackend.general.classes.DTO.pricing;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class PricingDTO {
    private BigDecimal base;
    private BigDecimal additional;
    private BigDecimal finalPrice;
}
