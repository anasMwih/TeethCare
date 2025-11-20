package ma.teethcare.repository.modules.cabinet.api;

import ma.teethcare.entities.CabinetMedicale;
import ma.teethcare.repository.common.CrudRepository;

public interface CabinetMedicaleRepository extends CrudRepository<CabinetMedicale, Long> {

    CabinetMedicale findByNom(String nom);

    CabinetMedicale findByEmail(String email);

    CabinetMedicale findByCin(String cin);
}