package ma.teethcare.entities;

import lombok.*;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Statistiques {
    private long id;
    private String nom;
    private Enum categorie;
    private Double chiffre;
    private LocalDate dateCalcul;

    // Statistiques liées à un CabinetMedicale
    private CabinetMedicale cabinetMedicale;

}
