package cinemmaxbackend.web.repositories.ShowTime;

import cinemmaxbackend.general.classes.classic.ShowTime.ShowTime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ShowTimeRepository extends JpaRepository<ShowTime, Long> {
    Optional<ShowTime> findById(long id);
    Optional<ShowTime> findByHall_IdAndShowTime(Long hallId, LocalDateTime showTime);
    List<ShowTime> findByFilmId(Long filmId);
    List<ShowTime> findByHall_Cinema_Location(String location);

    List<ShowTime> findByFilmIdAndHall_Cinema_LocationOrFilmIdAndHall_Cinema_Name(Long filmId1, String hallCinemaLocation,
                                                                                  Long filmId2, String hallCinemaName);
}
