package ma.dentalTech.repository.modules.patient.api;

import ma.dentalTech.entities.patient.Patient;
import ma.dentalTech.repository.common.CrudRepository;
import java.util.List;
import java.util.Optional;

public interface PatientRepository extends CrudRepository<Patient, Long> {

    // Recherche de patients par nom
    List<Patient> findByNom(String nom);

    // Recherche de patients par prénom
    List<Patient> findByPrenom(String prenom);

    // Recherche de patients par téléphone
    Optional<Patient> findByTelephone(String telephone);

    // Recherche de patients par CIN
    Optional<Patient> findByCin(String cin);

    // Recherche de patients par assurance
    List<Patient> findByAssurance(String assurance);

    // Recherche combinée nom et prénom
    List<Patient> findByNomAndPrenom(String nom, String prenom);

    // Compter le nombre total de patients
    long count();

    // Vérifier si un patient existe par CIN
    boolean existsByCin(String cin);

    // Vérifier si un patient existe par téléphone
    boolean existsByTelephone(String telephone);
}