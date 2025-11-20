package ma.teethcare.repository.modules.revenues.api;

import ma.teethcare.entities.Revenues;
import ma.teethcare.repository.common.CrudRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface RevenuesRepository extends CrudRepository<Revenues, Long> {

    List<Revenues> findByTitre(String titre);

    List<Revenues> findBetweenDates(LocalDateTime startDate, LocalDateTime endDate);

    Double getTotalRevenues();

    Double getTotalRevenuesBetweenDates(LocalDateTime startDate, LocalDateTime endDate);
}