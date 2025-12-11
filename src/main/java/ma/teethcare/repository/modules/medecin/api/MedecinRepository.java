package ma.teethcare.repository.modules.medecin.api;

import ma.teethcare.entities.Medecin;
import ma.teethcare.repository.common.CrudRepository;
import java.util.List;
import java.util.Optional;

public interface MedecinRepository extends CrudRepository<Medecin, Long> {

    // Recherche par nom/prenom
    Optional<Medecin> findByNom(String nom);
    Optional<Medecin> findByPrenom(String prenom);
    List<Medecin> searchByNomPrenom(String keyword);

    // Recherche par spécialité
    List<Medecin> findBySpecialite(String specialite);
    List<String> findAllSpecialites();

    // Recherche par numéros professionnels
    Optional<Medecin> findByNumeroRPPS(String numeroRPPS);
    Optional<Medecin> findByNumeroOrdre(String numeroOrdre);

    // Recherche par disponibilité
    List<Medecin> findByDisponible(Boolean disponible);
    List<Medecin> findMedecinsDisponibles();

    // Recherche par email/telephone (hérités de Utilisateur)
    Optional<Medecin> findByEmail(String email);
    Optional<Medecin> findByTelephone(String telephone);

    // Recherche avancée
    List<Medecin> search(String nom, String prenom, String specialite, Boolean disponible);

    // Statistiques
    long countBySpecialite(String specialite);
    long countDisponibles();
    long countIndisponibles();

    // Gestion de la disponibilité
    boolean updateDisponibilite(Long medecinId, Boolean disponible);

    // Médecins avec le plus de consultations
    List<Medecin> findTopMedecins(int limit);

    // Pagination avec tri
    List<Medecin> findAllOrderByNom(int limit, int offset);
    List<Medecin> findAllOrderBySpecialite(int limit, int offset);
    List<Medecin> findAllOrderByExperience(int limit, int offset);
}