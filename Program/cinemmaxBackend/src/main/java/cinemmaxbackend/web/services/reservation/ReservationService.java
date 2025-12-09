package cinemmaxbackend.web.services.reservation;

import cinemmaxbackend.general.classes.DTO.EmailReservationDetailsDTO.EmailReservationDetailsDTO;
import cinemmaxbackend.general.classes.DTO.cinema.CinemaDTO;
import cinemmaxbackend.general.classes.DTO.hall.HallDTO;
import cinemmaxbackend.general.classes.DTO.reservation.ReservationFullDetailsDTO;
import cinemmaxbackend.general.classes.DTO.reservation.ReservationMinDetailsDTO;
import cinemmaxbackend.general.classes.DTO.seat.EmailSeatDTO;
import cinemmaxbackend.general.classes.classic.ShowTime.ShowTime;
import cinemmaxbackend.general.classes.classic.reservation.BookedSeat;
import cinemmaxbackend.general.classes.classic.reservation.Reservation;
import cinemmaxbackend.general.classes.classic.reservation.ReservationSeat;
import cinemmaxbackend.general.classes.classic.seat.Seat;
import cinemmaxbackend.general.classes.commands.reservation.ReservationCommand;
import cinemmaxbackend.general.classes.enums.Status;
import cinemmaxbackend.general.classes.exceptions.DuplicateEntities.DuplicateReservationException;
import cinemmaxbackend.general.classes.exceptions.NotFound.ReservationNotFoundException;
import cinemmaxbackend.general.classes.exceptions.NotFound.SeatsNotFoundException;
import cinemmaxbackend.general.classes.exceptions.NotFound.ShowTimeNotFoundException;
import cinemmaxbackend.general.classes.records.DuplicateReservations;
import cinemmaxbackend.security.classes.general.User;
import cinemmaxbackend.security.repositories.UserRepository;
import cinemmaxbackend.security.services.email.EmailService;
import cinemmaxbackend.security.services.user.UserService;
import cinemmaxbackend.web.repositories.ShowTime.ShowTimeRepository;
import cinemmaxbackend.web.repositories.reservation.BookedSeatsRepository;
import cinemmaxbackend.web.repositories.reservation.ReservationRepository;
import cinemmaxbackend.web.repositories.reservation.ReservationSeatRepository;
import cinemmaxbackend.web.repositories.seat.SeatRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class ReservationService {
    private final ReservationRepository reservationRepository;
    private final ReservationSeatRepository reservationSeatRepository;
    private final BookedSeatsRepository bookedSeatsRepository;
    private final SeatRepository seatRepository;
    private final ShowTimeRepository showTimeRepository;
    private final UserService userService;
    private final EmailService emailService;

    public List<Reservation> getAllReservations() {
        return reservationRepository.findAll();
    }

    @Transactional
    public ResponseEntity<Map<String, String>> add(ReservationCommand command) {
        List<DuplicateReservations> duplicateReservations = new ArrayList<>();
        List<Long> notFound = new ArrayList<>();
        List<ReservationSeat> reservationSeatsToAdd = new ArrayList<>();
        Reservation savedRes;
        ReservationSeat savedSeat;

        ShowTime showTime = showTimeRepository.findById(command.getShowTimeId()).orElseThrow(() ->
                new ShowTimeNotFoundException("Show type with id: " + command.getShowTimeId() + " not found"));
        User user = userService.findById(command.getUserId()).orElseThrow(()
                -> new UsernameNotFoundException("User with id: " + command.getUserId() + " not found"));
        BigDecimal finalPrice = (showTime.getBasePrice()
                .add(showTime.getShowType().getAdditionalPrice()))
                .multiply(BigDecimal.valueOf(command.getSeatsIds().size()));

        Reservation reservation = Reservation.builder()
                .showTime(showTime)
                .user(user)
                .reservationTime(LocalDateTime.now())
                .status(Status.reserved)
                .finalPrice(finalPrice)
                .build();
        savedRes = reservationRepository.save(reservation);

        for (Long id : command.getSeatsIds()) {
            Optional<Seat> foundSeatOpt = seatRepository.findById(id);

            if (foundSeatOpt.isPresent()) {
                Seat foundSeat = foundSeatOpt.get();

                if (bookedSeatsRepository.existsByShowTime_IdAndSeat_Id(showTime.getId(), foundSeat.getId())) {
                    duplicateReservations.add(
                            new DuplicateReservations(
                                    reservation.getId(), showTime.getId(), foundSeat.getId(),
                                    foundSeat.getRowLetter(), foundSeat.getSeatNumber()));
                } else {


                    ReservationSeat reservationSeat = ReservationSeat.builder()
                            .reservation(reservation)
                            .seat(foundSeat)
                            .price(showTime.getBasePrice().add(showTime.getShowType().getAdditionalPrice()))
                            .build();

                    savedSeat = reservationSeatRepository.save(reservationSeat);

                    reservationSeatsToAdd.add(reservationSeat);

                    BookedSeat bookedSeat = new BookedSeat();
                    bookedSeat.setShowTime(showTime);
                    bookedSeat.setSeat(foundSeat);
                    bookedSeat.setReservationSeat(savedSeat);
                    bookedSeatsRepository.save(bookedSeat);

                }
            } else {
                notFound.add(id);
            }

        }

        if (!duplicateReservations.isEmpty()) {
            throw new DuplicateReservationException("Duplicate seats found: ", duplicateReservations);
        }

        if (!notFound.isEmpty()) {
            throw new SeatsNotFoundException("Seats not found: ", notFound);
        }

        /*List<BookedSeat> booked = bookedSeatsRepository.saveAll(bookedSeatsToAdd);*/

        /*reservation.setSeats(reservationSeatsToAdd);*/

        /*Reservation saved = reservationRepository.save(reservation);*/

        savedRes.setSeats(reservationSeatsToAdd);
        EmailReservationDetailsDTO emailDto =
                EmailService.prepareReservationForEmail(savedRes, user.getEmail(), user.getName());

        emailService.sendReservationDetailsMail(emailDto);


        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("message",
                "Reservation created successfully. Tickets will be sent to your email shortly."));

    }


    public List<ReservationMinDetailsDTO> getMinDetails(Long userId) {
        userService.findById(userId).orElseThrow(() ->
                new UsernameNotFoundException("User with id: " + userId + " not found"));

        return reservationRepository.findByUser_Id(userId).stream()
                .map(ReservationMinDetailsDTO::fromEntity)
                .toList();
    }

    public ReservationFullDetailsDTO getFullDetails(Long reservationId) {
        Reservation r = reservationRepository.findById(reservationId).orElseThrow(() ->
                new ReservationNotFoundException("Reservation with id: " + reservationId + " not found"));

        return ReservationFullDetailsDTO.fromEntity(r);
    }


}
