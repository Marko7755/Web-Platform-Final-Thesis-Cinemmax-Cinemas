package cinemmaxbackend.web.controllers.ShowType;

import cinemmaxbackend.general.classes.classic.ShowType.ShowType;
import cinemmaxbackend.general.classes.classic.cinema.Cinema;
import cinemmaxbackend.general.classes.commands.ShowType.ShowTypeCommand;
import cinemmaxbackend.general.classes.commands.cinema.CinemaCommand;
import cinemmaxbackend.web.services.ShowType.ShowTypeService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@AllArgsConstructor
@RequestMapping("/api/showTypes")
public class ShowTypeController {
    private final ShowTypeService showTypeService;

    @PreAuthorize("hasRole('admin')")
    @PostMapping("/save")
    public ResponseEntity<Map<String, String>> add(@RequestBody @Valid ShowTypeCommand showTypeCommand) {
        return showTypeService.add(showTypeCommand);
    }

    @GetMapping("/getAll")
    public List<ShowType> getAll() {
        return showTypeService.getAll();
    }


    @GetMapping("/getById/{id}")
    public ResponseEntity<ShowType> getById(@PathVariable Long id) {
        return showTypeService.getById(id);
    }

}
