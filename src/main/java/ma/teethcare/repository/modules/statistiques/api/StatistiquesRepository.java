package ma.teethcare.repository.modules.statistiques.api;

import ma.teethcare.entities.Statistiques;
import ma.teethcare.repository.common.CrudRepository;

import java.time.LocalDate;
import java.util.List;

public interface StatistiquesRepository extends CrudRepository<Statistiques, Long> {

    List<Statistiques> findByCategorie(String categorie);

    List<Statistiques> findByDate(LocalDate date);

    List<Statistiques> findBetweenDates(LocalDate startDate, LocalDate endDate);

    List<Statistiques> findLatest(int limit);
}