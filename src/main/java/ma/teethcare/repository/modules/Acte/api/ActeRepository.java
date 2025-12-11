package ma.teethcare.repository.modules.acte.api;

import ma.teethcare.entities.Acte;
import ma.teethcare.repository.common.CrudRepository;
import java.util.List;
import java.util.Optional;

public interface ActeRepository extends CrudRepository<Acte, Long> {

    // Recherche par libellé
    Optional<Acte> findByLibelle(String libelle);
    List<Acte> findByLibelleContaining(String keyword);

    // Recherche par catégorie
    List<Acte> findByCategorie(String categorie);
    List<String> findAllCategories();

    // Recherche par prix
    List<Acte> findByPrixDeBaseLessThan(Double prixMax);
    List<Acte> findByPrixDeBaseBetween(Double prixMin, Double prixMax);

    // Recherche avancée
    List<Acte> search(String libelle, String categorie, Double prixMin, Double prixMax);

    // Statistiques
    long countByCategorie(String categorie);
    Double getMoyennePrixByCategorie(String categorie);

    // Pagination avec tri
    List<Acte> findAllOrderByLibelle(int limit, int offset);
    List<Acte> findAllOrderByPrix(int limit, int offset);
    List<Acte> findAllOrderByCategorie(int limit, int offset);
}