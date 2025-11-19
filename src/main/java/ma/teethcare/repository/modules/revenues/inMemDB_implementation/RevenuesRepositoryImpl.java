package ma.teethcare.repository.modules.revenues.inMemDB_implementation;

import ma.teethcare.entities.Revenues;
import ma.teethcare.repository.modules.revenues.api.RevenuesRepository;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class RevenuesRepositoryImpl implements RevenuesRepository {

    private final List<Revenues> data = new ArrayList<>();
    private Long nextId = 1L;

    public RevenuesRepositoryImpl() {
        initializeTestData();
    }

    private void initializeTestData() {
        LocalDateTime now = LocalDateTime.now();

        data.add(Revenues.builder()
                .id(nextId++)
                .titre("Consultations")
                .description("Revenus consultations " + now.getMonth())
                .montant(45000.0)
                .datet(now.withDayOfMonth(1).withHour(0).withMinute(0))
                .build());

        data.add(Revenues.builder()
                .id(nextId++)
                .titre("Interventions Chirurgicales")
                .description("Revenus interventions et chirurgie")
                .montant(32000.0)
                .datet(now.minusDays(5))
                .build());

        data.add(Revenues.builder()
                .id(nextId++)
                .titre("Orthodontie")
                .description("Traitement orthodontique")
                .montant(18000.0)
                .datet(now.minusDays(7))
                .build());

        data.add(Revenues.builder()
                .id(nextId++)
                .titre("Prothèses Dentaires")
                .description("Pose de prothèses")
                .montant(12500.0)
                .datet(now.minusDays(10))
                .build());

        data.add(Revenues.builder()
                .id(nextId++)
                .titre("Détartrage et Soins")
                .description("Soins préventifs et hygiène")
                .montant(8500.0)
                .datet(now.minusDays(12))
                .build());

        data.sort(Comparator.comparing(Revenues::getDatet).reversed());
    }

    @Override
    public List<Revenues> findAll() {
        return new ArrayList<>(data);
    }

    @Override
    public Revenues findById(Long id) {
        return data.stream()
                .filter(r -> r.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    @Override
    public void create(Revenues revenues) {
        if (revenues.getId() == null) {
            revenues.setId(nextId++);
        }
        if (revenues.getDatet() == null) {
            revenues.setDatet(LocalDateTime.now());
        }
        data.add(revenues);
    }

    @Override
    public void update(Revenues revenues) {
        deleteById(revenues.getId());
        data.add(revenues);
        data.sort(Comparator.comparing(Revenues::getDatet).reversed());
    }

    @Override
    public void delete(Revenues revenues) {
        data.removeIf(r -> r.getId().equals(revenues.getId()));
    }

    @Override
    public void deleteById(Long id) {
        data.removeIf(r -> r.getId().equals(id));
    }

    @Override
    public List<Revenues> findByTitre(String titre) {
        return data.stream()
                .filter(r -> titre != null && titre.equalsIgnoreCase(r.getTitre()))
                .collect(Collectors.toList());
    }

    @Override
    public List<Revenues> findBetweenDates(LocalDateTime startDate, LocalDateTime endDate) {
        return data.stream()
                .filter(r -> r.getDatet() != null
                        && !r.getDatet().isBefore(startDate)
                        && !r.getDatet().isAfter(endDate))
                .collect(Collectors.toList());
    }

    @Override
    public Double getTotalRevenues() {
        return data.stream()
                .mapToDouble(r -> r.getMontant() != null ? r.getMontant() : 0.0)
                .sum();
    }

    @Override
    public Double getTotalRevenuesBetweenDates(LocalDateTime startDate, LocalDateTime endDate) {
        return findBetweenDates(startDate, endDate).stream()
                .mapToDouble(r -> r.getMontant() != null ? r.getMontant() : 0.0)
                .sum();
    }
}