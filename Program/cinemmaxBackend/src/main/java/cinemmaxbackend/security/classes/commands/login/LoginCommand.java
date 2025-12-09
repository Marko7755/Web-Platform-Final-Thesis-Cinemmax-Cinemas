package cinemmaxbackend.security.classes.commands.login;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LoginCommand {
    @NotBlank
    @Size(min = 4, max = 20, message = "Username must be at least 4 and max 20 characters!")
    private String username;
    @NotBlank
    @Size(min = 4, message = "Password must be at least 4 characters!")
    private String password;
}
