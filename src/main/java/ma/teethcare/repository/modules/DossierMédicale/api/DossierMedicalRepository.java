package ma.teethcare.repository.modules.dossier.api;

import ma.teethcare.entities.Certificat;
import ma.teethcare.entities.Consultation;
import ma.teethcare.entities.DossierMedical;
import ma.teethcare.entities.Ordonnance;
import ma.teethcare.entities.Patient;
import ma.teethcare.repository.common.CrudRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface DossierMedicalRepository extends CrudRepository<DossierMedical, Long> {

    Optional<DossierMedical> findByPatient(Patient patient);
    Optional<DossierMedical> findByPatientId(Long patientId);

    List<DossierMedical> findByDateCreation(LocalDate date);
    List<DossierMedical> findByDateCreationBetween(LocalDate start, LocalDate end);

    List<DossierMedical> findByObservationsContaining(String keyword);

    boolean existsById(Long id);
    boolean existsByPatientId(Long patientId);

    long count();
    List<DossierMedical> findPage(int limit, int offset);

    // ---- Relations avec Consultation ----
    List<Consultation> getConsultationsOfDossier(Long dossierId);
    void addConsultationToDossier(Long dossierId, Long consultationId);
    void removeConsultationFromDossier(Long dossierId, Long consultationId);

    // ---- Relations avec Ordonnance ----
    List<Ordonnance> getOrdonnancesOfDossier(Long dossierId);
    void addOrdonnanceToDossier(Long dossierId, Long ordonnanceId);
    void removeOrdonnanceFromDossier(Long dossierId, Long ordonnanceId);

    // ---- Relations avec Certificat ----
    List<Certificat> getCertificatsOfDossier(Long dossierId);
    void addCertificatToDossier(Long dossierId, Long certificatId);
    void removeCertificatFromDossier(Long dossierId, Long certificatId);
}