package ma.teethcare.repository.modules.statistiques.fileBase_implementation;

import ma.teethcare.entities.Statistiques;
import ma.teethcare.repository.modules.statistiques.api.StatistiquesRepository;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class StatistiquesRepositoryImpl implements StatistiquesRepository {

    private static final String CLASSPATH_FILE = "fileBase/statistiques.psv";
    private static final String HEADER = "ID|Nom|Categorie|Chiffre|DateCalcul";

    private final Path localFilePath =
            Paths.get(System.getProperty("user.home"), ".teethcare", "fileBase", "statistiques.psv");

    public StatistiquesRepositoryImpl() {
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
            throw new RuntimeException("Erreur d'initialisation du fichier statistiques.psv", e);
        }
    }

    private List<String> readAllLines() {
        try {
            return Files.readAllLines(localFilePath, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException("Erreur de lecture du fichier statistiques.psv", e);
        }
    }

    private void writeAllLines(List<String> lines) {
        try {
            Files.write(localFilePath, lines, StandardCharsets.UTF_8,
                    StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException e) {
            throw new RuntimeException("Erreur d'écriture dans statistiques.psv", e);
        }
    }

    @Override
    public List<Statistiques> findAll() {
        return readAllLines().stream()
                .skip(1)
                .filter(line -> !line.isBlank())
                .map(this::toStatistiques)
                .collect(Collectors.toList());
    }

    @Override
    public Statistiques findById(Long id) {
        return findAll().stream()
                .filter(s -> Objects.equals(s.getId(), id))
                .findFirst()
                .orElse(null);
    }

    @Override
    public void create(Statistiques statistiques) {
        List<Statistiques> statistiquesList = findAll();
        long newId = statistiquesList.stream()
                .mapToLong(s -> s.getId() == null ? 0 : s.getId())
                .max().orElse(0) + 1;
        statistiques.setId(newId);
        if (statistiques.getDateCalcul() == null) {
            statistiques.setDateCalcul(LocalDate.now());
        }
        statistiquesList.add(statistiques);
        saveAll(statistiquesList);
    }

    @Override
    public void update(Statistiques statistiques) {
        List<Statistiques> statistiquesList = findAll();
        for (int i = 0; i < statistiquesList.size(); i++) {
            if (Objects.equals(statistiquesList.get(i).getId(), statistiques.getId())) {
                statistiquesList.set(i, statistiques);
                saveAll(statistiquesList);
                return;
            }
        }
        throw new RuntimeException("Statistique avec ID " + statistiques.getId() + " introuvable.");
    }

    @Override
    public void delete(Statistiques statistiques) {
        if (statistiques != null && statistiques.getId() != null)
            deleteById(statistiques.getId());
    }

    @Override
    public void deleteById(Long id) {
        List<Statistiques> statistiquesList = findAll().stream()
                .filter(s -> !Objects.equals(s.getId(), id))
                .collect(Collectors.toList());
        saveAll(statistiquesList);
    }

    @Override
    public List<Statistiques> findByCategorie(String categorie) {
        return findAll().stream()
                .filter(s -> categorie != null && categorie.equalsIgnoreCase(s.getCatégorie()))
                .collect(Collectors.toList());
    }

    @Override
    public List<Statistiques> findByDate(LocalDate date) {
        return findAll().stream()
                .filter(s -> date != null && date.equals(s.getDateCalcul()))
                .collect(Collectors.toList());
    }

    @Override
    public List<Statistiques> findBetweenDates(LocalDate startDate, LocalDate endDate) {
        return findAll().stream()
                .filter(s -> s.getDateCalcul() != null
                        && !s.getDateCalcul().isBefore(startDate)
                        && !s.getDateCalcul().isAfter(endDate))
                .collect(Collectors.toList());
    }

    @Override
    public List<Statistiques> findLatest(int limit) {
        return findAll().stream()
                .sorted(Comparator.comparing(Statistiques::getDateCalcul).reversed())
                .limit(limit)
                .collect(Collectors.toList());
    }

    private void saveAll(List<Statistiques> statistiquesList) {
        List<String> lines = new ArrayList<>();
        lines.add(HEADER);
        for (Statistiques s : statistiquesList) {
            lines.add(String.join("|",
                    stringOrNull(s.getId()),
                    stringOrNull(s.getNom()),
                    stringOrNull(s.getCatégorie()),
                    stringOrNull(s.getChiffre()),
                    stringOrNull(s.getDateCalcul())
            ));
        }
        writeAllLines(lines);
    }

    private Statistiques toStatistiques(String line) {
        String[] t = line.split("\\|", -1);
        Statistiques s = new Statistiques();
        s.setId(parseLong(t[0]));
        s.setNom(parseNullableString(t[1]));
        s.setCatégorie(parseNullableString(t[2]));
        s.setChiffre(parseDouble(t[3]));
        s.setDateCalcul(parseNullableLocalDate(t[4]));
        return s;
    }

    private String parseNullableString(String s) {
        return (s == null || s.isBlank() || s.equalsIgnoreCase("null")) ? null : s.trim();
    }

    private LocalDate parseNullableLocalDate(String s) {
        if (s == null || s.isBlank() || s.equalsIgnoreCase("null")) return null;
        try { return LocalDate.parse(s.trim()); } catch (Exception e) { return null; }
    }

    private Long parseLong(String s) {
        try { return (s == null || s.isBlank() || s.equalsIgnoreCase("null")) ? null : Long.parseLong(s.trim()); }
        catch (NumberFormatException e) { return null; }
    }

    private Double parseDouble(String s) {
        try { return (s == null || s.isBlank() || s.equalsIgnoreCase("null")) ? null : Double.parseDouble(s.trim()); }
        catch (NumberFormatException e) { return null; }
    }

    private String stringOrNull(Object o) {
        return (o == null) ? "null" : o.toString();
    }
}