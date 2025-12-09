package cinemmaxbackend.security.classes.commands.passwordReset.tokenAndPasswordCommand;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TokenPasswordCommand {
    @NotBlank(message = "Token must not be blank")
    @Size(min = 36, max = 36, message = "Token must be 36 characters long!")
    private String token;
    @NotBlank(message = "Password must not be blank!")
    @Size(min = 4, message = "Password must be at least 4 characters!")
    private String newPassword;
    @NotBlank(message = "Password confirmation must not be blank!")
    @Size(min = 4, message = "Password confirmation must be at least 4 characters!")
    private String passwordConfirmation;
}
