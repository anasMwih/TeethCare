package ma.teethcare.entities;

import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CabinetMedicale {
    private Long idUser;
    private String nom;
    private String email;
    private String logo;
    private String cin;
    private String tel1;
    private String tel2;
    private String siteweb;
    private String instagram;


    // CabinetMedicale a :
    private List<Charges> charges;
    private List<Revenues> revenues;
    private List<Statistiques> statistiques;
    private List<Staff> staffs;

}
