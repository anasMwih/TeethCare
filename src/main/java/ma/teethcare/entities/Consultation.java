package ma.teethcare.entities;

import java.time.LocalDate;
import java.util.List;

public class Consultation {
    private Long id;
    private LocalDate date;
    private String symptomes;
    private String diagnostic;
    private String traitement;
    private Double prix;

    // Relations
    private Patient patient;
    private DossierMedical dossierMedical;
    private List<InterventionMedecin> interventions;
    private List<Prescription> prescriptions;
    private Facture facture;

    public Consultation() {}

    public Consultation(Patient patient, LocalDate date, String symptomes) {
        this.patient = patient;
        this.date = date;
        this.symptomes = symptomes;
    }

    // Getters et Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }

    public String getSymptomes() { return symptomes; }
    public void setSymptomes(String symptomes) { this.symptomes = symptomes; }

    public String getDiagnostic() { return diagnostic; }
    public void setDiagnostic(String diagnostic) { this.diagnostic = diagnostic; }

    public String getTraitement() { return traitement; }
    public void setTraitement(String traitement) { this.traitement = traitement; }

    public Double getPrix() { return prix; }
    public void setPrix(Double prix) { this.prix = prix; }

    public Patient getPatient() { return patient; }
    public void setPatient(Patient patient) { this.patient = patient; }

    public DossierMedical getDossierMedical() { return dossierMedical; }
    public void setDossierMedical(DossierMedical dossierMedical) { this.dossierMedical = dossierMedical; }

    public List<InterventionMedecin> getInterventions() { return interventions; }
    public void setInterventions(List<InterventionMedecin> interventions) { this.interventions = interventions; }

    public List<Prescription> getPrescriptions() { return prescriptions; }
    public void setPrescriptions(List<Prescription> prescriptions) { this.prescriptions = prescriptions; }

    public Facture getFacture() { return facture; }
    public void setFacture(Facture facture) { this.facture = facture; }
}