package ma.teethcare.entities;

import lombok.*;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Role {
    private Long idRole;
    private Enum libelle;
    private List<String> privileges;
}
