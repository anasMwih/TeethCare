package ma.teethcare.repository.modules.facture.api;

import ma.teethcare.entities.Consultation;
import ma.teethcare.entities.Facture;
import ma.teethcare.entities.Patient;
import ma.teethcare.repository.common.CrudRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface FactureRepository extends CrudRepository<Facture, Long> {

    Optional<Facture> findByConsultation(Consultation consultation);
    Optional<Facture> findByConsultationId(Long consultationId);

    List<Facture> findByPatient(Patient patient);
    List<Facture> findByPatientId(Long patientId);

    List<Facture> findByStatut(String statut);

    List<Facture> findByDateFacture(LocalDate date);
    List<Facture> findByDateFactureBetween(LocalDateTime start, LocalDateTime end);

    List<Facture> findByNumeroFacture(String numero);
    List<Facture> findByNumeroFactureContaining(String keyword);

    List<Facture> findByMontantTotalBetween(Double min, Double max);
    List<Facture> findByResteAPayerGreaterThan(Double montant);
    List<Facture> findByResteAPayerEquals(Double montant);

    boolean existsById(Long id);
    boolean existsByConsultationId(Long consultationId);
    boolean existsByNumeroFacture(String numeroFacture);

    long count();
    long countByStatut(String statut);
    long countByPatientId(Long patientId);

    List<Facture> findPage(int limit, int offset);

    // ---- Statistiques financières ----
    Double sumMontantTotal();
    Double sumMontantTotalByStatut(String statut);
    Double sumMontantTotalByPatientId(Long patientId);
    Double sumMontantTotalByDate(LocalDate date);

    Double sumMontantPaye();
    Double sumMontantPayeByStatut(String statut);

    Double sumResteAPayer();
    Double sumResteAPayerByPatientId(Long patientId);

    // ---- Méthodes de mise à jour ----
    void updateStatut(Long factureId, String statut);
    void updateMontantPaye(Long factureId, Double montantPaye);
    void updateResteAPayer(Long factureId, Double resteAPayer);

    // ---- Recherche avancée ----
    List<Facture> searchByPatientNomPrenom(String keyword);
    List<Facture> findEnRetard(int joursRetard);
}