package ma.teethcare.entities;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Patient {
    private Long id;
    private String nom;
    private String prenom;
    private LocalDate dateNaissance;
    private Genre sexe;
    private String telephone;
    private String email;
    private String adresse;
    private String assurance;
    private String groupeSanguin;
    private String profession;
    private LocalDate dateCreation;
    private LocalDate dateMiseAJour;

    // Relations
    private DossierMedical dossierMedical;
    private List<Consultation> consultations = new ArrayList<>();
    private List<RendezVous> rendezVous = new ArrayList<>();
    private List<Antecedent> antecedents = new ArrayList<>();

    // Enum Genre
    public enum Genre {
        MASCULIN("M"),
        FEMININ("F"),
        AUTRE("OTHER");

        private final String code;

        Genre(String code) {
            this.code = code;
        }

        public String getCode() {
            return code;
        }

        public static Genre fromCode(String code) {
            for (Genre genre : values()) {
                if (genre.code.equals(code)) {
                    return genre;
                }
            }
            return AUTRE;
        }
    }

    // Constructeurs
    public Patient() {
        this.dateCreation = LocalDate.now();
        this.dateMiseAJour = LocalDate.now();
    }

    public Patient(String nom, String prenom, LocalDate dateNaissance, String telephone, Genre sexe) {
        this();
        this.nom = nom;
        this.prenom = prenom;
        this.dateNaissance = dateNaissance;
        this.telephone = telephone;
        this.sexe = sexe;
    }

    // Constructeur complet
    public Patient(String nom, String prenom, LocalDate dateNaissance, Genre sexe,
                   String telephone, String email, String adresse, String assurance) {
        this(nom, prenom, dateNaissance, telephone, sexe);
        this.email = email;
        this.adresse = adresse;
        this.assurance = assurance;
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

    public Genre getSexe() { return sexe; }
    public void setSexe(Genre sexe) { this.sexe = sexe; }

    public String getTelephone() { return telephone; }
    public void setTelephone(String telephone) { this.telephone = telephone; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getAdresse() { return adresse; }
    public void setAdresse(String adresse) { this.adresse = adresse; }

    public String getAssurance() { return assurance; }
    public void setAssurance(String assurance) { this.assurance = assurance; }

    public String getGroupeSanguin() { return groupeSanguin; }
    public void setGroupeSanguin(String groupeSanguin) { this.groupeSanguin = groupeSanguin; }

    public String getProfession() { return profession; }
    public void setProfession(String profession) { this.profession = profession; }

    public LocalDate getDateCreation() { return dateCreation; }
    public void setDateCreation(LocalDate dateCreation) { this.dateCreation = dateCreation; }

    public LocalDate getDateMiseAJour() { return dateMiseAJour; }
    public void setDateMiseAJour(LocalDate dateMiseAJour) { this.dateMiseAJour = dateMiseAJour; }

    // Getters et Setters pour les relations
    public DossierMedical getDossierMedical() { return dossierMedical; }
    public void setDossierMedical(DossierMedical dossierMedical) { this.dossierMedical = dossierMedical; }

    public List<Consultation> getConsultations() { return consultations; }
    public void setConsultations(List<Consultation> consultations) { this.consultations = consultations; }

    public List<RendezVous> getRendezVous() { return rendezVous; }
    public void setRendezVous(List<RendezVous> rendezVous) { this.rendezVous = rendezVous; }

    public List<Antecedent> getAntecedents() { return antecedents; }
    public void setAntecedents(List<Antecedent> antecedents) { this.antecedents = antecedents; }

    // Méthodes utilitaires
    public void addConsultation(Consultation consultation) {
        this.consultations.add(consultation);
        consultation.setPatient(this);
    }

    public void addRendezVous(RendezVous rdv) {
        this.rendezVous.add(rdv);
        rdv.setPatient(this);
    }

    public void addAntecedent(Antecedent antecedent) {
        this.antecedents.add(antecedent);
        antecedent.setPatient(this);
    }

    public String getNomComplet() {
        return prenom + " " + nom;
    }

    public int getAge() {
        if (dateNaissance == null) return 0;
        return LocalDate.now().getYear() - dateNaissance.getYear();
    }

    @Override
    public String toString() {
        return "Patient{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", prenom='" + prenom + '\'' +
                ", dateNaissance=" + dateNaissance +
                ", sexe=" + sexe +
                ", telephone='" + telephone + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}