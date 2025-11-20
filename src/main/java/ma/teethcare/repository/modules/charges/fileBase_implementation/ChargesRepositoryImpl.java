package ma.teethcare.repository.modules.charges.fileBase_implementation;

import ma.teethcare.entities.Charges;
import ma.teethcare.repository.modules.charges.api.ChargesRepository;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class ChargesRepositoryImpl implements ChargesRepository {

    private static final String CLASSPATH_FILE = "fileBase/charges.psv";
    private static final String HEADER = "ID|Titre|Description|Montant|Date";

    private final Path localFilePath =
            Paths.get(System.getProperty("user.home"), ".teethcare", "fileBase", "charges.psv");

    public ChargesRepositoryImpl() {
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
            throw new RuntimeException("Erreur d'initialisation du fichier charges.psv", e);
        }
    }

    private List<String> readAllLines() {
        try {
            return Files.readAllLines(localFilePath, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException("Erreur de lecture du fichier charges.psv", e);
        }
    }

    private void writeAllLines(List<String> lines) {
        try {
            Files.write(localFilePath, lines, StandardCharsets.UTF_8,
                    StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException e) {
            throw new RuntimeException("Erreur d'écriture dans charges.psv", e);
        }
    }

    @Override
    public List<Charges> findAll() {
        return readAllLines().stream()
                .skip(1)
                .filter(line -> !line.isBlank())
                .map(this::toCharges)
                .collect(Collectors.toList());
    }

    @Override
    public Charges findById(Long id) {
        return findAll().stream()
                .filter(c -> Objects.equals(c.getId(), id))
                .findFirst()
                .orElse(null);
    }

    @Override
    public void create(Charges charges) {
        List<Charges> chargesList = findAll();
        long newId = chargesList.stream()
                .mapToLong(c -> c.getId() == null ? 0 : c.getId())
                .max().orElse(0) + 1;
        charges.setId(newId);
        if (charges.getDatet() == null) {
            charges.setDatet(LocalDateTime.now());
        }
        chargesList.add(charges);
        saveAll(chargesList);
    }

    @Override
    public void update(Charges charges) {
        List<Charges> chargesList = findAll();
        for (int i = 0; i < chargesList.size(); i++) {
            if (Objects.equals(chargesList.get(i).getId(), charges.getId())) {
                chargesList.set(i, charges);
                saveAll(chargesList);
                return;
            }
        }
        throw new RuntimeException("Charge avec ID " + charges.getId() + " introuvable.");
    }

    @Override
    public void delete(Charges charges) {
        if (charges != null && charges.getId() != null)
            deleteById(charges.getId());
    }

    @Override
    public void deleteById(Long id) {
        List<Charges> chargesList = findAll().stream()
                .filter(c -> !Objects.equals(c.getId(), id))
                .collect(Collectors.toList());
        saveAll(chargesList);
    }

    @Override
    public List<Charges> findByTitre(String titre) {
        return findAll().stream()
                .filter(c -> titre != null && titre.equalsIgnoreCase(c.getTitre()))
                .collect(Collectors.toList());
    }

    @Override
    public List<Charges> findBetweenDates(LocalDateTime startDate, LocalDateTime endDate) {
        return findAll().stream()
                .filter(c -> c.getDatet() != null
                        && !c.getDatet().isBefore(startDate)
                        && !c.getDatet().isAfter(endDate))
                .collect(Collectors.toList());
    }

    @Override
    public Double getTotalCharges() {
        return findAll().stream()
                .mapToDouble(c -> c.getMontant() != null ? c.getMontant() : 0.0)
                .sum();
    }

    @Override
    public Double getTotalChargesBetweenDates(LocalDateTime startDate, LocalDateTime endDate) {
        return findBetweenDates(startDate, endDate).stream()
                .mapToDouble(c -> c.getMontant() != null ? c.getMontant() : 0.0)
                .sum();
    }

    private void saveAll(List<Charges> chargesList) {
        List<String> lines = new ArrayList<>();
        lines.add(HEADER);
        for (Charges c : chargesList) {
            lines.add(String.join("|",
                    stringOrNull(c.getId()),
                    stringOrNull(c.getTitre()),
                    stringOrNull(c.getDescription()),
                    stringOrNull(c.getMontant()),
                    stringOrNull(c.getDatet())
            ));
        }
        writeAllLines(lines);
    }

    private Charges toCharges(String line) {
        String[] t = line.split("\\|", -1);
        Charges c = new Charges();
        c.setId(parseLong(t[0]));
        c.setTitre(parseNullableString(t[1]));
        c.setDescription(parseNullableString(t[2]));
        c.setMontant(parseDouble(t[3]));
        c.setDatet(parseNullableLocalDateTime(t[4]));
        return c;
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