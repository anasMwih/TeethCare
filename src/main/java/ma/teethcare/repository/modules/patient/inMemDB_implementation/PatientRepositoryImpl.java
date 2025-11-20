package ma.dentalTech.repository.modules.patient.inMemDB_implementation;

import ma.dentalTech.entities.patient.Patient;
import ma.dentalTech.repository.modules.patient.api.PatientRepository;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

public class PatientRepositoryImpl implements PatientRepository {
    private final Map<Long, Patient> patients = new HashMap<>();
    private final AtomicLong idCounter = new AtomicLong(1);

    @Override
    public Patient save(Patient patient) {
        if (patient.getId() == null) {
            patient.setId(idCounter.getAndIncrement());
        }
        patients.put(patient.getId(), patient);
        return patient;
    }

    @Override
    public Optional<Patient> findById(Long id) {
        return Optional.ofNullable(patients.get(id));
    }

    @Override
    public List<Patient> findAll() {
        return new ArrayList<>(patients.values());
    }

    @Override
    public void deleteById(Long id) {
        patients.remove(id);
    }

    @Override
    public List<Patient> findByNom(String nom) {
        return patients.values().stream()
                .filter(patient -> patient.getNom() != null && patient.getNom().equalsIgnoreCase(nom))
                .collect(Collectors.toList());
    }

    @Override
    public List<Patient> findByPrenom(String prenom) {
        return patients.values().stream()
                .filter(patient -> patient.getPrenom() != null && patient.getPrenom().equalsIgnoreCase(prenom))
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Patient> findByTelephone(String telephone) {
        return patients.values().stream()
                .filter(patient -> patient.getTelephone() != null && patient.getTelephone().equals(telephone))
                .findFirst();
    }

    @Override
    public Optional<Patient> findByCin(String cin) {
        return patients.values().stream()
                .filter(patient -> patient.getCin() != null && patient.getCin().equals(cin))
                .findFirst();
    }

    @Override
    public List<Patient> findByAssurance(String assurance) {
        return patients.values().stream()
                .filter(patient -> patient.getAssurance() != null && patient.getAssurance().equalsIgnoreCase(assurance))
                .collect(Collectors.toList());
    }

    @Override
    public List<Patient> findByNomAndPrenom(String nom, String prenom) {
        return patients.values().stream()
                .filter(patient -> patient.getNom() != null && patient.getNom().equalsIgnoreCase(nom) &&
                        patient.getPrenom() != null && patient.getPrenom().equalsIgnoreCase(prenom))
                .collect(Collectors.toList());
    }

    @Override
    public long count() {
        return patients.size();
    }

    @Override
    public boolean existsByCin(String cin) {
        return patients.values().stream()
                .anyMatch(patient -> patient.getCin() != null && patient.getCin().equals(cin));
    }

    @Override
    public boolean existsByTelephone(String telephone) {
        return patients.values().stream()
                .anyMatch(patient -> patient.getTelephone() != null && patient.getTelephone().equals(telephone));
    }
}