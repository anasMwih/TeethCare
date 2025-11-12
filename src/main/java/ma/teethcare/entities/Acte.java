package com.teethcare.entites;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "acte")
public class Acte {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idActe;

    @Column(name = "libelle")
    private String libelle;

    @Column(name = "categorie")
    private String categorie;

    @Column(name = "prix_base")
    private Double prixDeBase;

    public Acte() {}

    public Acte(String libelle, String categorie, Double prixDeBase) {
        this.libelle = libelle;
        this.categorie = categorie;
        this.prixDeBase = prixDeBase;
    }

    public Long getIdActe() { return idActe; }
    public void setIdActe(Long idActe) { this.idActe = idActe; }

    public String getLibelle() { return libelle; }
    public void setLibelle(String libelle) { this.libelle = libelle; }

    public String getCategorie() { return categorie; }
    public void setCategorie(String categorie) { this.categorie = categorie; }

    public Double getPrixDeBase() { return prixDeBase; }
    public void setPrixDeBase(Double prixDeBase) { this.prixDeBase = prixDeBase; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Acte acte = (Acte) o;
        return Objects.equals(idActe, acte.idActe);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idActe);
    }

    @Override
    public String toString() {
        return "Acte{" +
                "idActe=" + idActe +
                ", libelle='" + libelle + '\'' +
                ", categorie='" + categorie + '\'' +
                ", prixDeBase=" + prixDeBase +
                '}';
    }
}