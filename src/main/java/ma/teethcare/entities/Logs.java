package ma.teethcare.entities;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Logs {
    private Long id;
    private LocalDate dateCreation;
    private LocalDateTime dateHeureCreation;
    private String module;
    private String action;
    private String utilisateur;
    private String details;
    private String statut; // SUCCES, ERREUR, AVERTISSEMENT

    // === RELATIONS AVEC LES AUTRES ENTITÉS ===

    // Relations directes (optionnelles)
    private Patient patient;
    private Consultation consultation;
    private RDV rdv;
    private Utilisateur utilisateurEntity;

    // Relations flexibles (pour toutes les entités)
    private String entiteConcernee; // Nom de l'entité (ex: "Patient", "Consultation", "RDV")
    private Long idEntiteConcernee; // ID de l'entité concernée

    // === CONSTRUCTEURS ===

    public Logs() {}

    // Constructeur pour une entité spécifique
    public Logs(String module, String action, String utilisateur, String entiteConcernee, Long idEntiteConcernee) {
        this.dateCreation = LocalDate.now();
        this.dateHeureCreation = LocalDateTime.now();
        this.module = module;
        this.action = action;
        this.utilisateur = utilisateur;
        this.entiteConcernee = entiteConcernee;
        this.idEntiteConcernee = idEntiteConcernee;
        this.statut = "SUCCES";
    }

    // Constructeur pour un patient
    public Logs(String action, Patient patient, String utilisateur) {
        this("PATIENT", action, utilisateur, "Patient", patient.getId());
        this.patient = patient;
    }

    // Constructeur pour une consultation
    public Logs(String action, Consultation consultation, String utilisateur) {
        this("CONSULTATION", action, utilisateur, "Consultation", consultation.getId());
        this.consultation = consultation;
    }

    // Constructeur pour un RDV
    public Logs(String action, RDV rdv, String utilisateur) {
        this("RDV", action, utilisateur, "RDV", rdv.getId());
        this.rdv = rdv;
    }

    // === MÉTHODES UTILITAIRES ===

    public void marquerSucces() {
        this.statut = "SUCCES";
    }

    public void marquerErreur(String messageErreur) {
        this.statut = "ERREUR";
        this.details = messageErreur;
    }

    public void marquerAvertissement(String message) {
        this.statut = "AVERTISSEMENT";
        this.details = message;
    }

    public String getResume() {
        return String.format("[%s] %s - %s - %s",
                dateHeureCreation, module, action, utilisateur);
    }

    // === GETTERS ET SETTERS ===

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public LocalDate getDateCreation() { return dateCreation; }
    public void setDateCreation(LocalDate dateCreation) { this.dateCreation = dateCreation; }

    public LocalDateTime getDateHeureCreation() { return dateHeureCreation; }
    public void setDateHeureCreation(LocalDateTime dateHeureCreation) { this.dateHeureCreation = dateHeureCreation; }

    public String getModule() { return module; }
    public void setModule(String module) { this.module = module; }

    public String getAction() { return action; }
    public void setAction(String action) { this.action = action; }

    public String getUtilisateur() { return utilisateur; }
    public void setUtilisateur(String utilisateur) { this.utilisateur = utilisateur; }

    public String getDetails() { return details; }
    public void setDetails(String details) { this.details = details; }

    public String getStatut() { return statut; }
    public void setStatut(String statut) { this.statut = statut; }

    // Getters et Setters pour les relations directes
    public Patient getPatient() { return patient; }
    public void setPatient(Patient patient) { this.patient = patient; }

    public Consultation getConsultation() { return consultation; }
    public void setConsultation(Consultation consultation) { this.consultation = consultation; }

    public RDV getRdv() { return rdv; }
    public void setRdv(RDV rdv) { this.rdv = rdv; }

    public Utilisateur getUtilisateurEntity() { return utilisateurEntity; }
    public void setUtilisateurEntity(Utilisateur utilisateurEntity) { this.utilisateurEntity = utilisateurEntity; }

    // Getters et Setters pour les relations flexibles
    public String getEntiteConcernee() { return entiteConcernee; }
    public void setEntiteConcernee(String entiteConcernee) { this.entiteConcernee = entiteConcernee; }

    public Long getIdEntiteConcernee() { return idEntiteConcernee; }
    public void setIdEntiteConcernee(Long idEntiteConcernee) { this.idEntiteConcernee = idEntiteConcernee; }
}