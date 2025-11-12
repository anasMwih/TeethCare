package ma.teethcare.modules.patient.api;

import ma.teethcare.common.CrudRepository;
import ma.teethcare.entities.Patient;
import java.util.List;
import java.util.Optional;

public interface PatientRepository extends CrudRepository<Patient, Long> {

    // === RECHERCHES SPÉCIFIQUES PATIENT ===
    List<Patient> findByNom(String nom);
    List<Patient> findByPrenom(String prenom);
    List<Patient> findByNomComplet(String nom, String prenom);
    List<Patient> findByTelephone(String telephone);
    List<Patient> findByEmail(String email);
    List<Patient> findByAssurance(String assurance);

    // === STATISTIQUES ===
    List<Patient> findPatientsRecents(int jours);
    Optional<Patient> findByTelephoneExact(String telephone);
    boolean existsByEmail(String email);
}
