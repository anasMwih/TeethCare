package ma.teethcare.repository.modules.facture.impl.mySQL;

import ma.teethcare.entities.Consultation;
import ma.teethcare.entities.Facture;
import ma.teethcare.entities.Patient;
import ma.teethcare.conf.SessionFactory;
import ma.teethcare.repository.common.RowMappers;
import ma.teethcare.repository.modules.facture.api.FactureRepository;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class FactureRepositoryImpl implements FactureRepository {

    // -------- CRUD --------
    @Override
    public List<Facture> findAll() {
        String sql = "SELECT f.*, p.*, c.* FROM Facture f " +
                "LEFT JOIN Patient p ON f.patient_id = p.id " +
                "LEFT JOIN Consultation c ON f.consultation_id = c.id " +
                "ORDER BY f.dateFacture DESC";
        List<Facture> out = new ArrayList<>();
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                out.add(mapFactureWithRelations(rs));
            }
        } catch (SQLException e) { throw new RuntimeException(e); }
        return out;
    }

    @Override
    public Facture findById(Long id) {
        String sql = "SELECT f.*, p.*, c.* FROM Facture f " +
                "LEFT JOIN Patient p ON f.patient_id = p.id " +
                "LEFT JOIN Consultation c ON f.consultation_id = c.id " +
                "WHERE f.id = ?";
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapFactureWithRelations(rs);
                }
                return null;
            }
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    @Override
    public void create(Facture facture) {
        String sql = "INSERT INTO Facture(montantTotal, montantPaye, resteAPayer, statut, " +
                "dateFacture, numeroFacture, consultation_id, patient_id) " +
                "VALUES(?,?,?,?,?,?,?,?)";
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setDouble(1, facture.getMontantTotal());
            ps.setDouble(2, facture.getMontantPaye() != null ? facture.getMontantPaye() : 0.0);
            ps.setDouble(3, facture.getResteAPayer() != null ? facture.getResteAPayer() : facture.getMontantTotal());
            ps.setString(4, facture.getStatut());
            ps.setTimestamp(5, Timestamp.valueOf(facture.getDateFacture()));
            ps.setString(6, facture.getNumeroFacture());
            ps.setLong(7, facture.getConsultation().getId());
            ps.setLong(8, facture.getPatient().getId());
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) facture.setId(keys.getLong(1));
            }
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    @Override
    public void update(Facture facture) {
        String sql = "UPDATE Facture SET montantTotal=?, montantPaye=?, resteAPayer=?, statut=?, " +
                "dateFacture=?, numeroFacture=?, consultation_id=?, patient_id=? WHERE id=?";
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setDouble(1, facture.getMontantTotal());
            ps.setDouble(2, facture.getMontantPaye() != null ? facture.getMontantPaye() : 0.0);
            ps.setDouble(3, facture.getResteAPayer() != null ? facture.getResteAPayer() : facture.getMontantTotal());
            ps.setString(4, facture.getStatut());
            ps.setTimestamp(5, Timestamp.valueOf(facture.getDateFacture()));
            ps.setString(6, facture.getNumeroFacture());
            ps.setLong(7, facture.getConsultation().getId());
            ps.setLong(8, facture.getPatient().getId());
            ps.setLong(9, facture.getId());
            ps.executeUpdate();
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    @Override
    public void delete(Facture facture) {
        if (facture != null) deleteById(facture.getId());
    }

    @Override
    public void deleteById(Long id) {
        String sql = "DELETE FROM Facture WHERE id = ?";
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, id);
            ps.executeUpdate();
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    // -------- Recherches spécifiques --------
    @Override
    public Optional<Facture> findByConsultation(Consultation consultation) {
        return findByConsultationId(consultation.getId());
    }

    @Override
    public Optional<Facture> findByConsultationId(Long consultationId) {
        String sql = "SELECT f.*, p.*, c.* FROM Facture f " +
                "LEFT JOIN Patient p ON f.patient_id = p.id " +
                "LEFT JOIN Consultation c ON f.consultation_id = c.id " +
                "WHERE f.consultation_id = ?";
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, consultationId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapFactureWithRelations(rs));
                }
                return Optional.empty();
            }
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    @Override
    public List<Facture> findByPatient(Patient patient) {
        return findByPatientId(patient.getId());
    }

    @Override
    public List<Facture> findByPatientId(Long patientId) {
        String sql = "SELECT f.*, p.*, c.* FROM Facture f " +
                "LEFT JOIN Patient p ON f.patient_id = p.id " +
                "LEFT JOIN Consultation c ON f.consultation_id = c.id " +
                "WHERE f.patient_id = ? " +
                "ORDER BY f.dateFacture DESC";
        return executeQueryWithRelations(sql, patientId);
    }

    @Override
    public List<Facture> findByStatut(String statut) {
        String sql = "SELECT f.*, p.*, c.* FROM Facture f " +
                "LEFT JOIN Patient p ON f.patient_id = p.id " +
                "LEFT JOIN Consultation c ON f.consultation_id = c.id " +
                "WHERE f.statut = ? " +
                "ORDER BY f.dateFacture DESC";
        return executeQueryWithRelations(sql, statut);
    }

    @Override
    public List<Facture> findByDateFacture(LocalDate date) {
        String sql = "SELECT f.*, p.*, c.* FROM Facture f " +
                "LEFT JOIN Patient p ON f.patient_id = p.id " +
                "LEFT JOIN Consultation c ON f.consultation_id = c.id " +
                "WHERE DATE(f.dateFacture) = ? " +
                "ORDER BY f.dateFacture DESC";
        return executeQueryWithRelations(sql, date);
    }

    @Override
    public List<Facture> findByDateFactureBetween(LocalDateTime start, LocalDateTime end) {
        String sql = "SELECT f.*, p.*, c.* FROM Facture f " +
                "LEFT JOIN Patient p ON f.patient_id = p.id " +
                "LEFT JOIN Consultation c ON f.consultation_id = c.id " +
                "WHERE f.dateFacture BETWEEN ? AND ? " +
                "ORDER BY f.dateFacture DESC";
        List<Facture> out = new ArrayList<>();
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setTimestamp(1, Timestamp.valueOf(start));
            ps.setTimestamp(2, Timestamp.valueOf(end));
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    out.add(mapFactureWithRelations(rs));
                }
            }
        } catch (SQLException e) { throw new RuntimeException(e); }
        return out;
    }

    @Override
    public List<Facture> findByNumeroFacture(String numero) {
        String sql = "SELECT f.*, p.*, c.* FROM Facture f " +
                "LEFT JOIN Patient p ON f.patient_id = p.id " +
                "LEFT JOIN Consultation c ON f.consultation_id = c.id " +
                "WHERE f.numeroFacture = ?";
        return executeQueryWithRelations(sql, numero);
    }

    @Override
    public List<Facture> findByNumeroFactureContaining(String keyword) {
        String sql = "SELECT f.*, p.*, c.* FROM Facture f " +
                "LEFT JOIN Patient p ON f.patient_id = p.id " +
                "LEFT JOIN Consultation c ON f.consultation_id = c.id " +
                "WHERE f.numeroFacture LIKE ? " +
                "ORDER BY f.dateFacture DESC";
        return executeQueryWithRelations(sql, "%" + keyword + "%");
    }

    @Override
    public List<Facture> findByMontantTotalBetween(Double min, Double max) {
        String sql = "SELECT f.*, p.*, c.* FROM Facture f " +
                "LEFT JOIN Patient p ON f.patient_id = p.id " +
                "LEFT JOIN Consultation c ON f.consultation_id = c.id " +
                "WHERE f.montantTotal BETWEEN ? AND ? " +
                "ORDER BY f.montantTotal DESC";
        List<Facture> out = new ArrayList<>();
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setDouble(1, min);
            ps.setDouble(2, max);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    out.add(mapFactureWithRelations(rs));
                }
            }
        } catch (SQLException e) { throw new RuntimeException(e); }
        return out;
    }

    @Override
    public List<Facture> findByResteAPayerGreaterThan(Double montant) {
        String sql = "SELECT f.*, p.*, c.* FROM Facture f " +
                "LEFT JOIN Patient p ON f.patient_id = p.id " +
                "LEFT JOIN Consultation c ON f.consultation_id = c.id " +
                "WHERE f.resteAPayer > ? " +
                "ORDER BY f.resteAPayer DESC";
        return executeQueryWithRelations(sql, montant);
    }

    @Override
    public List<Facture> findByResteAPayerEquals(Double montant) {
        String sql = "SELECT f.*, p.*, c.* FROM Facture f " +
                "LEFT JOIN Patient p ON f.patient_id = p.id " +
                "LEFT JOIN Consultation c ON f.consultation_id = c.id " +
                "WHERE f.resteAPayer = ? " +
                "ORDER BY f.dateFacture DESC";
        return executeQueryWithRelations(sql, montant);
    }

    @Override
    public boolean existsById(Long id) {
        String sql = "SELECT 1 FROM Facture WHERE id = ?";
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
        String sql = "SELECT 1 FROM Facture WHERE consultation_id = ?";
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, consultationId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    @Override
    public boolean existsByNumeroFacture(String numeroFacture) {
        String sql = "SELECT 1 FROM Facture WHERE numeroFacture = ?";
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, numeroFacture);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    @Override
    public long count() {
        String sql = "SELECT COUNT(*) FROM Facture";
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            rs.next();
            return rs.getLong(1);
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    @Override
    public long countByStatut(String statut) {
        String sql = "SELECT COUNT(*) FROM Facture WHERE statut = ?";
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, statut);
            try (ResultSet rs = ps.executeQuery()) {
                rs.next();
                return rs.getLong(1);
            }
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    @Override
    public long countByPatientId(Long patientId) {
        String sql = "SELECT COUNT(*) FROM Facture WHERE patient_id = ?";
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
    public List<Facture> findPage(int limit, int offset) {
        String sql = "SELECT f.*, p.*, c.* FROM Facture f " +
                "LEFT JOIN Patient p ON f.patient_id = p.id " +
                "LEFT JOIN Consultation c ON f.consultation_id = c.id " +
                "ORDER BY f.dateFacture DESC LIMIT ? OFFSET ?";
        List<Facture> out = new ArrayList<>();
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, limit);
            ps.setInt(2, offset);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    out.add(mapFactureWithRelations(rs));
                }
            }
        } catch (SQLException e) { throw new RuntimeException(e); }
        return out;
    }

    // -------- Statistiques financières --------
    @Override
    public Double sumMontantTotal() {
        String sql = "SELECT SUM(montantTotal) FROM Facture";
        return executeSumQuery(sql);
    }

    @Override
    public Double sumMontantTotalByStatut(String statut) {
        String sql = "SELECT SUM(montantTotal) FROM Facture WHERE statut = ?";
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, statut);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next() && rs.getObject(1) != null) {
                    return rs.getDouble(1);
                }
                return 0.0;
            }
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    @Override
    public Double sumMontantTotalByPatientId(Long patientId) {
        String sql = "SELECT SUM(montantTotal) FROM Facture WHERE patient_id = ?";
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

    @Override
    public Double sumMontantTotalByDate(LocalDate date) {
        String sql = "SELECT SUM(montantTotal) FROM Facture WHERE DATE(dateFacture) = ?";
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
    public Double sumMontantPaye() {
        String sql = "SELECT SUM(montantPaye) FROM Facture";
        return executeSumQuery(sql);
    }

    @Override
    public Double sumMontantPayeByStatut(String statut) {
        String sql = "SELECT SUM(montantPaye) FROM Facture WHERE statut = ?";
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, statut);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next() && rs.getObject(1) != null) {
                    return rs.getDouble(1);
                }
                return 0.0;
            }
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    @Override
    public Double sumResteAPayer() {
        String sql = "SELECT SUM(resteAPayer) FROM Facture";
        return executeSumQuery(sql);
    }

    @Override
    public Double sumResteAPayerByPatientId(Long patientId) {
        String sql = "SELECT SUM(resteAPayer) FROM Facture WHERE patient_id = ?";
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

    // -------- Méthodes de mise à jour --------
    @Override
    public void updateStatut(Long factureId, String statut) {
        String sql = "UPDATE Facture SET statut = ? WHERE id = ?";
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, statut);
            ps.setLong(2, factureId);
            ps.executeUpdate();
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    @Override
    public void updateMontantPaye(Long factureId, Double montantPaye) {
        String sql = "UPDATE Facture SET montantPaye = ?, resteAPayer = montantTotal - ? WHERE id = ?";
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setDouble(1, montantPaye);
            ps.setDouble(2, montantPaye);
            ps.setLong(3, factureId);
            ps.executeUpdate();
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    @Override
    public void updateResteAPayer(Long factureId, Double resteAPayer) {
        String sql = "UPDATE Facture SET resteAPayer = ?, montantPaye = montantTotal - ? WHERE id = ?";
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setDouble(1, resteAPayer);
            ps.setDouble(2, resteAPayer);
            ps.setLong(3, factureId);
            ps.executeUpdate();
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    // -------- Recherche avancée --------
    @Override
    public List<Facture> searchByPatientNomPrenom(String keyword) {
        String sql = "SELECT f.*, p.*, c.* FROM Facture f " +
                "LEFT JOIN Patient p ON f.patient_id = p.id " +
                "LEFT JOIN Consultation c ON f.consultation_id = c.id " +
                "WHERE p.nom LIKE ? OR p.prenom LIKE ? " +
                "ORDER BY f.dateFacture DESC";
        List<Facture> out = new ArrayList<>();
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            String like = "%" + keyword + "%";
            ps.setString(1, like);
            ps.setString(2, like);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    out.add(mapFactureWithRelations(rs));
                }
            }
        } catch (SQLException e) { throw new RuntimeException(e); }
        return out;
    }

    @Override
    public List<Facture> findEnRetard(int joursRetard) {
        String sql = "SELECT f.*, p.*, c.* FROM Facture f " +
                "LEFT JOIN Patient p ON f.patient_id = p.id " +
                "LEFT JOIN Consultation c ON f.consultation_id = c.id " +
                "WHERE f.dateFacture < DATE_SUB(NOW(), INTERVAL ? DAY) " +
                "AND f.statut = 'EN_ATTENTE' " +
                "AND f.resteAPayer > 0 " +
                "ORDER BY f.dateFacture ASC";
        List<Facture> out = new ArrayList<>();
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, joursRetard);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    out.add(mapFactureWithRelations(rs));
                }
            }
        } catch (SQLException e) { throw new RuntimeException(e); }
        return out;
    }

    // -------- Méthodes utilitaires --------
    private Facture mapFactureWithRelations(ResultSet rs) throws SQLException {
        Facture facture = RowMappers.mapFacture(rs);

        // Mapping Patient
        Patient patient = new Patient();
        patient.setId(rs.getLong("p.id"));
        patient.setNom(rs.getString("p.nom"));
        patient.setPrenom(rs.getString("p.prenom"));
        facture.setPatient(patient);

        // Mapping Consultation (si disponible)
        if (rs.getObject("c.id") != null) {
            Consultation consultation = new Consultation();
            consultation.setId(rs.getLong("c.id"));
            consultation.setDate(rs.getDate("c.date").toLocalDate());
            consultation.setSymptomes(rs.getString("c.symptomes"));
            consultation.setPrix(rs.getDouble("c.prix"));
            facture.setConsultation(consultation);
        }

        return facture;
    }

    private List<Facture> executeQueryWithRelations(String sql, Object param) {
        List<Facture> out = new ArrayList<>();
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            if (param instanceof Long) {
                ps.setLong(1, (Long) param);
            } else if (param instanceof String) {
                ps.setString(1, (String) param);
            } else if (param instanceof LocalDate) {
                ps.setDate(1, Date.valueOf((LocalDate) param));
            } else if (param instanceof Double) {
                ps.setDouble(1, (Double) param);
            }
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    out.add(mapFactureWithRelations(rs));
                }
            }
        } catch (SQLException e) { throw new RuntimeException(e); }
        return out;
    }

    private Double executeSumQuery(String sql) {
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next() && rs.getObject(1) != null) {
                return rs.getDouble(1);
            }
            return 0.0;
        } catch (SQLException e) { throw new RuntimeException(e); }
    }
}