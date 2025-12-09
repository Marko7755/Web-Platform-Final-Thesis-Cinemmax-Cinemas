package cinemmaxbackend.web.repositories.cinema;

import cinemmaxbackend.general.classes.classic.cinema.Cinema;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CinemaRepository extends JpaRepository<Cinema, Long> {
    Optional<Cinema> findByNameIgnoreCase(String name);
}
