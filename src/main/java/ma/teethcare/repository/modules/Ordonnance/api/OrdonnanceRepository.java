package ma.teethcare.repository.modules.ordonnance.api;

import ma.teethcare.entities.Consultation;
import ma.teethcare.entities.Ordonnance;
import ma.teethcare.entities.Prescription;
import ma.teethcare.repository.common.CrudRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface OrdonnanceRepository extends CrudRepository<Ordonnance, Long> {

    List<Ordonnance> findByConsultation(Consultation consultation);
    List<Ordonnance> findByConsultationId(Long consultationId);

    List<Ordonnance> findByDate(LocalDate date);
    List<Ordonnance> findByDateBetween(LocalDate start, LocalDate end);

    List<Ordonnance> findByInstructionsContaining(String keyword);

    boolean existsById(Long id);
    boolean existsByConsultationId(Long consultationId);

    long count();
    long countByConsultationId(Long consultationId);
    long countByDate(LocalDate date);

    List<Ordonnance> findPage(int limit, int offset);

    // ---- Statistiques ----
    long countOrdonnancesByMonth(int year, int month);

    // ---- Relations avec Prescription ----
    List<Prescription> getPrescriptionsOfOrdonnance(Long ordonnanceId);
    void addPrescriptionToOrdonnance(Long ordonnanceId, Long prescriptionId);
    void removePrescriptionFromOrdonnance(Long ordonnanceId, Long prescriptionId);
    void removeAllPrescriptionsFromOrdonnance(Long ordonnanceId);

    // ---- Recherche avancée ----
    List<Ordonnance> searchByConsultationPatient(String patientNom);
    List<Ordonnance> findRecentOrdonnances(int days);
}