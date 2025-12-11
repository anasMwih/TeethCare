package ma.teethcare.repository.modules.dossier.impl.mySQL;

import ma.teethcare.entities.Certificat;
import ma.teethcare.entities.Consultation;
import ma.teethcare.entities.DossierMedical;
import ma.teethcare.entities.Ordonnance;
import ma.teethcare.entities.Patient;
import ma.teethcare.conf.SessionFactory;
import ma.teethcare.repository.common.RowMappers;
import ma.teethcare.repository.modules.dossier.api.DossierMedicalRepository;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DossierMedicalRepositoryImpl implements DossierMedicalRepository {

    // -------- CRUD --------
    @Override
    public List<DossierMedical> findAll() {
        String sql = "SELECT d.*, p.* FROM DossierMedical d " +
                "JOIN Patient p ON d.patient_id = p.id " +
                "ORDER BY d.dateCreation DESC";
        List<DossierMedical> out = new ArrayList<>();
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                DossierMedical dossier = RowMappers.mapDossierMedical(rs);
                dossier.setPatient(RowMappers.mapPatient(rs));
                out.add(dossier);
            }
        } catch (SQLException e) { throw new RuntimeException(e); }
        return out;
    }

    @Override
    public DossierMedical findById(Long id) {
        String sql = "SELECT d.*, p.* FROM DossierMedical d " +
                "JOIN Patient p ON d.patient_id = p.id " +
                "WHERE d.id = ?";
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    DossierMedical dossier = RowMappers.mapDossierMedical(rs);
                    dossier.setPatient(RowMappers.mapPatient(rs));
                    return dossier;
                }
                return null;
            }
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    @Override
    public void create(DossierMedical d) {
        String sql = "INSERT INTO DossierMedical(dateCreation, observations, patient_id) VALUES(?,?,?)";
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setDate(1, Date.valueOf(d.getDateCreation()));
            ps.setString(2, d.getObservations());
            ps.setLong(3, d.getPatient().getId());
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) d.setId(keys.getLong(1));
            }
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    @Override
    public void update(DossierMedical d) {
        String sql = "UPDATE DossierMedical SET dateCreation=?, observations=?, patient_id=? WHERE id=?";
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setDate(1, Date.valueOf(d.getDateCreation()));
            ps.setString(2, d.getObservations());
            ps.setLong(3, d.getPatient().getId());
            ps.setLong(4, d.getId());
            ps.executeUpdate();
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    @Override
    public void delete(DossierMedical d) {
        if (d != null) deleteById(d.getId());
    }

    @Override
    public void deleteById(Long id) {
        String sql = "DELETE FROM DossierMedical WHERE id = ?";
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, id);
            ps.executeUpdate();
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    // -------- Recherches spécifiques --------
    @Override
    public Optional<DossierMedical> findByPatient(Patient patient) {
        return findByPatientId(patient.getId());
    }

    @Override
    public Optional<DossierMedical> findByPatientId(Long patientId) {
        String sql = "SELECT d.*, p.* FROM DossierMedical d " +
                "JOIN Patient p ON d.patient_id = p.id " +
                "WHERE d.patient_id = ?";
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, patientId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    DossierMedical dossier = RowMappers.mapDossierMedical(rs);
                    dossier.setPatient(RowMappers.mapPatient(rs));
                    return Optional.of(dossier);
                }
                return Optional.empty();
            }
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    @Override
    public List<DossierMedical> findByDateCreation(LocalDate date) {
        String sql = "SELECT d.*, p.* FROM DossierMedical d " +
                "JOIN Patient p ON d.patient_id = p.id " +
                "WHERE DATE(d.dateCreation) = ? " +
                "ORDER BY d.dateCreation DESC";
        List<DossierMedical> out = new ArrayList<>();
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setDate(1, Date.valueOf(date));
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    DossierMedical dossier = RowMappers.mapDossierMedical(rs);
                    dossier.setPatient(RowMappers.mapPatient(rs));
                    out.add(dossier);
                }
            }
        } catch (SQLException e) { throw new RuntimeException(e); }
        return out;
    }

    @Override
    public List<DossierMedical> findByDateCreationBetween(LocalDate start, LocalDate end) {
        String sql = "SELECT d.*, p.* FROM DossierMedical d " +
                "JOIN Patient p ON d.patient_id = p.id " +
                "WHERE d.dateCreation BETWEEN ? AND ? " +
                "ORDER BY d.dateCreation DESC";
        List<DossierMedical> out = new ArrayList<>();
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setDate(1, Date.valueOf(start));
            ps.setDate(2, Date.valueOf(end));
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    DossierMedical dossier = RowMappers.mapDossierMedical(rs);
                    dossier.setPatient(RowMappers.mapPatient(rs));
                    out.add(dossier);
                }
            }
        } catch (SQLException e) { throw new RuntimeException(e); }
        return out;
    }

    @Override
    public List<DossierMedical> findByObservationsContaining(String keyword) {
        String sql = "SELECT d.*, p.* FROM DossierMedical d " +
                "JOIN Patient p ON d.patient_id = p.id " +
                "WHERE d.observations LIKE ? " +
                "ORDER BY d.dateCreation DESC";
        List<DossierMedical> out = new ArrayList<>();
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, "%" + keyword + "%");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    DossierMedical dossier = RowMappers.mapDossierMedical(rs);
                    dossier.setPatient(RowMappers.mapPatient(rs));
                    out.add(dossier);
                }
            }
        } catch (SQLException e) { throw new RuntimeException(e); }
        return out;
    }

    @Override
    public boolean existsById(Long id) {
        String sql = "SELECT 1 FROM DossierMedical WHERE id = ?";
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    @Override
    public boolean existsByPatientId(Long patientId) {
        String sql = "SELECT 1 FROM DossierMedical WHERE patient_id = ?";
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, patientId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    @Override
    public long count() {
        String sql = "SELECT COUNT(*) FROM DossierMedical";
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            rs.next();
            return rs.getLong(1);
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    @Override
    public List<DossierMedical> findPage(int limit, int offset) {
        String sql = "SELECT d.*, p.* FROM DossierMedical d " +
                "JOIN Patient p ON d.patient_id = p.id " +
                "ORDER BY d.dateCreation DESC LIMIT ? OFFSET ?";
        List<DossierMedical> out = new ArrayList<>();
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, limit);
            ps.setInt(2, offset);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    DossierMedical dossier = RowMappers.mapDossierMedical(rs);
                    dossier.setPatient(RowMappers.mapPatient(rs));
                    out.add(dossier);
                }
            }
        } catch (SQLException e) { throw new RuntimeException(e); }
        return out;
    }

    // -------- Relations avec Consultation --------
    @Override
    public List<Consultation> getConsultationsOfDossier(Long dossierId) {
        String sql = "SELECT c.* FROM Consultation c " +
                "WHERE c.dossier_id = ? " +
                "ORDER BY c.dateConsultation DESC";
        List<Consultation> out = new ArrayList<>();
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, dossierId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) out.add(RowMappers.mapConsultation(rs));
            }
        } catch (SQLException e) { throw new RuntimeException(e); }
        return out;
    }

    @Override
    public void addConsultationToDossier(Long dossierId, Long consultationId) {
        String sql = "UPDATE Consultation SET dossier_id = ? WHERE id = ?";
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, dossierId);
            ps.setLong(2, consultationId);
            ps.executeUpdate();
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    @Override
    public void removeConsultationFromDossier(Long dossierId, Long consultationId) {
        String sql = "UPDATE Consultation SET dossier_id = NULL WHERE id = ? AND dossier_id = ?";
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, consultationId);
            ps.setLong(2, dossierId);
            ps.executeUpdate();
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    // -------- Relations avec Ordonnance --------
    @Override
    public List<Ordonnance> getOrdonnancesOfDossier(Long dossierId) {
        String sql = "SELECT o.* FROM Ordonnance o " +
                "WHERE o.dossier_id = ? " +
                "ORDER BY o.dateOrdonnance DESC";
        List<Ordonnance> out = new ArrayList<>();
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, dossierId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) out.add(RowMappers.mapOrdonnance(rs));
            }
        } catch (SQLException e) { throw new RuntimeException(e); }
        return out;
    }

    @Override
    public void addOrdonnanceToDossier(Long dossierId, Long ordonnanceId) {
        String sql = "UPDATE Ordonnance SET dossier_id = ? WHERE id = ?";
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, dossierId);
            ps.setLong(2, ordonnanceId);
            ps.executeUpdate();
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    @Override
    public void removeOrdonnanceFromDossier(Long dossierId, Long ordonnanceId) {
        String sql = "UPDATE Ordonnance SET dossier_id = NULL WHERE id = ? AND dossier_id = ?";
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, ordonnanceId);
            ps.setLong(2, dossierId);
            ps.executeUpdate();
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    // -------- Relations avec Certificat --------
    @Override
    public List<Certificat> getCertificatsOfDossier(Long dossierId) {
        String sql = "SELECT cert.* FROM Certificat cert " +
                "WHERE cert.dossier_id = ? " +
                "ORDER BY cert.dateCertificat DESC";
        List<Certificat> out = new ArrayList<>();
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, dossierId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) out.add(RowMappers.mapCertificat(rs));
            }
        } catch (SQLException e) { throw new RuntimeException(e); }
        return out;
    }

    @Override
    public void addCertificatToDossier(Long dossierId, Long certificatId) {
        String sql = "UPDATE Certificat SET dossier_id = ? WHERE id = ?";
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, dossierId);
            ps.setLong(2, certificatId);
            ps.executeUpdate();
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    @Override
    public void removeCertificatFromDossier(Long dossierId, Long certificatId) {
        String sql = "UPDATE Certificat SET dossier_id = NULL WHERE id = ? AND dossier_id = ?";
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, certificatId);
            ps.setLong(2, dossierId);
            ps.executeUpdate();
        } catch (SQLException e) { throw new RuntimeException(e); }
    }
}