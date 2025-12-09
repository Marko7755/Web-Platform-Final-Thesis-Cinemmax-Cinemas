package cinemmaxbackend.general.classes.DTO.ShowTimeDetails;

import cinemmaxbackend.general.classes.DTO.ShowType.ShowTypeDTO;
import cinemmaxbackend.general.classes.DTO.cinema.CinemaDTO;
import cinemmaxbackend.general.classes.DTO.film.FilmDTO;
import cinemmaxbackend.general.classes.DTO.hall.HallDTO;
import cinemmaxbackend.general.classes.DTO.pricing.PricingDTO;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class ShowTimeDetailsDTO {
    private Long id;
    private LocalDateTime showTime;
    private FilmDTO film;
    private ShowTypeDTO showType;
    private HallDTO hall;
    private CinemaDTO cinema;
    private PricingDTO pricing;
}
