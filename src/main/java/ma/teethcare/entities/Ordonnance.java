package ma.teethcare.entities;

import java.time.LocalDate;
import java.util.List;

public class Ordonnance {
    private Long id;
    private LocalDate date;
    private String instructions;

    // Relations
    private Consultation consultation;
    private List<Prescription> prescriptions;

    public Ordonnance() {}

    public Ordonnance(Consultation consultation, LocalDate date) {
        this.consultation = consultation;
        this.date = date;
    }

    // Getters et Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }

    public String getInstructions() { return instructions; }
    public void setInstructions(String instructions) { this.instructions = instructions; }

    public Consultation getConsultation() { return consultation; }
    public void setConsultation(Consultation consultation) { this.consultation = consultation; }

    public List<Prescription> getPrescriptions() { return prescriptions; }
    public void setPrescriptions(List<Prescription> prescriptions) { this.prescriptions = prescriptions; }
}