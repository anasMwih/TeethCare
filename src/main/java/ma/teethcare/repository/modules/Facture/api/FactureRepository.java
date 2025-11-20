package ma.dentalTech.repository.modules.facture.api;

import ma.dentalTech.entities.Facture;
import ma.dentalTech.entities.Patient;
import ma.dentalTech.entities.Consultation;
import ma.dentalTech.repository.common.CrudRepository;
import java.util.List;
import java.util.Optional;

public interface FactureRepository extends CrudRepository<Facture, Long> {

    // Recherche des factures par patient (relation Facture-Patient)
    List<Facture> findByPatient(Patient patient);

    // Recherche des factures par patient ID
    List<Facture> findByPatientId(Long patientId);

    // Recherche des factures par consultation (relation Facture-Consultation)
    Optional<Facture> findByConsultation(Consultation consultation);

    // Recherche des factures par consultation ID
    Optional<Facture> findByConsultationId(Long consultationId);

    // Recherche des factures par statut de paiement
    List<Facture> findByStatutPaiement(String statutPaiement);

    // Recherche des factures impayées
    List<Facture> findByStatutPaiementNot(String statutPaiement);

    // Recherche des factures par date
    List<Facture> findByDateFacture(String date);

    // Recherche des factures par montant supérieur à
    List<Facture> findByMontantGreaterThan(Double montant);

    // Recherche des factures par montant inférieur à
    List<Facture> findByMontantLessThan(Double montant);

    // Facture la plus récente d'un patient
    Optional<Facture> findTopByPatientIdOrderByDateFactureDesc(Long patientId);

    // Compter le nombre de factures par patient
    long countByPatientId(Long patientId);

    // Compter le nombre de factures par statut
    long countByStatutPaiement(String statutPaiement);

    // Somme des montants des factures par patient
    Double sumMontantByPatientId(Long patientId);

    // Vérifier si une facture existe pour une consultation
    boolean existsByConsultationId(Long consultationId);

    // Vérifier si un patient a des factures impayées
    boolean existsByPatientIdAndStatutPaiementNot(Long patientId, String statutPaiement);
}