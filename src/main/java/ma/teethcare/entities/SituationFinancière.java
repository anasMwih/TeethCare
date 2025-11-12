package com.teethcare.entites;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "situation_financiere")
public class SituationFinancière {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idSF;

    @Column(name = "totale_des_actes")
    private Double totaleDesActes;

    @Column(name = "totale_paye")
    private Double totalePayé;

    @Column(name = "credit")
    private Double crédit;

    @Enumerated(EnumType.STRING)
    @Column(name = "statut")
    private StatutFinancier statut;

    @Enumerated(EnumType.STRING)
    @Column(name = "en_promo")
    private EnPromo enPromo;

    public SituationFinancière() {}

    public SituationFinancière(Double totaleDesActes, Double totalePayé, Double crédit, StatutFinancier statut, EnPromo enPromo) {
        this.totaleDesActes = totaleDesActes;
        this.totalePayé = totalePayé;
        this.crédit = crédit;
        this.statut = statut;
        this.enPromo = enPromo;
    }

    public Long getIdSF() { return idSF; }
    public void setIdSF(Long idSF) { this.idSF = idSF; }

    public Double getTotaleDesActes() { return totaleDesActes; }
    public void setTotaleDesActes(Double totaleDesActes) { this.totaleDesActes = totaleDesActes; }

    public Double getTotalePayé() { return totalePayé; }
    public void setTotalePayé(Double totalePayé) { this.totalePayé = totalePayé; }

    public Double getCrédit() { return crédit; }
    public void setCrédit(Double crédit) { this.crédit = crédit; }

    public StatutFinancier getStatut() { return statut; }
    public void setStatut(StatutFinancier statut) { this.statut = statut; }

    public EnPromo getEnPromo() { return enPromo; }
    public void setEnPromo(EnPromo enPromo) { this.enPromo = enPromo; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SituationFinancière that = (SituationFinancière) o;
        return Objects.equals(idSF, that.idSF);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idSF);
    }

    @Override
    public String toString() {
        return "SituationFinancière{" +
                "idSF=" + idSF +
                ", totaleDesActes=" + totaleDesActes +
                ", totalePayé=" + totalePayé +
                ", crédit=" + crédit +
                ", statut=" + statut +
                ", enPromo=" + enPromo +
                '}';
    }
}