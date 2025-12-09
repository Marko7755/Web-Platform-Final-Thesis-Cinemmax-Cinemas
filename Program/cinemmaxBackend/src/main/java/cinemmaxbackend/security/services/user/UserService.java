package cinemmaxbackend.security.services.user;

import cinemmaxbackend.general.classes.classic.hall.Hall;
import cinemmaxbackend.security.classes.commands.passwordReset.tokenAndPasswordCommand.TokenPasswordCommand;
import cinemmaxbackend.security.classes.dto.UserDTO;
import cinemmaxbackend.security.classes.enums.Role;
import cinemmaxbackend.security.classes.general.User;
import cinemmaxbackend.security.classes.general.tokens.PasswordResetToken;
import cinemmaxbackend.security.interfaces.UserServiceInterface;
import cinemmaxbackend.security.repositories.UserRepository;
import cinemmaxbackend.security.services.tokens.PasswordResetTokenService;
import cinemmaxbackend.security.utils.SecurityUtils;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;


@Service("userService")
@AllArgsConstructor
public class UserService implements UserServiceInterface, UserDetailsService {

    private UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final PasswordResetTokenService passwordResetService;

    @Override
    public User getCurrentUser() {
        Optional<String> usernameOptional = SecurityUtils.getCurrentUserName();
        return usernameOptional.flatMap(username -> userRepository.findByUsernameIgnoreCase(username)).orElseThrow(()
                -> new UsernameNotFoundException("User not found"));
    }

    public UserDTO getCurrentUserDTO() {
        Optional<String> usernameOptional = SecurityUtils.getCurrentUserName();

        User user = usernameOptional
                .flatMap(username -> userRepository.findByUsernameIgnoreCase(username))
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        return UserDTO.fromEntity(user);
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsernameIgnoreCase(username).orElseThrow(()
                -> new UsernameNotFoundException("User not found"));

        if (user == null) {
            throw new UsernameNotFoundException("Unknown user " + username);
        }

        Role role = user.getRole();

        return org.springframework.security.core.userdetails.User.withUsername(user.getUsername())
                .password(user.getPassword())
                .roles(String.valueOf(role))
                .accountExpired(false)
                .accountLocked(false)
                .credentialsExpired(false)
                .disabled(false)
                .build();
    }

    @Override
    public ResponseEntity<Map<String, String>> updatePassword(TokenPasswordCommand command) {
        String token = command.getToken();
        PasswordResetToken passwordToken = passwordResetService.getToken(token).orElseThrow(() ->
                new IllegalArgumentException("Invalid or missing password reset token"));

        PasswordResetToken verifiedToken = passwordResetService.verifyExpiration(passwordToken);
        User userToChangePass = verifiedToken.getUser();

        String encryptedPassword = bCryptPasswordEncoder.encode(command.getNewPassword());
        userToChangePass.setPassword(encryptedPassword);
        userRepository.save(userToChangePass);
        passwordResetService.deleteToken(token);
        return ResponseEntity.ok(Map.of("message", "Your password has been reset successfully. " +
                "You can now log in with your new password."));
    }

    @Override
    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }


    public boolean checkDuplicate(String username) {
        Optional<User> userOpt = userRepository.findByUsernameIgnoreCase(username);
        return userOpt.isPresent();
    }

    public boolean checkDuplicateEmail(String email) {
        return userRepository.existsByEmailIgnoreCase(email);
    }

}
