package ma.teethcare.entities;

import java.time.LocalDateTime;

public class FileAttente {
    private Long id;
    private LocalDateTime dateArrivee;
    private Integer position;
    private String statut; // EN_ATTENTE, EN_COURS, TERMINE, ANNULE
    private String priorite; // NORMAL, URGENT

    // Relations
    private Patient patient;

    public FileAttente() {}

    public FileAttente(Patient patient, LocalDateTime dateArrivee, Integer position) {
        this.patient = patient;
        this.dateArrivee = dateArrivee;
        this.position = position;
        this.statut = "EN_ATTENTE";
        this.priorite = "NORMAL";
    }

    // Getters et Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public LocalDateTime getDateArrivee() { return dateArrivee; }
    public void setDateArrivee(LocalDateTime dateArrivee) { this.dateArrivee = dateArrivee; }

    public Integer getPosition() { return position; }
    public void setPosition(Integer position) { this.position = position; }

    public String getStatut() { return statut; }
    public void setStatut(String statut) { this.statut = statut; }

    public String getPriorite() { return priorite; }
    public void setPriorite(String priorite) { this.priorite = priorite; }

    public Patient getPatient() { return patient; }
    public void setPatient(Patient patient) { this.patient = patient; }
}