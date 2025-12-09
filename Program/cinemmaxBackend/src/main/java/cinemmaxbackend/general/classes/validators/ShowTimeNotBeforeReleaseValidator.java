package cinemmaxbackend.general.classes.validators;

import cinemmaxbackend.general.annotations.ShowTimeNotBeforeRelease;
import cinemmaxbackend.general.classes.commands.ShowTime.ShowTimeCommand;
import cinemmaxbackend.web.repositories.film.FilmRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Component
@RequiredArgsConstructor
public class ShowTimeNotBeforeReleaseValidator implements ConstraintValidator<ShowTimeNotBeforeRelease, ShowTimeCommand> {

    private final FilmRepository filmRepository;

    @Override
    public boolean isValid(ShowTimeCommand cmd, ConstraintValidatorContext ctx) {

        if (cmd == null || cmd.getFilmId() == null || cmd.getShowTime() == null) {
            return true;
        }

        var filmOpt = filmRepository.findById(cmd.getFilmId());
        if (filmOpt.isEmpty() || filmOpt.get().getCinemaReleaseDate() == null) {
            return true;
        }

        var showDate = cmd.getShowTime().toLocalDate();
        LocalDate release = filmOpt.get().getCinemaReleaseDate();
        String formatted = release.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));

        boolean ok = !showDate.isBefore(release);
        if(!ok) {
            ctx.disableDefaultConstraintViolation();
            ctx.buildConstraintViolationWithTemplate(
                    "Show time date must be on/after the film's release date (" + formatted + ")")
                    .addPropertyNode("error")
                    .addConstraintViolation();
        }
        return ok;
    }
}
