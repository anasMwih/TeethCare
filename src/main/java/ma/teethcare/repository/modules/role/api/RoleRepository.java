package ma.teethcare.repository.modules.role.api;

import ma.teethcare.entities.Role;
import ma.teethcare.repository.common.CrudRepository;

import java.util.List;

/**
 * Repository pour la gestion des rôles
 */
public interface RoleRepository extends CrudRepository<Role, Long> {

    /**
     * Recherche un rôle par son libellé
     * @param libelle Libellé du rôle
     * @return Rôle trouvé ou null
     */
    Role findByLibelle(String libelle);

    /**
     * Recherche les rôles d'un utilisateur
     * @param userId ID de l'utilisateur
     * @return Liste des rôles
     */
    List<Role> findByUserId(Long userId);

    /**
     * Vérifie si un utilisateur a un privilège spécifique
     * @param userId ID de l'utilisateur
     * @param privilege Privilège à vérifier
     * @return true si l'utilisateur a le privilège
     */
    boolean hasPrivilege(Long userId, String privilege);
}