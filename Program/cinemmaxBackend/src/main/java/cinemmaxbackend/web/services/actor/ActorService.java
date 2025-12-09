package cinemmaxbackend.web.services.actor;

import cinemmaxbackend.general.classes.DTO.actor.ActorDTO;
import cinemmaxbackend.general.classes.classic.actor.Actor;
import cinemmaxbackend.general.classes.classic.director.Director;
import cinemmaxbackend.general.classes.commands.actor.ActorCommand;
import cinemmaxbackend.general.classes.exceptions.DuplicateEntities.DuplicateActorException;
import cinemmaxbackend.general.classes.exceptions.DuplicateEntities.DuplicateHallException;
import cinemmaxbackend.web.repositories.actor.ActorRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@Data
@AllArgsConstructor
public class ActorService {
    private ActorRepository actorRepository;

    public List<ActorDTO> findActorsByInput(String input) {
        if (input.length() < 2) return List.of();
        return actorRepository.findByInput(input)
                .stream()
                .map(ActorDTO::fromEntity)
                .toList();
    }

    public ResponseEntity<Map<String, String>> addActor(ActorCommand actorCommand) {
        if (checkDuplicate(actorCommand.getName(), actorCommand.getSurname())) {
            throw new DuplicateActorException("Actor: " + actorCommand.getName() + " " +
                    actorCommand.getSurname() + " already exists");
        }
        else {
            Actor actorToSave = Actor.convertToActor(actorCommand);

            Actor savedActor = actorRepository.save(actorToSave);
            if (savedActor.getId() != null) {
                return ResponseEntity.ok(Map.of("message", "Actor saved successfully"));
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(Map.of("error", "Failed to save actor"));
            }
        }

    }

    private boolean checkDuplicate(String name, String surname) {
        Optional<Actor> opt = actorRepository.findByNameAndSurname(name, surname);
        return opt.isPresent();
    }

}
