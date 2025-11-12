package ma.teethcare.entities;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Adresse {
    private String rue;
    private String ville;
    private String codePostal;
    private String pays;
}
