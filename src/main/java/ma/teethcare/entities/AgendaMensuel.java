package ma.teethcare.entities;

import lombok.*;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AgendaMensuel {
    private Enum mois;
    private List<Jour> joursNonDisponible;


    // AgendaMensuel appartient à un Médecin
    private Medecin medecin;


}
