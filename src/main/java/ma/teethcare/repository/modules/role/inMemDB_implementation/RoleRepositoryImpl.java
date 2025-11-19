package ma.teethcare.repository.modules.role.inMemDB_implementation;

import ma.teethcare.entities.Role;
import ma.teethcare.repository.modules.role.api.RoleRepository;

import java.util.*;
import java.util.stream.Collectors;

public class RoleRepositoryImpl implements RoleRepository {

    private final List<Role> data = new ArrayList<>();
    private Long nextId = 1L;

    public RoleRepositoryImpl() {
        initializeTestData();
    }

    private void initializeTestData() {
        // Rôle Admin
        Role admin = Role.builder()
                .idRole(nextId++)
                .libellé("ADMIN")
                .privileges(Arrays.asList(
                        "MANAGE_USERS",
                        "MANAGE_ROLES",
                        "VIEW_STATS",
                        "MANAGE_CABINET",
                        "MANAGE_BACKUP",
                        "MANAGE_SETTINGS"
                ))
                .build();
        data.add(admin);

        // Rôle Médecin
        Role medecin = Role.builder()
                .idRole(nextId++)
                .libellé("MEDECIN")
                .privileges(Arrays.asList(
                        "VIEW_PATIENTS",
                        "MANAGE_PATIENTS",
                        "MANAGE_CONSULTATIONS",
                        "MANAGE_PRESCRIPTIONS",
                        "VIEW_MEDICAL_RECORDS",
                        "MANAGE_APPOINTMENTS",
                        "VIEW_STATS"
                ))
                .build();
        data.add(medecin);

        // Rôle Secrétaire
        Role secretaire = Role.builder()
                .idRole(nextId++)
                .libellé("SECRETAIRE")
                .privileges(Arrays.asList(
                        "VIEW_PATIENTS",
                        "MANAGE_PATIENTS",
                        "MANAGE_APPOINTMENTS",
                        "VIEW_BILLING",
                        "MANAGE_BILLING",
                        "SEND_NOTIFICATIONS"
                ))
                .build();
        data.add(secretaire);

        data.sort(Comparator.comparing(Role::getIdRole));
    }

    @Override
    public List<Role> findAll() {
        return new ArrayList<>(data);
    }

    @Override
    public Role findById(Long id) {
        return data.stream()
                .filter(r -> r.getIdRole().equals(id))
                .findFirst()
                .orElse(null);
    }

    @Override
    public void create(Role role) {
        if (role.getIdRole() == null) {
            role.setIdRole(nextId++);
        }
        data.add(role);
    }

    @Override
    public void update(Role role) {
        deleteById(role.getIdRole());
        data.add(role);
        data.sort(Comparator.comparing(Role::getIdRole));
    }

    @Override
    public void delete(Role role) {
        data.removeIf(r -> r.getIdRole().equals(role.getIdRole()));
    }

    @Override
    public void deleteById(Long id) {
        data.removeIf(r -> r.getIdRole().equals(id));
    }

    @Override
    public Role findByLibelle(String libelle) {
        return data.stream()
                .filter(r -> libelle != null && libelle.equalsIgnoreCase(r.getLibellé()))
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<Role> findByUserId(Long userId) {
        // Dans une vraie implémentation, il faudrait une table de liaison
        // Pour les tests, on retourne des rôles basés sur l'ID
        List<Role> userRoles = new ArrayList<>();

        if (userId == 1L) { // Admin
            userRoles.add(findByLibelle("ADMIN"));
        } else if (userId == 2L || userId == 3L) { // Médecins
            userRoles.add(findByLibelle("MEDECIN"));
        } else if (userId == 4L || userId == 5L) { // Secrétaires
            userRoles.add(findByLibelle("SECRETAIRE"));
        }

        return userRoles;
    }

    @Override
    public boolean hasPrivilege(Long userId, String privilege) {
        List<Role> userRoles = findByUserId(userId);
        return userRoles.stream()
                .flatMap(r -> r.getPrivileges() != null ? r.getPrivileges().stream() : java.util.stream.Stream.empty())
                .anyMatch(p -> p.equalsIgnoreCase(privilege));
    }
}