package cinemmaxbackend.security.classes.dto;

import cinemmaxbackend.security.classes.enums.Role;
import cinemmaxbackend.security.classes.general.User;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserDTO {
    private Long id;
    private String name;
    private String surname;
    private String username;
    private String email;
    private Role role;

    public static UserDTO fromEntity(User user) {
        return new UserDTO(user.getId(), user.getName(), user.getSurname(),
                user.getUsername(), user.getEmail(), user.getRole());
    }
}
