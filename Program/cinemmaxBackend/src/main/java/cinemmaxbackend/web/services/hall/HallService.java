package cinemmaxbackend.web.services.hall;

import cinemmaxbackend.general.classes.classic.cinema.Cinema;
import cinemmaxbackend.general.classes.classic.hall.Hall;
import cinemmaxbackend.general.classes.commands.hall.HallCommand;
import cinemmaxbackend.general.classes.exceptions.DuplicateEntities.DuplicateHallException;
import cinemmaxbackend.general.classes.exceptions.NotFound.CinemaNotFoundException;
import cinemmaxbackend.general.classes.exceptions.NotFound.HallNotFoundException;
import cinemmaxbackend.web.repositories.cinema.CinemaRepository;
import cinemmaxbackend.web.repositories.hall.HallRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@AllArgsConstructor
public class HallService {
    private HallRepository hallRepository;
    private CinemaRepository cinemaRepository;

    public List<Hall> getAll() {
        return hallRepository.findAll();
    }


    public ResponseEntity<Hall> getById(Long id) {
        Optional<Hall> hallOpt = hallRepository.findById(id);
        return hallOpt.map(ResponseEntity::ok).orElseThrow(() -> new HallNotFoundException("Hall with id " + id + " not found"));
    }


    public ResponseEntity<Map<String, String>> add(HallCommand hallCommand) {
        if (checkDuplicate(hallCommand.getCinemaId(), hallCommand.getNumber())) {
            throw new DuplicateHallException("Hall number: " + hallCommand.getNumber() + " already exists for this cinema");
        } else {
            Optional<Cinema> cinemaOpt = cinemaRepository.findById(hallCommand.getCinemaId());
            if (cinemaOpt.isPresent()) {
                Hall cinemaToSave = Hall.convertToHall(hallCommand);
                cinemaToSave.setCinema(cinemaOpt.get());

                Hall savedCinema = hallRepository.save(cinemaToSave);
                if (savedCinema.getId() != null) {
                    return ResponseEntity.ok(Map.of("message", "Hall saved successfully"));
                } else {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                            .body(Map.of("error", "Failed to save hall"));
                }
            } else {
                throw new CinemaNotFoundException("Cinema with id: " + hallCommand.getCinemaId() + " does not exist");
            }

        }
    }

    public ResponseEntity<List<Hall>> getAllByCinemaId(Long cinemaId) {
        if(cinemaRepository.findById(cinemaId).isEmpty()) {
            throw new CinemaNotFoundException("Cinema with id: " + cinemaId + " not found");
        }
        Optional<List<Hall>> halls = hallRepository.findAllByCinema_Id(cinemaId);
        return halls.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }


    private boolean checkDuplicate(Long cinemaId, Integer hallNumber) {
        Optional<Hall> hallOpt = hallRepository.findByCinema_IdAndNumber(cinemaId, hallNumber);
        return hallOpt.isPresent();
    }

}
