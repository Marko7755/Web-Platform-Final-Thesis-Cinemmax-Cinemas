package cinemmaxbackend.security.services.tokens;

import cinemmaxbackend.general.classes.exceptions.PasswordReset.PasswordResetTokenException;
import cinemmaxbackend.security.classes.general.User;
import cinemmaxbackend.security.classes.general.tokens.PasswordResetToken;
import cinemmaxbackend.security.repositories.UserRepository;
import cinemmaxbackend.security.repositories.tokens.PasswordResetTokenRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
@Data
@AllArgsConstructor
public class PasswordResetTokenService {
    private PasswordResetTokenRepository resetPassRepository;
    private UserRepository userRepository;

    public PasswordResetToken createToken(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        resetPassRepository.findByUser(user).ifPresent(resetPassRepository::delete);

        PasswordResetToken resetToken = PasswordResetToken.builder()
                .token(UUID.randomUUID().toString())
                .user(user)
                .expiration(Instant.now().plus(Duration.ofMinutes(20)))
                .build();

        return resetPassRepository.save(resetToken);

    }

    public Optional<PasswordResetToken> getToken(String token) {
        return resetPassRepository.findByToken(token);
    }


    public PasswordResetToken verifyExpiration(PasswordResetToken resetToken) {
        if(resetToken.getExpiration().compareTo(Instant.now()) < 0) {
            resetPassRepository.delete(resetToken);
            throw new PasswordResetTokenException("Password refresh token is expired. Please make a new password reset.");
        }
        return resetToken;
    }


    /*public User getUserByToken(String token) {
        return resetPassRepository.findByToken(token)
                .map(PasswordResetToken::getUser)
                .orElseThrow(() -> new IllegalArgumentException("Invalid or expired token"));
    }*/


    public void deleteToken(String resetToken) {
        resetPassRepository.findByToken(resetToken).ifPresent(resetPassRepository::delete);
    }


}
