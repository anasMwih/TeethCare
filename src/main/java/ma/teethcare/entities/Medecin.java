package ma.teethcare.entities;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Medecin extends Staff {
    private String specialite;
    private AgendaMensuel agendaMensuel;
}
