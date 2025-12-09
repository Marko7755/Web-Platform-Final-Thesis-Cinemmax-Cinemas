package cinemmaxbackend.web.services.cinema;

import cinemmaxbackend.general.classes.classic.cinema.Cinema;
import cinemmaxbackend.general.classes.commands.cinema.CinemaCommand;
import cinemmaxbackend.general.classes.exceptions.DuplicateEntities.DuplicateCinemaException;
import cinemmaxbackend.general.classes.exceptions.NotFound.CinemaNotFoundException;
import cinemmaxbackend.general.classes.exceptions.NotFound.FilmNotFoundException;
import cinemmaxbackend.web.repositories.cinema.CinemaRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@AllArgsConstructor
public class CinemaService {
    private final CinemaRepository cinemaRepository;

    public List<Cinema> getAll() {
        return cinemaRepository.findAll();
    }


    public ResponseEntity<Cinema> getById(Long id) {
        Optional<Cinema> cinemaOpt = cinemaRepository.findById(id);
        return cinemaOpt.map(ResponseEntity::ok).orElseThrow(() -> new CinemaNotFoundException("Cinema with id " + id + " not found"));
    }


    public ResponseEntity<Map<String, String>> add(CinemaCommand cinemaCommand) {
        if (checkDuplicate(cinemaCommand.getName())) {
            throw new DuplicateCinemaException("Cinema with name: " + cinemaCommand.getName() + " already exists");
        } else {
            Cinema cinemaToSave = Cinema.convertToCinema(cinemaCommand);

            Cinema savedCinema = cinemaRepository.save(cinemaToSave);
            if (savedCinema.getId() != null) {
                return ResponseEntity.ok(Map.of("message", "Cinema saved successfully"));
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(Map.of("error", "Failed to save cinema"));
            }
        }
    }


    @Transactional
    public ResponseEntity<Map<String, String>> edit(Long id, CinemaCommand cinemaCommand) {

        Cinema cinemaToEdit = cinemaRepository.findById(id).orElseThrow(()
                -> new CinemaNotFoundException("Cinema with id " + id + " not found"));

        cinemaToEdit.setLocation(cinemaCommand.getLocation());
        cinemaToEdit.setName(cinemaCommand.getName());

        return ResponseEntity.ok(Map.of("message", "Cinema updated successfully"));
    }

    private boolean checkDuplicate(String cinemaName) {
        Optional<Cinema> cinemaOtp = cinemaRepository.findByNameIgnoreCase(cinemaName);
        return cinemaOtp.isPresent();
    }


    @Transactional
    public ResponseEntity<Map<String, String>> delete(Long id) {
        cinemaRepository.findById(id).orElseThrow(()
                -> new CinemaNotFoundException("Cinema with id " + id + " not found"));

        cinemaRepository.deleteById(id);

        if (!cinemaRepository.existsById(id)) {
            return ResponseEntity.status(204).build();
        } else {
            return ResponseEntity.badRequest().body(Map.of("message", "There was an error while deleting the cinema"));
        }
    }
}
