package ma.teethcare.repository.modules.certificat.api;

import ma.teethcare.entities.Certificat;
import ma.teethcare.repository.common.CrudRepository;
import java.time.LocalDate;
import java.util.List;

public interface CertificatRepository extends CrudRepository<Certificat, Long> {

    // Recherche par dossier médical
    List<Certificat> findByDossierMedicalId(Long dossierMedicalId);

    // Recherche par consultation
    List<Certificat> findByConsultationId(Long consultationId);

    // Recherche par type
    List<Certificat> findByType(String type);
    List<String> findAllTypes();

    // Recherche par date d'émission
    List<Certificat> findByDateEmission(LocalDate date);
    List<Certificat> findByDateEmissionBetween(LocalDate start, LocalDate end);

    // Recherche par date d'expiration
    List<Certificat> findByDateExpiration(LocalDate date);
    List<Certificat> findCertificatsExpirantAvant(LocalDate date);
    List<Certificat> findCertificatsExpires();
    List<Certificat> findCertificatsValides();

    // Recherche par patient (via dossier médical)
    List<Certificat> findByPatientId(Long patientId);

    // Recherche avancée
    List<Certificat> search(String type, Long patientId, LocalDate dateDebut, LocalDate dateFin, Boolean valide);

    // Statistiques
    long countByType(String type);
    long countByDossierMedicalId(Long dossierMedicalId);
    long countCertificatsExpires();
    long countCertificatsValides();

    // Vérifications
    boolean isCertificatValide(Long certificatId);
    boolean hasPatientCertificatValide(Long patientId, String type);

    // Suppression en cascade
    void deleteByDossierMedicalId(Long dossierMedicalId);
    void deleteByConsultationId(Long consultationId);
}