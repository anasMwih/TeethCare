package ma.teethcare.repository.modules.consultation.impl.memoryBase;

import ma.teethcare.entities.Consultation;
import ma.teethcare.entities.DossierMedical;
import ma.teethcare.entities.Facture;
import ma.teethcare.entities.InterventionMedecin;
import ma.teethcare.entities.Patient;
import ma.teethcare.entities.Prescription;
import ma.teethcare.repository.modules.consultation.api.ConsultationRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class ConsultationRepositoryImpl implements ConsultationRepository {

    private final List<Consultation> data = new ArrayList<>();
    private long nextId = 1L;

    public ConsultationRepositoryImpl() {
        // Données d'exemple

        // Patients fictifs
        Patient patient1 = new Patient();
        patient1.setId(1L);
        patient1.setNom("Amal");
        patient1.setPrenom("Z.");

        Patient patient2 = new Patient();
        patient2.setId(2L);
        patient2.setNom("Hassan");
        patient2.setPrenom("B.");

        // Dossiers fictifs
        DossierMedical dossier1 = new DossierMedical();
        dossier1.setId(1L);

        DossierMedical dossier2 = new DossierMedical();
        dossier2.setId(2L);

        // Consultation 1
        Consultation consultation1 = new Consultation();
        consultation1.setId(nextId++);
        consultation1.setPatient(patient1);
        consultation1.setDossierMedical(dossier1);
        consultation1.setDate(LocalDate.now().minusDays(5));
        consultation1.setSymptomes("Douleur dentaire côté gauche, sensibilité au froid");
        consultation1.setDiagnostic("Caries sur molaire 36");
        consultation1.setTraitement("Obturation composite");
        consultation1.setPrix(400.0);
        data.add(consultation1);

        // Consultation 2
        Consultation consultation2 = new Consultation();
        consultation2.setId(nextId++);
        consultation2.setPatient(patient1);
        consultation2.setDossierMedical(dossier1);
        consultation2.setDate(LocalDate.now().minusDays(2));
        consultation2.setSymptomes("Contrôle après traitement");
        consultation2.setDiagnostic("Guérison normale");
        consultation2.setTraitement("Nettoyage et contrôle");
        consultation2.setPrix(150.0);
        data.add(consultation2);

        // Consultation 3
        Consultation consultation3 = new Consultation();
        consultation3.setId(nextId++);
        consultation3.setPatient(patient2);
        consultation3.setDossierMedical(dossier2);
        consultation3.setDate(LocalDate.now().minusDays(1));
        consultation3.setSymptomes("Bleeding gums, bad breath");
        consultation3.setDiagnostic("Gingivite modérée");
        consultation3.setTraitement("Détartrage et conseils d'hygiène");
        consultation3.setPrix(300.0);
        data.add(consultation3);

        // Consultation 4 (aujourd'hui)
        Consultation consultation4 = new Consultation();
        consultation4.setId(nextId++);
        consultation4.setPatient(patient2);
        consultation4.setDossierMedical(dossier2);
        consultation4.setDate(LocalDate.now());
        consultation4.setSymptomes("Urgence : dent cassée");
        consultation4.setDiagnostic("Fracture coronaire dent 21");
        consultation4.setTraitement("Couronne provisoire");
        consultation4.setPrix(600.0);
        data.add(consultation4);

        data.sort(Comparator.comparing(Consultation::getDate).reversed());
    }

    @Override
    public List<Consultation> findAll() {
        return List.copyOf(data);
    }

    @Override
    public Consultation findById(Long id) {
        return data.stream()
                .filter(c -> c.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    @Override
    public void create(Consultation consultation) {
        if (consultation.getId() == null) {
            consultation.setId(nextId++);
        }
        data.add(consultation);
        data.sort(Comparator.comparing(Consultation::getDate).reversed());
    }

    @Override
    public void update(Consultation consultation) {
        deleteById(consultation.getId());
        data.add(consultation);
        data.sort(Comparator.comparing(Consultation::getDate).reversed());
    }

    @Override
    public void delete(Consultation consultation) {
        data.removeIf(c -> c.getId().equals(consultation.getId()));
    }

    @Override
    public void deleteById(Long id) {
        data.removeIf(c -> c.getId().equals(id));
    }

    @Override
    public List<Consultation> findByPatient(Patient patient) {
        return data.stream()
                .filter(c -> c.getPatient() != null && c.getPatient().getId().equals(patient.getId()))
                .collect(Collectors.toList());
    }

    @Override
    public List<Consultation> findByPatientId(Long patientId) {
        return data.stream()
                .filter(c -> c.getPatient() != null && c.getPatient().getId().equals(patientId))
                .collect(Collectors.toList());
    }

    @Override
    public List<Consultation> findByDossierMedical(DossierMedical dossier) {
        return data.stream()
                .filter(c -> c.getDossierMedical() != null && c.getDossierMedical().getId().equals(dossier.getId()))
                .collect(Collectors.toList());
    }

    @Override
    public List<Consultation> findByDossierMedicalId(Long dossierId) {
        return data.stream()
                .filter(c -> c.getDossierMedical() != null && c.getDossierMedical().getId().equals(dossierId))
                .collect(Collectors.toList());
    }

    @Override
    public List<Consultation> findByDate(LocalDate date) {
        return data.stream()
                .filter(c -> c.getDate() != null && c.getDate().equals(date))
                .collect(Collectors.toList());
    }

    @Override
    public List<Consultation> findByDateBetween(LocalDate start, LocalDate end) {
        return data.stream()
                .filter(c -> c.getDate() != null
                        && !c.getDate().isBefore(start)
                        && !c.getDate().isAfter(end))
                .collect(Collectors.toList());
    }

    @Override
    public List<Consultation> findByPrixBetween(Double min, Double max) {
        return data.stream()
                .filter(c -> c.getPrix() != null
                        && c.getPrix() >= min
                        && c.getPrix() <= max)
                .collect(Collectors.toList());
    }

    @Override
    public List<Consultation> findBySymptomesContaining(String keyword) {
        return data.stream()
                .filter(c -> c.getSymptomes() != null
                        && c.getSymptomes().toLowerCase().contains(keyword.toLowerCase()))
                .collect(Collectors.toList());
    }

    @Override
    public List<Consultation> findByDiagnosticContaining(String keyword) {
        return data.stream()
                .filter(c -> c.getDiagnostic() != null
                        && c.getDiagnostic().toLowerCase().contains(keyword.toLowerCase()))
                .collect(Collectors.toList());
    }

    @Override
    public List<Consultation> findByTraitementContaining(String keyword) {
        return data.stream()
                .filter(c -> c.getTraitement() != null
                        && c.getTraitement().toLowerCase().contains(keyword.toLowerCase()))
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Facture> getFactureOfConsultation(Long consultationId) {
        Consultation consultation = findById(consultationId);
        return consultation != null
                ? Optional.ofNullable(consultation.getFacture())
                : Optional.empty();
    }

    @Override
    public boolean existsById(Long id) {
        return data.stream().anyMatch(c -> c.getId().equals(id));
    }

    @Override
    public long count() {
        return data.size();
    }

    @Override
    public List<Consultation> findPage(int limit, int offset) {
        return data.stream()
                .skip(offset)
                .limit(limit)
                .collect(Collectors.toList());
    }

    @Override
    public long countByDate(LocalDate date) {
        return data.stream()
                .filter(c -> c.getDate() != null && c.getDate().equals(date))
                .count();
    }

    @Override
    public long countByPatientId(Long patientId) {
        return data.stream()
                .filter(c -> c.getPatient() != null && c.getPatient().getId().equals(patientId))
                .count();
    }

    @Override
    public Double sumPrixByDate(LocalDate date) {
        return data.stream()
                .filter(c -> c.getDate() != null && c.getDate().equals(date))
                .mapToDouble(c -> c.getPrix() != null ? c.getPrix() : 0.0)
                .sum();
    }

    @Override
    public Double sumPrixByPatientId(Long patientId) {
        return data.stream()
                .filter(c -> c.getPatient() != null && c.getPatient().getId().equals(patientId))
                .mapToDouble(c -> c.getPrix() != null ? c.getPrix() : 0.0)
                .sum();
    }

    @Override
    public List<InterventionMedecin> getInterventionsOfConsultation(Long consultationId) {
        Consultation consultation = findById(consultationId);
        return consultation != null && consultation.getInterventions() != null
                ? consultation.getInterventions()
                : List.of();
    }

    @Override
    public void addInterventionToConsultation(Long consultationId, Long interventionId) {
        Consultation consultation = findById(consultationId);
        if (consultation != null && consultation.getInterventions() != null) {
            InterventionMedecin intervention = new InterventionMedecin();
            intervention.setId(interventionId);
            consultation.getInterventions().add(intervention);
        }
    }

    @Override
    public void removeInterventionFromConsultation(Long consultationId, Long interventionId) {
        Consultation consultation = findById(consultationId);
        if (consultation != null && consultation.getInterventions() != null) {
            consultation.getInterventions().removeIf(i -> i.getId().equals(interventionId));
        }
    }

    @Override
    public List<Prescription> getPrescriptionsOfConsultation(Long consultationId) {
        Consultation consultation = findById(consultationId);
        return consultation != null && consultation.getPrescriptions() != null
                ? consultation.getPrescriptions()
                : List.of();
    }

    @Override
    public void addPrescriptionToConsultation(Long consultationId, Long prescriptionId) {
        Consultation consultation = findById(consultationId);
        if (consultation != null && consultation.getPrescriptions() != null) {
            Prescription prescription = new Prescription();
            prescription.setId(prescriptionId);
            consultation.getPrescriptions().add(prescription);
        }
    }

    @Override
    public void removePrescriptionFromConsultation(Long consultationId, Long prescriptionId) {
        Consultation consultation = findById(consultationId);
        if (consultation != null && consultation.getPrescriptions() != null) {
            consultation.getPrescriptions().removeIf(p -> p.getId().equals(prescriptionId));
        }
    }
}