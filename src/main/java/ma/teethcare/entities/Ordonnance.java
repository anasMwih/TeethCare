package com.teethcare.entites;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Objects;

@Entity
@Table(name = "ordonnance")
public class Ordonnance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idOrdonnance;

    @Column(name = "date_ordonnance")
    private LocalDate dateOrdonnance;

    @Column(name = "description", length = 1000)
    private String description;

    public Ordonnance() {}

    public Ordonnance(LocalDate dateOrdonnance, String description) {
        this.dateOrdonnance = dateOrdonnance;
        this.description = description;
    }

    public Long getIdOrdonnance() { return idOrdonnance; }
    public void setIdOrdonnance(Long idOrdonnance) { this.idOrdonnance = idOrdonnance; }

    public LocalDate getDateOrdonnance() { return dateOrdonnance; }
    public void setDateOrdonnance(LocalDate dateOrdonnance) { this.dateOrdonnance = dateOrdonnance; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Ordonnance that = (Ordonnance) o;
        return Objects.equals(idOrdonnance, that.idOrdonnance);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idOrdonnance);
    }

    @Override
    public String toString() {
        return "Ordonnance{" +
                "idOrdonnance=" + idOrdonnance +
                ", dateOrdonnance=" + dateOrdonnance +
                ", description='" + description + '\'' +
                '}';
    }
}