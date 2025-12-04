package ma.teethcare.entities;

public class Prescription {
    private Long id;
    private Integer quantite;
    private String posologie; // Comment prendre le médicament
    private Integer dureeTraitement; // en jours

    // Relations
    private Ordonnance ordonnance;
    private Medicament medicament;

    public Prescription() {}

    public Prescription(Ordonnance ordonnance, Medicament medicament, Integer quantite) {
        this.ordonnance = ordonnance;
        this.medicament = medicament;
        this.quantite = quantite;
    }

    // Getters et Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Integer getQuantite() { return quantite; }
    public void setQuantite(Integer quantite) { this.quantite = quantite; }

    public String getPosologie() { return posologie; }
    public void setPosologie(String posologie) { this.posologie = posologie; }

    public Integer getDureeTraitement() { return dureeTraitement; }
    public void setDureeTraitement(Integer dureeTraitement) { this.dureeTraitement = dureeTraitement; }

    public Ordonnance getOrdonnance() { return ordonnance; }
    public void setOrdonnance(Ordonnance ordonnance) { this.ordonnance = ordonnance; }

    public Medicament getMedicament() { return medicament; }
    public void setMedicament(Medicament medicament) { this.medicament = medicament; }
}