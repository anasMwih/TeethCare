package ma.teethcare.entities;

import lombok.Data;
import lombok.EqualsAndHashCode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class Patient extends BaseEntity {
    private Long idPatient;
    private String nom;
    private String prenom;
    private LocalDate dateNaissance;
    private String cin;
    private String adresse;
    private String telephone;
    private String email;
    private AssuranceType assurance;
    private CaseType caseType;

    // Relations (seront implémentées plus tard)
    private List<DossierMedical> dossiersMedicaux = new ArrayList<>();
    private List<RendezVous> rendezVous = new ArrayList<>();
    private List<Facture> factures = new ArrayList<>();

    public enum AssuranceType {
        CNSS, CNOPS, RAMED, PRIVEE, AUCUNE
    }

    public enum CaseType {
        NOUVEAU, SUIVI, URGENCE, CHRONIQUE
    }

    // Constructeur pratique
    public Patient(String nom, String prenom, String cin, String telephone) {
        this.nom = nom;
        this.prenom = prenom;
        this.cin = cin;
        this.telephone = telephone;
        this.caseType = CaseType.NOUVEAU;
    }

    public Patient() {
        // Constructeur par défaut
    }

    // Méthode utilitaire
    public String getNomComplet() {
        return nom + " " + prenom;
    }
}
