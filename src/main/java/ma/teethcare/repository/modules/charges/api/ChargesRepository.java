package ma.teethcare.repository.modules.charges.api;

import ma.teethcare.entities.Charges;
import ma.teethcare.repository.common.CrudRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface ChargesRepository extends CrudRepository<Charges, Long> {

    List<Charges> findByTitre(String titre);

    List<Charges> findBetweenDates(LocalDateTime startDate, LocalDateTime endDate);

    Double getTotalCharges();

    Double getTotalChargesBetweenDates(LocalDateTime startDate, LocalDateTime endDate);
}