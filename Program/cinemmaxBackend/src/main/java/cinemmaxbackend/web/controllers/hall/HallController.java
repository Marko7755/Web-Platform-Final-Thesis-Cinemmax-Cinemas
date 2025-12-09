package cinemmaxbackend.web.controllers.hall;

import cinemmaxbackend.general.classes.classic.hall.Hall;
import cinemmaxbackend.general.classes.commands.hall.HallCommand;
import cinemmaxbackend.web.services.hall.HallService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@AllArgsConstructor
@RequestMapping("/api/halls")
public class HallController {
    private final HallService hallService;

    @GetMapping("/getAll")
    public List<Hall> getAllHalls() {
        return hallService.getAll();
    }


    @GetMapping("/getById/{id}")
    public ResponseEntity<Hall> getById(@PathVariable Long id) {
        return hallService.getById(id);
    }

    @PreAuthorize("hasRole('admin')")
    @PostMapping("/save")
    public ResponseEntity<Map<String, String>> addHall(@RequestBody @Valid HallCommand hallCommand) {
        return hallService.add(hallCommand);
    }

    @GetMapping("/getAllByCinemaId/{id}")
    public ResponseEntity<List<Hall>> getByCinemaId(@PathVariable Long id) {
        return hallService.getAllByCinemaId(id);
    }

}
