package cinemmaxbackend.web.repositories.reservation;

import cinemmaxbackend.general.classes.classic.reservation.ReservationSeat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReservationSeatRepository extends JpaRepository<ReservationSeat, Long> {
    boolean existsByReservation_ShowTime_IdAndSeatId(Long reservationShowTimeId, Long seatId);
}
