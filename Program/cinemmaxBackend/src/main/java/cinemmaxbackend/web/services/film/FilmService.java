package cinemmaxbackend.web.services.film;

import cinemmaxbackend.general.classes.DTO.film.BestRatedFilmDTO;
import cinemmaxbackend.general.classes.DTO.film.FilmPreviewDTO;
import cinemmaxbackend.general.classes.classic.actor.Actor;
import cinemmaxbackend.general.classes.classic.director.Director;
import cinemmaxbackend.general.classes.classic.film.Film;
import cinemmaxbackend.general.classes.classic.film.FilmMapper;
import cinemmaxbackend.general.classes.commands.film.FilmCommand;
import cinemmaxbackend.general.classes.commands.film.NameSurnamePair;
import cinemmaxbackend.general.classes.exceptions.DuplicateEntities.DuplicateFilmException;
import cinemmaxbackend.general.classes.exceptions.NotFound.FilmNotFoundException;
import cinemmaxbackend.web.repositories.actor.ActorRepository;
import cinemmaxbackend.web.repositories.director.DirectorRepository;
import cinemmaxbackend.web.repositories.film.FilmRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@AllArgsConstructor
public class FilmService {
    private final FilmRepository filmRepository;
    private final ActorRepository actorRepository;
    private final DirectorRepository directorRepository;
    private final FilmMapper filmMapper;

/*    private List<Actor> convertToActor(List<NameSurnamePair> nameSurnamePairs) {
        return nameSurnamePairs.stream()
                .map(p -> actorRepository.findByNameAndSurname(p.getName(), p.getSurname())
                        .orElseThrow(() -> new ActorNotFoundException("Actor not found: " + p.getName() + " " + p.getSurname())))
                .collect(Collectors.toList());
    }

    private List<Director> convertToDirector(List<NameSurnamePair> nameSurnamePairs) {
        return nameSurnamePairs.stream()
                .map(p -> directorRepository.findByNameAndSurname(p.getName(), p.getSurname())
                        .orElseThrow(() -> new DirectorNotFoundException("Director not found: " + p.getName() + " " + p.getSurname())))
                .collect(Collectors.toList());
    }*/

    @Transactional
    public ResponseEntity<Map<String, String>> addFilm(FilmCommand filmCommand) {
        if (checkDuplicate(filmCommand.getName())) {
            throw new DuplicateFilmException("Film with name: " + filmCommand.getName() + " already exists");
        } else {
            List<Actor> existingActors = new ArrayList<>();
            List<Actor> newActors = new ArrayList<>();

            for (NameSurnamePair a : filmCommand.getActors()) {
                Optional<Actor> found = actorRepository.findByNameAndSurname(a.getName(), a.getSurname());

                if (found.isPresent()) {
                    existingActors.add(found.get());
                } else {
                    Actor newActor = new Actor();
                    newActor.setName(a.getName());
                    newActor.setSurname(a.getSurname());
                    newActors.add(newActor);
                }
            }

            List<Actor> savedActors = actorRepository.saveAll(newActors);

            List<Actor> allActors = new ArrayList<>();
            allActors.addAll(existingActors);
            allActors.addAll(savedActors);


            List<Director> existingDirectors = new ArrayList<>();
            List<Director> newDirectors = new ArrayList<>();

            for (NameSurnamePair d : filmCommand.getDirectors()) {
                Optional<Director> found = directorRepository.findByNameAndSurname(d.getName(), d.getSurname());

                if (found.isPresent()) {
                    existingDirectors.add(found.get());
                } else {
                    Director newDirector = new Director();
                    newDirector.setName(d.getName());
                    newDirector.setSurname(d.getSurname());
                    newDirectors.add(newDirector);
                }
            }

            List<Director> savedDirectors = directorRepository.saveAll(newDirectors);

            List<Director> allDirectors = new ArrayList<>();
            allDirectors.addAll(existingDirectors);
            allDirectors.addAll(savedDirectors);

            Film filmToSave = filmMapper.mapForCreate(filmCommand, allActors, allDirectors);

            Film savedFilm = filmRepository.save(filmToSave);
            if (savedFilm.getId() != null) {
                return ResponseEntity.ok(Map.of("message", "Film saved successfully"));
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(Map.of("error", "Failed to save film"));
            }
        }
    }

