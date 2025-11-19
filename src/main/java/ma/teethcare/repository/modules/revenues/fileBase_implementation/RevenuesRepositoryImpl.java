package ma.teethcare.repository.modules.revenues.fileBase_implementation;

import ma.teethcare.entities.Revenues;
import ma.teethcare.repository.modules.revenues.api.RevenuesRepository;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class RevenuesRepositoryImpl implements RevenuesRepository {

    private static final String CLASSPATH_FILE = "fileBase/revenues.psv";
    private static final String HEADER = "ID|Titre|Description|Montant|Date";

    private final Path localFilePath =
            Paths.get(System.getProperty("user.home"), ".teethcare", "fileBase", "revenues.psv");

    public RevenuesRepositoryImpl() {
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
            throw new RuntimeException("Erreur d'initialisation du fichier revenues.psv", e);
        }
    }

    private List<String> readAllLines() {
        try {
            return Files.readAllLines(localFilePath, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException("Erreur de lecture du fichier revenues.psv", e);
        }
    }

    private void writeAllLines(List<String> lines) {
        try {
            Files.write(localFilePath, lines, StandardCharsets.UTF_8,
                    StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException e) {
            throw new RuntimeException("Erreur d'écriture dans revenues.psv", e);
        }
    }

    @Override
    public List<Revenues> findAll() {
        return readAllLines().stream()
                .skip(1)
                .filter(line -> !line.isBlank())
                .map(this::toRevenues)
                .collect(Collectors.toList());
    }

    @Override
    public Revenues findById(Long id) {
        return findAll().stream()
                .filter(r -> Objects.equals(r.getId(), id))
                .findFirst()
                .orElse(null);
    }

    @Override
    public void create(Revenues revenues) {
        List<Revenues> revenuesList = findAll();
        long newId = revenuesList.stream()
                .mapToLong(r -> r.getId() == null ? 0 : r.getId())
                .max().orElse(0) + 1;
        revenues.setId(newId);
        if (revenues.getDatet() == null) {
            revenues.setDatet(LocalDateTime.now());
        }
        revenuesList.add(revenues);
        saveAll(revenuesList);
    }

    @Override
    public void update(Revenues revenues) {
        List<Revenues> revenuesList = findAll();
        for (int i = 0; i < revenuesList.size(); i++) {
            if (Objects.equals(revenuesList.get(i).getId(), revenues.getId())) {
                revenuesList.set(i, revenues);
                saveAll(revenuesList);
                return;
            }
        }
        throw new RuntimeException("Revenue avec ID " + revenues.getId() + " introuvable.");
    }

    @Override
    public void delete(Revenues revenues) {
        if (revenues != null && revenues.getId() != null)
            deleteById(revenues.getId());
    }

    @Override
    public void deleteById(Long id) {
        List<Revenues> revenuesList = findAll().stream()
                .filter(r -> !Objects.equals(r.getId(), id))
                .collect(Collectors.toList());
        saveAll(revenuesList);
    }

    @Override
    public List<Revenues> findByTitre(String titre) {
        return findAll().stream()
                .filter(r -> titre != null && titre.equalsIgnoreCase(r.getTitre()))
                .collect(Collectors.toList());
    }

    @Override
    public List<Revenues> findBetweenDates(LocalDateTime startDate, LocalDateTime endDate) {
        return findAll().stream()
                .filter(r -> r.getDatet() != null
                        && !r.getDatet().isBefore(startDate)
                        && !r.getDatet().isAfter(endDate))
                .collect(Collectors.toList());
    }

    @Override
    public Double getTotalRevenues() {
        return findAll().stream()
                .mapToDouble(r -> r.getMontant() != null ? r.getMontant() : 0.0)
                .sum();
    }

    @Override
    public Double getTotalRevenuesBetweenDates(LocalDateTime startDate, LocalDateTime endDate) {
        return findBetweenDates(startDate, endDate).stream()
                .mapToDouble(r -> r.getMontant() != null ? r.getMontant() : 0.0)
                .sum();
    }

    private void saveAll(List<Revenues> revenuesList) {
        List<String> lines = new ArrayList<>();
        lines.add(HEADER);
        for (Revenues r : revenuesList) {
            lines.add(String.join("|",
                    stringOrNull(r.getId()),
                    stringOrNull(r.getTitre()),
                    stringOrNull(r.getDescription()),
                    stringOrNull(r.getMontant()),
                    stringOrNull(r.getDatet())
            ));
        }
        writeAllLines(lines);
    }

    private Revenues toRevenues(String line) {
        String[] t = line.split("\\|", -1);
        Revenues r = new Revenues();
        r.setId(parseLong(t[0]));
        r.setTitre(parseNullableString(t[1]));
        r.setDescription(parseNullableString(t[2]));
        r.setMontant(parseDouble(t[3]));
        r.setDatet(parseNullableLocalDateTime(t[4]));
        return r;
    }

    private String parseNullableString(String s) {
        return (s == null || s.isBlank() || s.equalsIgnoreCase("null")) ? null : s.trim();
    }

    private LocalDateTime parseNullableLocalDateTime(String s) {
        if (s == null || s.isBlank() || s.equalsIgnoreCase("null")) return null;
        try { return LocalDateTime.parse(s.trim()); } catch (Exception e) { return null; }
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