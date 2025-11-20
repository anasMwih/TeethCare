package ma.teethcare.repository.modules.secretaire.fileBase_implementation;



import ma.teethcare.entities.Secretaire;
import ma.teethcare.repository.modules.secretaire.api.SecretaireRepository;

public class SecretaireRepositoryImpl implements SecretaireRepository {

    private final List<Secretaire> data = new ArrayList<>();

    public SecretaireRepositoryImpl() {
        LocalDateTime now = LocalDateTime.now();

        data.add(Secretaire.builder()
                .id(1L)
                .nom("Samira")
                .prenom("Khalil")
                .email("samira.khalil@cabinet.com")
                .telephone("0661-123456")
                .salaire(6000.0)
                .dateCreation(now.minusHours(3))
                .cabinetId(1L)
                .build());

        data.add(Secretaire.builder()
                .id(2L)
                .nom("Karim")
                .prenom("Chakir")
                .email("karim.chakir@cabinet.com")
                .telephone("0662-987654")
                .salaire(5800.0)
                .dateCreation(now.minusHours(1))
                .cabinetId(1L)
                .build());

        data.sort(Comparator.comparing(Secretaire::getId));
    }

    @Override
    public List<Secretaire> findAll() {
        return List.copyOf(data);
    }

    @Override
    public Secretaire findById(Long id) {
        return data.stream().filter(s -> Objects.equals(s.getId(), id)).findFirst().orElse(null);
    }

    @Override
    public void create(Secretaire secretaire) {
        data.add(secretaire);
    }

    @Override
    public void update(Secretaire secretaire) {
        deleteById(secretaire.getId());
        data.add(secretaire);
    }

    @Override
    public void delete(Secretaire secretaire) {
        deleteById(secretaire.getId());
    }

    @Override
    public void deleteById(Long id) {
        data.removeIf(s -> Objects.equals(s.getId(), id));
    }
}
