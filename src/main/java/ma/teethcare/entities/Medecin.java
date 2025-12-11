package ma.teethcare.entities;

import java.util.ArrayList;
import java.util.List;

public class Medecin extends Staff {
    private String specialite;
    private String numeroRPPS; // Numéro RPPS (Répertoire Partagé des Professionnels de Santé)
    private String numeroOrdre; // Numéro d'ordre des médecins
    private String titre; // Docteur, Professeur, etc.
    private Integer anneesExperience;
    private Double tauxHoraire;
    private Boolean disponible;

    // Relations
    private AgendaMensuel agendaMensuel;
    private List<Consultation> consultations = new ArrayList<>();
    private List<RDV> rendezVous = new ArrayList<>();
    private List<DossierMedical> dossiersMedicaux = new ArrayList<>();
    private List<InterventionMedecin> interventions = new ArrayList<>();

    // Constructeurs
    public Medecin() {
        super();
        this.disponible = true;
    }

    public Medecin(String nom, String prenom, String email, String telephone, String specialite) {
        super(nom, prenom, email, telephone);
        this.specialite = specialite;
        this.disponible = true;
    }

    public Medecin(String nom, String prenom, String email, String telephone,
                   String specialite, String numeroRPPS, String numeroOrdre) {
        this(nom, prenom, email, telephone, specialite);
        this.numeroRPPS = numeroRPPS;
        this.numeroOrdre = numeroOrdre;
    }

    // Getters et Setters
    public String getSpecialite() {
        return specialite;
    }

    public void setSpecialite(String specialite) {
        this.specialite = specialite;
    }

    public String getNumeroRPPS() {
        return numeroRPPS;
    }

    public void setNumeroRPPS(String numeroRPPS) {
        this.numeroRPPS = numeroRPPS;
    }

    public String getNumeroOrdre() {
        return numeroOrdre;
    }

    public void setNumeroOrdre(String numeroOrdre) {
        this.numeroOrdre = numeroOrdre;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public Integer getAnneesExperience() {
        return anneesExperience;
    }

    public void setAnneesExperience(Integer anneesExperience) {
        this.anneesExperience = anneesExperience;
    }

    public Double getTauxHoraire() {
        return tauxHoraire;
    }

    public void setTauxHoraire(Double tauxHoraire) {
        this.tauxHoraire = tauxHoraire;
    }

    public Boolean getDisponible() {
        return disponible;
    }

    public void setDisponible(Boolean disponible) {
        this.disponible = disponible;
    }

    // Getters et Setters pour les relations
    public AgendaMensuel getAgendaMensuel() {
        return agendaMensuel;
    }

    public void setAgendaMensuel(AgendaMensuel agendaMensuel) {
        this.agendaMensuel = agendaMensuel;
    }

    public List<Consultation> getConsultations() {
        return consultations;
    }

    public void setConsultations(List<Consultation> consultations) {
        this.consultations = consultations;
    }

    public List<RDV> getRendezVous() {
        return rendezVous;
    }

    public void setRendezVous(List<RDV> rendezVous) {
        this.rendezVous = rendezVous;
    }

    public List<DossierMedical> getDossiersMedicaux() {
        return dossiersMedicaux;
    }

    public void setDossiersMedicaux(List<DossierMedical> dossiersMedicaux) {
        this.dossiersMedicaux = dossiersMedicaux;
    }

    public List<InterventionMedecin> getInterventions() {
        return interventions;
    }

    public void setInterventions(List<InterventionMedecin> interventions) {
        this.interventions = interventions;
    }

    // Méthodes utilitaires
    public void addConsultation(Consultation consultation) {
        if (!this.consultations.contains(consultation)) {
            this.consultations.add(consultation);
            consultation.setMedecin(this);
        }
    }

    public void removeConsultation(Consultation consultation) {
        if (this.consultations.remove(consultation)) {
            consultation.setMedecin(null);
        }
    }

    public void addRendezVous(RDV rdv) {
        if (!this.rendezVous.contains(rdv)) {
            this.rendezVous.add(rdv);
            rdv.setMedecin(this);
        }
    }

    public void removeRendezVous(RDV rdv) {
        if (this.rendezVous.remove(rdv)) {
            rdv.setMedecin(null);
        }
    }

    public void addDossierMedical(DossierMedical dossier) {
        if (!this.dossiersMedicaux.contains(dossier)) {
            this.dossiersMedicaux.add(dossier);
            dossier.setMedecinResponsable(this);
        }
    }

    public void removeDossierMedical(DossierMedical dossier) {
        if (this.dossiersMedicaux.remove(dossier)) {
            dossier.setMedecinResponsable(null);
        }
    }

    public String getNomComplet() {
        String titreStr = (titre != null && !titre.isEmpty()) ? titre + " " : "";
        return titreStr + getPrenom() + " " + getNom();
    }

    public String getSpecialiteComplete() {
        return "Médecin " + specialite;
    }

    public boolean estDisponible() {
        return disponible != null && disponible;
    }

    @Override
    public String toString() {
        return "Medecin{" +
                "id=" + getId() +
                ", nom='" + getNom() + '\'' +
                ", prenom='" + getPrenom() + '\'' +
                ", specialite='" + specialite + '\'' +
                ", numeroRPPS='" + numeroRPPS + '\'' +
                ", disponible=" + disponible +
                '}';
    }
}