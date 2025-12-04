package ma.teethcare.repository.modules.facture.impl.memoryBase;

import ma.teethcare.entities.Consultation;
import ma.teethcare.entities.Facture;
import ma.teethcare.entities.Patient;
import ma.teethcare.repository.modules.facture.api.FactureRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class FactureRepositoryImpl implements FactureRepository {

    private final List<Facture> data = new ArrayList<>();
    private long nextId = 1L;
    private long factureNumber = 1000L;

    public FactureRepositoryImpl() {
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

        // Consultations fictives
        Consultation consultation1 = new Consultation();
        consultation1.setId(1L);
        consultation1.setPatient(patient1);
        consultation1.setPrix(400.0);

        Consultation consultation2 = new Consultation();
        consultation2.setId(2L);
        consultation2.setPatient(patient1);
        consultation2.setPrix(150.0);

        Consultation consultation3 = new Consultation();
        consultation3.setId(3L);
        consultation3.setPatient(patient2);
        consultation3.setPrix(300.0);

        Consultation consultation4 = new Consultation();
        consultation4.setId(4L);
        consultation4.setPatient(patient2);
        consultation4.setPrix(600.0);

        // Facture 1 - Payée
        Facture facture1 = new Facture();
        facture1.setId(nextId++);
        facture1.setConsultation(consultation1);
        facture1.setPatient(patient1);
        facture1.setMontantTotal(400.0);
        facture1.setMontantPaye(400.0);
        facture1.setResteAPayer(0.0);
        facture1.setStatut("PAYÉ");
        facture1.setDateFacture(LocalDateTime.now().minusDays(10));
        facture1.setNumeroFacture("FACT-" + (factureNumber++));
        data.add(facture1);

        // Facture 2 - En attente (partiellement payée)
        Facture facture2 = new Facture();
        facture2.setId(nextId++);
        facture2.setConsultation(consultation2);
        facture2.setPatient(patient1);
        facture2.setMontantTotal(150.0);
        facture2.setMontantPaye(100.0);
        facture2.setResteAPayer(50.0);
        facture2.setStatut("EN_ATTENTE");
        facture2.setDateFacture(LocalDateTime.now().minusDays(5));
        facture2.setNumeroFacture("FACT-" + (factureNumber++));
        data.add(facture2);

        // Facture 3 - En attente
        Facture facture3 = new Facture();
        facture3.setId(nextId++);
        facture3.setConsultation(consultation3);
        facture3.setPatient(patient2);
        facture3.setMontantTotal(300.0);
        facture3.setMontantPaye(0.0);
        facture3.setResteAPayer(300.0);
        facture3.setStatut("EN_ATTENTE");
        facture3.setDateFacture(LocalDateTime.now().minusDays(3));
        facture3.setNumeroFacture("FACT-" + (factureNumber++));
        data.add(facture3);

        // Facture 4 - Payée (aujourd'hui)
        Facture facture4 = new Facture();
        facture4.setId(nextId++);
        facture4.setConsultation(consultation4);
        facture4.setPatient(patient2);
        facture4.setMontantTotal(600.0);
        facture4.setMontantPaye(600.0);
        facture4.setResteAPayer(0.0);
        facture4.setStatut("PAYÉ");
        facture4.setDateFacture(LocalDateTime.now());
        facture4.setNumeroFacture("FACT-" + (factureNumber++));
        data.add(facture4);

        data.sort(Comparator.comparing(Facture::getDateFacture).reversed());
    }

    @Override
    public List<Facture> findAll() {
        return List.copyOf(data);
    }

    @Override
    public Facture findById(Long id) {
        return data.stream()
                .filter(f -> f.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    @Override
    public void create(Facture facture) {
        if (facture.getId() == null) {
            facture.setId(nextId++);
        }
        if (facture.getNumeroFacture() == null) {
            facture.setNumeroFacture("FACT-" + (factureNumber++));
        }
        data.add(facture);
        data.sort(Comparator.comparing(Facture::getDateFacture).reversed());
    }

    @Override
    public void update(Facture facture) {
        deleteById(facture.getId());
        data.add(facture);
        data.sort(Comparator.comparing(Facture::getDateFacture).reversed());
    }

    @Override
    public void delete(Facture facture) {
        data.removeIf(f -> f.getId().equals(facture.getId()));
    }

    @Override
    public void deleteById(Long id) {
        data.removeIf(f -> f.getId().equals(id));
    }

    @Override
    public Optional<Facture> findByConsultation(Consultation consultation) {
        return data.stream()
                .filter(f -> f.getConsultation() != null && f.getConsultation().getId().equals(consultation.getId()))
                .findFirst();
    }

    @Override
    public Optional<Facture> findByConsultationId(Long consultationId) {
        return data.stream()
                .filter(f -> f.getConsultation() != null && f.getConsultation().getId().equals(consultationId))
                .findFirst();
    }

    @Override
    public List<Facture> findByPatient(Patient patient) {
        return data.stream()
                .filter(f -> f.getPatient() != null && f.getPatient().getId().equals(patient.getId()))
                .collect(Collectors.toList());
    }

    @Override
    public List<Facture> findByPatientId(Long patientId) {
        return data.stream()
                .filter(f -> f.getPatient() != null && f.getPatient().getId().equals(patientId))
                .collect(Collectors.toList());
    }

    @Override
    public List<Facture> findByStatut(String statut) {
        return data.stream()
                .filter(f -> f.getStatut() != null && f.getStatut().equalsIgnoreCase(statut))
                .collect(Collectors.toList());
    }

    @Override
    public List<Facture> findByDateFacture(LocalDate date) {
        return data.stream()
                .filter(f -> f.getDateFacture() != null && f.getDateFacture().toLocalDate().equals(date))
                .collect(Collectors.toList());
    }

    @Override
    public List<Facture> findByDateFactureBetween(LocalDateTime start, LocalDateTime end) {
        return data.stream()
                .filter(f -> f.getDateFacture() != null
                        && !f.getDateFacture().isBefore(start)
                        && !f.getDateFacture().isAfter(end))
                .collect(Collectors.toList());
    }

    @Override
    public List<Facture> findByNumeroFacture(String numero) {
        return data.stream()
                .filter(f -> f.getNumeroFacture() != null && f.getNumeroFacture().equals(numero))
                .collect(Collectors.toList());
    }

    @Override
    public List<Facture> findByNumeroFactureContaining(String keyword) {
        return data.stream()
                .filter(f -> f.getNumeroFacture() != null
                        && f.getNumeroFacture().toLowerCase().contains(keyword.toLowerCase()))
                .collect(Collectors.toList());
    }

    @Override
    public List<Facture> findByMontantTotalBetween(Double min, Double max) {
        return data.stream()
                .filter(f -> f.getMontantTotal() != null
                        && f.getMontantTotal() >= min
                        && f.getMontantTotal() <= max)
                .collect(Collectors.toList());
    }

    @Override
    public List<Facture> findByResteAPayerGreaterThan(Double montant) {
        return data.stream()
                .filter(f -> f.getResteAPayer() != null && f.getResteAPayer() > montant)
                .collect(Collectors.toList());
    }

    @Override
    public List<Facture> findByResteAPayerEquals(Double montant) {
        return data.stream()
                .filter(f -> f.getResteAPayer() != null && f.getResteAPayer().equals(montant))
                .collect(Collectors.toList());
    }

    @Override
    public boolean existsById(Long id) {
        return data.stream().anyMatch(f -> f.getId().equals(id));
    }

    @Override
    public boolean existsByConsultationId(Long consultationId) {
        return data.stream().anyMatch(f -> f.getConsultation() != null && f.getConsultation().getId().equals(consultationId));
    }

    @Override
    public boolean existsByNumeroFacture(String numeroFacture) {
        return data.stream().anyMatch(f -> f.getNumeroFacture() != null && f.getNumeroFacture().equals(numeroFacture));
    }

    @Override
    public long count() {
        return data.size();
    }

    @Override
    public long countByStatut(String statut) {
        return data.stream()
                .filter(f -> f.getStatut() != null && f.getStatut().equalsIgnoreCase(statut))
                .count();
    }

    @Override
    public long countByPatientId(Long patientId) {
        return data.stream()
                .filter(f -> f.getPatient() != null && f.getPatient().getId().equals(patientId))
                .count();
    }

    @Override
    public List<Facture> findPage(int limit, int offset) {
        return data.stream()
                .skip(offset)
                .limit(limit)
                .collect(Collectors.toList());
    }

    @Override
    public Double sumMontantTotal() {
        return data.stream()
                .mapToDouble(f -> f.getMontantTotal() != null ? f.getMontantTotal() : 0.0)
                .sum();
    }

    @Override
    public Double sumMontantTotalByStatut(String statut) {
        return data.stream()
                .filter(f -> f.getStatut() != null && f.getStatut().equalsIgnoreCase(statut))
                .mapToDouble(f -> f.getMontantTotal() != null ? f.getMontantTotal() : 0.0)
                .sum();
    }

    @Override
    public Double sumMontantTotalByPatientId(Long patientId) {
        return data.stream()
                .filter(f -> f.getPatient() != null && f.getPatient().getId().equals(patientId))
                .mapToDouble(f -> f.getMontantTotal() != null ? f.getMontantTotal() : 0.0)
                .sum();
    }

    @Override
    public Double sumMontantTotalByDate(LocalDate date) {
        return data.stream()
                .filter(f -> f.getDateFacture() != null && f.getDateFacture().toLocalDate().equals(date))
                .mapToDouble(f -> f.getMontantTotal() != null ? f.getMontantTotal() : 0.0)
                .sum();
    }

    @Override
    public Double sumMontantPaye() {
        return data.stream()
                .mapToDouble(f -> f.getMontantPaye() != null ? f.getMontantPaye() : 0.0)
                .sum();
    }

    @Override
    public Double sumMontantPayeByStatut(String statut) {
        return data.stream()
                .filter(f -> f.getStatut() != null && f.getStatut().equalsIgnoreCase(statut))
                .mapToDouble(f -> f.getMontantPaye() != null ? f.getMontantPaye() : 0.0)
                .sum();
    }

    @Override
    public Double sumResteAPayer() {
        return data.stream()
                .mapToDouble(f -> f.getResteAPayer() != null ? f.getResteAPayer() : 0.0)
                .sum();
    }

    @Override
    public Double sumResteAPayerByPatientId(Long patientId) {
        return data.stream()
                .filter(f -> f.getPatient() != null && f.getPatient().getId().equals(patientId))
                .mapToDouble(f -> f.getResteAPayer() != null ? f.getResteAPayer() : 0.0)
                .sum();
    }

    @Override
    public void updateStatut(Long factureId, String statut) {
        Facture facture = findById(factureId);
        if (facture != null) {
            facture.setStatut(statut);
        }
    }

    @Override
    public void updateMontantPaye(Long factureId, Double montantPaye) {
        Facture facture = findById(factureId);
        if (facture != null) {
            facture.setMontantPaye(montantPaye);
            // Mettre à jour le reste à payer
            if (facture.getMontantTotal() != null) {
                facture.setResteAPayer(facture.getMontantTotal() - montantPaye);
            }
        }
    }

    @Override
    public void updateResteAPayer(Long factureId, Double resteAPayer) {
        Facture facture = findById(factureId);
        if (facture != null) {
            facture.setResteAPayer(resteAPayer);
            // Mettre à jour le montant payé
            if (facture.getMontantTotal() != null) {
                facture.setMontantPaye(facture.getMontantTotal() - resteAPayer);
            }
        }
    }

    @Override
    public List<Facture> searchByPatientNomPrenom(String keyword) {
        return data.stream()
                .filter(f -> f.getPatient() != null
                        && (f.getPatient().getNom() != null && f.getPatient().getNom().toLowerCase().contains(keyword.toLowerCase())
                        || f.getPatient().getPrenom() != null && f.getPatient().getPrenom().toLowerCase().contains(keyword.toLowerCase())))
                .collect(Collectors.toList());
    }

    @Override
    public List<Facture> findEnRetard(int joursRetard) {
        LocalDateTime dateLimite = LocalDateTime.now().minusDays(joursRetard);
        return data.stream()
                .filter(f -> f.getDateFacture() != null
                        && f.getDateFacture().isBefore(dateLimite)
                        && "EN_ATTENTE".equalsIgnoreCase(f.getStatut())
                        && f.getResteAPayer() != null && f.getResteAPayer() > 0)
                .collect(Collectors.toList());
    }
}