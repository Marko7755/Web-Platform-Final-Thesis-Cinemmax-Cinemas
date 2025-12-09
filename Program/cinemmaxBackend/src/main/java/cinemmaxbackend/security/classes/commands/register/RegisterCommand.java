package cinemmaxbackend.security.classes.commands.register;


import cinemmaxbackend.security.classes.enums.Role;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class RegisterCommand {
    @NotBlank(message = "User name must not be blank!")
    private String name;
    @NotBlank(message = "User surname must not be blank!")
    private String surname;
    @NotBlank(message = "Username must not be blank!")
    @Size(min = 4, max = 20, message = "Username must be at least 4 and max 20 characters!")
    private String username;
    @NotBlank(message = "Password must not be blank!")
    @Size(min = 4, message = "Password must be at least 4 characters!")
    private String password;
    @NotBlank(message = "Password confirmation must not be blank!")
    @Size(min = 4, message = "Password confirmation must be at least 4 characters!")
    private String passwordConfirmation;
    @NotBlank(message = "Email must not be blank!")
    @Email(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$", message = "Email must be in correct format!")
    private String email;
    @NotNull(message = "User role must not be blank!")
    private Role role;
}
