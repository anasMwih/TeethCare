package ma.teethcare.repository.modules.cabinet.fileBase_implementation;

import ma.teethcare.entities.CabinetMedicale;
import ma.teethcare.repository.modules.cabinet.api.CabinetMedicaleRepository;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;
import java.util.stream.Collectors;

public class CabinetMedicaleRepositoryImpl implements CabinetMedicaleRepository {

    private static final String CLASSPATH_FILE = "fileBase/cabinets.psv";
    private static final String HEADER = "ID|Nom|Email|Logo|CIN|Tel1|Tel2|Siteweb|Instagram";

    private final Path localFilePath =
            Paths.get(System.getProperty("user.home"), ".teethcare", "fileBase", "cabinets.psv");

    public CabinetMedicaleRepositoryImpl() {
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
            throw new RuntimeException("Erreur d'initialisation du fichier cabinets.psv", e);
        }
    }

    private List<String> readAllLines() {
        try {
            return Files.readAllLines(localFilePath, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException("Erreur de lecture du fichier cabinets.psv", e);
        }
    }

    private void writeAllLines(List<String> lines) {
        try {
            Files.write(localFilePath, lines, StandardCharsets.UTF_8,
                    StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException e) {
            throw new RuntimeException("Erreur d'écriture dans cabinets.psv", e);
        }
    }

    @Override
    public List<CabinetMedicale> findAll() {
        return readAllLines().stream()
                .skip(1)
                .filter(line -> !line.isBlank())
                .map(this::toCabinet)
                .collect(Collectors.toList());
    }

    @Override
    public CabinetMedicale findById(Long id) {
        return findAll().stream()
                .filter(c -> Objects.equals(c.getIdUser(), id))
                .findFirst()
                .orElse(null);
    }

    @Override
    public void create(CabinetMedicale cabinet) {
        List<CabinetMedicale> cabinets = findAll();
        long newId = cabinets.stream()
                .mapToLong(c -> c.getIdUser() == null ? 0 : c.getIdUser())
                .max().orElse(0) + 1;
        cabinet.setIdUser(newId);
        cabinets.add(cabinet);
        saveAll(cabinets);
    }

    @Override
    public void update(CabinetMedicale cabinet) {
        List<CabinetMedicale> cabinets = findAll();
        for (int i = 0; i < cabinets.size(); i++) {
            if (Objects.equals(cabinets.get(i).getIdUser(), cabinet.getIdUser())) {
                cabinets.set(i, cabinet);
                saveAll(cabinets);
                return;
            }
        }
        throw new RuntimeException("Cabinet avec ID " + cabinet.getIdUser() + " introuvable.");
    }

    @Override
    public void delete(CabinetMedicale cabinet) {
        if (cabinet != null && cabinet.getIdUser() != null)
            deleteById(cabinet.getIdUser());
    }

    @Override
    public void deleteById(Long id) {
        List<CabinetMedicale> cabinets = findAll().stream()
                .filter(c -> !Objects.equals(c.getIdUser(), id))
                .collect(Collectors.toList());
        saveAll(cabinets);
    }

    @Override
    public CabinetMedicale findByNom(String nom) {
        return findAll().stream()
                .filter(c -> nom != null && nom.equalsIgnoreCase(c.getNom()))
                .findFirst()
                .orElse(null);
    }

    @Override
    public CabinetMedicale findByEmail(String email) {
        return findAll().stream()
                .filter(c -> email != null && email.equalsIgnoreCase(c.getEmail()))
                .findFirst()
                .orElse(null);
    }

    @Override
    public CabinetMedicale findByCin(String cin) {
        return findAll().stream()
                .filter(c -> cin != null && cin.equalsIgnoreCase(c.getCin()))
                .findFirst()
                .orElse(null);
    }

    private void saveAll(List<CabinetMedicale> cabinets) {
        List<String> lines = new ArrayList<>();
        lines.add(HEADER);
        for (CabinetMedicale c : cabinets) {
            lines.add(String.join("|",
                    stringOrNull(c.getIdUser()),
                    stringOrNull(c.getNom()),
                    stringOrNull(c.getEmail()),
                    stringOrNull(c.getLogo()),
                    stringOrNull(c.getCin()),
                    stringOrNull(c.getTel1()),
                    stringOrNull(c.getTel2()),
                    stringOrNull(c.getSiteweb()),
                    stringOrNull(c.getInstagram())
            ));
        }
        writeAllLines(lines);
    }

    private CabinetMedicale toCabinet(String line) {
        String[] t = line.split("\\|", -1);
        CabinetMedicale c = new CabinetMedicale();
        c.setIdUser(parseLong(t[0]));
        c.setNom(parseNullableString(t[1]));
        c.setEmail(parseNullableString(t[2]));
        c.setLogo(parseNullableString(t[3]));
        c.setCin(parseNullableString(t[4]));
        c.setTel1(parseNullableString(t[5]));
        c.setTel2(parseNullableString(t[6]));
        c.setSiteweb(parseNullableString(t[7]));
        c.setInstagram(parseNullableString(t[8]));
        return c;
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