package ma.teethcare.entities;

import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public abstract class BaseEntity {
    private Long identite;
    private LocalDate dateCreation;
    private LocalDateTime dateModification;
    private String modifiePar;
    private String creePar;

}