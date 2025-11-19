package ma.teethcare.repository.modules.agenda.inMemDB_implementation;

import ma.teethcare.entities.AgendaMensuel;
import ma.teethcare.repository.modules.agenda.api.AgendaMensuelRepository;

import java.time.LocalDate;
import java.util.*;

public class AgendaMensuelRepositoryImpl implements AgendaMensuelRepository {

    private final List<AgendaMensuel> data = new ArrayList<>();
    private Long nextId = 1L;

    public AgendaMensuelRepositoryImpl() {
        initializeTestData();
    }

    private void initializeTestData() {
        // Agenda du mois en cours
        String currentMonth = LocalDate.now().getMonth().name();

        AgendaMensuel currentAgenda = AgendaMensuel.builder()
                .id(nextId++)
                .mois(currentMonth)
                .joursNonDisponible(Arrays.asList("2025-11-20", "2025-11-21", "2025-11-27"))
                .build();
        data.add(currentAgenda);

        // Agenda du mois prochain
        String nextMonth = LocalDate.now().plusMonths(1).getMonth().name();

        AgendaMensuel nextAgenda = AgendaMensuel.builder()
                .id(nextId++)
                .mois(nextMonth)
                .joursNonDisponible(Arrays.asList("2025-12-25", "2025-12-26"))
                .build();
        data.add(nextAgenda);

        // Agendas des mois précédents
        data.add(AgendaMensuel.builder()
                .id(nextId++)
                .mois("OCTOBER")
                .joursNonDisponible(Arrays.asList("2025-10-15", "2025-10-22"))
                .build());

        data.add(AgendaMensuel.builder()
                .id(nextId++)
                .mois("SEPTEMBER")
                .joursNonDisponible(Arrays.asList("2025-09-10", "2025-09-17"))
                .build());
    }

    @Override
    public List<AgendaMensuel> findAll() {
        return new ArrayList<>(data);
    }

    @Override
    public AgendaMensuel findById(Long id) {
        return data.stream()
                .filter(a -> a.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    @Override
    public void create(AgendaMensuel agenda) {
        if (agenda.getId() == null) {
            agenda.setId(nextId++);
        }
        data.add(agenda);
    }

    @Override
    public void update(AgendaMensuel agenda) {
        deleteById(agenda.getId());
        data.add(agenda);
        data.sort(Comparator.comparing(AgendaMensuel::getId));
    }

    @Override
    public void delete(AgendaMensuel agenda) {
        data.removeIf(a -> a.getId().equals(agenda.getId()));
    }

    @Override
    public void deleteById(Long id) {
        data.removeIf(a -> a.getId().equals(id));
    }

    @Override
    public AgendaMensuel findByMois(String mois) {
        return data.stream()
                .filter(a -> mois != null && mois.equalsIgnoreCase(a.getMois()))
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<AgendaMensuel> findByMedecinId(Long medecinId) {
        // Dans une vraie implémentation, filtrer par médecin
        // Pour les tests, retourner tous les agendas
        return findAll();
    }

    @Override
    public AgendaMensuel findCurrentByMedecinId(Long medecinId) {
        String currentMonth = LocalDate.now().getMonth().name();
        return findByMois(currentMonth);
    }
}