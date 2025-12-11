package ma.teethcare.repository.modules.patient.api;

import ma.teethcare.entities.Antecedent;
import ma.teethcare.entities.Patient;
import ma.teethcare.entities.enums.CategorieAntecedent;
import ma.teethcare.entities.enums.NiveauRisque;
import ma.teethcare.repository.common.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface AntecedentRepository extends CrudRepository<Antecedent, Long> {

    // Recherche
    Optional<Antecedent> findByNom(String nom);
    List<Antecedent> findByCategorie(CategorieAntecedent categorie);
    List<Antecedent> findByNiveauRisque(NiveauRisque niveau);

    // Recherche combinée
    List<Antecedent> findByCategorieAndNiveau(CategorieAntecedent categorie, NiveauRisque niveau);

    // Statistiques
    boolean existsById(Long id);
    long count();
    long countByCategorie(CategorieAntecedent categorie);
    long countByNiveauRisque(NiveauRisque niveau);

    // Pagination
    List<Antecedent> findPage(int limit, int offset);
    List<Antecedent> findAllSortedByNom(int limit, int offset);
    List<Antecedent> findAllSortedByNiveauRisque(int limit, int offset);

    // ---- Navigation inverse Many-to-Many ----
    List<Patient> getPatientsHavingAntecedent(Long antecedentId);
    long countPatientsByAntecedent(Long antecedentId);
}