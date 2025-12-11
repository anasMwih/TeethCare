package ma.teethcare.repository.modules.medecin.impl.memoryBase;

import ma.teethcare.entities.Medecin;
import ma.teethcare.repository.modules.medecin.api.MedecinRepository;

import java.util.*;
import java.util.stream.Collectors;

public class MedecinRepositoryImpl implements MedecinRepository {

    private final List<Medecin> data = new ArrayList<>();
    private long nextId = 1;

    public MedecinRepositoryImpl() {
        // Données d'exemple
        data.add(createMedecin("Smith", "John", "john.smith@teethcare.ma", "0612345678",
                "Orthodontiste", "12345678901", "12345", 15, 500.0, true));
        data.add(createMedecin("Johnson", "Sarah", "sarah.j@teethcare.ma", "0623456789",
                "Parodontiste", "23456789012", "23456", 10, 450.0, true));
        data.add(createMedecin("Martin", "Pierre", "p.martin@teethcare.ma", "0634567890",
                "Chirurgien-dentiste", "34567890123", "34567", 20, 600.0, false));
        data.add(createMedecin("Dubois", "Marie", "m.dubois@teethcare.ma", "0645678901",
                "Pédodontiste", "45678901234", "45678", 8, 400.0, true));
    }

    private Medecin createMedecin(String nom, String prenom, String email, String telephone,
                                  String specialite, String numeroRPPS, String numeroOrdre,
                                  Integer experience, Double tauxHoraire, Boolean disponible) {
        Medecin medecin = new Medecin();
        medecin.setId(nextId++);
        medecin.setNom(nom);
        medecin.setPrenom(prenom);
        medecin.setEmail(email);
        medecin.setTelephone(telephone);
        medecin.setSpecialite(specialite);
        medecin.setNumeroRPPS(numeroRPPS);
        medecin.setNumeroOrdre(numeroOrdre);
        medecin.setAnneesExperience(experience);
        medecin.setTauxHoraire(tauxHoraire);
        medecin.setDisponible(disponible);
        medecin.setTitre("Dr.");
        return medecin;
    }

    // -------- CRUD --------
    @Override
    public List<Medecin> findAll() {
        return new ArrayList<>(data);
    }

    @Override
    public Medecin findById(Long id) {
        return data.stream()
                .filter(m -> m.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    @Override
    public void create(Medecin medecin) {
        if (medecin.getId() == null) {
            medecin.setId(nextId++);
        }
        data.add(medecin);
    }

    @Override
    public void update(Medecin medecin) {
        deleteById(medecin.getId());
        data.add(medecin);
    }

    @Override
    public void delete(Medecin medecin) {
        data.removeIf(m -> m.getId().equals(medecin.getId()));
    }

    @Override
    public void deleteById(Long id) {
        data.removeIf(m -> m.getId().equals(id));
    }

    // -------- Méthodes spécifiques --------
    @Override
    public Optional<Medecin> findByNom(String nom) {
        return data.stream()
                .filter(m -> nom.equalsIgnoreCase(m.getNom()))
                .findFirst();
    }

    @Override
    public Optional<Medecin> findByPrenom(String prenom) {
        return data.stream()
                .filter(m -> prenom.equalsIgnoreCase(m.getPrenom()))
                .findFirst();
    }

    @Override
    public List<Medecin> searchByNomPrenom(String keyword) {
        String lowerKeyword = keyword.toLowerCase();
        return data.stream()
                .filter(m -> m.getNom().toLowerCase().contains(lowerKeyword) ||
                        m.getPrenom().toLowerCase().contains(lowerKeyword))
                .collect(Collectors.toList());
    }

    @Override
    public List<Medecin> findBySpecialite(String specialite) {
        return data.stream()
                .filter(m -> specialite.equals(m.getSpecialite()))
                .collect(Collectors.toList());
    }

    @Override
    public List<String> findAllSpecialites() {
        return data.stream()
                .map(Medecin::getSpecialite)
                .distinct()
                .sorted()
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Medecin> findByNumeroRPPS(String numeroRPPS) {
        return data.stream()
                .filter(m -> numeroRPPS.equals(m.getNumeroRPPS()))
                .findFirst();
    }

    @Override
    public List<Medecin> findByDisponible(Boolean disponible) {
        return data.stream()
                .filter(m -> disponible.equals(m.getDisponible()))
                .collect(Collectors.toList());
    }

    @Override
    public List<Medecin> findMedecinsDisponibles() {
        return findByDisponible(true);
    }

    @Override
    public Optional<Medecin> findByEmail(String email) {
        return data.stream()
                .filter(m -> email.equalsIgnoreCase(m.getEmail()))
                .findFirst();
    }

    @Override
    public Optional<Medecin> findByTelephone(String telephone) {
        return data.stream()
                .filter(m -> telephone.equals(m.getTelephone()))
                .findFirst();
    }

    @Override
    public long countBySpecialite(String specialite) {
        return data.stream()
                .filter(m -> specialite.equals(m.getSpecialite()))
                .count();
    }

    @Override
    public long countDisponibles() {
        return data.stream()
                .filter(m -> Boolean.TRUE.equals(m.getDisponible()))
                .count();
    }

    @Override
    public boolean updateDisponibilite(Long medecinId, Boolean disponible) {
        Medecin medecin = findById(medecinId);
        if (medecin != null) {
            medecin.setDisponible(disponible);
            return true;
        }
        return false;
    }

    @Override
    public List<Medecin> findTopMedecins(int limit) {
        return data.stream()
                .sorted((m1, m2) -> {
                    // Trier par expérience décroissante
                    Integer exp1 = m1.getAnneesExperience() != null ? m1.getAnneesExperience() : 0;
                    Integer exp2 = m2.getAnneesExperience() != null ? m2.getAnneesExperience() : 0;
                    return exp2.compareTo(exp1);
                })
                .limit(limit)
                .collect(Collectors.toList());
    }

    @Override
    public List<Medecin> findAllOrderByNom(int limit, int offset) {
        return data.stream()
                .sorted(Comparator.comparing(Medecin::getNom, String.CASE_INSENSITIVE_ORDER))
                .skip(offset)
                .limit(limit)
                .collect(Collectors.toList());
    }
}