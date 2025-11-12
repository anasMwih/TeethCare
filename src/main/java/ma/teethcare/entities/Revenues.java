package ma.teethcare.entities;

import lombok.*;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Revenues {
    private String titre;
    private String description;
    private Double montant;
    private LocalDateTime date;

    // Revenues appartient à un seul CabinetMedicale
    private CabinetMedicale cabinetMedicale;

}
