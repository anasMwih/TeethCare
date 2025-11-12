package com.teethcare.entites;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "prescription")
public class Prescription {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idPrescription;

    @Column(name = "quantite")
    private Integer quantité;

    @Column(name = "frequence")
    private String fréquence;

    @Column(name = "duree_jours")
    private Integer duréeEnJours;

    public Prescription() {}

    public Prescription(Integer quantité, String fréquence, Integer duréeEnJours) {
        this.quantité = quantité;
        this.fréquence = fréquence;
        this.duréeEnJours = duréeEnJours;
    }

    public Long getIdPrescription() { return idPrescription; }
    public void setIdPrescription(Long idPrescription) { this.idPrescription = idPrescription; }

    public Integer getQuantité() { return quantité; }
    public void setQuantité(Integer quantité) { this.quantité = quantité; }

    public String getFréquence() { return fréquence; }
    public void setFréquence(String fréquence) { this.fréquence = fréquence; }

    public Integer getDuréeEnJours() { return duréeEnJours; }
    public void setDuréeEnJours(Integer duréeEnJours) { this.duréeEnJours = duréeEnJours; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Prescription that = (Prescription) o;
        return Objects.equals(idPrescription, that.idPrescription);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idPrescription);
    }

    @Override
    public String toString() {
        return "Prescription{" +
                "idPrescription=" + idPrescription +
                ", quantité=" + quantité +
                ", fréquence='" + fréquence + '\'' +
                ", duréeEnJours=" + duréeEnJours +
                '}';
    }
}