package cinemmaxbackend.general.classes.commands.film;

import cinemmaxbackend.general.interfaces.OnCreate;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
public class FilmCommand {
    @NotBlank(message = "Name is required")
    private String name;

    @NotBlank(message = "Genre is required")
    private String genre;

    @NotBlank(message = "About is required")
    private String about;

    @NotNull(message = "IMDB rating must not be null")
    @DecimalMin(value = "0.0", message = "IMDB rating must be at least 0.0")
    @DecimalMax(value = "10.0", message = "IMDB rating must be at most 10.0")
    private BigDecimal imdbRating;

    @NotNull(message = "Age rate is required")
    @Min(value = 0, message = "Age rate must be 0 or greater")
    private Integer ageRate;

    @NotNull(message = "Duration is required")
    @Min(value = 1, message = "Duration must be greater than 0")
    private Integer duration;

    @NotBlank(message = "Image URL is required")
    @Pattern(
            regexp = "https?://.+",
            message = "Image URL must be a valid URL"
    )
    private String imageUrl;

    @NotBlank(message = "Trailer URL is required")
    @Pattern(
            regexp = "https?://.+",
            message = "Trailer URL must be a valid URL"
    )
    private String trailerUrl;

    @NotNull(message = "Cinema release date is required")
    //This validator is for OnCreate(Film creation) only, not for Edit
    @FutureOrPresent(groups = OnCreate.class, message = "Cinema release date cannot be in the past")
    private LocalDate cinemaReleaseDate;

    @NotNull(message = "Cinema end date is required")
    @FutureOrPresent(groups = OnCreate.class, message = "Cinema end date cannot be in the past")
    private LocalDate cinemaEndDate;

    @NotEmpty(message = "At least one actor is required")
    private List<@Valid NameSurnamePair> actors;

    @NotEmpty(message = "At least one director is required")
    private List<@Valid NameSurnamePair> directors;

}
