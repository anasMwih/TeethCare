package ma.teethcare.repository.modules.consultation.api;

import ma.teethcare.entities.Consultation;
import ma.teethcare.entities.DossierMedical;
import ma.teethcare.entities.Facture;
import ma.teethcare.entities.InterventionMedecin;
import ma.teethcare.entities.Patient;
import ma.teethcare.entities.Prescription;
import ma.teethcare.repository.common.CrudRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ConsultationRepository extends CrudRepository<Consultation, Long> {

    List<Consultation> findByPatient(Patient patient);
    List<Consultation> findByPatientId(Long patientId);

    List<Consultation> findByDossierMedical(DossierMedical dossier);
    List<Consultation> findByDossierMedicalId(Long dossierId);

    List<Consultation> findByDate(LocalDate date);
    List<Consultation> findByDateBetween(LocalDate start, LocalDate end);

    List<Consultation> findByPrixBetween(Double min, Double max);

    List<Consultation> findBySymptomesContaining(String keyword);
    List<Consultation> findByDiagnosticContaining(String keyword);
    List<Consultation> findByTraitementContaining(String keyword);

    Optional<Facture> getFactureOfConsultation(Long consultationId);

    boolean existsById(Long id);
    long count();
    List<Consultation> findPage(int limit, int offset);

    // ---- Statistiques ----
    long countByDate(LocalDate date);
    long countByPatientId(Long patientId);
    Double sumPrixByDate(LocalDate date);
    Double sumPrixByPatientId(Long patientId);

    // ---- Relations avec InterventionMedecin ----
    List<InterventionMedecin> getInterventionsOfConsultation(Long consultationId);
    void addInterventionToConsultation(Long consultationId, Long interventionId);
    void removeInterventionFromConsultation(Long consultationId, Long interventionId);

    // ---- Relations avec Prescription ----
    List<Prescription> getPrescriptionsOfConsultation(Long consultationId);
    void addPrescriptionToConsultation(Long consultationId, Long prescriptionId);
    void removePrescriptionFromConsultation(Long consultationId, Long prescriptionId);
}