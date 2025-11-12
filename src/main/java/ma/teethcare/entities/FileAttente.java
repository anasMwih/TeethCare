package com.teethcare.entites;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Objects;

@Entity
@Table(name = "file_attente")
public class FileAttente {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idFileAttente;

    @Column(name = "date_file")
    private LocalDate dateFile;

    @Enumerated(EnumType.STRING)
    @Column(name = "statut")
    private StatutFileAttente statut;

    public FileAttente() {}

    public FileAttente(LocalDate dateFile, StatutFileAttente statut) {
        this.dateFile = dateFile;
        this.statut = statut;
    }

    public Long getIdFileAttente() { return idFileAttente; }
    public void setIdFileAttente(Long idFileAttente) { this.idFileAttente = idFileAttente; }

    public LocalDate getDateFile() { return dateFile; }
    public void setDateFile(LocalDate dateFile) { this.dateFile = dateFile; }

    public StatutFileAttente getStatut() { return statut; }
    public void setStatut(StatutFileAttente statut) { this.statut = statut; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FileAttente that = (FileAttente) o;
        return Objects.equals(idFileAttente, that.idFileAttente);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idFileAttente);
    }

    @Override
    public String toString() {
        return "FileAttente{" +
                "idFileAttente=" + idFileAttente +
                ", dateFile=" + dateFile +
                ", statut=" + statut +
                '}';
    }
}