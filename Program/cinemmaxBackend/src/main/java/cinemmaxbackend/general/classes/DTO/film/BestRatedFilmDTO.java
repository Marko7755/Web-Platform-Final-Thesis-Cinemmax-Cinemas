package cinemmaxbackend.general.classes.DTO.film;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class BestRatedFilmDTO {
    private Long id;
    private String name;
    private String imageUrl;
    private BigDecimal imdbRating;
}
