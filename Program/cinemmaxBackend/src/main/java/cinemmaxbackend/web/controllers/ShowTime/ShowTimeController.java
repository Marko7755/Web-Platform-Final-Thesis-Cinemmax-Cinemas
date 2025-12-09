package cinemmaxbackend.web.controllers.ShowTime;

import cinemmaxbackend.general.classes.DTO.ShowTimeDetails.ShowTimeDetailsDTO;
import cinemmaxbackend.general.classes.DTO.seat.SeatDTO;
import cinemmaxbackend.general.classes.classic.ShowTime.ShowTime;
import cinemmaxbackend.general.classes.commands.ShowTime.ShowTimeCommand;
import cinemmaxbackend.web.services.ShowTime.ShowTimeService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@AllArgsConstructor
@RequestMapping("/api/showTimes")
public class ShowTimeController {
    private final ShowTimeService showTimeService;

    @PreAuthorize("hasRole('admin')")
    @PostMapping("/save")
    public ResponseEntity<Map<String, String>> add(@RequestBody @Valid ShowTimeCommand showTimeCommand) {
        return showTimeService.add(showTimeCommand);
    }


    @GetMapping("/getAll")
    public List<ShowTime> getAll() {
        return showTimeService.getAll();
    }

    @GetMapping("/getAllByFilmId/{id}")
    public List<ShowTime> getAllByFilmId(@PathVariable Long id) {
        return showTimeService.getAllByFilmId(id);
    }

    @GetMapping("/getAllByLocation")
    public List<ShowTime> getAllByFilmId(@RequestParam String location) {
        return showTimeService.getAllByLocation(location);
    }

    @GetMapping("/getAllByFilmIdAndLocation/{id}")
    public List<ShowTime> getAllByFilmIdAndLocation(@PathVariable Long id, @RequestParam String location) {
        return showTimeService.getAllByFilmIdAndLocation(id, location);
    }

    @GetMapping("/getShowTimeDetails/{id}")
    public ShowTimeDetailsDTO getShowTimeDetails(@PathVariable Long id) {
        return showTimeService.getShowTimeDetails(id);
    }

    @GetMapping("/getSeatsForShowTime/{id}")
    public List<SeatDTO> getAllSeatsForShowTime(@PathVariable Long id) {
        return showTimeService.getAllSeatsForShowTime(id);
    }

}
