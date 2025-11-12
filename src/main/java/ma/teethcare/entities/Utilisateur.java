package ma.teethcare.entities;

import lombok.*;
import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Utilisateur {
    private Long idUser;
    private String nom;
    private String email;
    private Adresse adresse;
    private String cin;
    private String tél;
    private String login;
    private String motDePass;
    private Enum sexe;
    private LocalDate lastLoginDate;
    private LocalDate dateNaissance;

    // Utilisateur a plusieurs rôles et notifications
    private List<Role> roles;
    private List<Notification> notifications;

}
