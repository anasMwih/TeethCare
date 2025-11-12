package com.teethcare.entites;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "facture")
public class Facture {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idFacture;

    @Column(name = "totale_facture")
    private Double totaleFacture;

    @Column(name = "totale_paye")
    private Double totalePayé;

    @Column(name = "reste")
    private Double reste;

    @Enumerated(EnumType.STRING)
    @Column(name = "statut")
    private StatutFacture statut;

    @Column(name = "date_facture")
    private LocalDateTime dateFacture;

    public Facture() {}

    public Facture(Double totaleFacture, Double totalePayé, Double reste, StatutFacture statut, LocalDateTime dateFacture) {
        this.totaleFacture = totaleFacture;
        this.totalePayé = totalePayé;
        this.reste = reste;
        this.statut = statut;
        this.dateFacture = dateFacture;
    }

    public Long getIdFacture() { return idFacture; }
    public void setIdFacture(Long idFacture) { this.idFacture = idFacture; }

    public Double getTotaleFacture() { return totaleFacture; }
    public void setTotaleFacture(Double totaleFacture) { this.totaleFacture = totaleFacture; }

    public Double getTotalePayé() { return totalePayé; }
    public void setTotalePayé(Double totalePayé) { this.totalePayé = totalePayé; }

    public Double getReste() { return reste; }
    public void setReste(Double reste) { this.reste = reste; }

    public StatutFacture getStatut() { return statut; }
    public void setStatut(StatutFacture statut) { this.statut = statut; }

    public LocalDateTime getDateFacture() { return dateFacture; }
    public void setDateFacture(LocalDateTime dateFacture) { this.dateFacture = dateFacture; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Facture facture = (Facture) o;
        return Objects.equals(idFacture, facture.idFacture);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idFacture);
    }

    @Override
    public String toString() {
        return "Facture{" +
                "idFacture=" + idFacture +
                ", totaleFacture=" + totaleFacture +
                ", totalePayé=" + totalePayé +
                ", reste=" + reste +
                ", statut=" + statut +
                ", dateFacture=" + dateFacture +
                '}';
    }
}