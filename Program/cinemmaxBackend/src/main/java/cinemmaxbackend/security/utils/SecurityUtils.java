package cinemmaxbackend.security.utils;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

public class SecurityUtils {
    public static Optional<String> getCurrentUserName() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();//gives the current authentication object

        if(authentication != null && authentication.isAuthenticated()) {
            Object principal = authentication.getPrincipal(); //current authenticated User
            if(principal instanceof UserDetails userDetails) {
                return Optional.ofNullable(userDetails.getUsername());
            }
            else if (principal instanceof String) {
                return Optional.of((String) principal);
            }
        }
        return Optional.empty();
    }
}
