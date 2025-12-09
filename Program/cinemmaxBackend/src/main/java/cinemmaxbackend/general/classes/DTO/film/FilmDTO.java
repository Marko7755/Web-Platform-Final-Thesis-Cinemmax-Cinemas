package cinemmaxbackend.general.classes.DTO.film;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class FilmDTO {
    private Long id;
    private String name;
    private BigDecimal imdbRating;
    private Integer ageRate;
    private String imageUrl;
}
