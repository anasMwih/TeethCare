package ma.teethcare.repository.modules.finance.impl.memoryBase;

import ma.teethcare.entities.SituationFinanciere;
import ma.teethcare.entities.enums.EnPromo;
import ma.teethcare.entities.enums.StatutFinancier;
import ma.teethcare.repository.modules.finance.api.SituationFinanciereRepository;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class SituationFinanciereRepositoryImpl implements SituationFinanciereRepository {

    private final List<SituationFinanciere> data = new ArrayList<>();
    private long nextId = 1L;

    public SituationFinanciereRepositoryImpl() {
        // Données d'exemple

        // Situation 1 - Solvable
        SituationFinanciere situation1 = new SituationFinanciere();
        situation1.setIdSF(nextId++);
        situation1.setTotaleDesActes(5000.0);
        situation1.setTotalePaye(5000.0);
        situation1.setCredit(0.0);
        situation1.setStatut(StatutFinancier.SOLVABLE);
        situation1.setEnPromo(EnPromo.NON);
        data.add(situation1);

        // Situation 2 - En retard partiel
        SituationFinanciere situation2 = new SituationFinanciere();
        situation2.setIdSF(nextId++);
        situation2.setTotaleDesActes(3000.0);
        situation2.setTotalePaye(2000.0);
        situation2.setCredit(1000.0);
        situation2.setStatut(StatutFinancier.EN_RETARD);
        situation2.setEnPromo(EnPromo.OUI);
        data.add(situation2);

        // Situation 3 - En négociation
        SituationFinanciere situation3 = new SituationFinanciere();
        situation3.setIdSF(nextId++);
        situation3.setTotaleDesActes(8000.0);
        situation3.setTotalePaye(4000.0);
        situation3.setCredit(4000.0);
        situation3.setStatut(StatutFinancier.EN_NEGOCIATION);
        situation3.setEnPromo(EnPromo.NON);
        data.add(situation3);

        // Situation 4 - Solvable avec promotion
        SituationFinanciere situation4 = new SituationFinanciere();
        situation4.setIdSF(nextId++);
        situation4.setTotaleDesActes(2500.0);
        situation4.setTotalePaye(2500.0);
        situation4.setCredit(0.0);
        situation4.setStatut(StatutFinancier.SOLVABLE);
        situation4.setEnPromo(EnPromo.OUI);
        data.add(situation4);

        // Situation 5 - Défaillant
        SituationFinanciere situation5 = new SituationFinanciere();
        situation5.setIdSF(nextId++);
        situation5.setTotaleDesActes(10000.0);
        situation5.setTotalePaye(2000.0);
        situation5.setCredit(8000.0);
        situation5.setStatut(StatutFinancier.DEFAILLANT);
        situation5.setEnPromo(EnPromo.NON);
        data.add(situation5);

        data.sort(Comparator.comparing(SituationFinanciere::getCredit).reversed());
    }

    @Override
    public List<SituationFinanciere> findAll() {
        return List.copyOf(data);
    }

    @Override
    public SituationFinanciere findById(Long id) {
        return data.stream()
                .filter(s -> s.getIdSF().equals(id))
                .findFirst()
                .orElse(null);
    }

    @Override
    public void create(SituationFinanciere situation) {
        if (situation.getIdSF() == null) {
            situation.setIdSF(nextId++);
        }
        data.add(situation);
        data.sort(Comparator.comparing(SituationFinanciere::getCredit).reversed());
    }

    @Override
    public void update(SituationFinanciere situation) {
        deleteById(situation.getIdSF());
        data.add(situation);
        data.sort(Comparator.comparing(SituationFinanciere::getCredit).reversed());
    }

    @Override
    public void delete(SituationFinanciere situation) {
        data.removeIf(s -> s.getIdSF().equals(situation.getIdSF()));
    }

    @Override
    public void deleteById(Long id) {
        data.removeIf(s -> s.getIdSF().equals(id));
    }

    @Override
    public List<SituationFinanciere> findByStatut(StatutFinancier statut) {
        return data.stream()
                .filter(s -> s.getStatut() == statut)
                .collect(Collectors.toList());
    }

    @Override
    public List<SituationFinanciere> findByEnPromo(EnPromo enPromo) {
        return data.stream()
                .filter(s -> s.getEnPromo() == enPromo)
                .collect(Collectors.toList());
    }

    @Override
    public List<SituationFinanciere> findByCreditGreaterThan(Double minCredit) {
        return data.stream()
                .filter(s -> s.getCredit() != null && s.getCredit() > minCredit)
                .collect(Collectors.toList());
    }

    @Override
    public List<SituationFinanciere> findByCreditBetween(Double min, Double max) {
        return data.stream()
                .filter(s -> s.getCredit() != null
                        && s.getCredit() >= min
                        && s.getCredit() <= max)
                .collect(Collectors.toList());
    }

    @Override
    public List<SituationFinanciere> findByCreditEquals(Double credit) {
        return data.stream()
                .filter(s -> s.getCredit() != null && s.getCredit().equals(credit))
                .collect(Collectors.toList());
    }

    @Override
    public List<SituationFinanciere> findByTotaleDesActesBetween(Double min, Double max) {
        return data.stream()
                .filter(s -> s.getTotaleDesActes() != null
                        && s.getTotaleDesActes() >= min
                        && s.getTotaleDesActes() <= max)
                .collect(Collectors.toList());
    }

    @Override
    public List<SituationFinanciere> findByTotalePayeBetween(Double min, Double max) {
        return data.stream()
                .filter(s -> s.getTotalePaye() != null
                        && s.getTotalePaye() >= min
                        && s.getTotalePaye() <= max)
                .collect(Collectors.toList());
    }

    @Override
    public List<SituationFinanciere> findByCreditAndStatut(Double credit, StatutFinancier statut) {
        return data.stream()
                .filter(s -> s.getCredit() != null && s.getCredit().equals(credit) && s.getStatut() == statut)
                .collect(Collectors.toList());
    }

    @Override
    public boolean existsById(Long id) {
        return data.stream().anyMatch(s -> s.getIdSF().equals(id));
    }

    @Override
    public boolean existsByStatut(StatutFinancier statut) {
        return data.stream().anyMatch(s -> s.getStatut() == statut);
    }

    @Override
    public long count() {
        return data.size();
    }

    @Override
    public long countByStatut(StatutFinancier statut) {
        return data.stream()
                .filter(s -> s.getStatut() == statut)
                .count();
    }

    @Override
    public long countByEnPromo(EnPromo enPromo) {
        return data.stream()
                .filter(s -> s.getEnPromo() == enPromo)
                .count();
    }

    @Override
    public List<SituationFinanciere> findPage(int limit, int offset) {
        return data.stream()
                .skip(offset)
                .limit(limit)
                .collect(Collectors.toList());
    }

    @Override
    public Double sumTotaleDesActes() {
        return data.stream()
                .mapToDouble(s -> s.getTotaleDesActes() != null ? s.getTotaleDesActes() : 0.0)
                .sum();
    }

    @Override
    public Double sumTotalePaye() {
        return data.stream()
                .mapToDouble(s -> s.getTotalePaye() != null ? s.getTotalePaye() : 0.0)
                .sum();
    }

    @Override
    public Double sumCredit() {
        return data.stream()
                .mapToDouble(s -> s.getCredit() != null ? s.getCredit() : 0.0)
                .sum();
    }

    @Override
    public Double sumTotaleDesActesByStatut(StatutFinancier statut) {
        return data.stream()
                .filter(s -> s.getStatut() == statut)
                .mapToDouble(s -> s.getTotaleDesActes() != null ? s.getTotaleDesActes() : 0.0)
                .sum();
    }

    @Override
    public Double sumCreditByStatut(StatutFinancier statut) {
        return data.stream()
                .filter(s -> s.getStatut() == statut)
                .mapToDouble(s -> s.getCredit() != null ? s.getCredit() : 0.0)
                .sum();
    }

    @Override
    public Double avgCredit() {
        return data.stream()
                .filter(s -> s.getCredit() != null)
                .mapToDouble(SituationFinanciere::getCredit)
                .average()
                .orElse(0.0);
    }

    @Override
    public Double avgTotaleDesActes() {
        return data.stream()
                .filter(s -> s.getTotaleDesActes() != null)
                .mapToDouble(SituationFinanciere::getTotaleDesActes)
                .average()
                .orElse(0.0);
    }

    @Override
    public void updateCredit(Long id, Double nouveauCredit) {
        SituationFinanciere situation = findById(id);
        if (situation != null) {
            situation.setCredit(nouveauCredit);
            // Mettre à jour le statut automatiquement
            mettreAJourStatutAutomatique(id);
        }
    }

    @Override
    public void updateStatut(Long id, StatutFinancier nouveauStatut) {
        SituationFinanciere situation = findById(id);
        if (situation != null) {
            situation.setStatut(nouveauStatut);
        }
    }

    @Override
    public void updateTotalePaye(Long id, Double nouveauTotalePaye) {
        SituationFinanciere situation = findById(id);
        if (situation != null) {
            situation.setTotalePaye(nouveauTotalePaye);
            // Recalculer le crédit
            situation.setCredit(situation.getTotaleDesActes() - nouveauTotalePaye);
            // Mettre à jour le statut automatiquement
            mettreAJourStatutAutomatique(id);
        }
    }

    @Override
    public void calculerCreditAutomatique(Long id) {
        SituationFinanciere situation = findById(id);
        if (situation != null && situation.getTotaleDesActes() != null && situation.getTotalePaye() != null) {
            situation.setCredit(situation.getTotaleDesActes() - situation.getTotalePaye());
        }
    }

    @Override
    public void mettreAJourStatutAutomatique(Long id) {
        SituationFinanciere situation = findById(id);
        if (situation != null && situation.getCredit() != null) {
            if (situation.getCredit() == 0.0) {
                situation.setStatut(StatutFinancier.SOLVABLE);
            } else if (situation.getCredit() > 0 && situation.getCredit() <= 1000) {
                situation.setStatut(StatutFinancier.EN_RETARD);
            } else if (situation.getCredit() > 1000 && situation.getCredit() <= 5000) {
                situation.setStatut(StatutFinancier.EN_NEGOCIATION);
            } else if (situation.getCredit() > 5000) {
                situation.setStatut(StatutFinancier.DEFAILLANT);
            }
        }
    }

    @Override
    public List<SituationFinanciere> findSituationsProblematic() {
        return data.stream()
                .filter(s -> s.getStatut() == StatutFinancier.EN_RETARD
                        || s.getStatut() == StatutFinancier.EN_NEGOCIATION
                        || s.getStatut() == StatutFinancier.DEFAILLANT)
                .collect(Collectors.toList());
    }

    @Override
    public List<SituationFinanciere> findSituationsAvecPromotion() {
        return data.stream()
                .filter(s -> s.getEnPromo() == EnPromo.OUI)
                .collect(Collectors.toList());
    }
}