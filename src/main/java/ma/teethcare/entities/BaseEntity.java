package ma.teethcare.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public abstract class BaseEntity {
    private Long identite;
    private LocalDate dateCreation;
    private LocalDateTime dateModification;
    private String modifiePar;
    private String creePar;

}