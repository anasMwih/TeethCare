package ma.teethcare.repository.modules.finances.api;

import ma.teethcare.entities.Charges;
import ma.teethcare.repository.common.CrudRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface ChargesRepository extends CrudRepository<Charges, Long> {
    List<Charges> findByCabinetMedicaleId(Long cabinetId);
    List<Charges> findByDateBetween(LocalDateTime start, LocalDateTime end);
    List<Charges> findByMontantGreaterThan(Double montant);
    double getTotalChargesByCabinet(Long cabinetId);
}