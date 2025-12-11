package ma.teethcare.repository.modules.rdv.api;

import ma.teethcare.entities.RDV;
import ma.teethcare.repository.common.CrudRepository;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface RDVRepository extends CrudRepository<RDV, Long> {

    // Recherche par patient
    List<RDV> findByPatientId(Long patientId);
    List<RDV> findByPatientIdAndStatut(Long patientId, String statut);

    // Recherche par médecin
    List<RDV> findByMedecinId(Long medecinId);
    List<RDV> findByMedecinIdAndStatut(Long medecinId, String statut);

    // Recherche par date
    List<RDV> findByDate(LocalDate date);
    List<RDV> findByDateRange(LocalDateTime start, LocalDateTime end);
    List<RDV> findByDateRangeAndMedecin(LocalDateTime start, LocalDateTime end, Long medecinId);

    // Recherche par statut
    List<RDV> findByStatut(String statut);

    // RDV à venir/urgents
    List<RDV> findProchainsRDV(int jours);
    List<RDV> findRDVToday();
    List<RDV> findRDVEnRetard();

    // Vérification de conflit
    boolean existeConflit(LocalDateTime debut, LocalDateTime fin, Long medecinId);
    boolean existeConflitExcludeId(LocalDateTime debut, LocalDateTime fin, Long medecinId, Long rdvId);

    // Statistiques
    long countByPatientId(Long patientId);
    long countByMedecinId(Long medecinId);
    long countByStatut(String statut);
    long countByDate(LocalDate date);

    // Mise à jour statut
    boolean updateStatut(Long id, String statut);
    boolean annulerRDV(Long id, String raison);

    // Recherche avancée
    List<RDV> search(String patientNom, LocalDate date, String statut, Long medecinId);
}