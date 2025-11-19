package ma.teethcare.repository.modules.agenda.fileBase_implementation;

import ma.teethcare.entities.AgendaMensuel;
import ma.teethcare.repository.modules.agenda.api.AgendaMensuelRepository;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class AgendaMensuelRepositoryImpl implements AgendaMensuelRepository {

    private static final String CLASSPATH_FILE = "fileBase/agendas.psv";
    private static final String HEADER = "ID|Mois|JoursNonDisponible|MedecinId";

    private final Path localFilePath =
            Paths.get(System.getProperty("user.home"), ".teethcare", "fileBase", "agendas.psv");

    public AgendaMensuelRepositoryImpl() {
        initializeLocalCopy();
    }

    private void initializeLocalCopy() {
        try {
            if (!Files.exists(localFilePath)) {
                Files.createDirectories(localFilePath.getParent());
                URL resource = getClass().getClassLoader().getResource(CLASSPATH_FILE);
                if (resource != null) {
                    Path src = Paths.get(resource.toURI());
                    Files.copy(src, localFilePath, StandardCopyOption.REPLACE_EXISTING);
                } else {
                    Files.write(localFilePath, List.of(HEADER), StandardCharsets.UTF_8);
                }
            }
        } catch (IOException | URISyntaxException e) {
            throw new RuntimeException("Erreur d'initialisation du fichier agendas.psv", e);
        }
    }

    private List<String> readAllLines() {
        try {
            return Files.readAllLines(localFilePath, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException("Erreur de lecture du fichier agendas.psv", e);
        }
    }

    private void writeAllLines(List<String> lines) {
        try {
            Files.write(localFilePath, lines, StandardCharsets.UTF_8,
                    StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException e) {
            throw new RuntimeException("Erreur d'écriture dans agendas.psv", e);
        }
    }

    @Override
    public List<AgendaMensuel> findAll() {
        return readAllLines().stream()
                .skip(1)
                .filter(line -> !line.isBlank())
                .map(this::toAgenda)
                .collect(Collectors.toList());
    }

    @Override
    public AgendaMensuel findById(Long id) {
        return findAll().stream()
                .filter(a -> Objects.equals(a.getId(), id))
                .findFirst()
                .orElse(null);
    }

    @Override
    public void create(AgendaMensuel agenda) {
        List<AgendaMensuel> agendas = findAll();
        long newId = agendas.stream()
                .mapToLong(a -> a.getId() == null ? 0 : a.getId())
                .max().orElse(0) + 1;
        agenda.setId(newId);
        agendas.add(agenda);
        saveAll(agendas);
    }

    @Override
    public void update(AgendaMensuel agenda) {
        List<AgendaMensuel> agendas = findAll();
        for (int i = 0; i < agendas.size(); i++) {
            if (Objects.equals(agendas.get(i).getId(), agenda.getId())) {
                agendas.set(i, agenda);
                saveAll(agendas);
                return;
            }
        }
        throw new RuntimeException("Agenda avec ID " + agenda.getId() + " introuvable.");
    }

    @Override
    public void delete(AgendaMensuel agenda) {
        if (agenda != null && agenda.getId() != null)
            deleteById(agenda.getId());
    }

    @Override
    public void deleteById(Long id) {
        List<AgendaMensuel> agendas = findAll().stream()
                .filter(a -> !Objects.equals(a.getId(), id))
                .collect(Collectors.toList());
        saveAll(agendas);
    }

    @Override
    public AgendaMensuel findByMois(String mois) {
        return findAll().stream()
                .filter(a -> mois != null && mois.equalsIgnoreCase(a.getMois()))
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<AgendaMensuel> findByMedecinId(Long medecinId) {
        // À implémenter avec la relation Medecin-AgendaMensuel
        return findAll();
    }

    @Override
    public AgendaMensuel findCurrentByMedecinId(Long medecinId) {
        String currentMonth = LocalDate.now().getMonth().name();
        return findAll().stream()
                .filter(a -> currentMonth.equalsIgnoreCase(a.getMois()))
                .findFirst()
                .orElse(null);
    }

    private void saveAll(List<AgendaMensuel> agendas) {
        List<String> lines = new ArrayList<>();
        lines.add(HEADER);
        for (AgendaMensuel a : agendas) {
            String joursNonDispo = a.getJoursNonDisponible() != null ?
                    String.join(",", a.getJoursNonDisponible()) : "";
            lines.add(String.join("|",
                    stringOrNull(a.getId()),
                    stringOrNull(a.getMois()),
                    joursNonDispo,
                    "null" // MedecinId à implémenter
            ));
        }
        writeAllLines(lines);
    }

    private AgendaMensuel toAgenda(String line) {
        String[] t = line.split("\\|", -1);
        AgendaMensuel a = new AgendaMensuel();
        a.setId(parseLong(t[0]));
        a.setMois(parseNullableString(t[1]));

        // Parse jours non disponibles (comma-separated)
        if (t.length > 2 && t[2] != null && !t[2].isBlank()) {
            List<String> jours = Arrays.asList(t[2].split(","));
            a.setJoursNonDisponible(jours);
        }

        return a;
    }

    private String parseNullableString(String s) {
        return (s == null || s.isBlank() || s.equalsIgnoreCase("null")) ? null : s.trim();
    }

    private Long parseLong(String s) {
        try { return (s == null || s.isBlank() || s.equalsIgnoreCase("null")) ? null : Long.parseLong(s.trim()); }
        catch (NumberFormatException e) { return null; }
    }

    private String stringOrNull(Object o) {
        return (o == null) ? "null" : o.toString();
    }
}