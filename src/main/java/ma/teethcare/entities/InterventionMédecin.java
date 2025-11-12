package ma.teethcare.entities;

import java.time.LocalDate;

public class InterventionMedecin {
    private Long id;
    private LocalDate dateIntervention;
    private String typeIntervention;
    private String description;
    private Integer duree; // en minutes
    private Double cout;

    // Relations
    private Consultation consultation;
    private Medecin medecin;

    public InterventionMedecin() {}

    public InterventionMedecin(Consultation consultation, String typeIntervention, LocalDate dateIntervention) {
        this.consultation = consultation;
        this.typeIntervention = typeIntervention;
        this.dateIntervention = dateIntervention;
    }

    // Getters et Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public LocalDate getDateIntervention() { return dateIntervention; }
    public void setDateIntervention(LocalDate dateIntervention) { this.dateIntervention = dateIntervention; }

    public String getTypeIntervention() { return typeIntervention; }
    public void setTypeIntervention(String typeIntervention) { this.typeIntervention = typeIntervention; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Integer getDuree() { return duree; }
    public void setDuree(Integer duree) { this.duree = duree; }

    public Double getCout() { return cout; }
    public void setCout(Double cout) { this.cout = cout; }

    public Consultation getConsultation() { return consultation; }
    public void setConsultation(Consultation consultation) { this.consultation = consultation; }

    public Medecin getMedecin() { return medecin; }
    public void setMedecin(Medecin medecin) { this.medecin = medecin; }
}