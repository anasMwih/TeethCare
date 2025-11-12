package com.teethcare.entites;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "medicament")
public class Médicament {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idMedicament;

    @Column(name = "nom")
    private String nom;

    @Column(name = "laboratoire")
    private String laboratoire;

    @Column(name = "type")
    private String type;

    @Enumerated(EnumType.STRING)
    @Column(name = "forme")
    private FormeMedicament forme;

    @Column(name = "remboursable")
    private Boolean remboursable;

    @Column(name = "prix_unitaire")
    private Double prixUnitaire;

    @Column(name = "description", length = 1000)
    private String description;

    public Médicament() {}

    public Médicament(String nom, String laboratoire, String type, FormeMedicament forme, Boolean remboursable, Double prixUnitaire, String description) {
        this.nom = nom;
        this.laboratoire = laboratoire;
        this.type = type;
        this.forme = forme;
        this.remboursable = remboursable;
        this.prixUnitaire = prixUnitaire;
        this.description = description;
    }

    public Long getIdMedicament() { return idMedicament; }
    public void setIdMedicament(Long idMedicament) { this.idMedicament = idMedicament; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public String getLaboratoire() { return laboratoire; }
    public void setLaboratoire(String laboratoire) { this.laboratoire = laboratoire; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public FormeMedicament getForme() { return forme; }
    public void setForme(FormeMedicament forme) { this.forme = forme; }

    public Boolean getRemboursable() { return remboursable; }
    public void setRemboursable(Boolean remboursable) { this.remboursable = remboursable; }

    public Double getPrixUnitaire() { return prixUnitaire; }
    public void setPrixUnitaire(Double prixUnitaire) { this.prixUnitaire = prixUnitaire; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Médicament that = (Médicament) o;
        return Objects.equals(idMedicament, that.idMedicament);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idMedicament);
    }

    @Override
    public String toString() {
        return "Médicament{" +
                "idMedicament=" + idMedicament +
                ", nom='" + nom + '\'' +
                ", laboratoire='" + laboratoire + '\'' +
                ", type='" + type + '\'' +
                ", forme=" + forme +
                ", remboursable=" + remboursable +
                ", prixUnitaire=" + prixUnitaire +
                ", description='" + description + '\'' +
                '}';
    }
}