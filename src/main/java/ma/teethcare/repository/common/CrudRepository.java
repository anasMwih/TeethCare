package ma.teethcare.common;

import java.util.List;
import java.util.Optional;

/**
 * Interface générique pour les opérations CRUD (Create, Read, Update, Delete)
 *
 * @param <T> le type de l'entité
 * @param <ID> le type de l'identifiant (Long, String, etc.)
 */
public interface CrudRepository<T, ID> {

    /**
     * Sauvegarde une nouvelle entité
     * @param entity l'entité à sauvegarder
     * @return l'entité sauvegardée avec son ID généré
     */
    T save(T entity);

    /**
     * Met à jour une entité existante
     * @param entity l'entité à mettre à jour
     * @return l'entité mise à jour
     */
    T update(T entity);

    /**
     * Supprime une entité
     * @param entity l'entité à supprimer
     */
    void delete(T entity);

    /**
     * Supprime une entité par son ID
     * @param id l'ID de l'entité à supprimer
     */
    void deleteById(ID id);

    /**
     * Trouve une entité par son ID
     * @param id l'ID de l'entité à trouver
     * @return un Optional contenant l'entité si trouvée
     */
    Optional<T> findById(ID id);

    /**
     * Retourne toutes les entités
     * @return la liste de toutes les entités
     */
    List<T> findAll();

    /**
     * Compte le nombre total d'entités
     * @return le nombre d'entités
     */
    Long count();
}