package cinemmaxbackend.web.services.checkout;

import cinemmaxbackend.general.classes.commands.checkout.CheckoutCommand;
import cinemmaxbackend.web.services.reservation.ReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class CheckoutService {
    private final ReservationService reservationService;

    public ResponseEntity<Map<String, String>> payAndReserve(CheckoutCommand cmd) {
        if(!isApproved(cmd.getCardNumber(), cmd.getCvv())) {
            return ResponseEntity.badRequest().body(Map.of("message", "Card number not approved"));
        }
        return reservationService.add(cmd.getReservationCommand());
    }



    private boolean isApproved(String cardNumber, String cvv) {
        if (cardNumber == null || cardNumber.isBlank() || cvv == null || cvv.isBlank()) return false;

        String removeWhiteSpaces = cardNumber.replaceAll("\\s+", "");
        char last = removeWhiteSpaces.charAt(removeWhiteSpaces.length() - 1);
        boolean lastEven = Character.isDigit(last) && ((last - '0') % 2 == 0);

        char cvvLast = cvv.charAt(cvv.length() - 1);
        boolean cvvEven = Character.isDigit(cvvLast) && ((cvvLast - '0') % 2 == 0);

        return lastEven && cvvEven;

    }

}
