package cinemmaxbackend.web.repositories.hall;

import cinemmaxbackend.general.classes.classic.hall.Hall;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface HallRepository extends JpaRepository<Hall, Long> {
    Optional<Hall> findByCinema_IdAndNumber(Long cinemaId, Integer number);
    Optional<List<Hall>> findAllByCinema_Id(Long cinemaId);
}
