package ma.teethcare.entities;

import lombok.*;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Charges {
    private String titre;
    private String description;
    private Double montant;
    private LocalDateTime date;
}
