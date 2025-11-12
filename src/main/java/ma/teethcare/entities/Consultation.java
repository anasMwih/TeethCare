package com.teethcare.entites;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Objects;

@Entity
@Table(name = "consultation")
public class Consultation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idConsultation;

    @Column(name = "date_consultation")
    private LocalDate date;

    @Enumerated(EnumType.STRING)
    @Column(name = "statut")
    private StatutConsultation statut;

    @Column(name = "observation_medecin", length = 1000)
    private String observationMedecin;

    public Consultation() {}

    public Consultation(LocalDate date, StatutConsultation statut, String observationMedecin) {
        this.date = date;
        this.statut = statut;
        this.observationMedecin = observationMedecin;
    }

    public Long getIdConsultation() { return idConsultation; }
    public void setIdConsultation(Long idConsultation) { this.idConsultation = idConsultation; }

    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }

    public StatutConsultation getStatut() { return statut; }
    public void setStatut(StatutConsultation statut) { this.statut = statut; }

    public String getObservationMedecin() { return observationMedecin; }
    public void setObservationMedecin(String observationMedecin) { this.observationMedecin = observationMedecin; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Consultation that = (Consultation) o;
        return Objects.equals(idConsultation, that.idConsultation);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idConsultation);
    }

    @Override
    public String toString() {
        return "Consultation{" +
                "idConsultation=" + idConsultation +
                ", date=" + date +
                ", statut=" + statut +
                ", observationMedecin='" + observationMedecin + '\'' +
                '}';
    }
}