package ma.teethcare.repository.modules.utilisateur.api;

import ma.teethcare.entities.Utilisateur;
import ma.teethcare.repository.common.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface UtilisateurRepository extends CrudRepository<Utilisateur, Long> {
    Optional<Utilisateur> findByEmail(String email);
    Optional<Utilisateur> findByLogin(String login);
    List<Utilisateur> findByType(String type);
    List<Utilisateur> findByCabinetMedicaleId(Long cabinetId);
    List<Utilisateur> searchByNom(String keyword);
    boolean existsByLogin(String login);
    boolean existsByEmail(String email);
}