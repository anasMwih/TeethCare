package ma.teethcare.repository.modules.consultation.impl.mySQL;

import ma.teethcare.entities.Consultation;
import ma.teethcare.entities.DossierMedical;
import ma.teethcare.entities.Facture;
import ma.teethcare.entities.InterventionMedecin;
import ma.teethcare.entities.Patient;
import ma.teethcare.entities.Prescription;
import ma.teethcare.conf.SessionFactory;
import ma.teethcare.repository.common.RowMappers;
import ma.teethcare.repository.modules.consultation.api.ConsultationRepository;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ConsultationRepositoryImpl implements ConsultationRepository {

    // -------- CRUD --------
    @Override
    public List<Consultation> findAll() {
        String sql = "SELECT c.*, p.*, d.* FROM Consultation c " +
                "LEFT JOIN Patient p ON c.patient_id = p.id " +
                "LEFT JOIN DossierMedical d ON c.dossier_id = d.id " +
                "ORDER BY c.date DESC, c.id DESC";
        List<Consultation> out = new ArrayList<>();
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Consultation consultation = mapConsultationWithRelations(rs);
                out.add(consultation);
            }
        } catch (SQLException e) { throw new RuntimeException(e); }
        return out;
    }

    @Override
    public Consultation findById(Long id) {
        String sql = "SELECT c.*, p.*, d.* FROM Consultation c " +
                "LEFT JOIN Patient p ON c.patient_id = p.id " +
                "LEFT JOIN DossierMedical d ON c.dossier_id = d.id " +
                "WHERE c.id = ?";
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapConsultationWithRelations(rs);
                }
                return null;
            }
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    @Override
    public void create(Consultation consultation) {
        String sql = "INSERT INTO Consultation(date, symptomes, diagnostic, traitement, prix, patient_id, dossier_id) " +
                "VALUES(?,?,?,?,?,?,?)";
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setDate(1, Date.valueOf(consultation.getDate()));
            ps.setString(2, consultation.getSymptomes());
            ps.setString(3, consultation.getDiagnostic());
            ps.setString(4, consultation.getTraitement());
            if (consultation.getPrix() != null) {
                ps.setDouble(5, consultation.getPrix());
            } else {
                ps.setNull(5, Types.DOUBLE);
            }
            ps.setLong(6, consultation.getPatient().getId());
            if (consultation.getDossierMedical() != null) {
                ps.setLong(7, consultation.getDossierMedical().getId());
            } else {
                ps.setNull(7, Types.BIGINT);
            }
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) consultation.setId(keys.getLong(1));
            }
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    @Override
    public void update(Consultation consultation) {
        String sql = "UPDATE Consultation SET date=?, symptomes=?, diagnostic=?, traitement=?, " +
                "prix=?, patient_id=?, dossier_id=? WHERE id=?";
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setDate(1, Date.valueOf(consultation.getDate()));
            ps.setString(2, consultation.getSymptomes());
            ps.setString(3, consultation.getDiagnostic());
            ps.setString(4, consultation.getTraitement());
            if (consultation.getPrix() != null) {
                ps.setDouble(5, consultation.getPrix());
            } else {
                ps.setNull(5, Types.DOUBLE);
            }
            ps.setLong(6, consultation.getPatient().getId());
            if (consultation.getDossierMedical() != null) {
                ps.setLong(7, consultation.getDossierMedical().getId());
            } else {
                ps.setNull(7, Types.BIGINT);
            }
            ps.setLong(8, consultation.getId());
            ps.executeUpdate();
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    @Override
    public void delete(Consultation consultation) {
        if (consultation != null) deleteById(consultation.getId());
    }

    @Override
    public void deleteById(Long id) {
        String sql = "DELETE FROM Consultation WHERE id = ?";
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, id);
            ps.executeUpdate();
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    // -------- Recherches spécifiques --------
    @Override
    public List<Consultation> findByPatient(Patient patient) {
        return findByPatientId(patient.getId());
    }

    @Override
    public List<Consultation> findByPatientId(Long patientId) {
        String sql = "SELECT c.*, p.*, d.* FROM Consultation c " +
                "LEFT JOIN Patient p ON c.patient_id = p.id " +
                "LEFT JOIN DossierMedical d ON c.dossier_id = d.id " +
                "WHERE c.patient_id = ? " +
                "ORDER BY c.date DESC";
        return executeQueryWithRelations(sql, patientId);
    }

    @Override
    public List<Consultation> findByDossierMedical(DossierMedical dossier) {
        return findByDossierMedicalId(dossier.getId());
    }

    @Override
    public List<Consultation> findByDossierMedicalId(Long dossierId) {
        String sql = "SELECT c.*, p.*, d.* FROM Consultation c " +
                "LEFT JOIN Patient p ON c.patient_id = p.id " +
                "LEFT JOIN DossierMedical d ON c.dossier_id = d.id " +
                "WHERE c.dossier_id = ? " +
                "ORDER BY c.date DESC";
        return executeQueryWithRelations(sql, dossierId);
    }

    @Override
    public List<Consultation> findByDate(LocalDate date) {
        String sql = "SELECT c.*, p.*, d.* FROM Consultation c " +
                "LEFT JOIN Patient p ON c.patient_id = p.id " +
                "LEFT JOIN DossierMedical d ON c.dossier_id = d.id " +
                "WHERE DATE(c.date) = ? " +
                "ORDER BY c.date DESC";
        return executeQueryWithRelations(sql, date);
    }

    @Override
    public List<Consultation> findByDateBetween(LocalDate start, LocalDate end) {
        String sql = "SELECT c.*, p.*, d.* FROM Consultation c " +
                "LEFT JOIN Patient p ON c.patient_id = p.id " +
                "LEFT JOIN DossierMedical d ON c.dossier_id = d.id " +
                "WHERE c.date BETWEEN ? AND ? " +
                "ORDER BY c.date DESC";
        List<Consultation> out = new ArrayList<>();
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setDate(1, Date.valueOf(start));
            ps.setDate(2, Date.valueOf(end));
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    out.add(mapConsultationWithRelations(rs));
                }
            }
        } catch (SQLException e) { throw new RuntimeException(e); }
        return out;
    }

    @Override
    public List<Consultation> findByPrixBetween(Double min, Double max) {
        String sql = "SELECT c.*, p.*, d.* FROM Consultation c " +
                "LEFT JOIN Patient p ON c.patient_id = p.id " +
                "LEFT JOIN DossierMedical d ON c.dossier_id = d.id " +
                "WHERE c.prix BETWEEN ? AND ? " +
                "ORDER BY c.prix DESC";
        List<Consultation> out = new ArrayList<>();
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setDouble(1, min);
            ps.setDouble(2, max);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    out.add(mapConsultationWithRelations(rs));
                }
            }
        } catch (SQLException e) { throw new RuntimeException(e); }
        return out;
    }

    @Override
    public List<Consultation> findBySymptomesContaining(String keyword) {
        String sql = "SELECT c.*, p.*, d.* FROM Consultation c " +
                "LEFT JOIN Patient p ON c.patient_id = p.id " +
                "LEFT JOIN DossierMedical d ON c.dossier_id = d.id " +
                "WHERE c.symptomes LIKE ? " +
                "ORDER BY c.date DESC";
        return executeQueryWithRelations(sql, "%" + keyword + "%");
    }

    @Override
    public List<Consultation> findByDiagnosticContaining(String keyword) {
        String sql = "SELECT c.*, p.*, d.* FROM Consultation c " +
                "LEFT JOIN Patient p ON c.patient_id = p.id " +
                "LEFT JOIN DossierMedical d ON c.dossier_id = d.id " +
                "WHERE c.diagnostic LIKE ? " +
                "ORDER BY c.date DESC";
        return executeQueryWithRelations(sql, "%" + keyword + "%");
    }

    @Override
    public List<Consultation> findByTraitementContaining(String keyword) {
        String sql = "SELECT c.*, p.*, d.* FROM Consultation c " +
                "LEFT JOIN Patient p ON c.patient_id = p.id " +
                "LEFT JOIN DossierMedical d ON c.dossier_id = d.id " +
                "WHERE c.traitement LIKE ? " +
                "ORDER BY c.date DESC";
        return executeQueryWithRelations(sql, "%" + keyword + "%");
    }

    @Override
    public Optional<Facture> getFactureOfConsultation(Long consultationId) {
        String sql = "SELECT f.* FROM Facture f WHERE f.consultation_id = ?";
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, consultationId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(RowMappers.mapFacture(rs));
                }
                return Optional.empty();
            }
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    @Override
    public boolean existsById(Long id) {
        String sql = "SELECT 1 FROM Consultation WHERE id = ?";
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    @Override
    public long count() {
        String sql = "SELECT COUNT(*) FROM Consultation";
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            rs.next();
            return rs.getLong(1);
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    @Override
    public List<Consultation> findPage(int limit, int offset) {
        String sql = "SELECT c.*, p.*, d.* FROM Consultation c " +
                "LEFT JOIN Patient p ON c.patient_id = p.id " +
                "LEFT JOIN DossierMedical d ON c.dossier_id = d.id " +
                "ORDER BY c.date DESC LIMIT ? OFFSET ?";
        List<Consultation> out = new ArrayList<>();
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, limit);
            ps.setInt(2, offset);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    out.add(mapConsultationWithRelations(rs));
                }
            }
        } catch (SQLException e) { throw new RuntimeException(e); }
        return out;
    }

    // -------- Statistiques --------
    @Override
    public long countByDate(LocalDate date) {
        String sql = "SELECT COUNT(*) FROM Consultation WHERE DATE(date) = ?";
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setDate(1, Date.valueOf(date));
            try (ResultSet rs = ps.executeQuery()) {
                rs.next();
                return rs.getLong(1);
            }
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    @Override
    public long countByPatientId(Long patientId) {
        String sql = "SELECT COUNT(*) FROM Consultation WHERE patient_id = ?";
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, patientId);
            try (ResultSet rs = ps.executeQuery()) {
                rs.next();
                return rs.getLong(1);
            }
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    @Override
    public Double sumPrixByDate(LocalDate date) {
        String sql = "SELECT SUM(prix) FROM Consultation WHERE DATE(date) = ?";
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setDate(1, Date.valueOf(date));
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next() && rs.getObject(1) != null) {
                    return rs.getDouble(1);
                }
                return 0.0;
            }
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    @Override
    public Double sumPrixByPatientId(Long patientId) {
        String sql = "SELECT SUM(prix) FROM Consultation WHERE patient_id = ?";
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, patientId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next() && rs.getObject(1) != null) {
                    return rs.getDouble(1);
                }
                return 0.0;
            }
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    // -------- Relations avec InterventionMedecin --------
    @Override
    public List<InterventionMedecin> getInterventionsOfConsultation(Long consultationId) {
        String sql = "SELECT i.* FROM InterventionMedecin i " +
                "WHERE i.consultation_id = ? " +
                "ORDER BY i.dateIntervention DESC";
        List<InterventionMedecin> out = new ArrayList<>();
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, consultationId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) out.add(RowMappers.mapInterventionMedecin(rs));
            }
        } catch (SQLException e) { throw new RuntimeException(e); }
        return out;
    }

    @Override
    public void addInterventionToConsultation(Long consultationId, Long interventionId) {
        String sql = "UPDATE InterventionMedecin SET consultation_id = ? WHERE id = ?";
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, consultationId);
            ps.setLong(2, interventionId);
            ps.executeUpdate();
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    @Override
    public void removeInterventionFromConsultation(Long consultationId, Long interventionId) {
        String sql = "UPDATE InterventionMedecin SET consultation_id = NULL WHERE id = ? AND consultation_id = ?";
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, interventionId);
            ps.setLong(2, consultationId);
            ps.executeUpdate();
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    // -------- Relations avec Prescription --------
    @Override
    public List<Prescription> getPrescriptionsOfConsultation(Long consultationId) {
        String sql = "SELECT p.* FROM Prescription p " +
                "WHERE p.consultation_id = ? " +
                "ORDER BY p.datePrescription DESC";
        List<Prescription> out = new ArrayList<>();
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, consultationId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) out.add(RowMappers.mapPrescription(rs));
            }
        } catch (SQLException e) { throw new RuntimeException(e); }
        return out;
    }

    @Override
    public void addPrescriptionToConsultation(Long consultationId, Long prescriptionId) {
        String sql = "UPDATE Prescription SET consultation_id = ? WHERE id = ?";
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, consultationId);
            ps.setLong(2, prescriptionId);
            ps.executeUpdate();
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    @Override
    public void removePrescriptionFromConsultation(Long consultationId, Long prescriptionId) {
        String sql = "UPDATE Prescription SET consultation_id = NULL WHERE id = ? AND consultation_id = ?";
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, prescriptionId);
            ps.setLong(2, consultationId);
            ps.executeUpdate();
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    // -------- Méthodes utilitaires --------
    private Consultation mapConsultationWithRelations(ResultSet rs) throws SQLException {
        Consultation consultation = RowMappers.mapConsultation(rs);

        // Mapping Patient
        Patient patient = new Patient();
        patient.setId(rs.getLong("p.id"));
        patient.setNom(rs.getString("p.nom"));
        patient.setPrenom(rs.getString("p.prenom"));
        consultation.setPatient(patient);

        // Mapping DossierMedical (si disponible)
        if (rs.getObject("d.id") != null) {
            DossierMedical dossier = new DossierMedical();
            dossier.setId(rs.getLong("d.id"));
            dossier.setDateCreation(rs.getDate("d.dateCreation").toLocalDate());
            dossier.setObservations(rs.getString("d.observations"));
            consultation.setDossierMedical(dossier);
        }

        return consultation;
    }

    private List<Consultation> executeQueryWithRelations(String sql, Object param) {
        List<Consultation> out = new ArrayList<>();
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            if (param instanceof Long) {
                ps.setLong(1, (Long) param);
            } else if (param instanceof LocalDate) {
                ps.setDate(1, Date.valueOf((LocalDate) param));
            } else if (param instanceof String) {
                ps.setString(1, (String) param);
            }
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    out.add(mapConsultationWithRelations(rs));
                }
            }
        } catch (SQLException e) { throw new RuntimeException(e); }
        return out;
    }
}