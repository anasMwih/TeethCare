package ma.teethcare.repository.modules.intervention.impl.memoryBase;

import ma.teethcare.entities.InterventionMedecin;
import ma.teethcare.entities.Consultation;
import ma.teethcare.entities.Medecin;
import ma.teethcare.repository.modules.intervention.api.InterventionMedecinRepository;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class InterventionMedecinRepositoryImpl implements InterventionMedecinRepository {

    private final List<InterventionMedecin> data = new ArrayList<>();
    private long nextId = 1;

    public InterventionMedecinRepositoryImpl() {
        // Données d'exemple
        Consultation cons1 = new Consultation();
        cons1.setId(1L);

        Consultation cons2 = new Consultation();
        cons2.setId(2L);

        Medecin med1 = new Medecin();
        med1.setId(1L);
        med1.setNom("Dr. Smith");

        Medecin med2 = new Medecin();
        med2.setId(2L);
        med2.setNom("Dr. Johnson");

        LocalDate today = LocalDate.now();
        LocalDate yesterday = today.minusDays(1);
        LocalDate lastWeek = today.minusDays(7);

        data.add(createIntervention(cons1, med1, "Extraction dentaire", yesterday, 45, 800.0));
        data.add(createIntervention(cons1, med1, "Détartrage", yesterday, 30, 400.0));
        data.add(createIntervention(cons2, med2, "Obturation", today, 60, 500.0));
        data.add(createIntervention(cons2, med1, "Radio panoramique", today, 20, 350.0));
        data.add(createIntervention(cons1, med2, "Consultation contrôle", lastWeek, 15, 200.0));
    }

    private InterventionMedecin createIntervention(Consultation consultation, Medecin medecin,
                                                   String type, LocalDate date, Integer duree, Double cout) {
        InterventionMedecin intervention = new InterventionMedecin();
        intervention.setId(nextId++);
        intervention.setConsultation(consultation);
        intervention.setMedecin(medecin);
        intervention.setTypeIntervention(type);
        intervention.setDateIntervention(date);
        intervention.setDuree(duree);
        intervention.setCout(cout);
        intervention.setDescription("Intervention de type " + type);
        return intervention;
    }

    // -------- CRUD --------
    @Override
    public List<InterventionMedecin> findAll() {
        return new ArrayList<>(data);
    }

    @Override
    public InterventionMedecin findById(Long id) {
        return data.stream()
                .filter(i -> i.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    @Override
    public void create(InterventionMedecin intervention) {
        if (intervention.getId() == null) {
            intervention.setId(nextId++);
        }
        data.add(intervention);
    }

    @Override
    public void update(InterventionMedecin intervention) {
        deleteById(intervention.getId());
        data.add(intervention);
    }

    @Override
    public void delete(InterventionMedecin intervention) {
        data.removeIf(i -> i.getId().equals(intervention.getId()));
    }

    @Override
    public void deleteById(Long id) {
        data.removeIf(i -> i.getId().equals(id));
    }

    // -------- Méthodes spécifiques --------
    @Override
    public List<InterventionMedecin> findByConsultationId(Long consultationId) {
        return data.stream()
                .filter(i -> i.getConsultation() != null && i.getConsultation().getId().equals(consultationId))
                .collect(Collectors.toList());
    }

    @Override
    public List<InterventionMedecin> findByMedecinId(Long medecinId) {
        return data.stream()
                .filter(i -> i.getMedecin() != null && i.getMedecin().getId().equals(medecinId))
                .collect(Collectors.toList());
    }

    @Override
    public List<InterventionMedecin> findByDate(LocalDate date) {
        return data.stream()
                .filter(i -> i.getDateIntervention() != null && i.getDateIntervention().equals(date))
                .collect(Collectors.toList());
    }

    @Override
    public List<InterventionMedecin> findByDateRange(LocalDate start, LocalDate end) {
        return data.stream()
                .filter(i -> i.getDateIntervention() != null &&
                        !i.getDateIntervention().isBefore(start) &&
                        !i.getDateIntervention().isAfter(end))
                .collect(Collectors.toList());
    }

    @Override
    public List<InterventionMedecin> findByTypeIntervention(String type) {
        return data.stream()
                .filter(i -> type.equals(i.getTypeIntervention()))
                .collect(Collectors.toList());
    }

    @Override
    public List<String> findAllTypesIntervention() {
        return data.stream()
                .map(InterventionMedecin::getTypeIntervention)
                .distinct()
                .sorted()
                .collect(Collectors.toList());
    }

    @Override
    public List<InterventionMedecin> findByDureeGreaterThan(Integer minutes) {
        return data.stream()
                .filter(i -> i.getDuree() != null && i.getDuree() > minutes)
                .collect(Collectors.toList());
    }

    @Override
    public List<InterventionMedecin> findByCoutGreaterThan(Double coutMin) {
        return data.stream()
                .filter(i -> i.getCout() != null && i.getCout() > coutMin)
                .collect(Collectors.toList());
    }

    @Override
    public long countByMedecinId(Long medecinId) {
        return data.stream()
                .filter(i -> i.getMedecin() != null && i.getMedecin().getId().equals(medecinId))
                .count();
    }

    @Override
    public long countByTypeIntervention(String type) {
        return data.stream()
                .filter(i -> type.equals(i.getTypeIntervention()))
                .count();
    }

    @Override
    public Double getCoutTotalByMedecin(Long medecinId) {
        return data.stream()
                .filter(i -> i.getMedecin() != null && i.getMedecin().getId().equals(medecinId))
                .mapToDouble(InterventionMedecin::getCout)
                .sum();
    }

    @Override
    public Double getCoutTotalByConsultation(Long consultationId) {
        return data.stream()
                .filter(i -> i.getConsultation() != null && i.getConsultation().getId().equals(consultationId))
                .mapToDouble(InterventionMedecin::getCout)
                .sum();
    }

    @Override
    public Integer getDureeTotaleByMedecin(Long medecinId) {
        return data.stream()
                .filter(i -> i.getMedecin() != null && i.getMedecin().getId().equals(medecinId))
                .mapToInt(InterventionMedecin::getDuree)
                .sum();
    }

    @Override
    public void deleteByConsultationId(Long consultationId) {
        data.removeIf(i -> i.getConsultation() != null && i.getConsultation().getId().equals(consultationId));
    }

    @Override
    public void deleteByMedecinId(Long medecinId) {
        data.removeIf(i -> i.getMedecin() != null && i.getMedecin().getId().equals(medecinId));
    }

    @Override
    public List<InterventionMedecin> search(String typeIntervention, Long medecinId,
                                            LocalDate dateDebut, LocalDate dateFin, Double coutMax) {
        return data.stream()
                .filter(i -> {
                    boolean matches = true;
                    if (typeIntervention != null && !typeIntervention.isEmpty()) {
                        matches = matches && typeIntervention.equals(i.getTypeIntervention());
                    }
                    if (medecinId != null) {
                        matches = matches && i.getMedecin() != null && i.getMedecin().getId().equals(medecinId);
                    }
                    if (dateDebut != null) {
                        matches = matches && i.getDateIntervention() != null &&
                                !i.getDateIntervention().isBefore(dateDebut);
                    }
                    if (dateFin != null) {
                        matches = matches && i.getDateIntervention() != null &&
                                !i.getDateIntervention().isAfter(dateFin);
                    }
                    if (coutMax != null) {
                        matches = matches && i.getCout() != null && i.getCout() <= coutMax;
                    }
                    return matches;
                })
                .collect(Collectors.toList());
    }
}