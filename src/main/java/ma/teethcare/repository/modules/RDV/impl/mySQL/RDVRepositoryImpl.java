package ma.teethcare.repository.modules.rdv.impl.mySQL;

import ma.teethcare.entities.RDV;
import ma.teethcare.entities.Patient;
import ma.teethcare.entities.Medecin;
import ma.teethcare.repository.modules.rdv.api.RDVRepository;
import ma.teethcare.repository.common.RowMappers;
import ma.teethcare.conf.SessionFactory;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class RDVRepositoryImpl implements RDVRepository {

    // -------- CRUD --------
    @Override
    public List<RDV> findAll() {
        String sql = "SELECT * FROM rendezvous ORDER BY date_rdv";
        List<RDV> out = new ArrayList<>();
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) out.add(RowMappers.mapRDV(rs));
        } catch (SQLException e) { throw new RuntimeException(e); }
        return out;
    }

    @Override
    public RDV findById(Long id) {
        String sql = "SELECT * FROM rendezvous WHERE id = ?";
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return RowMappers.mapRDV(rs);
                return null;
            }
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    @Override
    public void create(RDV rdv) {
        String sql = """
            INSERT INTO rendezvous(patient_id, medecin_id, date_rdv, statut, notes, type_consultation, created_at)
            VALUES(?, ?, ?, ?, ?, ?, ?)
            """;
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setLong(1, rdv.getPatient().getId());
            ps.setLong(2, rdv.getMedecin().getId());
            ps.setTimestamp(3, Timestamp.valueOf(rdv.getDateRdv()));
            ps.setString(4, rdv.getStatut());
            ps.setString(5, rdv.getNotes());
            ps.setString(6, rdv.getTypeConsultation());
            ps.setTimestamp(7, new Timestamp(System.currentTimeMillis()));

            ps.executeUpdate();

            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) rdv.setId(keys.getLong(1));
            }
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    @Override
    public void update(RDV rdv) {
        String sql = """
            UPDATE rendezvous SET 
                patient_id = ?, medecin_id = ?, date_rdv = ?, 
                statut = ?, notes = ?, type_consultation = ?, updated_at = ?
            WHERE id = ?
            """;
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setLong(1, rdv.getPatient().getId());
            ps.setLong(2, rdv.getMedecin().getId());
            ps.setTimestamp(3, Timestamp.valueOf(rdv.getDateRdv()));
            ps.setString(4, rdv.getStatut());
            ps.setString(5, rdv.getNotes());
            ps.setString(6, rdv.getTypeConsultation());
            ps.setTimestamp(7, new Timestamp(System.currentTimeMillis()));
            ps.setLong(8, rdv.getId());

            ps.executeUpdate();
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    @Override
    public void delete(RDV rdv) {
        if (rdv != null) deleteById(rdv.getId());
    }

    @Override
    public void deleteById(Long id) {
        String sql = "DELETE FROM rendezvous WHERE id = ?";
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, id);
            ps.executeUpdate();
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    // -------- Méthodes spécifiques --------
    @Override
    public List<RDV> findByPatientId(Long patientId) {
        String sql = "SELECT * FROM rendezvous WHERE patient_id = ? ORDER BY date_rdv DESC";
        List<RDV> out = new ArrayList<>();
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, patientId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) out.add(RowMappers.mapRDV(rs));
            }
        } catch (SQLException e) { throw new RuntimeException(e); }
        return out;
    }

    @Override
    public List<RDV> findByMedecinId(Long medecinId) {
        String sql = "SELECT * FROM rendezvous WHERE medecin_id = ? ORDER BY date_rdv ASC";
        List<RDV> out = new ArrayList<>();
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, medecinId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) out.add(RowMappers.mapRDV(rs));
            }
        } catch (SQLException e) { throw new RuntimeException(e); }
        return out;
    }

    @Override
    public List<RDV> findByDate(LocalDate date) {
        String sql = "SELECT * FROM rendezvous WHERE DATE(date_rdv) = ? ORDER BY date_rdv ASC";
        List<RDV> out = new ArrayList<>();
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setDate(1, Date.valueOf(date));
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) out.add(RowMappers.mapRDV(rs));
            }
        } catch (SQLException e) { throw new RuntimeException(e); }
        return out;
    }

    @Override
    public List<RDV> findByDateRange(LocalDateTime start, LocalDateTime end) {
        String sql = "SELECT * FROM rendezvous WHERE date_rdv BETWEEN ? AND ? ORDER BY date_rdv ASC";
        List<RDV> out = new ArrayList<>();
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setTimestamp(1, Timestamp.valueOf(start));
            ps.setTimestamp(2, Timestamp.valueOf(end));
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) out.add(RowMappers.mapRDV(rs));
            }
        } catch (SQLException e) { throw new RuntimeException(e); }
        return out;
    }

    @Override
    public List<RDV> findByStatut(String statut) {
        String sql = "SELECT * FROM rendezvous WHERE statut = ? ORDER BY date_rdv DESC";
        List<RDV> out = new ArrayList<>();
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, statut);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) out.add(RowMappers.mapRDV(rs));
            }
        } catch (SQLException e) { throw new RuntimeException(e); }
        return out;
    }

    @Override
    public List<RDV> findProchainsRDV(int jours) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime limit = now.plusDays(jours);

        String sql = "SELECT * FROM rendezvous WHERE date_rdv BETWEEN ? AND ? AND statut IN ('PLANIFIE', 'CONFIRME') ORDER BY date_rdv ASC";
        List<RDV> out = new ArrayList<>();
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setTimestamp(1, Timestamp.valueOf(now));
            ps.setTimestamp(2, Timestamp.valueOf(limit));
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) out.add(RowMappers.mapRDV(rs));
            }
        } catch (SQLException e) { throw new RuntimeException(e); }
        return out;
    }

    @Override
    public boolean existeConflit(LocalDateTime debut, LocalDateTime fin, Long medecinId) {
        String sql = """
            SELECT COUNT(*) as count FROM rendezvous 
            WHERE medecin_id = ? 
            AND statut IN ('PLANIFIE', 'CONFIRME')
            AND ((date_rdv <= ? AND DATE_ADD(date_rdv, INTERVAL 30 MINUTE) > ?)
                 OR (date_rdv >= ? AND date_rdv < ?))
            """;
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setLong(1, medecinId);
            ps.setTimestamp(2, Timestamp.valueOf(fin));
            ps.setTimestamp(3, Timestamp.valueOf(debut));
            ps.setTimestamp(4, Timestamp.valueOf(debut));
            ps.setTimestamp(5, Timestamp.valueOf(fin));

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("count") > 0;
                }
            }
        } catch (SQLException e) { throw new RuntimeException(e); }
        return false;
    }

    @Override
    public long countByPatientId(Long patientId) {
        String sql = "SELECT COUNT(*) FROM rendezvous WHERE patient_id = ?";
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
    public long countByMedecinId(Long medecinId) {
        String sql = "SELECT COUNT(*) FROM rendezvous WHERE medecin_id = ?";
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, medecinId);
            try (ResultSet rs = ps.executeQuery()) {
                rs.next();
                return rs.getLong(1);
            }
        } catch (SQLException e) { throw new RuntimeException(e); }
    }
}