package cinemmaxbackend.general.classes.DTO.cinema;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CinemaDTO {
    private Long id;
    private String location;
    private String name;
}
