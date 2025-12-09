package cinemmaxbackend.web.services.director;

import cinemmaxbackend.general.classes.DTO.director.DirectorDTO;
import cinemmaxbackend.general.classes.classic.cinema.Cinema;
import cinemmaxbackend.general.classes.classic.director.Director;
import cinemmaxbackend.general.classes.commands.director.DirectorCommand;
import cinemmaxbackend.general.classes.exceptions.DuplicateEntities.DuplicateCinemaException;
import cinemmaxbackend.general.classes.exceptions.DuplicateEntities.DuplicateDirectorException;
import cinemmaxbackend.general.classes.exceptions.DuplicateEntities.DuplicateHallException;
import cinemmaxbackend.web.repositories.director.DirectorRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@AllArgsConstructor
public class DirectorService {
    private DirectorRepository directorRepository;

    public List<DirectorDTO> findDirectorsByInput(String input) {
        if (input.length() < 2) return List.of();
        return directorRepository.findByInput(input)
                .stream()
                .map(DirectorDTO::fromEntity)
                .toList();
    }

    public ResponseEntity<Map<String, String>> addDirector(DirectorCommand directorCommand) {
        if (checkDuplicate(directorCommand.getName(), directorCommand.getSurname())) {
            throw new DuplicateDirectorException("Director: " + directorCommand.getName() + " " +
                    directorCommand.getSurname() + " already exists");
        }
        else {
            Director directorToSave = Director.convertToDirector(directorCommand);

            Director savedDirector = directorRepository.save(directorToSave);
            if (savedDirector.getId() != null) {
                return ResponseEntity.ok(Map.of("message", "Director saved successfully"));
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(Map.of("error", "Failed to save director"));
            }
        }

    }

    private boolean checkDuplicate(String name, String surname) {
        Optional<Director> opt = directorRepository.findByNameAndSurname(name, surname);
        return opt.isPresent();
    }

}
