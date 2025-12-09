package cinemmaxbackend.web.services.ShowTime;

import cinemmaxbackend.general.classes.DTO.ShowTimeDetails.ShowTimeDetailsDTO;
import cinemmaxbackend.general.classes.DTO.ShowType.ShowTypeDTO;
import cinemmaxbackend.general.classes.DTO.cinema.CinemaDTO;
import cinemmaxbackend.general.classes.DTO.film.FilmDTO;
import cinemmaxbackend.general.classes.DTO.hall.HallDTO;
import cinemmaxbackend.general.classes.DTO.pricing.PricingDTO;
import cinemmaxbackend.general.classes.DTO.seat.SeatDTO;
import cinemmaxbackend.general.classes.classic.ShowTime.ShowTime;
import cinemmaxbackend.general.classes.classic.ShowType.ShowType;
import cinemmaxbackend.general.classes.classic.film.Film;
import cinemmaxbackend.general.classes.classic.hall.Hall;
import cinemmaxbackend.general.classes.classic.seat.Seat;
import cinemmaxbackend.general.classes.commands.ShowTime.ShowTimeCommand;
import cinemmaxbackend.general.classes.enums.Status;
import cinemmaxbackend.general.classes.exceptions.DuplicateEntities.DuplicateShowTimeException;
import cinemmaxbackend.general.classes.exceptions.NotFound.FilmNotFoundException;
import cinemmaxbackend.general.classes.exceptions.NotFound.HallNotFoundException;
import cinemmaxbackend.general.classes.exceptions.NotFound.ShowTimeNotFoundException;
import cinemmaxbackend.general.classes.exceptions.NotFound.ShowTypeNotFoundException;
import cinemmaxbackend.web.repositories.ShowTime.ShowTimeRepository;
import cinemmaxbackend.web.repositories.ShowType.ShowTypeRepository;
import cinemmaxbackend.web.repositories.film.FilmRepository;
import cinemmaxbackend.web.repositories.hall.HallRepository;
import cinemmaxbackend.web.repositories.reservation.BookedSeatsRepository;
import cinemmaxbackend.web.repositories.reservation.ReservationRepository;
import cinemmaxbackend.web.repositories.reservation.ReservationSeatRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ShowTimeService {
    private final ShowTimeRepository showTimeRepository;
    private final FilmRepository filmRepository;
    private final HallRepository hallRepository;
    private final ShowTypeRepository showTypeRepository;
    private final BookedSeatsRepository bookedSeatsRepository;

    private ShowTime convertToShowTime(ShowTimeCommand cmd) {
        Film f = filmRepository.findById(cmd.getFilmId()).orElseThrow(() ->
                new FilmNotFoundException("Film with id: " + cmd.getFilmId() + " not found"));
        Hall h = hallRepository.findById(cmd.getHallId()).orElseThrow(() ->
                new HallNotFoundException("Hall with id: " + cmd.getHallId() + " not found"));
        ShowType sT = showTypeRepository.findById(cmd.getTypeId()).orElseThrow(() ->
                new ShowTypeNotFoundException("Show type with id: " + cmd.getTypeId() + " not found"));
        ShowTime st = new ShowTime();
        st.setFilm(f);
        st.setHall(h);
        st.setShowTime(cmd.getShowTime());
        st.setShowType(sT);
        st.setBasePrice(cmd.getBasePrice());
        return st;
    }

    public List<ShowTime> getAll() {
        return showTimeRepository.findAll();
    }


    public List<ShowTime> getAllByFilmId(Long filmId) {
        return showTimeRepository.findByFilmId(filmId);
    }

    public ResponseEntity<Map<String, String>> add(ShowTimeCommand showTimeCommand) {
        ShowTime showTimeToAdd = convertToShowTime(showTimeCommand);

        if(checkDuplicate(showTimeCommand.getHallId(), showTimeCommand.getShowTime())) {
            throw new DuplicateShowTimeException("Show time for hall " + showTimeCommand.getHallId() +
                    " and show time " + showTimeCommand.getShowTime() + " already exists");
        }

        ShowTime added = showTimeRepository.save(showTimeToAdd);

        if(added.getId() != null) {
            return ResponseEntity.ok(Map.of("message", "Show Time saved successfully"));
        }
        else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message", "Error saving ShowTime"));
        }
    }

    private boolean checkDuplicate(Long hallId, LocalDateTime showTime) {
        Optional<ShowTime> opt = showTimeRepository.findByHall_IdAndShowTime(hallId, showTime);
        return opt.isPresent();
    }

    public List<ShowTime> getAllByLocation(String location) {
        return showTimeRepository.findByHall_Cinema_Location(location);
    }

    public List<ShowTime> getAllByFilmIdAndLocation(Long id, String cinemaNameOrLocation) {
        return showTimeRepository.findByFilmIdAndHall_Cinema_LocationOrFilmIdAndHall_Cinema_Name
                (id, cinemaNameOrLocation, id, cinemaNameOrLocation);
    }


    public ShowTimeDetailsDTO getShowTimeDetails(Long showTimeId) {
        ShowTime st = showTimeRepository.findById(showTimeId).orElseThrow(() ->
                new ShowTimeNotFoundException("Show time with id " + showTimeId + " not found"));
        BigDecimal base = st.getBasePrice();
        BigDecimal additional = st.getShowType().getAdditionalPrice();
        BigDecimal finalPrice = base.add(additional);

        return new ShowTimeDetailsDTO(
                st.getId(),
                st.getShowTime(),
                new FilmDTO(st.getFilm().getId(), st.getFilm().getName(), st.getFilm().getImdbRating(), st.getFilm().getAgeRate(), st.getFilm().getImageUrl()),
                new ShowTypeDTO(st.getShowType().getId(), st.getShowType().getType(), st.getShowType().getAdditionalPrice()),
                new HallDTO(st.getHall().getId(), st.getHall().getNumber()),
                new CinemaDTO(st.getHall().getCinema().getId(),  st.getHall().getCinema().getLocation(), st.getHall().getCinema().getName()),
                new PricingDTO(base, additional, finalPrice)
        );
    }

    public List<SeatDTO> getAllSeatsForShowTime(Long showTimeId) {
        ShowTime st = showTimeRepository.findById(showTimeId).orElseThrow(() ->
                new ShowTimeNotFoundException("Show time with id " + showTimeId + " not found"));
        List<Seat> seats = st.getHall().getSeats();
        return seats.stream()
                .map(seat -> new SeatDTO(seat.getId(), seat.getRowLetter(), seat.getSeatNumber(),
                        bookedSeatsRepository.existsByShowTime_IdAndSeat_Id(showTimeId, seat.getId())
                                ? Status.reserved : Status.available))
                .sorted(Comparator.comparing(SeatDTO::getRowLetter)
                        .thenComparing(SeatDTO::getSeatNumber))
                .toList();
    }


}
