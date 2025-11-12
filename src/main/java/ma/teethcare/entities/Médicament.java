package ma.teethcare.entities;

public class Medicament {
    private Long id;
    private String nom;
    private String type;
    private String forme; // COMPRIME, SIROP, GELULE, POMMADE, INJECTABLE, SPRAY
    private Double prix;

    // Relations
    private List<Prescription> prescriptions;

    public Medicament() {}

    public Medicament(String nom, String type, String forme, Double prix) {
        this.nom = nom;
        this.type = type;
        this.forme = forme;
        this.prix = prix;
    }

    // Getters et Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getForme() { return forme; }
    public void setForme(String forme) { this.forme = forme; }

    public Double getPrix() { return prix; }
    public void setPrix(Double prix) { this.prix = prix; }

    public List<Prescription> getPrescriptions() { return prescriptions; }
    public void setPrescriptions(List<Prescription> prescriptions) { this.prescriptions = prescriptions; }
}