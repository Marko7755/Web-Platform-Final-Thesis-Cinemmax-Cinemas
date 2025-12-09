package cinemmaxbackend.security.controllers.passwordReset;

import cinemmaxbackend.security.classes.commands.passwordReset.email.EmailCommand;
import cinemmaxbackend.security.classes.commands.passwordReset.tokenAndPasswordCommand.TokenPasswordCommand;
import cinemmaxbackend.security.classes.general.EmailDetails;
import cinemmaxbackend.security.classes.general.User;
import cinemmaxbackend.security.classes.general.tokens.PasswordResetToken;
import cinemmaxbackend.security.services.email.EmailService;
import cinemmaxbackend.security.services.tokens.PasswordResetTokenService;
import cinemmaxbackend.security.services.user.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

//Zaokruženi tok:
//Korisnik klikne "Zaboravljena lozinka"
//
//Backend generira token → sprema ga u bazu → šalje link s tokenom korisniku
//
//Korisnik klikne link → otvori se reset-password forma
//
//Frontend šalje novu lozinku + token natrag backendu
//
//Backend provjeri token → promijeni lozinku → potvrdi
//
//✅ Ukratko:
//Token se koristi kao privremena autentifikacija (umjesto lozinke) za reset lozinke.
//
//Stavlja se u link kako bi korisnik, klikom iz emaila, donio sa sobom "dokaz" da je on zatražio reset.
//
//Frontend koristi taj token da identificira korisnika kod slanja nove lozinke.

@RestController
@RequestMapping("/api/authenticate/forgottenPassword")
@AllArgsConstructor
public class PasswordResetController {

    private final UserService userService;
    private final PasswordResetTokenService passwordResetService;
    private final EmailService emailService;

    /*@PostMapping("/sendEmail")
    public ResponseEntity<Map<String, String>> forgotPassword(@RequestBody @Valid EmailCommand emailCommand) {
        Map<String, String> response = new HashMap<>();

        User user = userService.getUserByEmail(emailCommand.getEmail()).orElseThrow(() ->
                new UsernameNotFoundException("No account with " + emailCommand.getEmail() + " email was found."));

            String username = user.getUsername();

            PasswordResetToken passwordResetToken = passwordResetService.createToken(user.getEmail());
            String link = "http://localhost:4200/passwordReset?token=" + passwordResetToken.getToken();

            EmailDetails emailDetails = new EmailDetails(
                    emailCommand.getEmail(),
                    username,
                    "Password Reset Request - Cinemmax Cinemas",
                    "We've received your request to reset the password for your Cinemmax Cinemas account. " +
                            "Click the button below to proceed.",
                    link
            );
            try {
                emailService.sendHtmlMail(emailDetails);
                response.put("message", link);
                return ResponseEntity.ok(response);
            } catch (RuntimeException e) {
                response.put("error", "There was an error sending the password reset email: " + e.getMessage());
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
            }
            }*/


    @PostMapping("/sendEmail")
    public ResponseEntity<Map<String, String>> forgotPassword(@RequestBody @Valid EmailCommand emailCommand) {
        Map<String, String> response = new HashMap<>();

        User user = userService.getUserByEmail(emailCommand.getEmail()).orElseThrow(() ->
                new UsernameNotFoundException("No account with " + emailCommand.getEmail() + " email was found."));
        String username = user.getUsername();

        PasswordResetToken passwordResetToken = passwordResetService.createToken(user.getEmail());
        String link = "http://localhost:4200/passwordReset?token=" + passwordResetToken.getToken();
        response.put("link", link);

        EmailDetails emailDetails = new EmailDetails(
                emailCommand.getEmail(),
                username,
                "Password Reset Request - Cinemmax Cinemas",
                "We've received your request to reset the password for your Cinemmax Cinemas account. " +
                        "Click the button below to proceed.",
                link
        );
        try {
            emailService.sendPasswordResetMail(emailDetails);
            return ResponseEntity.ok(response);
        }
        catch (RuntimeException e) {
            response.put("error", "There was an error sending the password reset email: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }


    @PostMapping("/passwordReset")
    public ResponseEntity<Map<String, String>> passwordReset(@RequestBody @Valid TokenPasswordCommand tokenPasswordCommand) {
        if(!tokenPasswordCommand.getNewPassword().equals(tokenPasswordCommand.getPasswordConfirmation())) {
            throw new BadCredentialsException("Passwords do not match");
        }

        return userService.updatePassword(tokenPasswordCommand);
    }





}
