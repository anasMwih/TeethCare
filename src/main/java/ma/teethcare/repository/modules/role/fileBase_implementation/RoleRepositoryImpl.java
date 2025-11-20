package ma.teethcare.repository.modules.role.fileBase_implementation;

import ma.teethcare.entities.Role;
import ma.teethcare.repository.modules.role.api.RoleRepository;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;
import java.util.stream.Collectors;

public class RoleRepositoryImpl implements RoleRepository {

    private static final String CLASSPATH_FILE = "fileBase/roles.psv";
    private static final String HEADER = "ID|Libelle|Privileges";

    private final Path localFilePath =
            Paths.get(System.getProperty("user.home"), ".teethcare", "fileBase", "roles.psv");

    public RoleRepositoryImpl() {
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
            throw new RuntimeException("Erreur d'initialisation du fichier roles.psv", e);
        }
    }

    private List<String> readAllLines() {
        try {
            return Files.readAllLines(localFilePath, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException("Erreur de lecture du fichier roles.psv", e);
        }
    }

    private void writeAllLines(List<String> lines) {
        try {
            Files.write(localFilePath, lines, StandardCharsets.UTF_8,
                    StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException e) {
            throw new RuntimeException("Erreur d'écriture dans roles.psv", e);
        }
    }

    @Override
    public List<Role> findAll() {
        return readAllLines().stream()
                .skip(1)
                .filter(line -> !line.isBlank())
                .map(this::toRole)
                .collect(Collectors.toList());
    }

    @Override
    public Role findById(Long id) {
        return findAll().stream()
                .filter(r -> Objects.equals(r.getIdRole(), id))
                .findFirst()
                .orElse(null);
    }

    @Override
    public void create(Role role) {
        List<Role> roles = findAll();
        long newId = roles.stream()
                .mapToLong(r -> r.getIdRole() == null ? 0 : r.getIdRole())
                .max().orElse(0) + 1;
        role.setIdRole(newId);
        roles.add(role);
        saveAll(roles);
    }

    @Override
    public void update(Role role) {
        List<Role> roles = findAll();
        for (int i = 0; i < roles.size(); i++) {
            if (Objects.equals(roles.get(i).getIdRole(), role.getIdRole())) {
                roles.set(i, role);
                saveAll(roles);
                return;
            }
        }
        throw new RuntimeException("Role avec ID " + role.getIdRole() + " introuvable.");
    }

    @Override
    public void delete(Role role) {
        if (role != null && role.getIdRole() != null)
            deleteById(role.getIdRole());
    }

    @Override
    public void deleteById(Long id) {
        List<Role> roles = findAll().stream()
                .filter(r -> !Objects.equals(r.getIdRole(), id))
                .collect(Collectors.toList());
        saveAll(roles);
    }

    @Override
    public Role findByLibelle(String libelle) {
        return findAll().stream()
                .filter(r -> libelle != null && libelle.equalsIgnoreCase(r.getLibellé()))
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<Role> findByUserId(Long userId) {
        // Cette méthode nécessite une table de liaison User-Role
        // Pour l'instant, retourne une liste vide
        return new ArrayList<>();
    }

    @Override
    public boolean hasPrivilege(Long userId, String privilege) {
        List<Role> userRoles = findByUserId(userId);
        return userRoles.stream()
                .flatMap(r -> r.getPrivileges() != null ? r.getPrivileges().stream() : java.util.stream.Stream.empty())
                .anyMatch(p -> p.equalsIgnoreCase(privilege));
    }

    private void saveAll(List<Role> roles) {
        List<String> lines = new ArrayList<>();
        lines.add(HEADER);
        for (Role r : roles) {
            String privileges = r.getPrivileges() != null ?
                    String.join(",", r.getPrivileges()) : "";
            lines.add(String.join("|",
                    stringOrNull(r.getIdRole()),
                    stringOrNull(r.getLibellé()),
                    privileges
            ));
        }
        writeAllLines(lines);
    }

    private Role toRole(String line) {
        String[] t = line.split("\\|", -1);
        Role r = new Role();
        r.setIdRole(parseLong(t[0]));
        r.setLibellé(parseNullableString(t[1]));

        // Parse privileges (comma-separated)
        if (t.length > 2 && t[2] != null && !t[2].isBlank()) {
            List<String> privileges = Arrays.asList(t[2].split(","));
            r.setPrivileges(privileges);
        }

        return r;
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