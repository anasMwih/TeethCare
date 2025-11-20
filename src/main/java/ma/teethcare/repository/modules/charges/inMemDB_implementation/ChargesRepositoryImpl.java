package ma.teethcare.repository.modules.charges.inMemDB_implementation;

import ma.teethcare.entities.Charges;
import ma.teethcare.repository.modules.charges.api.ChargesRepository;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class ChargesRepositoryImpl implements ChargesRepository {

    private final List<Charges> data = new ArrayList<>();
    private Long nextId = 1L;

    public ChargesRepositoryImpl() {
        initializeTestData();
    }

    private void initializeTestData() {
        LocalDateTime now = LocalDateTime.now();

        data.add(Charges.builder()
                .id(nextId++)
                .titre("Loyer Cabinet")
                .description("Loyer mensuel " + now.getMonth())
                .montant(8000.0)
                .datet(now.withDayOfMonth(1).withHour(0).withMinute(0))
                .build());

        data.add(Charges.builder()
                .id(nextId++)
                .titre("Salaires Personnel")
                .description("Salaires mensuels équipe")
                .montant(35000.0)
                .datet(now.withDayOfMonth(1).withHour(0).withMinute(0))
                .build());

        data.add(Charges.builder()
                .id(nextId++)
                .titre("Matériel Dentaire")
                .description("Achat matériel et consommables")
                .montant(5500.0)
                .datet(now.minusDays(5))
                .build());

        data.add(Charges.builder()
                .id(nextId++)
                .titre("Électricité")
                .description("Facture électricité")
                .montant(1200.0)
                .datet(now.minusDays(10))
                .build());

        data.add(Charges.builder()
                .id(nextId++)
                .titre("Assurance Cabinet")
                .description("Prime assurance professionnelle")
                .montant(2500.0)
                .datet(now.minusDays(15))
                .build());

        data.sort(Comparator.comparing(Charges::getDatet).reversed());
    }

    @Override
    public List<Charges> findAll() {
        return new ArrayList<>(data);
    }

    @Override
    public Charges findById(Long id) {
        return data.stream()
                .filter(c -> c.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    @Override
    public void create(Charges charges) {
        if (charges.getId() == null) {
            charges.setId(nextId++);
        }
        if (charges.getDatet() == null) {
            charges.setDatet(LocalDateTime.now());
        }
        data.add(charges);
    }

    @Override
    public void update(Charges charges) {
        deleteById(charges.getId());
        data.add(charges);
        data.sort(Comparator.comparing(Charges::getDatet).reversed());
    }

    @Override
    public void delete(Charges charges) {
        data.removeIf(c -> c.getId().equals(charges.getId()));
    }

    @Override
    public void deleteById(Long id) {
        data.removeIf(c -> c.getId().equals(id));
    }

    @Override
    public List<Charges> findByTitre(String titre) {
        return data.stream()
                .filter(c -> titre != null && titre.equalsIgnoreCase(c.getTitre()))
                .collect(Collectors.toList());
    }

    @Override
    public List<Charges> findBetweenDates(LocalDateTime startDate, LocalDateTime endDate) {
        return data.stream()
                .filter(c -> c.getDatet() != null
                        && !c.getDatet().isBefore(startDate)
                        && !c.getDatet().isAfter(endDate))
                .collect(Collectors.toList());
    }

    @Override
    public Double getTotalCharges() {
        return data.stream()
                .mapToDouble(c -> c.getMontant() != null ? c.getMontant() : 0.0)
                .sum();
    }

    @Override
    public Double getTotalChargesBetweenDates(LocalDateTime startDate, LocalDateTime endDate) {
        return findBetweenDates(startDate, endDate).stream()
                .mapToDouble(c -> c.getMontant() != null ? c.getMontant() : 0.0)
                .sum();
    }
}