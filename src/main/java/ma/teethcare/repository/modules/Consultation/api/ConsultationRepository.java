package ma.dentalTech.repository.modules.dossierMedical.api;

import ma.dentalTech.entities.Consultation;
import ma.dentalTech.entities.Patient;
import ma.dentalTech.entities.Medecin;
import ma.dentalTech.repository.common.CrudRepository;
import java.util.List;
import java.util.Optional;

public interface ConsultationRepository extends CrudRepository<Consultation, Long> {

    // Recherche des consultations par patient (relation Consultation-Patient)
    List<Consultation> findByPatient(Patient patient);

    // Recherche des consultations par patient ID
    List<Consultation> findByPatientId(Long patientId);

    // Recherche des consultations par médecin (relation Consultation-Médecin)
    List<Consultation> findByMedecin(Medecin medecin);

    // Recherche des consultations par médecin ID
    List<Consultation> findByMedecinId(Long medecinId);

    // Recherche des consultations avec ordonnances (relation Consultation-Ordonnance)
    List<Consultation> findByOrdonnanceIsNotNull();

    // Recherche des consultations par date
    List<Consultation> findByDateConsultation(String date);

    // Recherche des consultations par type
    List<Consultation> findByTypeConsultation(String type);

    // Recherche des consultations par statut
    List<Consultation> findByStatut(String statut);

    // Consultation la plus récente d'un patient
    Optional<Consultation> findTopByPatientIdOrderByDateConsultationDesc(Long patientId);

    // Compter le nombre de consultations par patient
    long countByPatientId(Long patientId);

    // Compter le nombre de consultations par médecin
    long countByMedecinId(Long medecinId);

    // Vérifier si une consultation existe pour un patient à une date donnée
    boolean existsByPatientIdAndDateConsultation(Long patientId, String dateConsultation);

    // Recherche des consultations avec factures (relation Consultation-Facture)
    List<Consultation> findByFactureIsNotNull();
}