    private boolean checkDuplicate(String filmName) {
        Optional<Film> filmOpt = filmRepository.findByNameIgnoreCase(filmName);
        return filmOpt.isPresent();
    }

    public List<FilmPreviewDTO> getAllForAdmin() {
        return filmRepository.findAllByOrderByCinemaReleaseDateAsc().stream()
                .map(film -> FilmPreviewDTO.builder()
                        .id(film.getId())
                        .name(film.getName())
                        .imageUrl(film.getImageUrl())
                        .startDate(film.getCinemaReleaseDate())
                        .build())
                .toList();
    }

    public List<FilmPreviewDTO> getAllReleased() {
        return filmRepository.getAllReleased().stream()
                .map(film -> FilmPreviewDTO.builder()
                        .id(film.getId())
                        .name(film.getName())
                        .imageUrl(film.getImageUrl())
                        .startDate(film.getCinemaReleaseDate())
                        .build())
                .toList();
    }


    public List<FilmPreviewDTO> getUpcoming() {
        return filmRepository.getUpcoming().stream()
                .map(film -> FilmPreviewDTO.builder()
                        .id(film.getId())
                        .name(film.getName())
                        .imageUrl(film.getImageUrl())
                        .startDate(film.getCinemaReleaseDate())
                        .build())
                .toList();
    }


    public ResponseEntity<Film> getById(Long id) {
        Optional<Film> filmOpt = filmRepository.findById(id);
        return filmOpt.map(ResponseEntity::ok).orElseThrow(() -> new FilmNotFoundException("Film with id " + id + " not found"));
    }


    /*@Transactional
    public ResponseEntity<Map<String, String>> edit(@PathVariable Long id, @RequestBody FilmCommand filmCommand) {

        Film filmToEdit = filmRepository.findById(id).orElseThrow(()
                -> new FilmNotFoundException("Film with id " + id + " not found"));

        List<Actor> actors = convertToActor(filmCommand.getActors());
        List<Director> directors = convertToDirector(filmCommand.getDirectors());

        filmMapper.mapForUpdate(filmToEdit, filmCommand, actors, directors);

        //You don’t need filmRepository.save(filmToEdit) here because:
        //You used @Transactional
        //You loaded filmToEdit inside a @Transactional method → it’s a managed JPA entity -> Any in-place changes are tracked by Hibernate (dirty checking).
        //When you modify a managed entity (including its owning-side collections), Hibernate’s dirty checking detects the changes and flushes them on transaction commit (when the method returns).
        //Your actors/directors came from repositories too, so they’re also managed in the same persistence context. You’re just changing links.

        return ResponseEntity.ok(Map.of("message", "Film updated successfully"));
    }*/


