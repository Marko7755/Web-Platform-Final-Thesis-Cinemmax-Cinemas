package cinemmaxbackend.web.controllers.actor;

import cinemmaxbackend.general.classes.DTO.actor.ActorDTO;
import cinemmaxbackend.general.classes.classic.actor.Actor;
import cinemmaxbackend.general.classes.commands.actor.ActorCommand;
import cinemmaxbackend.web.services.actor.ActorService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/actors")
@AllArgsConstructor
public class ActorController {
    private final ActorService actorService;

    @GetMapping("/findByInput")
    public List<ActorDTO> findByInput(@RequestParam String input) {
        return actorService.findActorsByInput(input);
    }

    @PostMapping("/save")
    public ResponseEntity<Map<String, String>> addActor(@RequestBody @Valid ActorCommand actorCommand) {
        return actorService.addActor(actorCommand);
    }
}
