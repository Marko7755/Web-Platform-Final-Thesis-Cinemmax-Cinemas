package cinemmaxbackend.security.classes.general;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class EmailDetails {
    private String recipient;
    private String username;
    private String subject;
    private String body;
    private String link;
}
