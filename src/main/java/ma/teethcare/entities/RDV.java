package ma.teethcare.entities;

import java.time.LocalDateTime;

public class RDV {
    private Long id;
    private LocalDateTime dateRdv;
    private String statut; // PLANIFIE, CONFIRME, ANNULE, TERMINE
    private String notes;
    private String typeConsultation;

    // Relations
    private Patient patient;
    private Medecin medecin;

    public RDV() {}

    public RDV(Patient patient, Medecin medecin, LocalDateTime dateRdv) {
        this.patient = patient;
        this.medecin = medecin;
        this.dateRdv = dateRdv;
        this.statut = "PLANIFIE";
    }

    // Getters et Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public LocalDateTime getDateRdv() { return dateRdv; }
    public void setDateRdv(LocalDateTime dateRdv) { this.dateRdv = dateRdv; }

    public String getStatut() { return statut; }
    public void setStatut(String statut) { this.statut = statut; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }

    public String getTypeConsultation() { return typeConsultation; }
    public void setTypeConsultation(String typeConsultation) { this.typeConsultation = typeConsultation; }

    public Patient getPatient() { return patient; }
    public void setPatient(Patient patient) { this.patient = patient; }

    public Medecin getMedecin() { return medecin; }
    public void setMedecin(Medecin medecin) { this.medecin = medecin; }
}