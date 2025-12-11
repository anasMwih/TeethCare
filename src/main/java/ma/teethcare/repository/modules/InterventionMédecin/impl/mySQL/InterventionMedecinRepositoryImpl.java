package ma.teethcare.repository.modules.intervention.impl.mySQL;

import ma.teethcare.entities.InterventionMedecin;
import ma.teethcare.repository.modules.intervention.api.InterventionMedecinRepository;
import ma.teethcare.repository.common.RowMappers;
import ma.teethcare.conf.SessionFactory;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class InterventionMedecinRepositoryImpl implements InterventionMedecinRepository {

    // -------- CRUD --------
    @Override
    public List<InterventionMedecin> findAll() {
        String sql = """
            SELECT i.*, c.id as consultation_id, c.date as consultation_date,
                   m.id as medecin_id, m.nom as medecin_nom, m.prenom as medecin_prenom
            FROM interventions i
            LEFT JOIN consultations c ON i.consultation_id = c.id
            LEFT JOIN medecins m ON i.medecin_id = m.id
            ORDER BY i.date_intervention DESC
            """;
        List<InterventionMedecin> out = new ArrayList<>();
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) out.add(RowMappers.mapInterventionMedecin(rs));
        } catch (SQLException e) { throw new RuntimeException(e); }
        return out;
    }

    @Override
    public InterventionMedecin findById(Long id) {
        String sql = """
            SELECT i.*, c.id as consultation_id, c.date as consultation_date,
                   m.id as medecin_id, m.nom as medecin_nom, m.prenom as medecin_prenom
            FROM interventions i
            LEFT JOIN consultations c ON i.consultation_id = c.id
            LEFT JOIN medecins m ON i.medecin_id = m.id
            WHERE i.id = ?
            """;
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return RowMappers.mapInterventionMedecin(rs);
                return null;
            }
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    @Override
    public void create(InterventionMedecin intervention) {
        String sql = """
            INSERT INTO interventions(consultation_id, medecin_id, date_intervention, 
                                    type_intervention, description, duree, cout, created_at)
            VALUES(?, ?, ?, ?, ?, ?, ?, ?)
            """;
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setLong(1, intervention.getConsultation().getId());
            ps.setLong(2, intervention.getMedecin().getId());
            ps.setDate(3, Date.valueOf(intervention.getDateIntervention()));
            ps.setString(4, intervention.getTypeIntervention());
            ps.setString(5, intervention.getDescription());
            ps.setInt(6, intervention.getDuree());
            ps.setDouble(7, intervention.getCout());
            ps.setTimestamp(8, new Timestamp(System.currentTimeMillis()));

            ps.executeUpdate();

            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) intervention.setId(keys.getLong(1));
            }
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    @Override
    public void update(InterventionMedecin intervention) {
        String sql = """
            UPDATE interventions SET 
                consultation_id = ?, medecin_id = ?, date_intervention = ?, 
                type_intervention = ?, description = ?, duree = ?, cout = ?, updated_at = ?
            WHERE id = ?
            """;
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setLong(1, intervention.getConsultation().getId());
            ps.setLong(2, intervention.getMedecin().getId());
            ps.setDate(3, Date.valueOf(intervention.getDateIntervention()));
            ps.setString(4, intervention.getTypeIntervention());
            ps.setString(5, intervention.getDescription());
            ps.setInt(6, intervention.getDuree());
            ps.setDouble(7, intervention.getCout());
            ps.setTimestamp(8, new Timestamp(System.currentTimeMillis()));
            ps.setLong(9, intervention.getId());

            ps.executeUpdate();
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    @Override
    public void delete(InterventionMedecin intervention) {
        if (intervention != null) deleteById(intervention.getId());
    }

    @Override
    public void deleteById(Long id) {
        String sql = "DELETE FROM interventions WHERE id = ?";
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, id);
            ps.executeUpdate();
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    // -------- Méthodes spécifiques --------
    @Override
    public List<InterventionMedecin> findByConsultationId(Long consultationId) {
        String sql = """
            SELECT i.*, c.id as consultation_id, c.date as consultation_date,
                   m.id as medecin_id, m.nom as medecin_nom, m.prenom as medecin_prenom
            FROM interventions i
            LEFT JOIN consultations c ON i.consultation_id = c.id
            LEFT JOIN medecins m ON i.medecin_id = m.id
            WHERE i.consultation_id = ?
            ORDER BY i.date_intervention
            """;
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
    public List<InterventionMedecin> findByMedecinId(Long medecinId) {
        String sql = """
            SELECT i.*, c.id as consultation_id, c.date as consultation_date,
                   m.id as medecin_id, m.nom as medecin_nom, m.prenom as medecin_prenom
            FROM interventions i
            LEFT JOIN consultations c ON i.consultation_id = c.id
            LEFT JOIN medecins m ON i.medecin_id = m.id
            WHERE i.medecin_id = ?
            ORDER BY i.date_intervention DESC
            """;
        List<InterventionMedecin> out = new ArrayList<>();
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, medecinId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) out.add(RowMappers.mapInterventionMedecin(rs));
            }
        } catch (SQLException e) { throw new RuntimeException(e); }
        return out;
    }

    @Override
    public List<InterventionMedecin> findByDate(LocalDate date) {
        String sql = """
            SELECT i.*, c.id as consultation_id, c.date as consultation_date,
                   m.id as medecin_id, m.nom as medecin_nom, m.prenom as medecin_prenom
            FROM interventions i
            LEFT JOIN consultations c ON i.consultation_id = c.id
            LEFT JOIN medecins m ON i.medecin_id = m.id
            WHERE DATE(i.date_intervention) = ?
            ORDER BY i.date_intervention
            """;
        List<InterventionMedecin> out = new ArrayList<>();
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setDate(1, Date.valueOf(date));
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) out.add(RowMappers.mapInterventionMedecin(rs));
            }
        } catch (SQLException e) { throw new RuntimeException(e); }
        return out;
    }

    @Override
    public List<InterventionMedecin> findByDateRange(LocalDate start, LocalDate end) {
        String sql = """
            SELECT i.*, c.id as consultation_id, c.date as consultation_date,
                   m.id as medecin_id, m.nom as medecin_nom, m.prenom as medecin_prenom
            FROM interventions i
            LEFT JOIN consultations c ON i.consultation_id = c.id
            LEFT JOIN medecins m ON i.medecin_id = m.id
            WHERE i.date_intervention BETWEEN ? AND ?
            ORDER BY i.date_intervention
            """;
        List<InterventionMedecin> out = new ArrayList<>();
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setDate(1, Date.valueOf(start));
            ps.setDate(2, Date.valueOf(end));
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) out.add(RowMappers.mapInterventionMedecin(rs));
            }
        } catch (SQLException e) { throw new RuntimeException(e); }
        return out;
    }

    @Override
    public List<InterventionMedecin> findByTypeIntervention(String type) {
        String sql = """
            SELECT i.*, c.id as consultation_id, c.date as consultation_date,
                   m.id as medecin_id, m.nom as medecin_nom, m.prenom as medecin_prenom
            FROM interventions i
            LEFT JOIN consultations c ON i.consultation_id = c.id
            LEFT JOIN medecins m ON i.medecin_id = m.id
            WHERE i.type_intervention = ?
            ORDER BY i.date_intervention DESC
            """;
        List<InterventionMedecin> out = new ArrayList<>();
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, type);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) out.add(RowMappers.mapInterventionMedecin(rs));
            }
        } catch (SQLException e) { throw new RuntimeException(e); }
        return out;
    }

    @Override
    public List<String> findAllTypesIntervention() {
        String sql = "SELECT DISTINCT type_intervention FROM interventions ORDER BY type_intervention";
        List<String> out = new ArrayList<>();
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) out.add(rs.getString("type_intervention"));
        } catch (SQLException e) { throw new RuntimeException(e); }
        return out;
    }

    @Override
    public long countByMedecinId(Long medecinId) {
        String sql = "SELECT COUNT(*) FROM interventions WHERE medecin_id = ?";
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, medecinId);
            try (ResultSet rs = ps.executeQuery()) {
                rs.next();
                return rs.getLong(1);
            }
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    @Override
    public long countByTypeIntervention(String type) {
        String sql = "SELECT COUNT(*) FROM interventions WHERE type_intervention = ?";
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, type);
            try (ResultSet rs = ps.executeQuery()) {
                rs.next();
                return rs.getLong(1);
            }
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    @Override
    public Double getCoutTotalByMedecin(Long medecinId) {
        String sql = "SELECT SUM(cout) as total FROM interventions WHERE medecin_id = ?";
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, medecinId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getDouble("total");
                }
            }
        } catch (SQLException e) { throw new RuntimeException(e); }
        return 0.0;
    }

    @Override
    public Double getCoutTotalByConsultation(Long consultationId) {
        String sql = "SELECT SUM(cout) as total FROM interventions WHERE consultation_id = ?";
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, consultationId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getDouble("total");
                }
            }
        } catch (SQLException e) { throw new RuntimeException(e); }
        return 0.0;
    }

    @Override
    public Integer getDureeTotaleByMedecin(Long medecinId) {
        String sql = "SELECT SUM(duree) as total FROM interventions WHERE medecin_id = ?";
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, medecinId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("total");
                }
            }
        } catch (SQLException e) { throw new RuntimeException(e); }
        return 0;
    }

    @Override
    public void deleteByConsultationId(Long consultationId) {
        String sql = "DELETE FROM interventions WHERE consultation_id = ?";
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, consultationId);
            ps.executeUpdate();
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    @Override
    public void deleteByMedecinId(Long medecinId) {
        String sql = "DELETE FROM interventions WHERE medecin_id = ?";
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, medecinId);
            ps.executeUpdate();
        } catch (SQLException e) { throw new RuntimeException(e); }
    }
}