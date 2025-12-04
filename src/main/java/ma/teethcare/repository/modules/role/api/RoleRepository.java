package ma.teethcare.repository.modules.role.api;

import ma.teethcare.entities.Role;
import ma.teethcare.entities.Utilisateur;
import ma.teethcare.repository.common.CrudRepository;
import java.util.List;

public interface RoleRepository extends CrudRepository<Role, Long> {
    Role findByLibelle(String libelle);
    List<Utilisateur> getUtilisateursByRole(Long roleId);
    void addPrivilegeToRole(Long roleId, String privilege);
    void removePrivilegeFromRole(Long roleId, String privilege);
    List<String> getPrivilegesByRole(Long roleId);
}