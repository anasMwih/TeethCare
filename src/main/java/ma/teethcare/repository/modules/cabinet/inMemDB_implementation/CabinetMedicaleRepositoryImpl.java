package ma.teethcare.repository.modules.cabinet.inMemDB_implementation;

import ma.teethcare.entities.CabinetMedicale;
import ma.teethcare.repository.modules.cabinet.api.CabinetMedicaleRepository;

import java.util.*;

public class CabinetMedicaleRepositoryImpl implements CabinetMedicaleRepository {

    private final List<CabinetMedicale> data = new ArrayList<>();
    private Long nextId = 1L;

    public CabinetMedicaleRepositoryImpl() {
        initializeTestData();
    }

    private void initializeTestData() {
        CabinetMedicale cabinet = CabinetMedicale.builder()
                .idUser(nextId++)
                .nom("TeethCare Dental Clinic")
                .email("contact@teethcare.ma")
                .logo("teethcare_logo.png")
                .cin("K987654")
                .tel1("0522-123456")
                .tel2("0661-234567")
                .siteweb("www.teethcare.ma")
                .instagram("@teethcare_clinic")
                .charges(new ArrayList<>())
                .revenues(new ArrayList<>())
                .statistiques(new ArrayList<>())
                .staffs(new ArrayList<>())
                .build();
        data.add(cabinet);
    }

    @Override
    public List<CabinetMedicale> findAll() {
        return new ArrayList<>(data);
    }

    @Override
    public CabinetMedicale findById(Long id) {
        return data.stream()
                .filter(c -> c.getIdUser().equals(id))
                .findFirst()
                .orElse(null);
    }

    @Override
    public void create(CabinetMedicale cabinet) {
        if (cabinet.getIdUser() == null) {
            cabinet.setIdUser(nextId++);
        }
        data.add(cabinet);
    }

    @Override
    public void update(CabinetMedicale cabinet) {
        deleteById(cabinet.getIdUser());
        data.add(cabinet);
        data.sort(Comparator.comparing(CabinetMedicale::getIdUser));
    }

    @Override
    public void delete(CabinetMedicale cabinet) {
        data.removeIf(c -> c.getIdUser().equals(cabinet.getIdUser()));
    }

    @Override
    public void deleteById(Long id) {
        data.removeIf(c -> c.getIdUser().equals(id));
    }

    @Override
    public CabinetMedicale findByNom(String nom) {
        return data.stream()
                .filter(c -> nom != null && nom.equalsIgnoreCase(c.getNom()))
                .findFirst()
                .orElse(null);
    }

    @Override
    public CabinetMedicale findByEmail(String email) {
        return data.stream()
                .filter(c -> email != null && email.equalsIgnoreCase(c.getEmail()))
                .findFirst()
                .orElse(null);
    }

    @Override
    public CabinetMedicale findByCin(String cin) {
        return data.stream()
                .filter(c -> cin != null && cin.equalsIgnoreCase(c.getCin()))
                .findFirst()
                .orElse(null);
    }
}