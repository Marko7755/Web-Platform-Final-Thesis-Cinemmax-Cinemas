package cinemmaxbackend.web.services.seat;

import cinemmaxbackend.general.classes.DTO.seat.SeatDTO;
import cinemmaxbackend.general.classes.classic.hall.Hall;
import cinemmaxbackend.general.classes.classic.seat.Seat;
import cinemmaxbackend.general.classes.commands.seat.SeatCommand;
import cinemmaxbackend.general.classes.records.DuplicateSeat;
import cinemmaxbackend.general.classes.exceptions.DuplicateEntities.DuplicateSeatException;
import cinemmaxbackend.general.classes.exceptions.NotFound.HallNotFoundException;
import cinemmaxbackend.general.classes.exceptions.NotFound.SeatNotFoundException;
import cinemmaxbackend.web.repositories.hall.HallRepository;
import cinemmaxbackend.web.repositories.seat.SeatRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class SeatService {
    private final SeatRepository seatRepository;
    private final HallRepository hallRepository;

    private Seat convertToSeat(Hall hallToAddSeat, char rowLetter, Integer seatNumber) {
        Seat seatToAdd = new Seat();
        seatToAdd.setHall(hallToAddSeat);
        seatToAdd.setRowLetter(rowLetter);
        seatToAdd.setSeatNumber(seatNumber);
        return seatToAdd;
    }

    private String key(char row, int num) {
        return row + "-" + num;
    }

    public List<Seat> getAll() {
        return seatRepository.findAll();
    }

    public List<SeatDTO> getAllByHallId(Long hallId) {
        hallRepository.findById(hallId).orElseThrow(() ->
                new HallNotFoundException("Hall with id " + hallId + " not found"));

        return seatRepository.findByHall_Id(hallId).stream()
                .map(SeatDTO::fromSeat)
                .toList();
    }


    public ResponseEntity<Seat> getById(Long id) {
        Optional<Seat> seatOpt = seatRepository.findById(id);
        return seatOpt.map(ResponseEntity::ok).orElseThrow(() -> new SeatNotFoundException("Seat with id " + id + " not found"));
    }

    @Transactional
    public ResponseEntity<?> add(SeatCommand seatCommand) {
        Hall hall = hallRepository.findById(seatCommand.getHallId())
                .orElseThrow(() -> new HallNotFoundException(
                        "Hall with id " + seatCommand.getHallId() + " not found"));

        char s = seatCommand.getStartRow().charAt(0);   // start row letter
        char e = seatCommand.getEndRow().charAt(0);     // end row letter
        int seatsPerRow = seatCommand.getSeatsPerRow();

        List<Seat> existingSeats = seatRepository.findByHall_Id(hall.getId());
        Set<String> existingKeys = existingSeats.stream()
                .map(seat -> key(seat.getRowLetter(), seat.getSeatNumber()))
                .collect(Collectors.toSet());


        List<Seat> toSave = new ArrayList<>();
        List<DuplicateSeat> duplicates = new ArrayList<>();

        for (char rowLetter = s; rowLetter <= e; rowLetter++) {
            for (int seatNumber = 1; seatNumber <= seatsPerRow; seatNumber++) {
                String k = key(rowLetter, seatNumber);
                if (existingKeys.contains(k)) {
                    duplicates.add(new DuplicateSeat(
                            hall.getCinema().getName(),
                            hall.getNumber(),
                            rowLetter,
                            seatNumber));
                } else {
                    toSave.add(convertToSeat(hall, rowLetter, seatNumber));
                }
            }
        }

        if (!duplicates.isEmpty()) {
            throw new DuplicateSeatException("Duplicate seats found:", duplicates);
        }

        seatRepository.saveAll(toSave);

        int expected = (e - s + 1) * seatsPerRow;
        return ResponseEntity.ok(Map.of(
                "message", "Seats saved successfully",
                "inserted", toSave.size(),
                "expected", expected
        ));
    }


    /*public ResponseEntity<?> add(SeatCommand seatCommand) {
        Hall hallToAddSeat = hallRepository.findById(seatCommand.getHallId()).orElseThrow(() ->
                new HallNotFoundException("Hall with id " + seatCommand.getHallId() + " not found"));

        List<Seat> addedSeats = new ArrayList<>();
        List<Seat> failedToAddSeats = new ArrayList<>();
        List<Seat> duplicateSeats = new ArrayList<>();

        char s = seatCommand.getStartRow().charAt(0); //startingLetter
        char e = seatCommand.getEndRow().charAt(0); //endingLetter

        int numberOfSeatsToAdd = (e - s + 1) * seatCommand.getSeatsPerRow();

        for (char rowLetter = s; rowLetter <= e; rowLetter++) { //rowLetter - starting point
            for (int seatNumber = 1; seatNumber <= seatCommand.getSeatsPerRow(); seatNumber++) {
                if (checkDuplicate(seatCommand.getHallId(), rowLetter, seatNumber, hallToAddSeat.getCinema().getId())) {
                    duplicateSeats.add(convertToSeat(hallToAddSeat, rowLetter, seatNumber));
                }
            }
        }

        if (!duplicateSeats.isEmpty()) {
            List<DuplicateSeat> duplicatesToPrint = duplicateSeats.stream()
                    .map(seat -> new DuplicateSeat(
                            seat.getHall().getCinema().getName(),
                            seat.getHall().getNumber(),
                            seat.getRowLetter(),
                            seat.getSeatNumber()
                    )).toList();
            throw new DuplicateSeatException("Duplicate seats found:", duplicatesToPrint);
        }

        for (char rowLetter = s; rowLetter <= e; rowLetter++) { //rowLetter - starting point
            for (int seatNumber = 1; seatNumber <= seatCommand.getSeatsPerRow(); seatNumber++) {

                Seat seatToAdd = convertToSeat(hallToAddSeat, rowLetter, seatNumber);
                Seat addedSeat = seatRepository.save(seatToAdd);

                if (addedSeat.getId() != null) {
                    addedSeats.add(addedSeat);
                } else {
                    failedToAddSeats.add(seatToAdd);
                }

            }
        }

        if (numberOfSeatsToAdd == addedSeats.size()) { //if all seats are saved successfully, then return OK
            return ResponseEntity.ok(Map.of("message", "Seats saved successfully"));
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("Seats failed to add:", failedToAddSeats));
        }

    }*/

/*    private boolean checkDuplicate(Long hallId, Character rowLetter, Integer seatNumber, Long hallCinemaId) {
        Optional<Seat> seatOpt =
                seatRepository.findByHallIdAndRowLetterAndSeatNumberAndHall_CinemaId(hallId, rowLetter,
                        seatNumber, hallCinemaId);
        return seatOpt.isPresent();
    }*/

}
