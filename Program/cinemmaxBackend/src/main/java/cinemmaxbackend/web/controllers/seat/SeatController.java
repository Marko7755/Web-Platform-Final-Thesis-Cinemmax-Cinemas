package cinemmaxbackend.web.controllers.seat;

import cinemmaxbackend.general.classes.DTO.seat.SeatDTO;
import cinemmaxbackend.general.classes.classic.hall.Hall;
import cinemmaxbackend.general.classes.classic.seat.Seat;
import cinemmaxbackend.general.classes.commands.hall.HallCommand;
import cinemmaxbackend.general.classes.commands.seat.SeatCommand;
import cinemmaxbackend.web.services.seat.SeatService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@AllArgsConstructor
@RequestMapping("/api/seats")
public class SeatController {
    private final SeatService seatService;

    @GetMapping("/getAll")
    public List<Seat> getAllSeats() {
        return seatService.getAll();
    }

    @GetMapping("/getAllByHallId/{id}")
    public List<SeatDTO> getAllSeatsByHallId(@PathVariable Long id) {
        return seatService.getAllByHallId(id);
    }


    @GetMapping("/getById/{id}")
    public ResponseEntity<Seat> getById(@PathVariable Long id) {
        return seatService.getById(id);
    }

    @PreAuthorize("hasRole('admin')")
    @PostMapping("/save")
    public ResponseEntity<?> addHall(@RequestBody @Valid SeatCommand seatCommand) {
        return seatService.add(seatCommand);
    }


}
