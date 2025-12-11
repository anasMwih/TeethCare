package ma.teethcare.repository.modules.certificat.impl.memoryBase;

import ma.teethcare.entities.Certificat;
import ma.teethcare.entities.DossierMedical;
import ma.teethcare.entities.Consultation;
import ma.teethcare.repository.modules.certificat.api.CertificatRepository;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class CertificatRepositoryImpl implements CertificatRepository {

    private final List<Certificat> data = new ArrayList<>();
    private long nextId = 1;

    public CertificatRepositoryImpl() {
        // Données d'exemple
        DossierMedical dossier1 = new DossierMedical();
        dossier1.setId(1L);

        DossierMedical dossier2 = new DossierMedical();
        dossier2.setId(2L);

        Consultation cons1 = new Consultation();
        cons1.setId(1L);

        Consultation cons2 = new Consultation();
        cons2.setId(2L);

        LocalDate today = LocalDate.now();
        LocalDate yesterday = today.minusDays(1);
        LocalDate lastMonth = today.minusMonths(1);
        LocalDate nextWeek = today.plusDays(7);
        LocalDate nextMonth = today.plusMonths(1);
        LocalDate expiredDate = today.minusDays(10);

        data.add(createCertificat(dossier1, cons1, "Certificat médical", today, nextWeek, "Aptitude au travail"));
        data.add(createCertificat(dossier1, cons2, "Certificat d'arrêt maladie", yesterday, nextMonth, "Arrêt maladie 7 jours"));
        data.add(createCertificat(dossier2, cons1, "Certificat médical", lastMonth, expiredDate, "Contrôle médical"));
        data.add(createCertificat(dossier2, cons2, "Certificat de vaccination", today, today.plusYears(1), "Vaccination antitétanique"));
    }

    private Certificat createCertificat(DossierMedical dossier, Consultation consultation,
                                        String type, LocalDate emission, LocalDate expiration, String description) {
        Certificat certificat = new Certificat();
        certificat.setId(nextId++);
        certificat.setDossierMedical(dossier);
        certificat.setConsultation(consultation);
        certificat.setType(type);
        certificat.setDateEmission(emission);
        certificat.setDateExpiration(expiration);
        certificat.setDescription(description);
        return certificat;
    }

    // -------- CRUD --------
    @Override
    public List<Certificat> findAll() {
        return new ArrayList<>(data);
    }

    @Override
    public Certificat findById(Long id) {
        return data.stream()
                .filter(c -> c.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    @Override
    public void create(Certificat certificat) {
        if (certificat.getId() == null) {
            certificat.setId(nextId++);
        }
        data.add(certificat);
    }

    @Override
    public void update(Certificat certificat) {
        deleteById(certificat.getId());
        data.add(certificat);
    }

    @Override
    public void delete(Certificat certificat) {
        data.removeIf(c -> c.getId().equals(certificat.getId()));
    }

    @Override
    public void deleteById(Long id) {
        data.removeIf(c -> c.getId().equals(id));
    }

    // -------- Méthodes spécifiques --------
    @Override
    public List<Certificat> findByDossierMedicalId(Long dossierMedicalId) {
        return data.stream()
                .filter(c -> c.getDossierMedical() != null && c.getDossierMedical().getId().equals(dossierMedicalId))
                .collect(Collectors.toList());
    }

    @Override
    public List<Certificat> findByConsultationId(Long consultationId) {
        return data.stream()
                .filter(c -> c.getConsultation() != null && c.getConsultation().getId().equals(consultationId))
                .collect(Collectors.toList());
    }

    @Override
    public List<Certificat> findByType(String type) {
        return data.stream()
                .filter(c -> type.equals(c.getType()))
                .collect(Collectors.toList());
    }

    @Override
    public List<String> findAllTypes() {
        return data.stream()
                .map(Certificat::getType)
                .distinct()
                .sorted()
                .collect(Collectors.toList());
    }

    @Override
    public List<Certificat> findByDateEmission(LocalDate date) {
        return data.stream()
                .filter(c -> c.getDateEmission() != null && c.getDateEmission().equals(date))
                .collect(Collectors.toList());
    }

    @Override
    public List<Certificat> findByDateEmissionBetween(LocalDate start, LocalDate end) {
        return data.stream()
                .filter(c -> c.getDateEmission() != null &&
                        !c.getDateEmission().isBefore(start) &&
                        !c.getDateEmission().isAfter(end))
                .collect(Collectors.toList());
    }

    @Override
    public List<Certificat> findByDateExpiration(LocalDate date) {
        return data.stream()
                .filter(c -> c.getDateExpiration() != null && c.getDateExpiration().equals(date))
                .collect(Collectors.toList());
    }

    @Override
    public List<Certificat> findCertificatsExpirantAvant(LocalDate date) {
        LocalDate today = LocalDate.now();
        return data.stream()
                .filter(c -> c.getDateExpiration() != null &&
                        c.getDateExpiration().isBefore(date) &&
                        c.getDateExpiration().isAfter(today))
                .collect(Collectors.toList());
    }

    @Override
    public List<Certificat> findCertificatsExpires() {
        LocalDate today = LocalDate.now();
        return data.stream()
                .filter(c -> c.getDateExpiration() != null && c.getDateExpiration().isBefore(today))
                .collect(Collectors.toList());
    }

    @Override
    public List<Certificat> findCertificatsValides() {
        LocalDate today = LocalDate.now();
        return data.stream()
                .filter(c -> c.getDateExpiration() != null &&
                        (c.getDateExpiration().isAfter(today) || c.getDateExpiration().equals(today)))
                .collect(Collectors.toList());
    }

    @Override
    public long countByType(String type) {
        return data.stream()
                .filter(c -> type.equals(c.getType()))
                .count();
    }

    @Override
    public long countByDossierMedicalId(Long dossierMedicalId) {
        return data.stream()
                .filter(c -> c.getDossierMedical() != null && c.getDossierMedical().getId().equals(dossierMedicalId))
                .count();
    }

    @Override
    public long countCertificatsExpires() {
        return findCertificatsExpires().size();
    }

    @Override
    public long countCertificatsValides() {
        return findCertificatsValides().size();
    }

    @Override
    public boolean isCertificatValide(Long certificatId) {
        Certificat cert = findById(certificatId);
        if (cert == null || cert.getDateExpiration() == null) return false;

        LocalDate today = LocalDate.now();
        return !cert.getDateExpiration().isBefore(today);
    }

    @Override
    public void deleteByDossierMedicalId(Long dossierMedicalId) {
        data.removeIf(c -> c.getDossierMedical() != null && c.getDossierMedical().getId().equals(dossierMedicalId));
    }

    @Override
    public void deleteByConsultationId(Long consultationId) {
        data.removeIf(c -> c.getConsultation() != null && c.getConsultation().getId().equals(consultationId));
    }
}