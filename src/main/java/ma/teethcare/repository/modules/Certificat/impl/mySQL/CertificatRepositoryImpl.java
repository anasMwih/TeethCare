package ma.teethcare.repository.modules.certificat.impl.mySQL;

import ma.teethcare.entities.Certificat;
import ma.teethcare.repository.modules.certificat.api.CertificatRepository;
import ma.teethcare.repository.common.RowMappers;
import ma.teethcare.conf.SessionFactory;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class CertificatRepositoryImpl implements CertificatRepository {

    // -------- CRUD --------
    @Override
    public List<Certificat> findAll() {
        String sql = """
            SELECT c.*, dm.id as dossier_id, dm.date_creation as dossier_date,
                   cons.id as consultation_id, cons.date as consultation_date
            FROM certificats c
            LEFT JOIN dossiers_medicaux dm ON c.dossier_medical_id = dm.id
            LEFT JOIN consultations cons ON c.consultation_id = cons.id
            ORDER BY c.date_emission DESC
            """;
        List<Certificat> out = new ArrayList<>();
        try (Connection conn = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) out.add(RowMappers.mapCertificat(rs));
        } catch (SQLException e) { throw new RuntimeException(e); }
        return out;
    }

    @Override
    public Certificat findById(Long id) {
        String sql = """
            SELECT c.*, dm.id as dossier_id, dm.date_creation as dossier_date,
                   cons.id as consultation_id, cons.date as consultation_date
            FROM certificats c
            LEFT JOIN dossiers_medicaux dm ON c.dossier_medical_id = dm.id
            LEFT JOIN consultations cons ON c.consultation_id = cons.id
            WHERE c.id = ?
            """;
        try (Connection conn = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return RowMappers.mapCertificat(rs);
                return null;
            }
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    @Override
    public void create(Certificat certificat) {
        String sql = """
            INSERT INTO certificats(dossier_medical_id, consultation_id, type, 
                                  date_emission, date_expiration, description, created_at)
            VALUES(?, ?, ?, ?, ?, ?, ?)
            """;
        try (Connection conn = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setLong(1, certificat.getDossierMedical().getId());
            ps.setLong(2, certificat.getConsultation().getId());
            ps.setString(3, certificat.getType());
            ps.setDate(4, Date.valueOf(certificat.getDateEmission()));
            ps.setDate(5, certificat.getDateExpiration() != null ?
                    Date.valueOf(certificat.getDateExpiration()) : null);
            ps.setString(6, certificat.getDescription());
            ps.setTimestamp(7, new Timestamp(System.currentTimeMillis()));

            ps.executeUpdate();

            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) certificat.setId(keys.getLong(1));
            }
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    @Override
    public void update(Certificat certificat) {
        String sql = """
            UPDATE certificats SET 
                dossier_medical_id = ?, consultation_id = ?, type = ?, 
                date_emission = ?, date_expiration = ?, description = ?, updated_at = ?
            WHERE id = ?
            """;
        try (Connection conn = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, certificat.getDossierMedical().getId());
            ps.setLong(2, certificat.getConsultation().getId());
            ps.setString(3, certificat.getType());
            ps.setDate(4, Date.valueOf(certificat.getDateEmission()));
            ps.setDate(5, certificat.getDateExpiration() != null ?
                    Date.valueOf(certificat.getDateExpiration()) : null);
            ps.setString(6, certificat.getDescription());
            ps.setTimestamp(7, new Timestamp(System.currentTimeMillis()));
            ps.setLong(8, certificat.getId());

            ps.executeUpdate();
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    @Override
    public void delete(Certificat certificat) {
        if (certificat != null) deleteById(certificat.getId());
    }

    @Override
    public void deleteById(Long id) {
        String sql = "DELETE FROM certificats WHERE id = ?";
        try (Connection conn = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            ps.executeUpdate();
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    // -------- Méthodes spécifiques --------
    @Override
    public List<Certificat> findByDossierMedicalId(Long dossierMedicalId) {
        String sql = """
            SELECT c.*, dm.id as dossier_id, dm.date_creation as dossier_date,
                   cons.id as consultation_id, cons.date as consultation_date
            FROM certificats c
            LEFT JOIN dossiers_medicaux dm ON c.dossier_medical_id = dm.id
            LEFT JOIN consultations cons ON c.consultation_id = cons.id
            WHERE c.dossier_medical_id = ?
            ORDER BY c.date_emission DESC
            """;
        List<Certificat> out = new ArrayList<>();
        try (Connection conn = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, dossierMedicalId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) out.add(RowMappers.mapCertificat(rs));
            }
        } catch (SQLException e) { throw new RuntimeException(e); }
        return out;
    }

    @Override
    public List<Certificat> findByConsultationId(Long consultationId) {
        String sql = """
            SELECT c.*, dm.id as dossier_id, dm.date_creation as dossier_date,
                   cons.id as consultation_id, cons.date as consultation_date
            FROM certificats c
            LEFT JOIN dossiers_medicaux dm ON c.dossier_medical_id = dm.id
            LEFT JOIN consultations cons ON c.consultation_id = cons.id
            WHERE c.consultation_id = ?
            ORDER BY c.date_emission DESC
            """;
        List<Certificat> out = new ArrayList<>();
        try (Connection conn = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, consultationId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) out.add(RowMappers.mapCertificat(rs));
            }
        } catch (SQLException e) { throw new RuntimeException(e); }
        return out;
    }

    @Override
    public List<Certificat> findByType(String type) {
        String sql = """
            SELECT c.*, dm.id as dossier_id, dm.date_creation as dossier_date,
                   cons.id as consultation_id, cons.date as consultation_date
            FROM certificats c
            LEFT JOIN dossiers_medicaux dm ON c.dossier_medical_id = dm.id
            LEFT JOIN consultations cons ON c.consultation_id = cons.id
            WHERE c.type = ?
            ORDER BY c.date_emission DESC
            """;
        List<Certificat> out = new ArrayList<>();
        try (Connection conn = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, type);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) out.add(RowMappers.mapCertificat(rs));
            }
        } catch (SQLException e) { throw new RuntimeException(e); }
        return out;
    }

    @Override
    public List<String> findAllTypes() {
        String sql = "SELECT DISTINCT type FROM certificats ORDER BY type";
        List<String> out = new ArrayList<>();
        try (Connection conn = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) out.add(rs.getString("type"));
        } catch (SQLException e) { throw new RuntimeException(e); }
        return out;
    }

    @Override
    public List<Certificat> findByDateEmission(LocalDate date) {
        String sql = """
            SELECT c.*, dm.id as dossier_id, dm.date_creation as dossier_date,
                   cons.id as consultation_id, cons.date as consultation_date
            FROM certificats c
            LEFT JOIN dossiers_medicaux dm ON c.dossier_medical_id = dm.id
            LEFT JOIN consultations cons ON c.consultation_id = cons.id
            WHERE DATE(c.date_emission) = ?
            ORDER BY c.date_emission
            """;
        List<Certificat> out = new ArrayList<>();
        try (Connection conn = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDate(1, Date.valueOf(date));
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) out.add(RowMappers.mapCertificat(rs));
            }
        } catch (SQLException e) { throw new RuntimeException(e); }
        return out;
    }

    @Override
    public List<Certificat> findByDateEmissionBetween(LocalDate start, LocalDate end) {
        String sql = """
            SELECT c.*, dm.id as dossier_id, dm.date_creation as dossier_date,
                   cons.id as consultation_id, cons.date as consultation_date
            FROM certificats c
            LEFT JOIN dossiers_medicaux dm ON c.dossier_medical_id = dm.id
            LEFT JOIN consultations cons ON c.consultation_id = cons.id
            WHERE c.date_emission BETWEEN ? AND ?
            ORDER BY c.date_emission
            """;
        List<Certificat> out = new ArrayList<>();
        try (Connection conn = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDate(1, Date.valueOf(start));
            ps.setDate(2, Date.valueOf(end));
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) out.add(RowMappers.mapCertificat(rs));
            }
        } catch (SQLException e) { throw new RuntimeException(e); }
        return out;
    }

    @Override
    public List<Certificat> findCertificatsExpires() {
        String sql = """
            SELECT c.*, dm.id as dossier_id, dm.date_creation as dossier_date,
                   cons.id as consultation_id, cons.date as consultation_date
            FROM certificats c
            LEFT JOIN dossiers_medicaux dm ON c.dossier_medical_id = dm.id
            LEFT JOIN consultations cons ON c.consultation_id = cons.id
            WHERE c.date_expiration < CURDATE()
            ORDER BY c.date_expiration DESC
            """;
        List<Certificat> out = new ArrayList<>();
        try (Connection conn = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) out.add(RowMappers.mapCertificat(rs));
        } catch (SQLException e) { throw new RuntimeException(e); }
        return out;
    }

    @Override
    public List<Certificat> findCertificatsValides() {
        String sql = """
            SELECT c.*, dm.id as dossier_id, dm.date_creation as dossier_date,
                   cons.id as consultation_id, cons.date as consultation_date
            FROM certificats c
            LEFT JOIN dossiers_medicaux dm ON c.dossier_medical_id = dm.id
            LEFT JOIN consultations cons ON c.consultation_id = cons.id
            WHERE c.date_expiration >= CURDATE()
            ORDER BY c.date_expiration
            """;
        List<Certificat> out = new ArrayList<>();
        try (Connection conn = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) out.add(RowMappers.mapCertificat(rs));
        } catch (SQLException e) { throw new RuntimeException(e); }
        return out;
    }

    @Override
    public long countByType(String type) {
        String sql = "SELECT COUNT(*) FROM certificats WHERE type = ?";
        try (Connection conn = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, type);
            try (ResultSet rs = ps.executeQuery()) {
                rs.next();
                return rs.getLong(1);
            }
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    @Override
    public long countByDossierMedicalId(Long dossierMedicalId) {
        String sql = "SELECT COUNT(*) FROM certificats WHERE dossier_medical_id = ?";
        try (Connection conn = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, dossierMedicalId);
            try (ResultSet rs = ps.executeQuery()) {
                rs.next();
                return rs.getLong(1);
            }
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    @Override
    public long countCertificatsExpires() {
        String sql = "SELECT COUNT(*) FROM certificats WHERE date_expiration < CURDATE()";
        try (Connection conn = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            rs.next();
            return rs.getLong(1);
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    @Override
    public long countCertificatsValides() {
        String sql = "SELECT COUNT(*) FROM certificats WHERE date_expiration >= CURDATE()";
        try (Connection conn = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            rs.next();
            return rs.getLong(1);
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    @Override
    public boolean isCertificatValide(Long certificatId) {
        String sql = "SELECT date_expiration FROM certificats WHERE id = ?";
        try (Connection conn = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, certificatId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Date expiration = rs.getDate("date_expiration");
                    if (expiration == null) return false;
                    return !expiration.toLocalDate().isBefore(LocalDate.now());
                }
            }
        } catch (SQLException e) { throw new RuntimeException(e); }
        return false;
    }

    @Override
    public void deleteByDossierMedicalId(Long dossierMedicalId) {
        String sql = "DELETE FROM certificats WHERE dossier_medical_id = ?";
        try (Connection conn = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, dossierMedicalId);
            ps.executeUpdate();
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    @Override
    public void deleteByConsultationId(Long consultationId) {
        String sql = "DELETE FROM certificats WHERE consultation_id = ?";
        try (Connection conn = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, consultationId);
            ps.executeUpdate();
        } catch (SQLException e) { throw new RuntimeException(e); }
    }
}