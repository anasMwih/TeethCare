package ma.teethcare.entities;

import lombok.*;
import ma.teethcare.entities.enums.Sexe;
import ma.teethcare.entities.enums.TypeUtilisateur;

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
    private String adresse;
    private String cin;
    private String telephone;
    private String login;
    private String motDePass;
    private Sexe sexe;
    private LocalDate lastLoginDate;
    private LocalDate dateNaissance;

    // Attributs pour Staff
    private Double salaire;
    private Double prime;
    private LocalDate dateRecrutement;
    private Integer soldeConge;

    private TypeUtilisateur type;
    private String specialite; // Pour médecin
    private String numCNSS; // Pour secrétaire
    private Double commission; // Pour secrétaire

    // Relations
    private CabinetMedicale cabinetMedicale;
    private List<Role> roles;
    private List<Notification> notifications;
    private AgendaMensuel agendaMensuel; // Pour médecin
}
