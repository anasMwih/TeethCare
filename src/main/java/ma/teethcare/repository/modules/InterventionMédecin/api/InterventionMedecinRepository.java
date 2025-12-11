package ma.teethcare.repository.modules.intervention.api;

import ma.teethcare.entities.InterventionMedecin;
import ma.teethcare.repository.common.CrudRepository;
import java.time.LocalDate;
import java.util.List;

public interface InterventionMedecinRepository extends CrudRepository<InterventionMedecin, Long> {

    // Recherche par consultation
    List<InterventionMedecin> findByConsultationId(Long consultationId);

    // Recherche par médecin
    List<InterventionMedecin> findByMedecinId(Long medecinId);
    List<InterventionMedecin> findByMedecinIdAndDate(Long medecinId, LocalDate date);

    // Recherche par date
    List<InterventionMedecin> findByDate(LocalDate date);
    List<InterventionMedecin> findByDateRange(LocalDate start, LocalDate end);

    // Recherche par type d'intervention
    List<InterventionMedecin> findByTypeIntervention(String type);
    List<String> findAllTypesIntervention();

    // Recherche par durée
    List<InterventionMedecin> findByDureeGreaterThan(Integer minutes);
    List<InterventionMedecin> findByDureeBetween(Integer minMinutes, Integer maxMinutes);

    // Recherche par coût
    List<InterventionMedecin> findByCoutGreaterThan(Double coutMin);
    List<InterventionMedecin> findByCoutBetween(Double coutMin, Double coutMax);

    // Recherche avancée
    List<InterventionMedecin> search(String typeIntervention, Long medecinId, LocalDate dateDebut, LocalDate dateFin, Double coutMax);

    // Statistiques
    long countByMedecinId(Long medecinId);
    long countByTypeIntervention(String type);
    long countByDate(LocalDate date);

    // Calculs
    Double getCoutTotalByMedecin(Long medecinId);
    Double getCoutTotalByConsultation(Long consultationId);
    Double getCoutTotalByDate(LocalDate date);
    Integer getDureeTotaleByMedecin(Long medecinId);

    // Suppression en cascade
    void deleteByConsultationId(Long consultationId);
    void deleteByMedecinId(Long medecinId);
}