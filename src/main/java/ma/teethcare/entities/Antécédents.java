package ma.teethcare.entities;

import java.util.List;

public class Antecedents {
    private Long id;
    private String allergies;
    private String maladiesChroniques;
    private String traitementsEnCours;
    private String antecedentsFamiliaux;

    // Relations
    private Patient patient;

    public Antecedents() {}

    public Antecedents(Patient patient) {
        this.patient = patient;
    }

    // Getters et Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getAllergies() { return allergies; }
    public void setAllergies(String allergies) { this.allergies = allergies; }

    public String getMaladiesChroniques() { return maladiesChroniques; }
    public void setMaladiesChroniques(String maladiesChroniques) { this.maladiesChroniques = maladiesChroniques; }

    public String getTraitementsEnCours() { return traitementsEnCours; }
    public void setTraitementsEnCours(String traitementsEnCours) { this.traitementsEnCours = traitementsEnCours; }

    public String getAntecedentsFamiliaux() { return antecedentsFamiliaux; }
    public void setAntecedentsFamiliaux(String antecedentsFamiliaux) { this.antecedentsFamiliaux = antecedentsFamiliaux; }

    public Patient getPatient() { return patient; }
    public void setPatient(Patient patient) { this.patient = patient; }
}