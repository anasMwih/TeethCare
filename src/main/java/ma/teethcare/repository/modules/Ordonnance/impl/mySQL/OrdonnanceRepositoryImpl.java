package ma.teethcare.repository.modules.ordonnance.impl.mySQL;

import ma.teethcare.entities.Consultation;
import ma.teethcare.entities.Ordonnance;
import ma.teethcare.entities.Prescription;
import ma.teethcare.conf.SessionFactory;
import ma.teethcare.repository.common.RowMappers;
import ma.teethcare.repository.modules.ordonnance.api.OrdonnanceRepository;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class OrdonnanceRepositoryImpl implements OrdonnanceRepository {

    // -------- CRUD --------
    @Override
    public List<Ordonnance> findAll() {
        String sql = "SELECT o.*, c.* FROM Ordonnance o " +
                "LEFT JOIN Consultation c ON o.consultation_id = c.id " +
                "ORDER BY o.date DESC, o.id DESC";
        List<Ordonnance> out = new ArrayList<>();
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                out.add(mapOrdonnanceWithConsultation(rs));
            }
        } catch (SQLException e) { throw new RuntimeException(e); }
        return out;
    }

    @Override
    public Ordonnance findById(Long id) {
        String sql = "SELECT o.*, c.* FROM Ordonnance o " +
                "LEFT JOIN Consultation c ON o.consultation_id = c.id " +
                "WHERE o.id = ?";
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapOrdonnanceWithConsultation(rs);
                }
                return null;
            }
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    @Override
    public void create(Ordonnance ordonnance) {
        String sql = "INSERT INTO Ordonnance(date, instructions, consultation_id) VALUES(?,?,?)";
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setDate(1, Date.valueOf(ordonnance.getDate()));
            ps.setString(2, ordonnance.getInstructions());
            ps.setLong(3, ordonnance.getConsultation().getId());
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) ordonnance.setId(keys.getLong(1));
            }
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    @Override
    public void update(Ordonnance ordonnance) {
        String sql = "UPDATE Ordonnance SET date=?, instructions=?, consultation_id=? WHERE id=?";
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setDate(1, Date.valueOf(ordonnance.getDate()));
            ps.setString(2, ordonnance.getInstructions());
            ps.setLong(3, ordonnance.getConsultation().getId());
            ps.setLong(4, ordonnance.getId());
            ps.executeUpdate();
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    @Override
    public void delete(Ordonnance ordonnance) {
        if (ordonnance != null) deleteById(ordonnance.getId());
    }

    @Override
    public void deleteById(Long id) {
        String sql = "DELETE FROM Ordonnance WHERE id = ?";
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, id);
            ps.executeUpdate();
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    // -------- Recherches spécifiques --------
    @Override
    public List<Ordonnance> findByConsultation(Consultation consultation) {
        return findByConsultationId(consultation.getId());
    }

    @Override
    public List<Ordonnance> findByConsultationId(Long consultationId) {
        String sql = "SELECT o.*, c.* FROM Ordonnance o " +
                "LEFT JOIN Consultation c ON o.consultation_id = c.id " +
                "WHERE o.consultation_id = ? " +
                "ORDER BY o.date DESC";
        return executeQueryWithConsultation(sql, consultationId);
    }

    @Override
    public List<Ordonnance> findByDate(LocalDate date) {
        String sql = "SELECT o.*, c.* FROM Ordonnance o " +
                "LEFT JOIN Consultation c ON o.consultation_id = c.id " +
                "WHERE DATE(o.date) = ? " +
                "ORDER BY o.date DESC";
        return executeQueryWithConsultation(sql, date);
    }

    @Override
    public List<Ordonnance> findByDateBetween(LocalDate start, LocalDate end) {
        String sql = "SELECT o.*, c.* FROM Ordonnance o " +
                "LEFT JOIN Consultation c ON o.consultation_id = c.id " +
                "WHERE o.date BETWEEN ? AND ? " +
                "ORDER BY o.date DESC";
        List<Ordonnance> out = new ArrayList<>();
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setDate(1, Date.valueOf(start));
            ps.setDate(2, Date.valueOf(end));
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    out.add(mapOrdonnanceWithConsultation(rs));
                }
            }
        } catch (SQLException e) { throw new RuntimeException(e); }
        return out;
    }

    @Override
    public List<Ordonnance> findByInstructionsContaining(String keyword) {
        String sql = "SELECT o.*, c.* FROM Ordonnance o " +
                "LEFT JOIN Consultation c ON o.consultation_id = c.id " +
                "WHERE o.instructions LIKE ? " +
                "ORDER BY o.date DESC";
        return executeQueryWithConsultation(sql, "%" + keyword + "%");
    }

    @Override
    public boolean existsById(Long id) {
        String sql = "SELECT 1 FROM Ordonnance WHERE id = ?";
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    @Override
    public boolean existsByConsultationId(Long consultationId) {
        String sql = "SELECT 1 FROM Ordonnance WHERE consultation_id = ?";
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, consultationId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    @Override
    public long count() {
        String sql = "SELECT COUNT(*) FROM Ordonnance";
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            rs.next();
            return rs.getLong(1);
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    @Override
    public long countByConsultationId(Long consultationId) {
        String sql = "SELECT COUNT(*) FROM Ordonnance WHERE consultation_id = ?";
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, consultationId);
            try (ResultSet rs = ps.executeQuery()) {
                rs.next();
                return rs.getLong(1);
            }
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    @Override
    public long countByDate(LocalDate date) {
        String sql = "SELECT COUNT(*) FROM Ordonnance WHERE DATE(date) = ?";
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
    public List<Ordonnance> findPage(int limit, int offset) {
        String sql = "SELECT o.*, c.* FROM Ordonnance o " +
                "LEFT JOIN Consultation c ON o.consultation_id = c.id " +
                "ORDER BY o.date DESC LIMIT ? OFFSET ?";
        List<Ordonnance> out = new ArrayList<>();
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, limit);
            ps.setInt(2, offset);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    out.add(mapOrdonnanceWithConsultation(rs));
                }
            }
        } catch (SQLException e) { throw new RuntimeException(e); }
        return out;
    }

    // -------- Statistiques --------
    @Override
    public long countOrdonnancesByMonth(int year, int month) {
        String sql = "SELECT COUNT(*) FROM Ordonnance WHERE YEAR(date) = ? AND MONTH(date) = ?";
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, year);
            ps.setInt(2, month);
            try (ResultSet rs = ps.executeQuery()) {
                rs.next();
                return rs.getLong(1);
            }
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    // -------- Relations avec Prescription --------
    @Override
    public List<Prescription> getPrescriptionsOfOrdonnance(Long ordonnanceId) {
        String sql = "SELECT p.* FROM Prescription p " +
                "WHERE p.ordonnance_id = ? " +
                "ORDER BY p.id";
        List<Prescription> out = new ArrayList<>();
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, ordonnanceId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) out.add(RowMappers.mapPrescription(rs));
            }
        } catch (SQLException e) { throw new RuntimeException(e); }
        return out;
    }

    @Override
    public void addPrescriptionToOrdonnance(Long ordonnanceId, Long prescriptionId) {
        String sql = "UPDATE Prescription SET ordonnance_id = ? WHERE id = ?";
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, ordonnanceId);
            ps.setLong(2, prescriptionId);
            ps.executeUpdate();
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    @Override
    public void removePrescriptionFromOrdonnance(Long ordonnanceId, Long prescriptionId) {
        String sql = "UPDATE Prescription SET ordonnance_id = NULL WHERE id = ? AND ordonnance_id = ?";
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, prescriptionId);
            ps.setLong(2, ordonnanceId);
            ps.executeUpdate();
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    @Override
    public void removeAllPrescriptionsFromOrdonnance(Long ordonnanceId) {
        String sql = "UPDATE Prescription SET ordonnance_id = NULL WHERE ordonnance_id = ?";
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, ordonnanceId);
            ps.executeUpdate();
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    // -------- Recherche avancée --------
    @Override
    public List<Ordonnance> searchByConsultationPatient(String patientNom) {
        String sql = "SELECT o.*, c.* FROM Ordonnance o " +
                "LEFT JOIN Consultation c ON o.consultation_id = c.id " +
                "LEFT JOIN Patient p ON c.patient_id = p.id " +
                "WHERE p.nom LIKE ? OR p.prenom LIKE ? " +
                "ORDER BY o.date DESC";
        List<Ordonnance> out = new ArrayList<>();
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            String like = "%" + patientNom + "%";
            ps.setString(1, like);
            ps.setString(2, like);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    out.add(mapOrdonnanceWithConsultation(rs));
                }
            }
        } catch (SQLException e) { throw new RuntimeException(e); }
        return out;
    }

    @Override
    public List<Ordonnance> findRecentOrdonnances(int days) {
        String sql = "SELECT o.*, c.* FROM Ordonnance o " +
                "LEFT JOIN Consultation c ON o.consultation_id = c.id " +
                "WHERE o.date >= DATE_SUB(CURDATE(), INTERVAL ? DAY) " +
                "ORDER BY o.date DESC";
        return executeQueryWithConsultation(sql, days);
    }

    // -------- Méthodes utilitaires --------
    private Ordonnance mapOrdonnanceWithConsultation(ResultSet rs) throws SQLException {
        Ordonnance ordonnance = RowMappers.mapOrdonnance(rs);

        // Mapping Consultation
        if (rs.getObject("c.id") != null) {
            Consultation consultation = new Consultation();
            consultation.setId(rs.getLong("c.id"));
            consultation.setDate(rs.getDate("c.date").toLocalDate());
            consultation.setSymptomes(rs.getString("c.symptomes"));
            ordonnance.setConsultation(consultation);
        }

        return ordonnance;
    }

    private List<Ordonnance> executeQueryWithConsultation(String sql, Object param) {
        List<Ordonnance> out = new ArrayList<>();
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            if (param instanceof Long) {
                ps.setLong(1, (Long) param);
            } else if (param instanceof LocalDate) {
                ps.setDate(1, Date.valueOf((LocalDate) param));
            } else if (param instanceof String) {
                ps.setString(1, (String) param);
            } else if (param instanceof Integer) {
                ps.setInt(1, (Integer) param);
            }
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    out.add(mapOrdonnanceWithConsultation(rs));
                }
            }
        } catch (SQLException e) { throw new RuntimeException(e); }
        return out;
    }
}