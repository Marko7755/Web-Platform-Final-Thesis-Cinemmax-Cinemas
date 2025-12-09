package cinemmaxbackend.security.interfaces;

import cinemmaxbackend.security.classes.commands.passwordReset.tokenAndPasswordCommand.TokenPasswordCommand;
import cinemmaxbackend.security.classes.general.User;
import cinemmaxbackend.security.classes.general.tokens.PasswordResetToken;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Map;
import java.util.Optional;

public interface UserServiceInterface {
    User getCurrentUser();
    UserDetails loadUserByUsername(String username) throws UsernameNotFoundException;
    ResponseEntity<Map<String, String>> updatePassword(TokenPasswordCommand token);
    Optional<User> getUserByEmail(String email);
    Optional<User> findById(Long id);

}
