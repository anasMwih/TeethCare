package ma.teethcare.entities;

import lombok.*;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Staff extends Utilisateur {
    private Double salaire;
    private Double prime;
    private LocalDate dateRecrutement;
    private int soldeConge;

    // Staff hérite de Utilisateur
    // Staff appartient à un CabinetMedicale
    private CabinetMedicale cabinetMedicale;

}
