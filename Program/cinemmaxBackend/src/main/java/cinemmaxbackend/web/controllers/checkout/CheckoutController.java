package cinemmaxbackend.web.controllers.checkout;

import cinemmaxbackend.general.classes.commands.checkout.CheckoutCommand;
import cinemmaxbackend.web.services.checkout.CheckoutService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/checkouts/")
@RequiredArgsConstructor
public class CheckoutController {
    private final CheckoutService checkoutService;

    @PostMapping("/payAndReserve")
    public ResponseEntity<Map<String, String>> payAndReserve(@Valid @RequestBody CheckoutCommand cmd) {
        return checkoutService.payAndReserve(cmd);
    }

}
