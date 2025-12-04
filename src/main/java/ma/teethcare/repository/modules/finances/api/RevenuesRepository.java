package ma.teethcare.repository.modules.finances.api;

import ma.teethcare.entities.Revenues;
import ma.teethcare.repository.common.CrudRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface RevenuesRepository extends CrudRepository<Revenues, Long> {
    List<Revenues> findByCabinetMedicaleId(Long cabinetId);
    List<Revenues> findByDateBetween(LocalDateTime start, LocalDateTime end);
    List<Revenues> findByMontantGreaterThan(Double montant);
    double getTotalRevenuesByCabinet(Long cabinetId);
    List<Revenues> getTopRevenues(Long cabinetId, int limit);
}