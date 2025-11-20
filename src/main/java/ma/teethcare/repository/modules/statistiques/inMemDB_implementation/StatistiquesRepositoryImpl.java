package ma.teethcare.repository.modules.statistiques.inMemDB_implementation;

import ma.teethcare.entities.Statistiques;
import ma.teethcare.repository.modules.statistiques.api.StatistiquesRepository;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class StatistiquesRepositoryImpl implements StatistiquesRepository {

    private final List<Statistiques> data = new ArrayList<>();
    private Long nextId = 1L;

    public StatistiquesRepositoryImpl() {
        initializeTestData();
    }

    private void initializeTestData() {
        LocalDate today = LocalDate.now();

        data.add(Statistiques.builder()
                .id(nextId++)
                .nom("Nombre de patients")
                .catégorie("PATIENTS")
                .chiffre(245.0)
                .dateCalcul(today)
                .build());

        data.add(Statistiques.builder()
                .id(nextId++)
                .nom("Consultations du jour")
                .catégorie("CONSULTATIONS")
                .chiffre(12.0)
                .dateCalcul(today)
                .build());

        data.add(Statistiques.builder()
                .id(nextId++)
                .nom("Revenus du mois")
                .catégorie("FINANCES")
                .chiffre(78000.0)
                .dateCalcul(today)
                .build());

        data.add(Statistiques.builder()
                .id(nextId++)
                .nom("Charges du mois")
                .catégorie("FINANCES")
                .chiffre(52200.0)
                .dateCalcul(today)
                .build());

        data.add(Statistiques.builder()
                .id(nextId++)
                .nom("Taux occupation")
                .catégorie("ACTIVITE")
                .chiffre(85.5)
                .dateCalcul(today)
                .build());

        data.add(Statistiques.builder()
                .id(nextId++)
                .nom("Nouveaux patients")
                .catégorie("PATIENTS")
                .chiffre(18.0)
                .dateCalcul(today.minusDays(1))
                .build());

        data.add(Statistiques.builder()
                .id(nextId++)
                .nom("RDV annulés")
                .catégorie("CONSULTATIONS")
                .chiffre(3.0)
                .dateCalcul(today.minusDays(1))
                .build());

        data.sort(Comparator.comparing(Statistiques::getDateCalcul).reversed());
    }

    @Override
    public List<Statistiques> findAll() {
        return new ArrayList<>(data);
    }

    @Override
    public Statistiques findById(Long id) {
        return data.stream()
                .filter(s -> s.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    @Override
    public void create(Statistiques statistiques) {
        if (statistiques.getId() == null) {
            statistiques.setId(nextId++);
        }
        if (statistiques.getDateCalcul() == null) {
            statistiques.setDateCalcul(LocalDate.now());
        }
        data.add(statistiques);
    }

    @Override
    public void update(Statistiques statistiques) {
        deleteById(statistiques.getId());
        data.add(statistiques);
        data.sort(Comparator.comparing(Statistiques::getDateCalcul).reversed());
    }

    @Override
    public void delete(Statistiques statistiques) {
        data.removeIf(s -> s.getId().equals(statistiques.getId()));
    }

    @Override
    public void deleteById(Long id) {
        data.removeIf(s -> s.getId().equals(id));
    }

    @Override
    public List<Statistiques> findByCategorie(String categorie) {
        return data.stream()
                .filter(s -> categorie != null && categorie.equalsIgnoreCase(s.getCatégorie()))
                .collect(Collectors.toList());
    }

    @Override
    public List<Statistiques> findByDate(LocalDate date) {
        return data.stream()
                .filter(s -> date != null && date.equals(s.getDateCalcul()))
                .collect(Collectors.toList());
    }

    @Override
    public List<Statistiques> findBetweenDates(LocalDate startDate, LocalDate endDate) {
        return data.stream()
                .filter(s -> s.getDateCalcul() != null
                        && !s.getDateCalcul().isBefore(startDate)
                        && !s.getDateCalcul().isAfter(endDate))
                .collect(Collectors.toList());
    }

    @Override
    public List<Statistiques> findLatest(int limit) {
        return data.stream()
                .sorted(Comparator.comparing(Statistiques::getDateCalcul).reversed())
                .limit(limit)
                .collect(Collectors.toList());
    }
}