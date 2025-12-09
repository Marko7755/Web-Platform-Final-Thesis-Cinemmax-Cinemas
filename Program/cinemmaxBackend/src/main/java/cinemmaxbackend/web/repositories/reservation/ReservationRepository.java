package cinemmaxbackend.web.repositories.reservation;

import cinemmaxbackend.general.classes.classic.reservation.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;


public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    /*boolean existsByShowTimeIdAndSeatId(Long showTimeId, Long seatId);
    Optional<Reservation> findByShowTimeIdAndSeatId(Long showTimeId, Long seatId);*/

    List<Reservation> findByUser_Id(Long id);

    boolean existsByIdAndUser_UsernameIgnoreCase(Long id, String userUsername);

}
