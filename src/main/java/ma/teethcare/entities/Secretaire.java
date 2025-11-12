package ma.teethcare.entities;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Secretaire extends Staff {
    private String numCNSS;
    private Double commission;

    // Secretaire hérite de Staff

}
