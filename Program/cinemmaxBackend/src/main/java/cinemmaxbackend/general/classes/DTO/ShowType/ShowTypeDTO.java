package cinemmaxbackend.general.classes.DTO.ShowType;


import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class ShowTypeDTO {
    private Long id;
    private String type;
    private BigDecimal additionalPrice;
}
