package ma.teethcare.entities;

import java.time.LocalDate;

public class Certificat {
    private Long id;
    private LocalDate dateEmission;
    private LocalDate dateExpiration;
    private String type;
    private String description;

    // Relations
    private DossierMedical dossierMedical;
    private Consultation consultation;

    public Certificat() {}

    public Certificat(DossierMedical dossierMedical, String type, LocalDate dateEmission) {
        this.dossierMedical = dossierMedical;
        this.type = type;
        this.dateEmission = dateEmission;
    }

    // Getters et Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public LocalDate getDateEmission() { return dateEmission; }
    public void setDateEmission(LocalDate dateEmission) { this.dateEmission = dateEmission; }

    public LocalDate getDateExpiration() { return dateExpiration; }
    public void setDateExpiration(LocalDate dateExpiration) { this.dateExpiration = dateExpiration; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public DossierMedical getDossierMedical() { return dossierMedical; }
    public void setDossierMedical(DossierMedical dossierMedical) { this.dossierMedical = dossierMedical; }

    public Consultation getConsultation() { return consultation; }
    public void setConsultation(Consultation consultation) { this.consultation = consultation; }
}