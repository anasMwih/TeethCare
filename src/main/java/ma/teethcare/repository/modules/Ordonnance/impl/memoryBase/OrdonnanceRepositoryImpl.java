package ma.teethcare.repository.modules.ordonnance.impl.memoryBase;

import ma.teethcare.entities.Consultation;
import ma.teethcare.entities.Ordonnance;
import ma.teethcare.entities.Prescription;
import ma.teethcare.repository.modules.ordonnance.api.OrdonnanceRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class OrdonnanceRepositoryImpl implements OrdonnanceRepository {

    private final List<Ordonnance> data = new ArrayList<>();
    private long nextId = 1L;

    public OrdonnanceRepositoryImpl() {
        // Données d'exemple

        // Consultations fictives
        Consultation consultation1 = new Consultation();
        consultation1.setId(1L);
        consultation1.setDate(LocalDate.now().minusDays(5));
        consultation1.setSymptomes("Douleur dentaire");

        Consultation consultation2 = new Consultation();
        consultation2.setId(2L);
        consultation2.setDate(LocalDate.now().minusDays(2));
        consultation2.setSymptomes("Contrôle post-traitement");

        Consultation consultation3 = new Consultation();
        consultation3.setId(3L);
        consultation3.setDate(LocalDate.now().minusDays(1));
        consultation3.setSymptomes("Gingivite");

        // Ordonnance 1
        Ordonnance ordonnance1 = new Ordonnance();
        ordonnance1.setId(nextId++);
        ordonnance1.setConsultation(consultation1);
        ordonnance1.setDate(LocalDate.now().minusDays(5));
        ordonnance1.setInstructions("Prendre l'antibiotique 3 fois par jour pendant 7 jours\nÉviter les aliments chauds\nRincer avec du bain de bouche après chaque repas");
        data.add(ordonnance1);

        // Ordonnance 2
        Ordonnance ordonnance2 = new Ordonnance();
        ordonnance2.setId(nextId++);
        ordonnance2.setConsultation(consultation1);
        ordonnance2.setDate(LocalDate.now().minusDays(4));
        ordonnance2.setInstructions("Antidouleur à prendre en cas de douleur\nMaximum 3 comprimés par jour");
        data.add(ordonnance2);

        // Ordonnance 3
        Ordonnance ordonnance3 = new Ordonnance();
        ordonnance3.setId(nextId++);
        ordonnance3.setConsultation(consultation2);
        ordonnance3.setDate(LocalDate.now().minusDays(2));
        ordonnance3.setInstructions("Continuer le bain de bouche 2 fois par jour\nPrendre rendez-vous dans 2 semaines pour contrôle");
        data.add(ordonnance3);

        // Ordonnance 4 (aujourd'hui)
        Ordonnance ordonnance4 = new Ordonnance();
        ordonnance4.setId(nextId++);
        ordonnance4.setConsultation(consultation3);
        ordonnance4.setDate(LocalDate.now());
        ordonnance4.setInstructions("Antibiotique pour infection gingivale\nBain de bouche antiseptique\nÉviter le tabac pendant le traitement");
        data.add(ordonnance4);

        data.sort(Comparator.comparing(Ordonnance::getDate).reversed());
    }

    @Override
    public List<Ordonnance> findAll() {
        return List.copyOf(data);
    }

    @Override
    public Ordonnance findById(Long id) {
        return data.stream()
                .filter(o -> o.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    @Override
    public void create(Ordonnance ordonnance) {
        if (ordonnance.getId() == null) {
            ordonnance.setId(nextId++);
        }
        data.add(ordonnance);
        data.sort(Comparator.comparing(Ordonnance::getDate).reversed());
    }

    @Override
    public void update(Ordonnance ordonnance) {
        deleteById(ordonnance.getId());
        data.add(ordonnance);
        data.sort(Comparator.comparing(Ordonnance::getDate).reversed());
    }

    @Override
    public void delete(Ordonnance ordonnance) {
        data.removeIf(o -> o.getId().equals(ordonnance.getId()));
    }

    @Override
    public void deleteById(Long id) {
        data.removeIf(o -> o.getId().equals(id));
    }

    @Override
    public List<Ordonnance> findByConsultation(Consultation consultation) {
        return data.stream()
                .filter(o -> o.getConsultation() != null && o.getConsultation().getId().equals(consultation.getId()))
                .collect(Collectors.toList());
    }

    @Override
    public List<Ordonnance> findByConsultationId(Long consultationId) {
        return data.stream()
                .filter(o -> o.getConsultation() != null && o.getConsultation().getId().equals(consultationId))
                .collect(Collectors.toList());
    }

    @Override
    public List<Ordonnance> findByDate(LocalDate date) {
        return data.stream()
                .filter(o -> o.getDate() != null && o.getDate().equals(date))
                .collect(Collectors.toList());
    }

    @Override
    public List<Ordonnance> findByDateBetween(LocalDate start, LocalDate end) {
        return data.stream()
                .filter(o -> o.getDate() != null
                        && !o.getDate().isBefore(start)
                        && !o.getDate().isAfter(end))
                .collect(Collectors.toList());
    }

    @Override
    public List<Ordonnance> findByInstructionsContaining(String keyword) {
        return data.stream()
                .filter(o -> o.getInstructions() != null
                        && o.getInstructions().toLowerCase().contains(keyword.toLowerCase()))
                .collect(Collectors.toList());
    }

    @Override
    public boolean existsById(Long id) {
        return data.stream().anyMatch(o -> o.getId().equals(id));
    }

    @Override
    public boolean existsByConsultationId(Long consultationId) {
        return data.stream().anyMatch(o -> o.getConsultation() != null && o.getConsultation().getId().equals(consultationId));
    }

    @Override
    public long count() {
        return data.size();
    }

    @Override
    public long countByConsultationId(Long consultationId) {
        return data.stream()
                .filter(o -> o.getConsultation() != null && o.getConsultation().getId().equals(consultationId))
                .count();
    }

    @Override
    public long countByDate(LocalDate date) {
        return data.stream()
                .filter(o -> o.getDate() != null && o.getDate().equals(date))
                .count();
    }

    @Override
    public List<Ordonnance> findPage(int limit, int offset) {
        return data.stream()
                .skip(offset)
                .limit(limit)
                .collect(Collectors.toList());
    }

    @Override
    public long countOrdonnancesByMonth(int year, int month) {
        return data.stream()
                .filter(o -> o.getDate() != null
                        && o.getDate().getYear() == year
                        && o.getDate().getMonthValue() == month)
                .count();
    }

    @Override
    public List<Prescription> getPrescriptionsOfOrdonnance(Long ordonnanceId) {
        Ordonnance ordonnance = findById(ordonnanceId);
        return ordonnance != null && ordonnance.getPrescriptions() != null
                ? ordonnance.getPrescriptions()
                : List.of();
    }

    @Override
    public void addPrescriptionToOrdonnance(Long ordonnanceId, Long prescriptionId) {
        Ordonnance ordonnance = findById(ordonnanceId);
        if (ordonnance != null && ordonnance.getPrescriptions() != null) {
            Prescription prescription = new Prescription();
            prescription.setId(prescriptionId);
            ordonnance.getPrescriptions().add(prescription);
        }
    }

    @Override
    public void removePrescriptionFromOrdonnance(Long ordonnanceId, Long prescriptionId) {
        Ordonnance ordonnance = findById(ordonnanceId);
        if (ordonnance != null && ordonnance.getPrescriptions() != null) {
            ordonnance.getPrescriptions().removeIf(p -> p.getId().equals(prescriptionId));
        }
    }

    @Override
    public void removeAllPrescriptionsFromOrdonnance(Long ordonnanceId) {
        Ordonnance ordonnance = findById(ordonnanceId);
        if (ordonnance != null && ordonnance.getPrescriptions() != null) {
            ordonnance.getPrescriptions().clear();
        }
    }

    @Override
    public List<Ordonnance> searchByConsultationPatient(String patientNom) {
        // Dans l'implémentation mémoire, on ne peut pas faire cette jointure facilement
        // On retourne une liste vide ou on pourrait avoir une structure de données plus complexe
        return List.of();
    }

    @Override
    public List<Ordonnance> findRecentOrdonnances(int days) {
        LocalDate cutoffDate = LocalDate.now().minusDays(days);
        return data.stream()
                .filter(o -> o.getDate() != null && !o.getDate().isBefore(cutoffDate))
                .collect(Collectors.toList());
    }
}