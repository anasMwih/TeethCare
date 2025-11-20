package ma.dentalTech.repository.modules.dossierMedical.api;

import ma.dentalTech.entities.Ordonnance;
import ma.dentalTech.entities.Consultation;
import ma.dentalTech.entities.Patient;
import ma.dentalTech.entities.Medicament;
import ma.dentalTech.repository.common.CrudRepository;
import java.util.List;
import java.util.Optional;

public interface OrdonnanceRepository extends CrudRepository<Ordonnance, Long> {

    // Recherche des ordonnances par consultation (relation Ordonnance-Consultation)
    List<Ordonnance> findByConsultation(Consultation consultation);

    // Recherche des ordonnances par consultation ID
    List<Ordonnance> findByConsultationId(Long consultationId);

    // Recherche des ordonnances par patient (relation Ordonnance-Patient)
    List<Ordonnance> findByPatient(Patient patient);

    // Recherche des ordonnances par patient ID
    List<Ordonnance> findByPatientId(Long patientId);

    // Recherche des ordonnances par médecin
    List<Ordonnance> findByMedecinId(Long medecinId);

    // Recherche des ordonnances par date
    List<Ordonnance> findByDateOrdonnance(String date);

    // Recherche des ordonnances contenant un médicament spécifique
    List<Ordonnance> findByMedicamentsContaining(Medicament medicament);

    // Ordonnance la plus récente d'un patient
    Optional<Ordonnance> findTopByPatientIdOrderByDateOrdonnanceDesc(Long patientId);

    // Compter le nombre d'ordonnances par patient
    long countByPatientId(Long patientId);

    // Compter le nombre d'ordonnances par médecin
    long countByMedecinId(Long medecinId);

    // Vérifier si une ordonnance existe pour une consultation
    boolean existsByConsultationId(Long consultationId);
}