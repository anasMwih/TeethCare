package ma.teethcare.entities;

import java.time.LocalDate;
import java.util.List;

public class Patient {
    private Long id;
    private String nom;
    private String prenom;
    private LocalDate dateNaissance;
    private String sexe;
    private String telephone;
    private String email;
    private String adresse;
    private String assurance;

    // Relations
    private List<DossierMedical> dossiersMedicaux;
    private List<Consultation> consultations;
    private List<RDV> rendezVous;
    private Antecedents antecedents;

    // Constructeurs, getters, setters
    public Patient() {}

    public Patient(String nom, String prenom, LocalDate dateNaissance, String telephone) {
        this.nom = nom;
        this.prenom = prenom;
        this.dateNaissance = dateNaissance;
        this.telephone = telephone;
    }

    // Getters et Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public String getPrenom() { return prenom; }
    public void setPrenom(String prenom) { this.prenom = prenom; }

    public LocalDate getDateNaissance() { return dateNaissance; }
    public void setDateNaissance(LocalDate dateNaissance) { this.dateNaissance = dateNaissance; }

    public String getSexe() { return sexe; }
    public void setSexe(String sexe) { this.sexe = sexe; }

    public String getTelephone() { return telephone; }
    public void setTelephone(String telephone) { this.telephone = telephone; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getAdresse() { return adresse; }
    public void setAdresse(String adresse) { this.adresse = adresse; }

    public String getAssurance() { return assurance; }
    public void setAssurance(String assurance) { this.assurance = assurance; }

    public List<DossierMedical> getDossiersMedicaux() { return dossiersMedicaux; }
    public void setDossiersMedicaux(List<DossierMedical> dossiersMedicaux) { this.dossiersMedicaux = dossiersMedicaux; }

    public List<Consultation> getConsultations() { return consultations; }
    public void setConsultations(List<Consultation> consultations) { this.consultations = consultations; }

    public List<RDV> getRendezVous() { return rendezVous; }
    public void setRendezVous(List<RDV> rendezVous) { this.rendezVous = rendezVous; }

    public Antecedents getAntecedents() { return antecedents; }
    public void setAntecedents(Antecedents antecedents) { this.antecedents = antecedents; }
}