package cinemmaxbackend.web.controllers.film;

import cinemmaxbackend.general.classes.DTO.film.BestRatedFilmDTO;
import cinemmaxbackend.general.classes.DTO.film.FilmPreviewDTO;
import cinemmaxbackend.general.classes.classic.film.Film;
import cinemmaxbackend.general.classes.commands.film.FilmCommand;
import cinemmaxbackend.general.interfaces.OnCreate;
import cinemmaxbackend.web.services.film.FilmService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/film")
@AllArgsConstructor
public class FilmController {
    private final FilmService filmService;

    @PreAuthorize("hasRole('admin')")
    @PostMapping("/save")
    //Here, the validatior uses OnCreate group so the releaseDate is validated as well and in edit is not
    public ResponseEntity<Map<String, String>> addFilm(@RequestBody @Validated(OnCreate.class) FilmCommand filmCommand) {
        return filmService.addFilm(filmCommand);
    }

    @GetMapping("/getAllReleased")
    public List<FilmPreviewDTO> getAllReleased() {
        return filmService.getAllReleased();
    }

    @GetMapping("/getAllForAdmin")
    public List<FilmPreviewDTO> getAllForAdmin() {
        return filmService.getAllForAdmin();
    }

    @GetMapping("/getUpcoming")
    public List<FilmPreviewDTO> getUpcoming() {
        return filmService.getUpcoming();
    }

    @GetMapping("/getById/{id}")
    public ResponseEntity<Film> getById(@PathVariable Long id) {
        return filmService.getById(id);
    }

    @PreAuthorize("hasRole('admin')")
    @PutMapping("/edit/{id}")
    public ResponseEntity<Map<String, String>> edit(@PathVariable Long id, @RequestBody @Valid FilmCommand filmCommand) {
        return filmService.edit(id, filmCommand);
    }

    @PreAuthorize("hasRole('admin')")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Map<String, String>> delete(@PathVariable Long id) {
        return filmService.deleteById(id);
    }


    @GetMapping("/getEndingIn10Days")
    public List<FilmPreviewDTO> getEndingIn10Days() {
        return filmService.getEndingSoon();
    }

    @GetMapping("/getBestRatedForAdmin")
    public List<BestRatedFilmDTO> getBestRatedForAdmin() {
        return filmService.getBestRatedForAdmin();
    }

    @GetMapping("/getBestRatedReleased")
    public List<BestRatedFilmDTO> getBestRatedReleased() {
        return filmService.getBestRatedReleased();
    }

    @GetMapping("/getAllGenres")
    public List<String> getAllGenres() {
        return filmService.getAllGenres();
    }

    @GetMapping("/getAllByGenreForAdmin")
    public List<FilmPreviewDTO> getAllGenresForAdmin(@RequestParam String genre) {
        return filmService.getAllByGenreForAdmin(genre);
    }

    @GetMapping("/getAllByGenre")
    public List<FilmPreviewDTO> getAllGenres(@RequestParam String genre) {
        return filmService.getAllByGenre(genre);
    }

}
