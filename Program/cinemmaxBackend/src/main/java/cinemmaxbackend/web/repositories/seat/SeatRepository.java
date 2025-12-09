package cinemmaxbackend.web.repositories.seat;

import cinemmaxbackend.general.classes.classic.cinema.Cinema;
import cinemmaxbackend.general.classes.classic.seat.Seat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SeatRepository extends JpaRepository<Seat, Long> {
    Optional<Seat> findByHallIdAndRowLetterAndSeatNumberAndHall_CinemaId(Long hallId, Character rowLetter,
                                                                         Integer seatNumber, Long hallCinemaId);
    Long countByHall_Id(Long hallId);

    List<Seat> findByHall_Id(Long hallId);
}
