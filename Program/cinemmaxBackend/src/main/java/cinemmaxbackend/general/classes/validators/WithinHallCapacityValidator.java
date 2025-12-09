package cinemmaxbackend.general.classes.validators;

import cinemmaxbackend.general.annotations.WithinHallCapacity;
import cinemmaxbackend.general.classes.classic.hall.Hall;
import cinemmaxbackend.general.classes.commands.seat.SeatCommand;
import cinemmaxbackend.general.classes.exceptions.NotFound.HallNotFoundException;
import cinemmaxbackend.web.repositories.hall.HallRepository;
import cinemmaxbackend.web.repositories.seat.SeatRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class WithinHallCapacityValidator implements ConstraintValidator<WithinHallCapacity, SeatCommand> {
    private final HallRepository hallRepository;
    private final SeatRepository seatRepository;

    @Override
    public boolean isValid(SeatCommand cmd, ConstraintValidatorContext ctx) {
        if (cmd == null) return true;

        if (cmd.getHallId() == null || cmd.getStartRow() == null
                || cmd.getEndRow() == null || cmd.getSeatsPerRow() == null) return true;

        char s = Character.toUpperCase(cmd.getStartRow().charAt(0));
        char e = Character.toUpperCase(cmd.getEndRow().charAt(0));

        if (s < 'A' || s > 'Z' || e < 'A' || e > 'Z') {
            return true;
        }

        if (s > e) {
            ctx.disableDefaultConstraintViolation();
            ctx.buildConstraintViolationWithTemplate("Start row must be less than or equal to end row")
                    .addPropertyNode("startRow")
                    .addConstraintViolation();
            return false;
        }

        int rows = (e - s + 1);
        long alreadyExistingSeats = seatRepository.countByHall_Id(cmd.getHallId());
        long totalSeatsWithoutExisting = (long) rows * cmd.getSeatsPerRow();
        long totalSeats = (long) rows * cmd.getSeatsPerRow() + alreadyExistingSeats;


        Hall hall = hallRepository.findById(cmd.getHallId()).orElse(null);
        if (hall == null) {
            throw new HallNotFoundException("Hall with id " + cmd.getHallId() + " not found");
        }



        int capacity = hall.getCapacity();
        int capacityRemaining = Math.toIntExact(capacity - alreadyExistingSeats);

        if (totalSeats > capacity) {
            ctx.disableDefaultConstraintViolation();
            ctx.buildConstraintViolationWithTemplate(
                            String.format("Adding %d seats exceeds hall capacity (%d)", totalSeatsWithoutExisting, capacity)
                                    + " - capacity remaining: " + capacityRemaining)
                    .addPropertyNode("error")
                    .addConstraintViolation();
            return false;
        }
        return true;
    }

}
