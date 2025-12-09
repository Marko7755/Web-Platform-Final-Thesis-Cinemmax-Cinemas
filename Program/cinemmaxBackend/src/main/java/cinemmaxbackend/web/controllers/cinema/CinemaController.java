package cinemmaxbackend.web.controllers.cinema;

import cinemmaxbackend.general.classes.classic.cinema.Cinema;
import cinemmaxbackend.general.classes.commands.cinema.CinemaCommand;
import cinemmaxbackend.web.services.cinema.CinemaService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/cinemas")
@AllArgsConstructor
public class CinemaController {
    private final CinemaService cinemaService;

    @GetMapping("/getAll")
    public List<Cinema> getAllCinemas() {
        return cinemaService.getAll();
    }


    @GetMapping("/getById/{id}")
    public ResponseEntity<Cinema> getById(@PathVariable Long id) {
        return cinemaService.getById(id);
    }

    @PreAuthorize("hasRole('admin')")
    @PostMapping("/save")
    public ResponseEntity<Map<String, String>> addCinema(@RequestBody @Valid CinemaCommand cinemaCommand) {
        return cinemaService.add(cinemaCommand);
    }

    @PreAuthorize("hasRole('admin')")
    @PutMapping("/edit/{id}")
    public ResponseEntity<Map<String, String>> edit(@PathVariable Long id, @RequestBody @Valid CinemaCommand cinemaCommand)  {
        return cinemaService.edit(id, cinemaCommand);
    }

    @PreAuthorize("hasRole('admin')")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Map<String, String>> delete(@PathVariable Long id) {
        return cinemaService.delete(id);
    }

}
