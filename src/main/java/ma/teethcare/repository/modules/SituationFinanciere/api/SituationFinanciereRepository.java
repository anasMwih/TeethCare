package ma.teethcare.repository.modules.finance.api;

import ma.teethcare.entities.SituationFinanciere;
import ma.teethcare.entities.enums.EnPromo;
import ma.teethcare.entities.enums.StatutFinancier;
import ma.teethcare.repository.common.CrudRepository;

import java.util.List;

public interface SituationFinanciereRepository extends CrudRepository<SituationFinanciere, Long> {

    List<SituationFinanciere> findByStatut(StatutFinancier statut);
    List<SituationFinanciere> findByEnPromo(EnPromo enPromo);

    List<SituationFinanciere> findByCreditGreaterThan(Double minCredit);
    List<SituationFinanciere> findByCreditBetween(Double min, Double max);
    List<SituationFinanciere> findByCreditEquals(Double credit);

    List<SituationFinanciere> findByTotaleDesActesBetween(Double min, Double max);
    List<SituationFinanciere> findByTotalePayeBetween(Double min, Double max);

    List<SituationFinanciere> findByCreditAndStatut(Double credit, StatutFinancier statut);

    boolean existsById(Long id);
    boolean existsByStatut(StatutFinancier statut);

    long count();
    long countByStatut(StatutFinancier statut);
    long countByEnPromo(EnPromo enPromo);

    List<SituationFinanciere> findPage(int limit, int offset);

    // ---- Statistiques financières ----
    Double sumTotaleDesActes();
    Double sumTotalePaye();
    Double sumCredit();

    Double sumTotaleDesActesByStatut(StatutFinancier statut);
    Double sumCreditByStatut(StatutFinancier statut);

    Double avgCredit();
    Double avgTotaleDesActes();

    // ---- Méthodes de mise à jour ----
    void updateCredit(Long id, Double nouveauCredit);
    void updateStatut(Long id, StatutFinancier nouveauStatut);
    void updateTotalePaye(Long id, Double nouveauTotalePaye);

    // ---- Calcul automatique ----
    void calculerCreditAutomatique(Long id);
    void mettreAJourStatutAutomatique(Long id);

    // ---- Recherche avancée ----
    List<SituationFinanciere> findSituationsProblematic();
    List<SituationFinanciere> findSituationsAvecPromotion();
}