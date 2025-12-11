package ma.teethcare.repository.modules.rdv.impl.memoryBase;

import ma.teethcare.entities.RDV;
import ma.teethcare.entities.Patient;
import ma.teethcare.entities.Medecin;
import ma.teethcare.repository.modules.rdv.api.RDVRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class RDVRepositoryImpl implements RDVRepository {

    private final List<RDV> data = new ArrayList<>();
    private long nextId = 1;

    public RDVRepositoryImpl() {
        // Données d'exemple
        LocalDateTime now = LocalDateTime.now();

        // Création de patients et médecins fictifs
        Patient p1 = new Patient();
        p1.setId(1L);
        p1.setFirstName("Amal");
        p1.setLastName("Z.");

        Patient p2 = new Patient();
        p2.setId(2L);
        p2.setFirstName("Hassan");
        p2.setLastName("B.");

        Medecin m1 = new Medecin();
        m1.setId(1L);
        m1.setNom("Dr. Smith");
        m1.setPrenom("John");

        Medecin m2 = new Medecin();
        m2.setId(2L);
        m2.setNom("Dr. Johnson");
        m2.setPrenom("Sarah");

        // Ajout des RDV d'exemple
        data.add(createRDV(p1, m1, now.plusDays(1).withHour(10).withMinute(0), "PLANIFIE"));
        data.add(createRDV(p2, m2, now.plusDays(2).withHour(14).withMinute(30), "CONFIRME"));
        data.add(createRDV(p1, m2, now.minusDays(1).withHour(9).withMinute(0), "TERMINE"));
        data.add(createRDV(p2, m1, now.plusDays(3).withHour(11).withMinute(15), "PLANIFIE"));
    }

    private RDV createRDV(Patient patient, Medecin medecin, LocalDateTime date, String statut) {
        RDV rdv = new RDV();
        rdv.setId(nextId++);
        rdv.setPatient(patient);
        rdv.setMedecin(medecin);
        rdv.setDateRdv(date);
        rdv.setStatut(statut);
        rdv.setTypeConsultation("Consultation standard");
        return rdv;
    }

    // -------- CRUD --------
    @Override
    public List<RDV> findAll() {
        return new ArrayList<>(data);
    }

    @Override
    public RDV findById(Long id) {
        return data.stream()
                .filter(r -> r.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    @Override
    public void create(RDV rdv) {
        if (rdv.getId() == null) {
            rdv.setId(nextId++);
        }
        data.add(rdv);
    }

    @Override
    public void update(RDV rdv) {
        deleteById(rdv.getId());
        data.add(rdv);
    }

    @Override
    public void delete(RDV rdv) {
        data.removeIf(r -> r.getId().equals(rdv.getId()));
    }

    @Override
    public void deleteById(Long id) {
        data.removeIf(r -> r.getId().equals(id));
    }

    // -------- Méthodes spécifiques --------
    @Override
    public List<RDV> findByPatientId(Long patientId) {
        return data.stream()
                .filter(r -> r.getPatient() != null && r.getPatient().getId().equals(patientId))
                .collect(Collectors.toList());
    }

    @Override
    public List<RDV> findByMedecinId(Long medecinId) {
        return data.stream()
                .filter(r -> r.getMedecin() != null && r.getMedecin().getId().equals(medecinId))
                .collect(Collectors.toList());
    }

    @Override
    public List<RDV> findByDate(LocalDate date) {
        return data.stream()
                .filter(r -> r.getDateRdv() != null && r.getDateRdv().toLocalDate().equals(date))
                .collect(Collectors.toList());
    }

    @Override
    public List<RDV> findByDateRange(LocalDateTime start, LocalDateTime end) {
        return data.stream()
                .filter(r -> r.getDateRdv() != null &&
                        !r.getDateRdv().isBefore(start) &&
                        !r.getDateRdv().isAfter(end))
                .collect(Collectors.toList());
    }

    @Override
    public List<RDV> findByStatut(String statut) {
        return data.stream()
                .filter(r -> statut.equals(r.getStatut()))
                .collect(Collectors.toList());
    }

    @Override
    public List<RDV> findProchainsRDV(int jours) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime limit = now.plusDays(jours);

        return data.stream()
                .filter(r -> r.getDateRdv() != null &&
                        r.getDateRdv().isAfter(now) &&
                        r.getDateRdv().isBefore(limit) &&
                        !"ANNULE".equals(r.getStatut()) &&
                        !"TERMINE".equals(r.getStatut()))
                .collect(Collectors.toList());
    }

    @Override
    public boolean existeConflit(LocalDateTime debut, LocalDateTime fin, Long medecinId) {
        // Supposons qu'un RDV dure 30 minutes
        LocalDateTime finRDV = debut.plusMinutes(30);

        return data.stream()
                .anyMatch(r -> r.getMedecin() != null &&
                        r.getMedecin().getId().equals(medecinId) &&
                        r.getDateRdv() != null &&
                        !"ANNULE".equals(r.getStatut()) &&
                        ((r.getDateRdv().isBefore(finRDV) &&
                                r.getDateRdv().plusMinutes(30).isAfter(debut)) ||
                                (r.getDateRdv().isAfter(debut) &&
                                        r.getDateRdv().isBefore(fin))));
    }

    @Override
    public long countByPatientId(Long patientId) {
        return data.stream()
                .filter(r -> r.getPatient() != null && r.getPatient().getId().equals(patientId))
                .count();
    }

    @Override
    public long countByMedecinId(Long medecinId) {
        return data.stream()
                .filter(r -> r.getMedecin() != null && r.getMedecin().getId().equals(medecinId))
                .count();
    }
}