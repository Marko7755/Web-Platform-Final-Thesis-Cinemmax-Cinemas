package cinemmaxbackend.general.classes.exceptions;

import cinemmaxbackend.general.classes.exceptions.DuplicateEntities.*;
import cinemmaxbackend.general.classes.exceptions.NotFound.*;
import cinemmaxbackend.general.classes.exceptions.Ownership.ReservationOwnershipException;
import cinemmaxbackend.general.classes.exceptions.PasswordReset.PasswordResetTokenException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class ValidationExceptions {

    //CommandsErrorMessages
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getFieldErrors().forEach(error -> {
            errors.put(error.getField(), error.getDefaultMessage());
        });

        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }


    //Empty body and Enum error handling
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String, String>> handleHttpMessageNotReadable(HttpMessageNotReadableException ex) {
        Map<String, String> error = new HashMap<>();
        Throwable cause = ex.getCause();

        if (cause == null) {
            error.put("error", "Request body is missing or unreadable.");
        } else if (cause instanceof InvalidFormatException formatException) {
            Class<?> targetType = formatException.getTargetType();

            if (targetType.isEnum()) {
                String fieldName = formatException.getPath().getFirst().getFieldName();
                String invalidValue = formatException.getValue().toString();
                String[] acceptedValues = new String[targetType.getEnumConstants().length];

                for (int i = 0; i < acceptedValues.length; i++) {
                    acceptedValues[i] = targetType.getEnumConstants()[i].toString();
                }

                error.put(fieldName, "Invalid value: '" + invalidValue + "'. Allowed values: " + String.join(", ", acceptedValues));
            } else {
                error.put("error", "Invalid format: " + cause.getMessage());
            }
        } else {
            error.put("error", "Malformed JSON request");
        }

        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }


    //Incorrect username or password, passwords do not match
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<Map<String, String>> handleBadCredentials(BadCredentialsException ex) {
        Map<String, String> error = new HashMap<>();
        error.put("error", ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.UNAUTHORIZED);
    }

    //Username does not exist in DB
    @ExceptionHandler(UsernameNotFoundException.class)
    @ResponseBody
    public ResponseEntity<Map<String, String>> handleBadUsername(UsernameNotFoundException ex) {
        Map<String, String> error = new HashMap<>();
        error.put("error", ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    //Token does not exist in DB
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> handleTokenNotFound(IllegalArgumentException ex) {
        Map<String, String> error = new HashMap<>();
        error.put("error", ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    //PasswordResetToken expired/not valid
    @ExceptionHandler(PasswordResetTokenException.class)
    public ResponseEntity<Map<String, String>> handleTokenNotValid(PasswordResetTokenException ex) {
        Map<String, String> error = new HashMap<>();
        error.put("error", ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }


    //Duplicate user
    @ExceptionHandler(DuplicateUserException.class)
    public ResponseEntity<Map<String, Object>> handleDuplicateUser(DuplicateUserException ex) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("error", ex.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(body);
    }

    //Duplicate email
    @ExceptionHandler(DuplicateEmailException.class)
    public ResponseEntity<Map<String, Object>> handleDuplicateEmail(DuplicateEmailException ex) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("error", ex.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(body);
    }

    //Duplicate actor
    @ExceptionHandler(DuplicateActorException.class)
    public ResponseEntity<Map<String, Object>> handleDuplicateActor(DuplicateActorException ex) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("error", ex.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(body);
    }

    //Duplicate director
    @ExceptionHandler(DuplicateDirectorException.class)
    public ResponseEntity<Map<String, Object>> handleDuplicateDirector(DuplicateDirectorException ex) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("error", ex.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(body);
    }

    //Duplicate film
    @ExceptionHandler(DuplicateFilmException.class)
    public ResponseEntity<Map<String, String>> handleDuplicateFilm(DuplicateFilmException ex) {
        Map<String, String> error = new HashMap<>();
        error.put("error", ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.CONFLICT);
    }


    //Duplicate cinema
    @ExceptionHandler(DuplicateCinemaException.class)
    public ResponseEntity<Map<String, String>> handleDuplicateHall(DuplicateCinemaException ex) {
        Map<String, String> error = new HashMap<>();
        error.put("error", ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.CONFLICT);
    }

    //Duplicate hall
    @ExceptionHandler(DuplicateHallException.class)
    public ResponseEntity<Map<String, String>> handleDuplicateHall(DuplicateHallException ex) {
        Map<String, String> error = new HashMap<>();
        error.put("error", ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.CONFLICT);
    }

    //Duplicate seat
    @ExceptionHandler(DuplicateSeatException.class)
    public ResponseEntity<Map<String, Object>> handleDuplicateSeat(DuplicateSeatException ex) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("error", ex.getMessage());
        body.put("duplicates", ex.getDuplicateSeats());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(body);
    }

    //Duplicate showType
    @ExceptionHandler(DuplicateShowTypeException.class)
    public ResponseEntity<Map<String, Object>> handleDuplicateShowTime(DuplicateShowTypeException ex) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("error", ex.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(body);
    }

    //Duplicate showTime
    @ExceptionHandler(DuplicateShowTimeException.class)
    public ResponseEntity<Map<String, Object>> handleDuplicateShowTime(DuplicateShowTimeException ex) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("error", ex.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(body);
    }

    //Duplicate reservation
    @ExceptionHandler(DuplicateReservationException.class)
    public ResponseEntity<Map<String, Object>> handleDuplicateReservation(DuplicateReservationException ex) {
        Map<String, Object> error = new LinkedHashMap<>();
        error.put("error", ex.getMessage());
        error.put("duplicates", ex.getDuplicates());
        return new ResponseEntity<>(error, HttpStatus.CONFLICT);
    }


    //Access denied - only admin can input
    @ExceptionHandler(org.springframework.security.access.AccessDeniedException.class)
    public ResponseEntity<String> handleAccessDenied(org.springframework.security.access.AccessDeniedException ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You are not authorized for this action.");
    }

    //Film not found
    @ExceptionHandler(FilmNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleFilmNotFound(FilmNotFoundException ex) {
        Map<String, String> error = new HashMap<>();
        error.put("error", ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    //Actor not found
    @ExceptionHandler(ActorNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleActorNotFound(ActorNotFoundException ex) {
        Map<String, String> error = new HashMap<>();
        error.put("error", ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    //Director not found
    @ExceptionHandler(DirectorNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleDirectorNotFound(DirectorNotFoundException ex) {
        Map<String, String> error = new HashMap<>();
        error.put("error", ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    //Cinema not found
    @ExceptionHandler(CinemaNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleHallNotFound(CinemaNotFoundException ex) {
        Map<String, String> error = new HashMap<>();
        error.put("error", ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    //Hall not found
    @ExceptionHandler(HallNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleHallNotFound(HallNotFoundException ex) {
        Map<String, String> error = new HashMap<>();
        error.put("error", ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    //Seat not found
    @ExceptionHandler(SeatNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleSeatNotFound(SeatNotFoundException ex) {
        Map<String, String> error = new HashMap<>();
        error.put("error", ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    //Seats not found
    @ExceptionHandler(SeatsNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleSeatsNotFound(SeatsNotFoundException ex) {
        Map<String, Object> error = new HashMap<>();
        error.put("error", ex.getMessage());
        error.put("seats", ex.getNotFound());
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    //ShowTime not found
    @ExceptionHandler(ShowTimeNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleShowTimeNotFound(ShowTimeNotFoundException ex) {
        Map<String, String> error = new HashMap<>();
        error.put("error", ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    //ShowType not found
    @ExceptionHandler(ShowTypeNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleShowTypeNotFound(ShowTypeNotFoundException ex) {
        Map<String, String> error = new HashMap<>();
        error.put("error", ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    //Reservation not found
    @ExceptionHandler(ReservationNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleReservationNotFound(ReservationNotFoundException ex) {
        Map<String, String> error = new HashMap<>();
        error.put("error", ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }



    //ReservationOwnership
    @ExceptionHandler(ReservationOwnershipException.class)
    public ResponseEntity<Map<String, String>> handleReservationOwnership(ReservationOwnershipException ex) {
        Map<String, String> error = new HashMap<>();
        error.put("code", "RESERVATION_OWNERSHIP");
        error.put("error", ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.FORBIDDEN);
    }
}
