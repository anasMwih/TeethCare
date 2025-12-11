package ma.teethcare.repository.modules.medicament.impl.memoryBase;

import ma.teethcare.entities.Medicament;
import ma.teethcare.repository.modules.medicament.api.MedicamentRepository;

import java.util.*;
import java.util.stream.Collectors;

public class MedicamentRepositoryImpl implements MedicamentRepository {

    private final List<Medicament> data = new ArrayList<>();
    private long nextId = 1;

    public MedicamentRepositoryImpl() {
        // Données d'exemple
        data.add(createMedicament("Paracétamol", "Analgésique", "COMPRIME", 15.50));
        data.add(createMedicament("Ibuprofène", "Anti-inflammatoire", "COMPRIME", 18.75));
        data.add(createMedicament("Amoxicilline", "Antibiotique", "COMPRIME", 45.00));
        data.add(createMedicament("Sirop Toux", "Antitussif", "SIROP", 32.25));
        data.add(createMedicament("Vitamine C", "Vitamine", "GELULE", 28.90));
        data.add(createMedicament("Pommade Cicatrisante", "Cicatrisant", "POMMADE", 55.00));
        data.add(createMedicament("Insuline", "Antidiabétique", "INJECTABLE", 120.00));
        data.add(createMedicament("Spray Nasal", "Décongestionnant", "SPRAY", 40.50));
    }

    private Medicament createMedicament(String nom, String type, String forme, Double prix) {
        Medicament med = new Medicament();
        med.setId(nextId++);
        med.setNom(nom);
        med.setType(type);
        med.setForme(forme);
        med.setPrix(prix);
        return med;
    }

    // -------- CRUD --------
    @Override
    public List<Medicament> findAll() {
        return new ArrayList<>(data);
    }

    @Override
    public Medicament findById(Long id) {
        return data.stream()
                .filter(m -> m.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    @Override
    public void create(Medicament medicament) {
        if (medicament.getId() == null) {
            medicament.setId(nextId++);
        }
        data.add(medicament);
    }

    @Override
    public void update(Medicament medicament) {
        deleteById(medicament.getId());
        data.add(medicament);
    }

    @Override
    public void delete(Medicament medicament) {
        data.removeIf(m -> m.getId().equals(medicament.getId()));
    }

    @Override
    public void deleteById(Long id) {
        data.removeIf(m -> m.getId().equals(id));
    }

    // -------- Méthodes spécifiques --------
    @Override
    public Optional<Medicament> findByNom(String nom) {
        return data.stream()
                .filter(m -> nom.equalsIgnoreCase(m.getNom()))
                .findFirst();
    }

    @Override
    public List<Medicament> findByNomContaining(String keyword) {
        String lowerKeyword = keyword.toLowerCase();
        return data.stream()
                .filter(m -> m.getNom().toLowerCase().contains(lowerKeyword))
                .collect(Collectors.toList());
    }

    @Override
    public List<Medicament> findByType(String type) {
        return data.stream()
                .filter(m -> type.equals(m.getType()))
                .collect(Collectors.toList());
    }

    @Override
    public List<Medicament> findByForme(String forme) {
        return data.stream()
                .filter(m -> forme.equals(m.getForme()))
                .collect(Collectors.toList());
    }

    @Override
    public List<Medicament> findByPrixLessThan(Double prixMax) {
        return data.stream()
                .filter(m -> m.getPrix() != null && m.getPrix() < prixMax)
                .collect(Collectors.toList());
    }

    @Override
    public List<Medicament> findByPrixBetween(Double prixMin, Double prixMax) {
        return data.stream()
                .filter(m -> m.getPrix() != null && m.getPrix() >= prixMin && m.getPrix() <= prixMax)
                .collect(Collectors.toList());
    }

    @Override
    public List<Medicament> search(String nom, String type, String forme, Double prixMax) {
        return data.stream()
                .filter(m -> {
                    boolean matches = true;
                    if (nom != null && !nom.isEmpty()) {
                        matches = matches && m.getNom().toLowerCase().contains(nom.toLowerCase());
                    }
                    if (type != null && !type.isEmpty()) {
                        matches = matches && type.equals(m.getType());
                    }
                    if (forme != null && !forme.isEmpty()) {
                        matches = matches && forme.equals(m.getForme());
                    }
                    if (prixMax != null) {
                        matches = matches && m.getPrix() != null && m.getPrix() <= prixMax;
                    }
                    return matches;
                })
                .collect(Collectors.toList());
    }

    @Override
    public long countByType(String type) {
        return data.stream()
                .filter(m -> type.equals(m.getType()))
                .count();
    }

    @Override
    public long countByForme(String forme) {
        return data.stream()
                .filter(m -> forme.equals(m.getForme()))
                .count();
    }

    @Override
    public List<Medicament> findAllOrderByNom(int limit, int offset) {
        return data.stream()
                .sorted(Comparator.comparing(Medicament::getNom, String.CASE_INSENSITIVE_ORDER))
                .skip(offset)
                .limit(limit)
                .collect(Collectors.toList());
    }

    @Override
    public List<Medicament> findAllOrderByPrix(int limit, int offset) {
        return data.stream()
                .sorted(Comparator.comparing(Medicament::getPrix))
                .skip(offset)
                .limit(limit)
                .collect(Collectors.toList());
    }
}