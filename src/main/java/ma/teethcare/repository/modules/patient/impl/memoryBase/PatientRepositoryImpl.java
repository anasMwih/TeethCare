package ma.dentalTech.repository.modules.patient.impl.memoryBase;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import ma.dentalTech.entities.enums.Assurance;
import ma.dentalTech.entities.enums.Sexe;
import ma.dentalTech.entities.patient.Antecedent;
import ma.dentalTech.entities.patient.Patient;
import ma.dentalTech.repository.modules.patient.api.PatientRepository;


public class PatientRepositoryImpl implements PatientRepository {


    private final List<Patient> data = new ArrayList<Patient>();

    public PatientRepositoryImpl() {




        // Données d'exemple : 3 patients d'aujourd'hui, 1 d'hier
        LocalDateTime now = LocalDateTime.now();
        data.add(Patient.builder()
                .id(1L).nom("Amal").prenom("Z.")
                .email("amal@example.com").telephone("0611-111111")
                .dateNaissance(LocalDate.of(1995, 5, 12))
                .dateCreation(now.minusMinutes(5))
                .sexe(Sexe.Femme).assurance(Assurance.CNSS)
                .build());

        data.add(Patient.builder()
                .id(2L).nom("Hassan").prenom("B.")
                .email("hassan@example.com").telephone("0622-222222")
                .dateNaissance(LocalDate.of(1989, 9, 23))
                .dateCreation(now.minusHours(1))
                .sexe(Sexe.Homme).assurance(Assurance.CNOPS)
                .build());

        data.add(Patient.builder()
                .id(3L).nom("Nour").prenom("C.")
                .email("nour@example.com").telephone("0633-333333")
                .dateNaissance(LocalDate.of(2000, 2, 2))
                .dateCreation(now.minusMinutes(30))
                .sexe(Sexe.Femme).assurance(Assurance.Autre)
                .build());

        data.add(Patient.builder()
                .id(4L).nom("Youssef").prenom("D.")
                .email("youssef@example.com").telephone("0644-444444")
                .dateNaissance(LocalDate.of(1992, 11, 1))
                .dateCreation(now.minusDays(1)) // hier → ne doit pas s'afficher
                .sexe(Sexe.Homme).assurance(Assurance.Aucune)
                .build());

       data.sort(Comparator.comparing(Patient::getId));
    }

    @Override
    public List<Patient> findAll() { return List.copyOf(data); }

    @Override
    public Patient findById(Long id) {
        return findAll().stream().filter(p -> p.getId().equals(id)).findFirst().orElse(null);
    }

    @Override
    public void create(Patient patient) { data.add(patient); }

    @Override
    public void update(Patient patient) {
        deleteById(patient.getId());
        data.add(patient);
    }

    @Override
    public void delete(Patient patient) { data.removeIf(p -> p.getId().equals(patient.getId())); }

    @Override
    public void deleteById(Long id) { data.removeIf(p -> p.getId().equals(id)); }

    @Override
    public Optional<Patient> findByEmail(String email) {
        return Optional.empty();
    }

    @Override
    public Optional<Patient> findByTelephone(String telephone) {
        return Optional.empty();
    }

    @Override
    public List<Patient> searchByNomPrenom(String keyword) {
        return List.of();
    }

    @Override
    public boolean existsById(Long id) {
        return false;
    }

    @Override
    public long count() {
        return 0;
    }

    @Override
    public List<Patient> findPage(int limit, int offset) {
        return List.of();
    }

    @Override
    public void addAntecedentToPatient(Long patientId, Long antecedentId) {

    }

    @Override
    public void removeAntecedentFromPatient(Long patientId, Long antecedentId) {

    }

    @Override
    public void removeAllAntecedentsFromPatient(Long patientId) {

    }

    @Override
    public List<Antecedent> getAntecedentsOfPatient(Long patientId) {
        return List.of();
    }

    @Override
    public List<Patient> getPatientsByAntecedent(Long antecedentId) {
        return List.of();
    }
}
