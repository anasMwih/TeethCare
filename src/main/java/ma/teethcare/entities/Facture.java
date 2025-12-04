package ma.teethcare.entities;

import java.time.LocalDateTime;

public class Facture {
    private Long id;
    private Double montantTotal;
    private Double montantPaye;
    private Double resteAPayer;
    private String statut; // PAYÉ, EN_ATTENTE, ANNULE
    private LocalDateTime dateFacture;
    private String numeroFacture;

    // Relations
    private Consultation consultation;
    private Patient patient;

    public Facture() {}

    public Facture(Consultation consultation, Double montantTotal, LocalDateTime dateFacture) {
        this.consultation = consultation;
        this.montantTotal = montantTotal;
        this.dateFacture = dateFacture;
        this.resteAPayer = montantTotal;
        this.statut = "EN_ATTENTE";
    }

    // Getters et Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Double getMontantTotal() { return montantTotal; }
    public void setMontantTotal(Double montantTotal) { this.montantTotal = montantTotal; }

    public Double getMontantPaye() { return montantPaye; }
    public void setMontantPaye(Double montantPaye) { this.montantPaye = montantPaye; }

    public Double getResteAPayer() { return resteAPayer; }
    public void setResteAPayer(Double resteAPayer) { this.resteAPayer = resteAPayer; }

    public String getStatut() { return statut; }
    public void setStatut(String statut) { this.statut = statut; }

    public LocalDateTime getDateFacture() { return dateFacture; }
    public void setDateFacture(LocalDateTime dateFacture) { this.dateFacture = dateFacture; }

    public String getNumeroFacture() { return numeroFacture; }
    public void setNumeroFacture(String numeroFacture) { this.numeroFacture = numeroFacture; }

    public Consultation getConsultation() { return consultation; }
    public void setConsultation(Consultation consultation) { this.consultation = consultation; }

    public Patient getPatient() { return patient; }
    public void setPatient(Patient patient) { this.patient = patient; }
}