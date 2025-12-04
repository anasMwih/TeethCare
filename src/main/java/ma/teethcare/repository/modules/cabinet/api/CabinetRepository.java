package ma.teethcare.repository.modules.cabinet.api;

import ma.teethcare.entities.CabinetMedicale;
import ma.teethcare.repository.common.CrudRepository;
import java.util.Optional;

public interface CabinetRepository extends CrudRepository<CabinetMedicale, Long> {
    Optional<CabinetMedicale> findByEmail(String email);
    Optional<CabinetMedicale> findByNom(String nom);
    boolean existsByEmail(String email);
    boolean existsByCin(String cin);
}