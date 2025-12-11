package ma.teethcare.repository.modules.medicament.api;

import ma.teethcare.entities.Medicament;
import ma.teethcare.repository.common.CrudRepository;
import java.util.List;
import java.util.Optional;

public interface MedicamentRepository extends CrudRepository<Medicament, Long> {

    // Recherche par nom
    Optional<Medicament> findByNom(String nom);
    List<Medicament> findByNomContaining(String keyword);

    // Recherche par type
    List<Medicament> findByType(String type);

    // Recherche par forme
    List<Medicament> findByForme(String forme);

    // Recherche par prix
    List<Medicament> findByPrixLessThan(Double prixMax);
    List<Medicament> findByPrixBetween(Double prixMin, Double prixMax);

    // Recherche avancée
    List<Medicament> search(String nom, String type, String forme, Double prixMax);

    // Statistiques
    long countByType(String type);
    long countByForme(String forme);

    // Pagination avec tri
    List<Medicament> findAllOrderByNom(int limit, int offset);
    List<Medicament> findAllOrderByPrix(int limit, int offset);
}