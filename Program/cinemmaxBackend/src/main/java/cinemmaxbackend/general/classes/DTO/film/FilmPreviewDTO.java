package cinemmaxbackend.general.classes.DTO.film;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;


@Data
@Builder
public class FilmPreviewDTO {
    private Long id;
    private String name;
    private String imageUrl;
    private LocalDate startDate;
    private LocalDate endDate;
}
