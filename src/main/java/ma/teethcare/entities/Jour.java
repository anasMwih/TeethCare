package ma.teethcare.entities;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Jour {
    private String nomJour;
    private boolean disponible;
}
