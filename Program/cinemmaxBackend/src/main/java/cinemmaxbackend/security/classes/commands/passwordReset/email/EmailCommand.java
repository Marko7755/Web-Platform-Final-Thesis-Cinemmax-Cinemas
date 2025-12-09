package cinemmaxbackend.security.classes.commands.passwordReset.email;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class EmailCommand {
    @NotBlank(message = "Email must not be blank!")
    @Email(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$", message = "Email must be in correct format!")
    private String email;
}
