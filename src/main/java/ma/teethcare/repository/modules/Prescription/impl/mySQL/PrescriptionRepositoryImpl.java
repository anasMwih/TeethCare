package ma.teethcare.repository.modules.prescription.impl.mySQL;

import ma.teethcare.entities.Prescription;
import ma.teethcare.repository.modules.prescription.api.PrescriptionRepository;
import ma.teethcare.repository.common.RowMappers;
import ma.teethcare.conf.SessionFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PrescriptionRepositoryImpl implements PrescriptionRepository {

    // -------- CRUD --------
    @Override
    public List<Prescription> findAll() {
        String sql = """
            SELECT p.*, o.id as ordonnance_id, o.date as ordonnance_date,
                   m.id as medicament_id, m.nom as medicament_nom, m.prix as medicament_prix
            FROM prescriptions p
            LEFT JOIN ordonnances o ON p.ordonnance_id = o.id
            LEFT JOIN medicaments m ON p.medicament_id = m.id
            ORDER BY p.id
            """;
        List<Prescription> out = new ArrayList<>();
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) out.add(RowMappers.mapPrescription(rs));
        } catch (SQLException e) { throw new RuntimeException(e); }
        return out;
    }

    @Override
    public Prescription findById(Long id) {
        String sql = """
            SELECT p.*, o.id as ordonnance_id, o.date as ordonnance_date,
                   m.id as medicament_id, m.nom as medicament_nom, m.prix as medicament_prix
            FROM prescriptions p
            LEFT JOIN ordonnances o ON p.ordonnance_id = o.id
            LEFT JOIN medicaments m ON p.medicament_id = m.id
            WHERE p.id = ?
            """;
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return RowMappers.mapPrescription(rs);
                return null;
            }
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    @Override
    public void create(Prescription prescription) {
        String sql = """
            INSERT INTO prescriptions(ordonnance_id, medicament_id, quantite, posologie, duree_traitement, created_at)
            VALUES(?, ?, ?, ?, ?, ?)
            """;
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setLong(1, prescription.getOrdonnance().getId());
            ps.setLong(2, prescription.getMedicament().getId());
            ps.setInt(3, prescription.getQuantite());
            ps.setString(4, prescription.getPosologie());
            ps.setInt(5, prescription.getDureeTraitement());
            ps.setTimestamp(6, new Timestamp(System.currentTimeMillis()));

            ps.executeUpdate();

            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) prescription.setId(keys.getLong(1));
            }
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    @Override
    public void update(Prescription prescription) {
        String sql = """
            UPDATE prescriptions SET 
                ordonnance_id = ?, medicament_id = ?, quantite = ?, 
                posologie = ?, duree_traitement = ?, updated_at = ?
            WHERE id = ?
            """;
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setLong(1, prescription.getOrdonnance().getId());
            ps.setLong(2, prescription.getMedicament().getId());
            ps.setInt(3, prescription.getQuantite());
            ps.setString(4, prescription.getPosologie());
            ps.setInt(5, prescription.getDureeTraitement());
            ps.setTimestamp(6, new Timestamp(System.currentTimeMillis()));
            ps.setLong(7, prescription.getId());

            ps.executeUpdate();
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    @Override
    public void delete(Prescription prescription) {
        if (prescription != null) deleteById(prescription.getId());
    }

    @Override
    public void deleteById(Long id) {
        String sql = "DELETE FROM prescriptions WHERE id = ?";
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, id);
            ps.executeUpdate();
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    // -------- Méthodes spécifiques --------
    @Override
    public List<Prescription> findByOrdonnanceId(Long ordonnanceId) {
        String sql = """
            SELECT p.*, o.id as ordonnance_id, o.date as ordonnance_date,
                   m.id as medicament_id, m.nom as medicament_nom, m.prix as medicament_prix
            FROM prescriptions p
            LEFT JOIN ordonnances o ON p.ordonnance_id = o.id
            LEFT JOIN medicaments m ON p.medicament_id = m.id
            WHERE p.ordonnance_id = ?
            ORDER BY p.id
            """;
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
    public List<Prescription> findByMedicamentId(Long medicamentId) {
        String sql = """
            SELECT p.*, o.id as ordonnance_id, o.date as ordonnance_date,
                   m.id as medicament_id, m.nom as medicament_nom, m.prix as medicament_prix
            FROM prescriptions p
            LEFT JOIN ordonnances o ON p.ordonnance_id = o.id
            LEFT JOIN medicaments m ON p.medicament_id = m.id
            WHERE p.medicament_id = ?
            ORDER BY p.id
            """;
        List<Prescription> out = new ArrayList<>();
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, medicamentId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) out.add(RowMappers.mapPrescription(rs));
            }
        } catch (SQLException e) { throw new RuntimeException(e); }
        return out;
    }

    @Override
    public List<Prescription> findByDureeTraitementGreaterThan(Integer jours) {
        String sql = """
            SELECT p.*, o.id as ordonnance_id, o.date as ordonnance_date,
                   m.id as medicament_id, m.nom as medicament_nom, m.prix as medicament_prix
            FROM prescriptions p
            LEFT JOIN ordonnances o ON p.ordonnance_id = o.id
            LEFT JOIN medicaments m ON p.medicament_id = m.id
            WHERE p.duree_traitement > ?
            ORDER BY p.duree_traitement DESC
            """;
        List<Prescription> out = new ArrayList<>();
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, jours);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) out.add(RowMappers.mapPrescription(rs));
            }
        } catch (SQLException e) { throw new RuntimeException(e); }
        return out;
    }

    @Override
    public List<Prescription> findByDureeTraitementBetween(Integer minJours, Integer maxJours) {
        String sql = """
            SELECT p.*, o.id as ordonnance_id, o.date as ordonnance_date,
                   m.id as medicament_id, m.nom as medicament_nom, m.prix as medicament_prix
            FROM prescriptions p
            LEFT JOIN ordonnances o ON p.ordonnance_id = o.id
            LEFT JOIN medicaments m ON p.medicament_id = m.id
            WHERE p.duree_traitement BETWEEN ? AND ?
            ORDER BY p.duree_traitement
            """;
        List<Prescription> out = new ArrayList<>();
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, minJours);
            ps.setInt(2, maxJours);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) out.add(RowMappers.mapPrescription(rs));
            }
        } catch (SQLException e) { throw new RuntimeException(e); }
        return out;
    }

    @Override
    public List<Prescription> findByOrdonnanceAndMedicament(Long ordonnanceId, Long medicamentId) {
        String sql = """
            SELECT p.*, o.id as ordonnance_id, o.date as ordonnance_date,
                   m.id as medicament_id, m.nom as medicament_nom, m.prix as medicament_prix
            FROM prescriptions p
            LEFT JOIN ordonnances o ON p.ordonnance_id = o.id
            LEFT JOIN medicaments m ON p.medicament_id = m.id
            WHERE p.ordonnance_id = ? AND p.medicament_id = ?
            """;
        List<Prescription> out = new ArrayList<>();
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, ordonnanceId);
            ps.setLong(2, medicamentId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) out.add(RowMappers.mapPrescription(rs));
            }
        } catch (SQLException e) { throw new RuntimeException(e); }
        return out;
    }

    @Override
    public long countByOrdonnanceId(Long ordonnanceId) {
        String sql = "SELECT COUNT(*) FROM prescriptions WHERE ordonnance_id = ?";
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, ordonnanceId);
            try (ResultSet rs = ps.executeQuery()) {
                rs.next();
                return rs.getLong(1);
            }
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    @Override
    public long countByMedicamentId(Long medicamentId) {
        String sql = "SELECT COUNT(*) FROM prescriptions WHERE medicament_id = ?";
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, medicamentId);
            try (ResultSet rs = ps.executeQuery()) {
                rs.next();
                return rs.getLong(1);
            }
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    @Override
    public Double getCoutTotalByOrdonnance(Long ordonnanceId) {
        String sql = """
            SELECT SUM(p.quantite * m.prix) as total
            FROM prescriptions p
            JOIN medicaments m ON p.medicament_id = m.id
            WHERE p.ordonnance_id = ?
            """;
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, ordonnanceId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getDouble("total");
                }
            }
        } catch (SQLException e) { throw new RuntimeException(e); }
        return 0.0;
    }

    @Override
    public Double getCoutTotalByMedicament(Long medicamentId) {
        String sql = """
            SELECT SUM(p.quantite * m.prix) as total
            FROM prescriptions p
            JOIN medicaments m ON p.medicament_id = m.id
            WHERE p.medicament_id = ?
            """;
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, medicamentId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getDouble("total");
                }
            }
        } catch (SQLException e) { throw new RuntimeException(e); }
        return 0.0;
    }

    @Override
    public void deleteByOrdonnanceId(Long ordonnanceId) {
        String sql = "DELETE FROM prescriptions WHERE ordonnance_id = ?";
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, ordonnanceId);
            ps.executeUpdate();
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    @Override
    public void deleteByMedicamentId(Long medicamentId) {
        String sql = "DELETE FROM prescriptions WHERE medicament_id = ?";
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, medicamentId);
            ps.executeUpdate();
        } catch (SQLException e) { throw new RuntimeException(e); }
    }
}