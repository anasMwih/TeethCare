package ma.teethcare.repository.modules.fileattente.impl.memoryBase;

import ma.teethcare.entities.FileAttente;
import ma.teethcare.entities.Patient;
import ma.teethcare.repository.modules.fileattente.api.FileAttenteRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class FileAttenteRepositoryImpl implements FileAttenteRepository {

    private final List<FileAttente> data = new ArrayList<>();
    private long nextId = 1;

    public FileAttenteRepositoryImpl() {
        // Données d'exemple
        LocalDateTime now = LocalDateTime.now();

        Patient p1 = new Patient();
        p1.setId(1L);
        p1.setFirstName("Amal");
        p1.setLastName("Z.");

        Patient p2 = new Patient();
        p2.setId(2L);
        p2.setFirstName("Hassan");
        p2.setLastName("B.");

        Patient p3 = new Patient();
        p3.setId(3L);
        p3.setFirstName("Nour");
        p3.setLastName("C.");

        Patient p4 = new Patient();
        p4.setId(4L);
        p4.setFirstName("Youssef");
        p4.setLastName("D.");

        // Patients en attente
        data.add(createFileAttente(p1, now.minusMinutes(30), 1, "EN_ATTENTE", "NORMAL"));
        data.add(createFileAttente(p2, now.minusMinutes(20), 2, "EN_ATTENTE", "URGENT"));
        data.add(createFileAttente(p3, now.minusMinutes(10), 3, "EN_COURS", "NORMAL"));
        data.add(createFileAttente(p4, now.minusHours(1), 0, "TERMINE", "NORMAL"));
    }

    private FileAttente createFileAttente(Patient patient, LocalDateTime dateArrivee,
                                          Integer position, String statut, String priorite) {
        FileAttente file = new FileAttente();
        file.setId(nextId++);
        file.setPatient(patient);
        file.setDateArrivee(dateArrivee);
        file.setPosition(position);
        file.setStatut(statut);
        file.setPriorite(priorite);
        return file;
    }

    // -------- CRUD --------
    @Override
    public List<FileAttente> findAll() {
        return new ArrayList<>(data);
    }

    @Override
    public FileAttente findById(Long id) {
        return data.stream()
                .filter(f -> f.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    @Override
    public void create(FileAttente fileAttente) {
        if (fileAttente.getId() == null) {
            fileAttente.setId(nextId++);
        }
        data.add(fileAttente);
        updatePositions();
    }

    @Override
    public void update(FileAttente fileAttente) {
        deleteById(fileAttente.getId());
        data.add(fileAttente);
        updatePositions();
    }

    @Override
    public void delete(FileAttente fileAttente) {
        data.removeIf(f -> f.getId().equals(fileAttente.getId()));
        updatePositions();
    }

    @Override
    public void deleteById(Long id) {
        data.removeIf(f -> f.getId().equals(id));
        updatePositions();
    }

    // -------- Méthodes spécifiques --------
    @Override
    public Optional<FileAttente> findByPatientId(Long patientId) {
        return data.stream()
                .filter(f -> f.getPatient() != null && f.getPatient().getId().equals(patientId))
                .findFirst();
    }

    @Override
    public List<FileAttente> findByStatut(String statut) {
        return data.stream()
                .filter(f -> statut.equals(f.getStatut()))
                .collect(Collectors.toList());
    }

    @Override
    public List<FileAttente> findEnAttente() {
        return findByStatut("EN_ATTENTE").stream()
                .sorted(Comparator.comparing(FileAttente::getPosition))
                .collect(Collectors.toList());
    }

    @Override
    public List<FileAttente> findEnCours() {
        return findByStatut("EN_COURS");
    }

    @Override
    public List<FileAttente> findByPriorite(String priorite) {
        return data.stream()
                .filter(f -> priorite.equals(f.getPriorite()))
                .collect(Collectors.toList());
    }

    @Override
    public List<FileAttente> findByDate(LocalDate date) {
        return data.stream()
                .filter(f -> f.getDateArrivee() != null && f.getDateArrivee().toLocalDate().equals(date))
                .collect(Collectors.toList());
    }

    @Override
    public FileAttente getProchainPatient() {
        return findEnAttente().stream()
                .filter(f -> "NORMAL".equals(f.getPriorite()))
                .min(Comparator.comparing(FileAttente::getPosition))
                .orElse(null);
    }

    @Override
    public FileAttente getProchainPatientUrgent() {
        return findEnAttente().stream()
                .filter(f -> "URGENT".equals(f.getPriorite()))
                .min(Comparator.comparing(FileAttente::getPosition))
                .orElse(null);
    }

    @Override
    public void updatePositions() {
        // Trier les patients en attente par priorité et date d'arrivée
        List<FileAttente> enAttente = findEnAttente();

        // Priorité URGENT d'abord, puis NORMAL
        enAttente.sort((f1, f2) -> {
            if (!f1.getPriorite().equals(f2.getPriorite())) {
                return f2.getPriorite().compareTo(f1.getPriorite()); // URGENT avant NORMAL
            }
            return f1.getDateArrivee().compareTo(f2.getDateArrivee());
        });

        // Mettre à jour les positions
        for (int i = 0; i < enAttente.size(); i++) {
            enAttente.get(i).setPosition(i + 1);
        }
    }

    @Override
    public int getPositionSuivante() {
        return findEnAttente().size() + 1;
    }

    @Override
    public int getNombrePatientsEnAttente() {
        return findEnAttente().size();
    }

    @Override
    public int getTempsAttenteEstime(Long fileAttenteId) {
        FileAttente file = findById(fileAttenteId);
        if (file == null || !"EN_ATTENTE".equals(file.getStatut())) return 0;

        // Estimation : 15 minutes par patient avant
        int position = file.getPosition();
        return (position - 1) * 15;
    }

    @Override
    public boolean prendreEnCharge(Long fileAttenteId, Long medecinId) {
        FileAttente file = findById(fileAttenteId);
        if (file != null && "EN_ATTENTE".equals(file.getStatut())) {
            file.setStatut("EN_COURS");
            updatePositions();
            return true;
        }
        return false;
    }

    @Override
    public boolean terminer(Long fileAttenteId) {
        FileAttente file = findById(fileAttenteId);
        if (file != null && ("EN_ATTENTE".equals(file.getStatut()) || "EN_COURS".equals(file.getStatut()))) {
            file.setStatut("TERMINE");
            file.setPosition(0);
            updatePositions();
            return true;
        }
        return false;
    }

    @Override
    public boolean reordonner(Long fileAttenteId, Integer nouvellePosition) {
        FileAttente file = findById(fileAttenteId);
        if (file == null || !"EN_ATTENTE".equals(file.getStatut())) return false;

        if (nouvellePosition < 1 || nouvellePosition > getNombrePatientsEnAttente()) {
            return false;
        }

        file.setPosition(nouvellePosition);
        updatePositions();
        return true;
    }

    @Override
    public long countByStatut(String statut) {
        return data.stream()
                .filter(f -> statut.equals(f.getStatut()))
                .count();
    }

    @Override
    public long countByPriorite(String priorite) {
        return data.stream()
                .filter(f -> priorite.equals(f.getPriorite()))
                .count();
    }

    @Override
    public double getTempsMoyenAttente() {
        List<FileAttente> termines = findByStatut("TERMINE");
        if (termines.isEmpty()) return 0.0;

        long totalMinutes = termines.stream()
                .filter(f -> f.getDateArrivee() != null)
                .mapToLong(f -> {
                    // Pour simplifier, estimation basée sur la position
                    return (f.getPosition() - 1) * 15L;
                })
                .sum();

        return (double) totalMinutes / termines.size();
    }
}