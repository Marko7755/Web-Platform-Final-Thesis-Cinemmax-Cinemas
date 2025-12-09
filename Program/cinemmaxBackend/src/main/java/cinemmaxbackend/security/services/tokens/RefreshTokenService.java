package cinemmaxbackend.security.services.tokens;

import cinemmaxbackend.security.classes.general.User;
import cinemmaxbackend.security.classes.general.tokens.RefreshToken;
import cinemmaxbackend.security.repositories.tokens.RefreshTokenRepository;
import cinemmaxbackend.security.repositories.UserRepository;
import jakarta.transaction.Transactional;
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
public class RefreshTokenService {
    private RefreshTokenRepository refreshTokenRepository;
    private UserRepository userRepository;


    public RefreshToken createRefreshToken(String username) {
        User user = userRepository.findByUsernameIgnoreCase(username).orElseThrow(()
                -> new UsernameNotFoundException("User not found"));

        RefreshToken refreshToken = RefreshToken.builder()
                .userInfo(user)
                .token(UUID.randomUUID().toString())
                .expiration(Instant.now().plus(Duration.ofDays(7)))
                .build();

        return refreshTokenRepository.save(refreshToken);
    }

    public Optional<RefreshToken> findByToken(String token) {
        return refreshTokenRepository.findByToken(token);
    }

    public RefreshToken verifyExpiration(RefreshToken refreshToken) {
        if(refreshToken.getExpiration().compareTo(Instant.now()) < 0) {
            refreshTokenRepository.delete(refreshToken);
            throw new RuntimeException(refreshToken.getToken() + " Refresh token is expired. Please make a new login.");
        }
        return refreshToken;
    }

    @Transactional
    public void deleteRefreshToken(String refreshToken) {
        refreshTokenRepository.deleteByToken(refreshToken);
    }

}
