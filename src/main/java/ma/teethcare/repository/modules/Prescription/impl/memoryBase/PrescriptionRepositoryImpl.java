package ma.teethcare.repository.modules.prescription.impl.memoryBase;

import ma.teethcare.entities.Prescription;
import ma.teethcare.entities.Ordonnance;
import ma.teethcare.entities.Medicament;
import ma.teethcare.repository.modules.prescription.api.PrescriptionRepository;

import java.util.*;
import java.util.stream.Collectors;

public class PrescriptionRepositoryImpl implements PrescriptionRepository {

    private final List<Prescription> data = new ArrayList<>();
    private long nextId = 1;

    public PrescriptionRepositoryImpl() {
        // Données d'exemple
        Ordonnance ord1 = new Ordonnance();
        ord1.setId(1L);

        Ordonnance ord2 = new Ordonnance();
        ord2.setId(2L);

        Medicament med1 = new Medicament();
        med1.setId(1L);
        med1.setNom("Paracétamol");
        med1.setPrix(15.50);

        Medicament med2 = new Medicament();
        med2.setId(2L);
        med2.setNom("Amoxicilline");
        med2.setPrix(45.00);

        Medicament med3 = new Medicament();
        med3.setId(3L);
        med3.setNom("Ibuprofène");
        med3.setPrix(18.75);

        // Prescriptions d'exemple
        data.add(createPrescription(ord1, med1, 20, "1 comprimé 3 fois par jour", 7));
        data.add(createPrescription(ord1, med2, 15, "1 comprimé matin et soir", 10));
        data.add(createPrescription(ord2, med3, 30, "1 comprimé si douleur", 5));
        data.add(createPrescription(ord2, med1, 10, "1 comprimé le soir", 3));
    }

    private Prescription createPrescription(Ordonnance ordonnance, Medicament medicament,
                                            Integer quantite, String posologie, Integer duree) {
        Prescription prescription = new Prescription();
        prescription.setId(nextId++);
        prescription.setOrdonnance(ordonnance);
        prescription.setMedicament(medicament);
        prescription.setQuantite(quantite);
        prescription.setPosologie(posologie);
        prescription.setDureeTraitement(duree);
        return prescription;
    }

    // -------- CRUD --------
    @Override
    public List<Prescription> findAll() {
        return new ArrayList<>(data);
    }

    @Override
    public Prescription findById(Long id) {
        return data.stream()
                .filter(p -> p.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    @Override
    public void create(Prescription prescription) {
        if (prescription.getId() == null) {
            prescription.setId(nextId++);
        }
        data.add(prescription);
    }

    @Override
    public void update(Prescription prescription) {
        deleteById(prescription.getId());
        data.add(prescription);
    }

    @Override
    public void delete(Prescription prescription) {
        data.removeIf(p -> p.getId().equals(prescription.getId()));
    }

    @Override
    public void deleteById(Long id) {
        data.removeIf(p -> p.getId().equals(id));
    }

    // -------- Méthodes spécifiques --------
    @Override
    public List<Prescription> findByOrdonnanceId(Long ordonnanceId) {
        return data.stream()
                .filter(p -> p.getOrdonnance() != null && p.getOrdonnance().getId().equals(ordonnanceId))
                .collect(Collectors.toList());
    }

    @Override
    public List<Prescription> findByMedicamentId(Long medicamentId) {
        return data.stream()
                .filter(p -> p.getMedicament() != null && p.getMedicament().getId().equals(medicamentId))
                .collect(Collectors.toList());
    }

    @Override
    public List<Prescription> findByDureeTraitementGreaterThan(Integer jours) {
        return data.stream()
                .filter(p -> p.getDureeTraitement() != null && p.getDureeTraitement() > jours)
                .collect(Collectors.toList());
    }

    @Override
    public List<Prescription> findByDureeTraitementBetween(Integer minJours, Integer maxJours) {
        return data.stream()
                .filter(p -> p.getDureeTraitement() != null &&
                        p.getDureeTraitement() >= minJours &&
                        p.getDureeTraitement() <= maxJours)
                .collect(Collectors.toList());
    }

    @Override
    public List<Prescription> findByOrdonnanceAndMedicament(Long ordonnanceId, Long medicamentId) {
        return data.stream()
                .filter(p -> p.getOrdonnance() != null && p.getOrdonnance().getId().equals(ordonnanceId) &&
                        p.getMedicament() != null && p.getMedicament().getId().equals(medicamentId))
                .collect(Collectors.toList());
    }

    @Override
    public long countByOrdonnanceId(Long ordonnanceId) {
        return data.stream()
                .filter(p -> p.getOrdonnance() != null && p.getOrdonnance().getId().equals(ordonnanceId))
                .count();
    }

    @Override
    public long countByMedicamentId(Long medicamentId) {
        return data.stream()
                .filter(p -> p.getMedicament() != null && p.getMedicament().getId().equals(medicamentId))
                .count();
    }

    @Override
    public Double getCoutTotalByOrdonnance(Long ordonnanceId) {
        return data.stream()
                .filter(p -> p.getOrdonnance() != null && p.getOrdonnance().getId().equals(ordonnanceId))
                .mapToDouble(p -> {
                    if (p.getMedicament() != null && p.getMedicament().getPrix() != null && p.getQuantite() != null) {
                        return p.getMedicament().getPrix() * p.getQuantite();
                    }
                    return 0.0;
                })
                .sum();
    }

    @Override
    public Double getCoutTotalByMedicament(Long medicamentId) {
        return data.stream()
                .filter(p -> p.getMedicament() != null && p.getMedicament().getId().equals(medicamentId))
                .mapToDouble(p -> {
                    if (p.getMedicament().getPrix() != null && p.getQuantite() != null) {
                        return p.getMedicament().getPrix() * p.getQuantite();
                    }
                    return 0.0;
                })
                .sum();
    }

    @Override
    public void deleteByOrdonnanceId(Long ordonnanceId) {
        data.removeIf(p -> p.getOrdonnance() != null && p.getOrdonnance().getId().equals(ordonnanceId));
    }

    @Override
    public void deleteByMedicamentId(Long medicamentId) {
        data.removeIf(p -> p.getMedicament() != null && p.getMedicament().getId().equals(medicamentId));
    }
}