package cinemmaxbackend.web.controllers.reservation;

import cinemmaxbackend.general.classes.DTO.reservation.ReservationFullDetailsDTO;
import cinemmaxbackend.general.classes.DTO.reservation.ReservationMinDetailsDTO;
import cinemmaxbackend.general.classes.classic.reservation.Reservation;
import cinemmaxbackend.general.classes.commands.reservation.ReservationCommand;
import cinemmaxbackend.security.services.user.UserService;
import cinemmaxbackend.web.services.reservation.ReservationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/reservations")
@RequiredArgsConstructor
public class ReservationController {
    private final ReservationService reservationService;
    private final UserService userService;

    @GetMapping("/getAll")
    public List<Reservation> getAllReservations() {
        return reservationService.getAllReservations();
    }


    @PreAuthorize("hasRole('admin')")
    @PostMapping("/add")
    public ResponseEntity<Map<String, String>> add(@Valid @RequestBody ReservationCommand command) {
        return reservationService.add(command);
    }

    @GetMapping("/getMinDetails")
    public List<ReservationMinDetailsDTO> getMinReservationDetails() {
        //Show only those reservations that the currently logged-in user has
        return reservationService.getMinDetails(userService.getCurrentUser().getId());
    }

    //show details only if the user is the real owner of that reservation
    @PreAuthorize("@reservationOwnership.isOwner(#id, authentication)")
    @GetMapping("/getFullDetails/{id}")
    public ReservationFullDetailsDTO getFullReservationDetails(@PathVariable Long id) {
        return reservationService.getFullDetails(id);
    }

}
