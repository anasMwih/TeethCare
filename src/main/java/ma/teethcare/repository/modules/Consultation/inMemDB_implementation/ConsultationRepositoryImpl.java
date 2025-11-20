package ma.dentalTech.repository.modules.dossierMedical.inMemDB_implementation;

import ma.dentalTech.entities.Consultation;
import ma.dentalTech.entities.Patient;
import ma.dentalTech.entities.Medecin;
import ma.dentalTech.repository.modules.dossierMedical.api.ConsultationRepository;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

public class ConsultationRepositoryImpl implements ConsultationRepository {
    private final Map<Long, Consultation> consultations = new HashMap<>();
    private final AtomicLong idCounter = new AtomicLong(1);

    @Override
    public Consultation save(Consultation consultation) {
        if (consultation.getId() == null) {
            consultation.setId(idCounter.getAndIncrement());
        }
        consultations.put(consultation.getId(), consultation);
        return consultation;
    }

    @Override
    public Optional<Consultation> findById(Long id) {
        return Optional.ofNullable(consultations.get(id));
    }

    @Override
    public List<Consultation> findAll() {
        return new ArrayList<>(consultations.values());
    }

    @Override
    public void deleteById(Long id) {
        consultations.remove(id);
    }

    @Override
    public List<Consultation> findByPatient(Patient patient) {
        return consultations.values().stream()
                .filter(consultation -> consultation.getPatient() != null && consultation.getPatient().equals(patient))
                .collect(Collectors.toList());
    }

    @Override
    public List<Consultation> findByPatientId(Long patientId) {
        return consultations.values().stream()
                .filter(consultation -> consultation.getPatient() != null && consultation.getPatient().getId().equals(patientId))
                .collect(Collectors.toList());
    }

    @Override
    public List<Consultation> findByMedecin(Medecin medecin) {
        return consultations.values().stream()
                .filter(consultation -> consultation.getMedecin() != null && consultation.getMedecin().equals(medecin))
                .collect(Collectors.toList());
    }

    @Override
    public List<Consultation> findByMedecinId(Long medecinId) {
        return consultations.values().stream()
                .filter(consultation -> consultation.getMedecin() != null && consultation.getMedecin().getId().equals(medecinId))
                .collect(Collectors.toList());
    }

    @Override
    public List<Consultation> findByOrdonnanceIsNotNull() {
        return consultations.values().stream()
                .filter(consultation -> consultation.getOrdonnance() != null)
                .collect(Collectors.toList());
    }

    @Override
    public List<Consultation> findByDateConsultation(String date) {
        return consultations.values().stream()
                .filter(consultation -> consultation.getDateConsultation() != null && consultation.getDateConsultation().equals(date))
                .collect(Collectors.toList());
    }

    @Override
    public List<Consultation> findByTypeConsultation(String type) {
        return consultations.values().stream()
                .filter(consultation -> consultation.getTypeConsultation() != null && consultation.getTypeConsultation().equalsIgnoreCase(type))
                .collect(Collectors.toList());
    }

    @Override
    public List<Consultation> findByStatut(String statut) {
        return consultations.values().stream()
                .filter(consultation -> consultation.getStatut() != null && consultation.getStatut().equalsIgnoreCase(statut))
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Consultation> findTopByPatientIdOrderByDateConsultationDesc(Long patientId) {
        return consultations.values().stream()
                .filter(consultation -> consultation.getPatient() != null && consultation.getPatient().getId().equals(patientId))
                .max(Comparator.comparing(Consultation::getDateConsultation));
    }

    @Override
    public long countByPatientId(Long patientId) {
        return consultations.values().stream()
                .filter(consultation -> consultation.getPatient() != null && consultation.getPatient().getId().equals(patientId))
                .count();
    }

    @Override
    public long countByMedecinId(Long medecinId) {
        return consultations.values().stream()
                .filter(consultation -> consultation.getMedecin() != null && consultation.getMedecin().getId().equals(medecinId))
                .count();
    }

    @Override
    public boolean existsByPatientIdAndDateConsultation(Long patientId, String dateConsultation) {
        return consultations.values().stream()
                .anyMatch(consultation -> consultation.getPatient() != null &&
                        consultation.getPatient().getId().equals(patientId) &&
                        consultation.getDateConsultation() != null &&
                        consultation.getDateConsultation().equals(dateConsultation));
    }

    @Override
    public List<Consultation> findByFactureIsNotNull() {
        return consultations.values().stream()
                .filter(consultation -> consultation.getFacture() != null)
                .collect(Collectors.toList());
    }
}