package com.teethcare.entites;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Objects;

@Entity
@Table(name = "rdv")
public class RDV {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idRdv;

    @Column(name = "date_rdv")
    private LocalDate date;

    @Column(name = "heure_rdv")
    private LocalTime heure;

    @Column(name = "motif")
    private String motif;

    @Enumerated(EnumType.STRING)
    @Column(name = "statut")
    private StatutRdv statut;

    @Column(name = "note_medecin", length = 1000)
    private String noteMedecin;

    public RDV() {}

    public RDV(LocalDate date, LocalTime heure, String motif, StatutRdv statut, String noteMedecin) {
        this.date = date;
        this.heure = heure;
        this.motif = motif;
        this.statut = statut;
        this.noteMedecin = noteMedecin;
    }

    public Long getIdRdv() { return idRdv; }
    public void setIdRdv(Long idRdv) { this.idRdv = idRdv; }

    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }

    public LocalTime getHeure() { return heure; }
    public void setHeure(LocalTime heure) { this.heure = heure; }

    public String getMotif() { return motif; }
    public void setMotif(String motif) { this.motif = motif; }

    public StatutRdv getStatut() { return statut; }
    public void setStatut(StatutRdv statut) { this.statut = statut; }

    public String getNoteMedecin() { return noteMedecin; }
    public void setNoteMedecin(String noteMedecin) { this.noteMedecin = noteMedecin; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RDV rdv = (RDV) o;
        return Objects.equals(idRdv, rdv.idRdv);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idRdv);
    }

    @Override
    public String toString() {
        return "RDV{" +
                "idRdv=" + idRdv +
                ", date=" + date +
                ", heure=" + heure +
                ", motif='" + motif + '\'' +
                ", statut=" + statut +
                ", noteMedecin='" + noteMedecin + '\'' +
                '}';
    }
}