    @Transactional
    public ResponseEntity<Map<String, String>> edit(@PathVariable Long id, @RequestBody FilmCommand filmCommand) {

        Film filmToEdit = filmRepository.findById(id).orElseThrow(()
                -> new FilmNotFoundException("Film with id " + id + " not found"));


        List<Actor> existingActors = new ArrayList<>();
        List<Actor> newActors = new ArrayList<>();

        for (NameSurnamePair a : filmCommand.getActors()) {
            Optional<Actor> found = actorRepository.findByNameAndSurname(a.getName(), a.getSurname());
            if (found.isPresent()) {
                existingActors.add(found.get());
            } else {
                Actor newActor = new Actor();
                newActor.setName(a.getName());
                newActor.setSurname(a.getSurname());
                newActors.add(newActor);
            }
        }

        List<Actor> savedActors = actorRepository.saveAll(newActors);

        List<Actor> allActors = new ArrayList<>();
        allActors.addAll(existingActors);
        allActors.addAll(savedActors);


        List<Director> existingDirectors = new ArrayList<>();
        List<Director> newDirectors = new ArrayList<>();

        for (NameSurnamePair d : filmCommand.getDirectors()) {
            Optional<Director> found = directorRepository.findByNameAndSurname(d.getName(), d.getSurname());
            if (found.isPresent()) {
                existingDirectors.add(found.get());
            } else {
                Director newDirector = new Director();
                newDirector.setName(d.getName());
                newDirector.setSurname(d.getSurname());
                newDirectors.add(newDirector);
            }
        }

        List<Director> savedDirectors = directorRepository.saveAll(newDirectors);

        List<Director> allDirectors = new ArrayList<>();
        allDirectors.addAll(existingDirectors);
        allDirectors.addAll(savedDirectors);


        filmMapper.mapForUpdate(filmToEdit, filmCommand, allActors, allDirectors);

        //You don’t need filmRepository.save(filmToEdit) here because:
        //You used @Transactional
        //You loaded filmToEdit inside a @Transactional method → it’s a managed JPA entity -> Any in-place changes are tracked by Hibernate (dirty checking).
        //When you modify a managed entity (including its owning-side collections), Hibernate’s dirty checking detects the changes and flushes them on transaction commit (when the method returns).
        //Your actors/directors came from repositories too, so they’re also managed in the same persistence context. You’re just changing links.

        return ResponseEntity.ok(Map.of("message", "Film updated successfully"));
    }


    @Transactional
    public ResponseEntity<Map<String, String>> deleteById(Long id) {
        /*Film foundFilm = */
        filmRepository.findById(id).orElseThrow(()
                -> new FilmNotFoundException("Film with id " + id + " not found"));

        /*foundFilm.getActors().clear();
        foundFilm.getDirectors().clear();
        filmRepository.save(foundFilm);*/

        filmRepository.deleteById(id);

        if (!filmRepository.existsById(id)) {
            return ResponseEntity.status(204).build();
        } else {
            return ResponseEntity.badRequest().body(Map.of("message", "There was an error while deleting the film"));
        }
    }


    public List<FilmPreviewDTO> getEndingSoon() {
        return filmRepository.getEndingIn10Days().stream()
                .map(film -> FilmPreviewDTO.builder()
                        .id(film.getId())
                        .name(film.getName())
                        .imageUrl(film.getImageUrl())
                        .endDate(film.getCinemaEndDate())
                        .build())
                .toList();
    }

    public List<BestRatedFilmDTO> getBestRatedForAdmin() {
        return filmRepository.getBestRatedForAdmin().stream()
                .map(film -> new BestRatedFilmDTO(film.getId(), film.getName(),
                        film.getImageUrl(), film.getImdbRating()))
                .toList();
    }

    public List<BestRatedFilmDTO> getBestRatedReleased() {
        return filmRepository.getBestRatedReleased().stream()
                .map(film -> new BestRatedFilmDTO(film.getId(), film.getName(),
                        film.getImageUrl(), film.getImdbRating()))
                .toList();
    }


    public List<String> getAllGenres() {
        return filmRepository.getAllDistinctGenres();
    }

    public List<FilmPreviewDTO> getAllByGenreForAdmin(String genre) {
        return filmRepository.getByGenreIgnoreCase(genre).stream()
                .map(film -> FilmPreviewDTO.builder()
                        .id(film.getId())
                        .name(film.getName())
                        .imageUrl(film.getImageUrl())
                        .startDate(film.getCinemaReleaseDate())
                        .build())
                .toList();
    }

    public List<FilmPreviewDTO> getAllByGenre(String genre) {
        return filmRepository.getByGenreIgnoreCaseReleased(genre).stream()
                .map(film -> FilmPreviewDTO.builder()
                        .id(film.getId())
                        .name(film.getName())
                        .imageUrl(film.getImageUrl())
                        .startDate(film.getCinemaReleaseDate())
                        .build())
                .toList();
    }


}