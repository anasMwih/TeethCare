package ma.teethcare.repository.modules.dossier.impl.memoryBase;

import ma.teethcare.entities.Certificat;
import ma.teethcare.entities.Consultation;
import ma.teethcare.entities.DossierMedical;
import ma.teethcare.entities.Ordonnance;
import ma.teethcare.entities.Patient;
import ma.teethcare.repository.modules.dossier.api.DossierMedicalRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class DossierMedicalRepositoryImpl implements DossierMedicalRepository {

    private final List<DossierMedical> data = new ArrayList<>();
    private long nextId = 1L;

    public DossierMedicalRepositoryImpl() {
        // Données d'exemple
        Patient patient1 = new Patient();
        patient1.setId(1L);
        patient1.setNom("Amal");
        patient1.setPrenom("Z.");

        Patient patient2 = new Patient();
        patient2.setId(2L);
        patient2.setNom("Hassan");
        patient2.setPrenom("B.");

        // Dossier 1
        DossierMedical dossier1 = new DossierMedical();
        dossier1.setId(nextId++);
        dossier1.setPatient(patient1);
        dossier1.setDateCreation(LocalDate.now().minusMonths(2));
        dossier1.setObservations("Première consultation, contrôle général");
        data.add(dossier1);

        // Dossier 2
        DossierMedical dossier2 = new DossierMedical();
        dossier2.setId(nextId++);
        dossier2.setPatient(patient2);
        dossier2.setDateCreation(LocalDate.now().minusMonths(1));
        dossier2.setObservations("Suivi traitement orthodontique");
        data.add(dossier2);

        // Dossier 3
        Patient patient3 = new Patient();
        patient3.setId(3L);
        patient3.setNom("Nour");
        patient3.setPrenom("C.");

        DossierMedical dossier3 = new DossierMedical();
        dossier3.setId(nextId++);
        dossier3.setPatient(patient3);
        dossier3.setDateCreation(LocalDate.now().minusDays(15));
        dossier3.setObservations("Urgence dentaire");
        data.add(dossier3);

        data.sort(Comparator.comparing(DossierMedical::getId));
    }

    @Override
    public List<DossierMedical> findAll() {
        return List.copyOf(data);
    }

    @Override
    public DossierMedical findById(Long id) {
        return data.stream()
                .filter(d -> d.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    @Override
    public void create(DossierMedical dossier) {
        if (dossier.getId() == null) {
            dossier.setId(nextId++);
        }
        data.add(dossier);
        data.sort(Comparator.comparing(DossierMedical::getId));
    }

    @Override
    public void update(DossierMedical dossier) {
        deleteById(dossier.getId());
        data.add(dossier);
        data.sort(Comparator.comparing(DossierMedical::getId));
    }

    @Override
    public void delete(DossierMedical dossier) {
        data.removeIf(d -> d.getId().equals(dossier.getId()));
    }

    @Override
    public void deleteById(Long id) {
        data.removeIf(d -> d.getId().equals(id));
    }

    @Override
    public Optional<DossierMedical> findByPatient(Patient patient) {
        return data.stream()
                .filter(d -> d.getPatient() != null && d.getPatient().getId().equals(patient.getId()))
                .findFirst();
    }

    @Override
    public Optional<DossierMedical> findByPatientId(Long patientId) {
        return data.stream()
                .filter(d -> d.getPatient() != null && d.getPatient().getId().equals(patientId))
                .findFirst();
    }

    @Override
    public List<DossierMedical> findByDateCreation(LocalDate date) {
        return data.stream()
                .filter(d -> d.getDateCreation() != null && d.getDateCreation().equals(date))
                .collect(Collectors.toList());
    }

    @Override
    public List<DossierMedical> findByDateCreationBetween(LocalDate start, LocalDate end) {
        return data.stream()
                .filter(d -> d.getDateCreation() != null
                        && !d.getDateCreation().isBefore(start)
                        && !d.getDateCreation().isAfter(end))
                .collect(Collectors.toList());
    }

    @Override
    public List<DossierMedical> findByObservationsContaining(String keyword) {
        return data.stream()
                .filter(d -> d.getObservations() != null
                        && d.getObservations().toLowerCase().contains(keyword.toLowerCase()))
                .collect(Collectors.toList());
    }

    @Override
    public boolean existsById(Long id) {
        return data.stream().anyMatch(d -> d.getId().equals(id));
    }

    @Override
    public boolean existsByPatientId(Long patientId) {
        return data.stream().anyMatch(d -> d.getPatient() != null && d.getPatient().getId().equals(patientId));
    }

    @Override
    public long count() {
        return data.size();
    }

    @Override
    public List<DossierMedical> findPage(int limit, int offset) {
        return data.stream()
                .skip(offset)
                .limit(limit)
                .collect(Collectors.toList());
    }

    @Override
    public List<Consultation> getConsultationsOfDossier(Long dossierId) {
        DossierMedical dossier = findById(dossierId);
        return dossier != null && dossier.getConsultations() != null
                ? dossier.getConsultations()
                : List.of();
    }

    @Override
    public void addConsultationToDossier(Long dossierId, Long consultationId) {
        DossierMedical dossier = findById(dossierId);
        if (dossier != null && dossier.getConsultations() != null) {
            Consultation consultation = new Consultation();
            consultation.setId(consultationId);
            dossier.getConsultations().add(consultation);
        }
    }

    @Override
    public void removeConsultationFromDossier(Long dossierId, Long consultationId) {
        DossierMedical dossier = findById(dossierId);
        if (dossier != null && dossier.getConsultations() != null) {
            dossier.getConsultations().removeIf(c -> c.getId().equals(consultationId));
        }
    }

    @Override
    public List<Ordonnance> getOrdonnancesOfDossier(Long dossierId) {
        DossierMedical dossier = findById(dossierId);
        return dossier != null && dossier.getOrdonnances() != null
                ? dossier.getOrdonnances()
                : List.of();
    }

    @Override
    public void addOrdonnanceToDossier(Long dossierId, Long ordonnanceId) {
        DossierMedical dossier = findById(dossierId);
        if (dossier != null && dossier.getOrdonnances() != null) {
            Ordonnance ordonnance = new Ordonnance();
            ordonnance.setId(ordonnanceId);
            dossier.getOrdonnances().add(ordonnance);
        }
    }

    @Override
    public void removeOrdonnanceFromDossier(Long dossierId, Long ordonnanceId) {
        DossierMedical dossier = findById(dossierId);
        if (dossier != null && dossier.getOrdonnances() != null) {
            dossier.getOrdonnances().removeIf(o -> o.getId().equals(ordonnanceId));
        }
    }

    @Override
    public List<Certificat> getCertificatsOfDossier(Long dossierId) {
        DossierMedical dossier = findById(dossierId);
        return dossier != null && dossier.getCertificats() != null
                ? dossier.getCertificats()
                : List.of();
    }

    @Override
    public void addCertificatToDossier(Long dossierId, Long certificatId) {
        DossierMedical dossier = findById(dossierId);
        if (dossier != null && dossier.getCertificats() != null) {
            Certificat certificat = new Certificat();
            certificat.setId(certificatId);
            dossier.getCertificats().add(certificat);
        }
    }

    @Override
    public void removeCertificatFromDossier(Long dossierId, Long certificatId) {
        DossierMedical dossier = findById(dossierId);
        if (dossier != null && dossier.getCertificats() != null) {
            dossier.getCertificats().removeIf(c -> c.getId().equals(certificatId));
        }
    }
}