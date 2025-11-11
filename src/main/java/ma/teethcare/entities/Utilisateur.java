package ma.teethcare.entities;

import lombok.Data;
import lombok.EqualsAndHashCode;
import java.time.LocalDate;

@Data
@EqualsAndHashCode(callSuper = true)
public class Utilisateur extends BaseEntity {
    private Long idUser;
    private String nom;
    private String prenom;
    private String email;
    private String password;  // sera crypté avec BCrypt
    private String telephone;
    private String cin;
    private ServiceType service;
    private LocalDate dateNaissance;
    private LocalDate lastLoginDate;
    private LocalDate dateEmbauche;
    private boolean actif = true;

    // Relations (seront implémentées plus tard)
    private Role role;

    public enum ServiceType {
        ADMINISTRATIF, MEDICAL, FINANCIER
    }

    // Constructeurs
    public Utilisateur() {}

    public Utilisateur(String nom, String prenom, String email, String password, String telephone) {
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.password = password;
        this.telephone = telephone;
        this.actif = true;
    }

    // Méthodes utilitaires
    public String getNomComplet() {
        return nom + " " + prenom;
    }

    public boolean estAdmin() {
        return role != null && role.getLibelle() == Role.LibelleRole.ADMIN;
    }

    public boolean estMedecin() {
        return this instanceof Medecin;
    }

    public boolean estSecretaire() {
        return this instanceof Secretaire;
    }
}