package cinemmaxbackend.web.repositories.reservation;

import cinemmaxbackend.general.classes.classic.reservation.BookedSeat;
import cinemmaxbackend.general.classes.classic.reservation.BookedSeatId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookedSeatsRepository extends JpaRepository<BookedSeat, BookedSeatId> {
    boolean existsByShowTime_IdAndSeat_Id(Long showTimeId, Long seatId);
}
