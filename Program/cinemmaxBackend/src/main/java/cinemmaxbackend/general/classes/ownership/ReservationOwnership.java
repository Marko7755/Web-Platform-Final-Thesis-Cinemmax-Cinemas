package cinemmaxbackend.general.classes.ownership;

import cinemmaxbackend.general.classes.exceptions.Ownership.ReservationOwnershipException;
import cinemmaxbackend.web.repositories.reservation.ReservationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component("reservationOwnership")
@RequiredArgsConstructor
public class ReservationOwnership {
    private final ReservationRepository reservationRepository;

    public boolean isOwner(Long reservationId, Authentication auth) {
        if (auth == null || !auth.isAuthenticated()) return false;
        String username = auth.getName();
        if(!reservationRepository.existsByIdAndUser_UsernameIgnoreCase(reservationId, username)) {
            throw new ReservationOwnershipException("You cannot view details for reservations that are not yours!");
        }
        return true;
    }

}
