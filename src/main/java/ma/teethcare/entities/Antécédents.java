package com.teethcare.entites;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "antecedents")
public class Antécédents {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idAntecedent;

    @Column(name = "nom")
    private String nom;

    @Column(name = "categorie")
    private String catégorie;

    @Enumerated(EnumType.STRING)
    @Column(name = "niveau_risque")
    private NiveauRisque niveauDeRisque;

    public Antécédents() {}

    public Antécédents(String nom, String catégorie, NiveauRisque niveauDeRisque) {
        this.nom = nom;
        this.catégorie = catégorie;
        this.niveauDeRisque = niveauDeRisque;
    }

    public Long getIdAntecedent() { return idAntecedent; }
    public void setIdAntecedent(Long idAntecedent) { this.idAntecedent = idAntecedent; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public String getCatégorie() { return catégorie; }
    public void setCatégorie(String catégorie) { this.catégorie = catégorie; }

    public NiveauRisque getNiveauDeRisque() { return niveauDeRisque; }
    public void setNiveauDeRisque(NiveauRisque niveauDeRisque) { this.niveauDeRisque = niveauDeRisque; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Antécédents that = (Antécédents) o;
        return Objects.equals(idAntecedent, that.idAntecedent);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idAntecedent);
    }

    @Override
    public String toString() {
        return "Antécédents{" +
                "idAntecedent=" + idAntecedent +
                ", nom='" + nom + '\'' +
                ", catégorie='" + catégorie + '\'' +
                ", niveauDeRisque=" + niveauDeRisque +
                '}';
    }
}