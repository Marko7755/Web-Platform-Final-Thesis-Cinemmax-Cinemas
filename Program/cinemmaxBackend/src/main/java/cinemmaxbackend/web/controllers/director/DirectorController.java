package cinemmaxbackend.web.controllers.director;

import cinemmaxbackend.general.classes.DTO.director.DirectorDTO;
import cinemmaxbackend.general.classes.commands.actor.ActorCommand;
import cinemmaxbackend.general.classes.commands.director.DirectorCommand;
import cinemmaxbackend.web.services.director.DirectorService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/directors")
@AllArgsConstructor
public class DirectorController {
    private final DirectorService directorService;

    @GetMapping("/findByInput")
    public List<DirectorDTO> findByInput(@RequestParam String input) {
        return directorService.findDirectorsByInput(input);
    }

    @PostMapping("/save")
    public ResponseEntity<Map<String, String>> addDirector(@RequestBody @Valid DirectorCommand directorCommand) {
       return directorService.addDirector(directorCommand);
    }
}
