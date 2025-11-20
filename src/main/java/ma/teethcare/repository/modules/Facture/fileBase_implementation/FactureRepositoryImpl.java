package ma.dentalTech.repository.modules.facture.fileBase_implementation;

import ma.dentalTech.entities.Facture;
import ma.dentalTech.entities.Patient;
import ma.dentalTech.entities.Consultation;
import ma.dentalTech.repository.modules.facture.api.FactureRepository;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

public class FactureRepositoryImpl implements FactureRepository {
    private final Map<Long, Facture> factures = new HashMap<>();
    private final AtomicLong idCounter = new AtomicLong(1);

    @Override
    public Facture save(Facture facture) {
        if (facture.getId() == null) {
            facture.setId(idCounter.getAndIncrement());
        }
        factures.put(facture.getId(), facture);
        return facture;
    }

    @Override
    public Optional<Facture> findById(Long id) {
        return Optional.ofNullable(factures.get(id));
    }

    @Override
    public List<Facture> findAll() {
        return new ArrayList<>(factures.values());
    }

    @Override
    public void deleteById(Long id) {
        factures.remove(id);
    }

    @Override
    public List<Facture> findByPatient(Patient patient) {
        return factures.values().stream()
                .filter(facture -> facture.getPatient() != null && facture.getPatient().equals(patient))
                .collect(Collectors.toList());
    }

    @Override
    public List<Facture> findByPatientId(Long patientId) {
        return factures.values().stream()
                .filter(facture -> facture.getPatient() != null && facture.getPatient().getId().equals(patientId))
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Facture> findByConsultation(Consultation consultation) {
        return factures.values().stream()
                .filter(facture -> facture.getConsultation() != null && facture.getConsultation().equals(consultation))
                .findFirst();
    }

    @Override
    public Optional<Facture> findByConsultationId(Long consultationId) {
        return factures.values().stream()
                .filter(facture -> facture.getConsultation() != null && facture.getConsultation().getId().equals(consultationId))
                .findFirst();
    }

    @Override
    public List<Facture> findByStatutPaiement(String statutPaiement) {
        return factures.values().stream()
                .filter(facture -> facture.getStatutPaiement() != null && facture.getStatutPaiement().equalsIgnoreCase(statutPaiement))
                .collect(Collectors.toList());
    }

    @Override
    public List<Facture> findByStatutPaiementNot(String statutPaiement) {
        return factures.values().stream()
                .filter(facture -> facture.getStatutPaiement() != null && !facture.getStatutPaiement().equalsIgnoreCase(statutPaiement))
                .collect(Collectors.toList());
    }

    @Override
    public List<Facture> findByDateFacture(String date) {
        return factures.values().stream()
                .filter(facture -> facture.getDateFacture() != null && facture.getDateFacture().equals(date))
                .collect(Collectors.toList());
    }

    @Override
    public List<Facture> findByMontantGreaterThan(Double montant) {
        return factures.values().stream()
                .filter(facture -> facture.getMontant() != null && facture.getMontant() > montant)
                .collect(Collectors.toList());
    }

    @Override
    public List<Facture> findByMontantLessThan(Double montant) {
        return factures.values().stream()
                .filter(facture -> facture.getMontant() != null && facture.getMontant() < montant)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Facture> findTopByPatientIdOrderByDateFactureDesc(Long patientId) {
        return factures.values().stream()
                .filter(facture -> facture.getPatient() != null && facture.getPatient().getId().equals(patientId))
                .max(Comparator.comparing(Facture::getDateFacture));
    }

    @Override
    public long countByPatientId(Long patientId) {
        return factures.values().stream()
                .filter(facture -> facture.getPatient() != null && facture.getPatient().getId().equals(patientId))
                .count();
    }

    @Override
    public long countByStatutPaiement(String statutPaiement) {
        return factures.values().stream()
                .filter(facture -> facture.getStatutPaiement() != null && facture.getStatutPaiement().equalsIgnoreCase(statutPaiement))
                .count();
    }

    @Override
    public Double sumMontantByPatientId(Long patientId) {
        return factures.values().stream()
                .filter(facture -> facture.getPatient() != null && facture.getPatient().getId().equals(patientId) && facture.getMontant() != null)
                .mapToDouble(Facture::getMontant)
                .sum();
    }

    @Override
    public boolean existsByConsultationId(Long consultationId) {
        return factures.values().stream()
                .anyMatch(facture -> facture.getConsultation() != null && facture.getConsultation().getId().equals(consultationId));
    }

    @Override
    public boolean existsByPatientIdAndStatutPaiementNot(Long patientId, String statutPaiement) {
        return factures.values().stream()
                .anyMatch(facture -> facture.getPatient() != null &&
                        facture.getPatient().getId().equals(patientId) &&
                        facture.getStatutPaiement() != null &&
                        !facture.getStatutPaiement().equalsIgnoreCase(statutPaiement));
    }
}