package ma.teethcare.repository.modules.prescription.api;

import ma.teethcare.entities.Prescription;
import ma.teethcare.repository.common.CrudRepository;
import java.util.List;

public interface PrescriptionRepository extends CrudRepository<Prescription, Long> {

    // Recherche par ordonnance
    List<Prescription> findByOrdonnanceId(Long ordonnanceId);

    // Recherche par médicament
    List<Prescription> findByMedicamentId(Long medicamentId);

    // Recherche par durée de traitement
    List<Prescription> findByDureeTraitementGreaterThan(Integer jours);
    List<Prescription> findByDureeTraitementBetween(Integer minJours, Integer maxJours);

    // Recherche combinée
    List<Prescription> findByOrdonnanceAndMedicament(Long ordonnanceId, Long medicamentId);

    // Statistiques
    long countByOrdonnanceId(Long ordonnanceId);
    long countByMedicamentId(Long medicamentId);

    // Calculs
    Double getCoutTotalByOrdonnance(Long ordonnanceId);
    Double getCoutTotalByMedicament(Long medicamentId);

    // Suppression en cascade
    void deleteByOrdonnanceId(Long ordonnanceId);
    void deleteByMedicamentId(Long medicamentId);
}