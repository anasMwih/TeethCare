package ma.teethcare.entities;

import java.time.LocalDate;
import java.util.List;

public class DossierMedical {
    private Long id;
    private LocalDate dateCreation;
    private String observations;

    // Relations
    private Patient patient;
    private List<Consultation> consultations;
    private List<Ordonnance> ordonnances;
    private List<Certificat> certificats;

    public DossierMedical() {}

    public DossierMedical(Patient patient, LocalDate dateCreation) {
        this.patient = patient;
        this.dateCreation = dateCreation;
    }

    // Getters et Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public LocalDate getDateCreation() { return dateCreation; }
    public void setDateCreation(LocalDate dateCreation) { this.dateCreation = dateCreation; }

    public String getObservations() { return observations; }
    public void setObservations(String observations) { this.observations = observations; }

    public Patient getPatient() { return patient; }
    public void setPatient(Patient patient) { this.patient = patient; }

    public List<Consultation> getConsultations() { return consultations; }
    public void setConsultations(List<Consultation> consultations) { this.consultations = consultations; }

    public List<Ordonnance> getOrdonnances() { return ordonnances; }
    public void setOrdonnances(List<Ordonnance> ordonnances) { this.ordonnances = ordonnances; }

    public List<Certificat> getCertificats() { return certificats; }
    public void setCertificats(List<Certificat> certificats) { this.certificats = certificats; }
}