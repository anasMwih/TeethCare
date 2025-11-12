package com.teethcare.entites;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Objects;

@Entity
@Table(name = "dossier_medicale")
public class DossierMédicale {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idDM;

    @Column(name = "date_creation")
    private LocalDate dateDeCréation;

    public DossierMédicale() {}

    public DossierMédicale(LocalDate dateDeCréation) {
        this.dateDeCréation = dateDeCréation;
    }

    public Long getIdDM() { return idDM; }
    public void setIdDM(Long idDM) { this.idDM = idDM; }

    public LocalDate getDateDeCréation() { return dateDeCréation; }
    public void setDateDeCréation(LocalDate dateDeCréation) { this.dateDeCréation = dateDeCréation; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DossierMédicale that = (DossierMédicale) o;
        return Objects.equals(idDM, that.idDM);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idDM);
    }

    @Override
    public String toString() {
        return "DossierMédicale{" +
                "idDM=" + idDM +
                ", dateDeCréation=" + dateDeCréation +
                '}';
    }
}