package ma.teethcare.repository.modules.statistiques.api;

import ma.teethcare.entities.Statistiques;
import ma.teethcare.entities.enums.CategorieStatistique;
import ma.teethcare.repository.common.CrudRepository;

import java.time.LocalDate;
import java.util.List;

public interface StatistiquesRepository extends CrudRepository<Statistiques, Long> {
    List<Statistiques> findByCabinetMedicaleId(Long cabinetId);
    List<Statistiques> findByCategorie(CategorieStatistique categorie);
    List<Statistiques> findByDateCalculBetween(LocalDate start, LocalDate end);
    Statistiques findLatestByCategorie(Long cabinetId, CategorieStatistique categorie);
    List<Statistiques> findTopByChiffre(Long cabinetId, int limit);
}