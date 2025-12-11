package ma.teethcare.repository.modules.acte.impl.memoryBase;

import ma.teethcare.entities.Acte;
import ma.teethcare.repository.modules.acte.api.ActeRepository;

import java.util.*;
import java.util.stream.Collectors;

public class ActeRepositoryImpl implements ActeRepository {

    private final List<Acte> data = new ArrayList<>();
    private long nextId = 1;

    public ActeRepositoryImpl() {
        // Données d'exemple pour un cabinet dentaire
        data.add(createActe("Consultation initiale", "Consultation", 300.0));
        data.add(createActe("Contrôle de routine", "Consultation", 200.0));
        data.add(createActe("Détartrage", "Hygiène", 400.0));
        data.add(createActe("Blanchiment dentaire", "Esthétique", 1500.0));
        data.add(createActe("Extraction dentaire simple", "Chirurgie", 800.0));
        data.add(createActe("Extraction dent de sagesse", "Chirurgie", 1500.0));
        data.add(createActe("Obturation (plombage)", "Restauratif", 500.0));
        data.add(createActe("Couronne céramique", "Prothèse", 2500.0));
        data.add(createActe("Bridge 3 éléments", "Prothèse", 6000.0));
        data.add(createActe("Implant dentaire", "Chirurgie", 7000.0));
        data.add(createActe("Radio panoramique", "Radiologie", 350.0));
        data.add(createActe("Radio rétro-alvéolaire", "Radiologie", 150.0));
    }

    private Acte createActe(String libelle, String categorie, Double prixDeBase) {
        Acte acte = new Acte();
        acte.setId(nextId++);
        acte.setLibelle(libelle);
        acte.setCategorie(categorie);
        acte.setPrixDeBase(prixDeBase);
        return acte;
    }

    // -------- CRUD --------
    @Override
    public List<Acte> findAll() {
        return new ArrayList<>(data);
    }

    @Override
    public Acte findById(Long id) {
        return data.stream()
                .filter(a -> a.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    @Override
    public void create(Acte acte) {
        if (acte.getId() == null) {
            acte.setId(nextId++);
        }
        data.add(acte);
    }

    @Override
    public void update(Acte acte) {
        deleteById(acte.getId());
        data.add(acte);
    }

    @Override
    public void delete(Acte acte) {
        data.removeIf(a -> a.getId().equals(acte.getId()));
    }

    @Override
    public void deleteById(Long id) {
        data.removeIf(a -> a.getId().equals(id));
    }

    // -------- Méthodes spécifiques --------
    @Override
    public Optional<Acte> findByLibelle(String libelle) {
        return data.stream()
                .filter(a -> libelle.equalsIgnoreCase(a.getLibelle()))
                .findFirst();
    }

    @Override
    public List<Acte> findByLibelleContaining(String keyword) {
        String lowerKeyword = keyword.toLowerCase();
        return data.stream()
                .filter(a -> a.getLibelle().toLowerCase().contains(lowerKeyword))
                .collect(Collectors.toList());
    }

    @Override
    public List<Acte> findByCategorie(String categorie) {
        return data.stream()
                .filter(a -> categorie.equals(a.getCategorie()))
                .collect(Collectors.toList());
    }

    @Override
    public List<String> findAllCategories() {
        return data.stream()
                .map(Acte::getCategorie)
                .distinct()
                .sorted()
                .collect(Collectors.toList());
    }

    @Override
    public List<Acte> findByPrixDeBaseLessThan(Double prixMax) {
        return data.stream()
                .filter(a -> a.getPrixDeBase() != null && a.getPrixDeBase() < prixMax)
                .collect(Collectors.toList());
    }

    @Override
    public List<Acte> findByPrixDeBaseBetween(Double prixMin, Double prixMax) {
        return data.stream()
                .filter(a -> a.getPrixDeBase() != null &&
                        a.getPrixDeBase() >= prixMin &&
                        a.getPrixDeBase() <= prixMax)
                .collect(Collectors.toList());
    }

    @Override
    public List<Acte> search(String libelle, String categorie, Double prixMin, Double prixMax) {
        return data.stream()
                .filter(a -> {
                    boolean matches = true;
                    if (libelle != null && !libelle.isEmpty()) {
                        matches = matches && a.getLibelle().toLowerCase().contains(libelle.toLowerCase());
                    }
                    if (categorie != null && !categorie.isEmpty()) {
                        matches = matches && categorie.equals(a.getCategorie());
                    }
                    if (prixMin != null) {
                        matches = matches && a.getPrixDeBase() != null && a.getPrixDeBase() >= prixMin;
                    }
                    if (prixMax != null) {
                        matches = matches && a.getPrixDeBase() != null && a.getPrixDeBase() <= prixMax;
                    }
                    return matches;
                })
                .collect(Collectors.toList());
    }

    @Override
    public long countByCategorie(String categorie) {
        return data.stream()
                .filter(a -> categorie.equals(a.getCategorie()))
                .count();
    }

    @Override
    public Double getMoyennePrixByCategorie(String categorie) {
        List<Acte> actes = findByCategorie(categorie);
        if (actes.isEmpty()) return 0.0;

        double somme = actes.stream()
                .mapToDouble(Acte::getPrixDeBase)
                .sum();
        return somme / actes.size();
    }

    @Override
    public List<Acte> findAllOrderByLibelle(int limit, int offset) {
        return data.stream()
                .sorted(Comparator.comparing(Acte::getLibelle, String.CASE_INSENSITIVE_ORDER))
                .skip(offset)
                .limit(limit)
                .collect(Collectors.toList());
    }

    @Override
    public List<Acte> findAllOrderByPrix(int limit, int offset) {
        return data.stream()
                .sorted(Comparator.comparing(Acte::getPrixDeBase))
                .skip(offset)
                .limit(limit)
                .collect(Collectors.toList());
    }

    @Override
    public List<Acte> findAllOrderByCategorie(int limit, int offset) {
        return data.stream()
                .sorted(Comparator.comparing(Acte::getCategorie)
                        .thenComparing(Acte::getLibelle))
                .skip(offset)
                .limit(limit)
                .collect(Collectors.toList());
    }
}