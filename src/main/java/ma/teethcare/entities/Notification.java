package ma.teethcare.entities;

import lombok.*;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Notification {
    private Long id;
    private Enum titre;
    private String message;
    private LocalDate date;
    private LocalTime time;
    private Enum type;
    private Enum priorité;

    // Notification appartient à un utilisateur
    private Utilisateur utilisateur;

}
