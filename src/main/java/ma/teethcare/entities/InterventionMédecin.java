package com.teethcare.entites;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Objects;

@Entity
@Table(name = "intervention_medecin")
public class InterventionMédecin {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idIntervention;

    @Column(name = "date_intervention")
    private LocalDate dateIntervention;

    @Column(name = "num_dent")
    private Integer numDent;

    public InterventionMédecin() {}

    public InterventionMédecin(LocalDate dateIntervention, Integer numDent) {
        this.dateIntervention = dateIntervention;
        this.numDent = numDent;
    }

    public Long getIdIntervention() { return idIntervention; }
    public void setIdIntervention(Long idIntervention) { this.idIntervention = idIntervention; }

    public LocalDate getDateIntervention() { return dateIntervention; }
    public void setDateIntervention(LocalDate dateIntervention) { this.dateIntervention = dateIntervention; }

    public Integer getNumDent() { return numDent; }
    public void setNumDent(Integer numDent) { this.numDent = numDent; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        InterventionMédecin that = (InterventionMédecin) o;
        return Objects.equals(idIntervention, that.idIntervention);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idIntervention);
    }

    @Override
    public String toString() {
        return "InterventionMédecin{" +
                "idIntervention=" + idIntervention +
                ", dateIntervention=" + dateIntervention +
                ", numDent=" + numDent +
                '}';
    }
}