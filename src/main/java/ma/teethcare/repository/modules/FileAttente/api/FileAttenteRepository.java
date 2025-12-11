package ma.teethcare.repository.modules.fileattente.api;

import ma.teethcare.entities.FileAttente;
import ma.teethcare.repository.common.CrudRepository;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface FileAttenteRepository extends CrudRepository<FileAttente, Long> {

    // Recherche par patient
    Optional<FileAttente> findByPatientId(Long patientId);
    List<FileAttente> findByPatientIdAndStatut(Long patientId, String statut);

    // Recherche par statut
    List<FileAttente> findByStatut(String statut);
    List<FileAttente> findEnAttente();
    List<FileAttente> findEnCours();
    List<FileAttente> findTermines();

    // Recherche par priorité
    List<FileAttente> findByPriorite(String priorite);
    List<FileAttente> findByPrioriteAndStatut(String priorite, String statut);

    // Recherche par date
    List<FileAttente> findByDate(LocalDate date);
    List<FileAttente> findByDateRange(LocalDateTime start, LocalDateTime end);

    // Gestion de la file d'attente
    FileAttente getProchainPatient();
    FileAttente getProchainPatientUrgent();
    void updatePositions();
    int getPositionSuivante();
    int getNombrePatientsEnAttente();
    int getTempsAttenteEstime(Long fileAttenteId);

    // Actions sur la file
    boolean prendreEnCharge(Long fileAttenteId, Long medecinId);
    boolean terminer(Long fileAttenteId);
    boolean annuler(Long fileAttenteId, String raison);
    boolean reordonner(Long fileAttenteId, Integer nouvellePosition);

    // Statistiques
    long countByStatut(String statut);
    long countByPriorite(String priorite);
    long countByDate(LocalDate date);
    double getTempsMoyenAttente();
    double getTempsMoyenTraitement();

    // Recherche avancée
    List<FileAttente> search(String patientNom, String statut, String priorite, LocalDate date);
}