package com.teethcare.entites;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Objects;

@Entity
@Table(name = "certificat")
public class Certificat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idCertificat;

    @Column(name = "date_debut")
    private LocalDate dateDebut;

    @Column(name = "date_fin")
    private LocalDate dateFin;

    @Column(name = "duree")
    private Integer durée;

    @Column(name = "note_medecin", length = 1000)
    private String noteMedecin;

    public Certificat() {}

    public Certificat(LocalDate dateDebut, LocalDate dateFin, Integer durée, String noteMedecin) {
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        this.durée = durée;
        this.noteMedecin = noteMedecin;
    }

    public Long getIdCertificat() { return idCertificat; }
    public void setIdCertificat(Long idCertificat) { this.idCertificat = idCertificat; }

    public LocalDate getDateDebut() { return dateDebut; }
    public void setDateDebut(LocalDate dateDebut) { this.dateDebut = dateDebut; }

    public LocalDate getDateFin() { return dateFin; }
    public void setDateFin(LocalDate dateFin) { this.dateFin = dateFin; }

    public Integer getDurée() { return durée; }
    public void setDurée(Integer durée) { this.durée = durée; }

    public String getNoteMedecin() { return noteMedecin; }
    public void setNoteMedecin(String noteMedecin) { this.noteMedecin = noteMedecin; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Certificat that = (Certificat) o;
        return Objects.equals(idCertificat, that.idCertificat);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idCertificat);
    }

    @Override
    public String toString() {
        return "Certificat{" +
                "idCertificat=" + idCertificat +
                ", dateDebut=" + dateDebut +
                ", dateFin=" + dateFin +
                ", durée=" + durée +
                ", noteMedecin='" + noteMedecin + '\'' +
                '}';
    }
}