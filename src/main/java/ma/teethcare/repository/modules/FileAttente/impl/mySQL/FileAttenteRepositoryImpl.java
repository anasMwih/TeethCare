package ma.teethcare.repository.modules.fileattente.impl.mySQL;

import ma.teethcare.entities.FileAttente;
import ma.teethcare.repository.modules.fileattente.api.FileAttenteRepository;
import ma.teethcare.repository.common.RowMappers;
import ma.teethcare.conf.SessionFactory;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class FileAttenteRepositoryImpl implements FileAttenteRepository {

    // -------- CRUD --------
    @Override
    public List<FileAttente> findAll() {
        String sql = """
            SELECT f.*, p.id as patient_id, p.first_name, p.last_name
            FROM file_attente f
            LEFT JOIN patients p ON f.patient_id = p.id
            ORDER BY f.priorite DESC, f.date_arrivee
            """;
        List<FileAttente> out = new ArrayList<>();
        try (Connection conn = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) out.add(RowMappers.mapFileAttente(rs));
        } catch (SQLException e) { throw new RuntimeException(e); }
        return out;
    }

    @Override
    public FileAttente findById(Long id) {
        String sql = """
            SELECT f.*, p.id as patient_id, p.first_name, p.last_name
            FROM file_attente f
            LEFT JOIN patients p ON f.patient_id = p.id
            WHERE f.id = ?
            """;
        try (Connection conn = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return RowMappers.mapFileAttente(rs);
                return null;
            }
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    @Override
    public void create(FileAttente fileAttente) {
        String sql = """
            INSERT INTO file_attente(patient_id, date_arrivee, position, statut, priorite, created_at)
            VALUES(?, ?, ?, ?, ?, ?)
            """;
        try (Connection conn = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setLong(1, fileAttente.getPatient().getId());
            ps.setTimestamp(2, Timestamp.valueOf(fileAttente.getDateArrivee()));
            ps.setInt(3, getPositionSuivante());
            ps.setString(4, fileAttente.getStatut());
            ps.setString(5, fileAttente.getPriorite());
            ps.setTimestamp(6, new Timestamp(System.currentTimeMillis()));

            ps.executeUpdate();

            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) fileAttente.setId(keys.getLong(1));
            }

            updatePositions();
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    @Override
    public void update(FileAttente fileAttente) {
        String sql = """
            UPDATE file_attente SET 
                patient_id = ?, date_arrivee = ?, position = ?, 
                statut = ?, priorite = ?, updated_at = ?
            WHERE id = ?
            """;
        try (Connection conn = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, fileAttente.getPatient().getId());
            ps.setTimestamp(2, Timestamp.valueOf(fileAttente.getDateArrivee()));
            ps.setInt(3, fileAttente.getPosition());
            ps.setString(4, fileAttente.getStatut());
            ps.setString(5, fileAttente.getPriorite());
            ps.setTimestamp(6, new Timestamp(System.currentTimeMillis()));
            ps.setLong(7, fileAttente.getId());

            ps.executeUpdate();

            if ("EN_ATTENTE".equals(fileAttente.getStatut())) {
                updatePositions();
            }
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    @Override
    public void delete(FileAttente fileAttente) {
        if (fileAttente != null) deleteById(fileAttente.getId());
    }

    @Override
    public void deleteById(Long id) {
        String sql = "DELETE FROM file_attente WHERE id = ?";
        try (Connection conn = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            ps.executeUpdate();
            updatePositions();
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    // -------- Méthodes spécifiques --------
    @Override
    public Optional<FileAttente> findByPatientId(Long patientId) {
        String sql = """
            SELECT f.*, p.id as patient_id, p.first_name, p.last_name
            FROM file_attente f
            LEFT JOIN patients p ON f.patient_id = p.id
            WHERE f.patient_id = ? AND f.statut IN ('EN_ATTENTE', 'EN_COURS')
            """;
        try (Connection conn = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, patientId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return Optional.of(RowMappers.mapFileAttente(rs));
                return Optional.empty();
            }
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    @Override
    public List<FileAttente> findByStatut(String statut) {
        String sql = """
            SELECT f.*, p.id as patient_id, p.first_name, p.last_name
            FROM file_attente f
            LEFT JOIN patients p ON f.patient_id = p.id
            WHERE f.statut = ?
            ORDER BY f.priorite DESC, f.date_arrivee
            """;
        List<FileAttente> out = new ArrayList<>();
        try (Connection conn = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, statut);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) out.add(RowMappers.mapFileAttente(rs));
            }
        } catch (SQLException e) { throw new RuntimeException(e); }
        return out;
    }

    @Override
    public List<FileAttente> findEnAttente() {
        return findByStatut("EN_ATTENTE");
    }

    @Override
    public List<FileAttente> findEnCours() {
        return findByStatut("EN_COURS");
    }

    @Override
    public List<FileAttente> findByPriorite(String priorite) {
        String sql = """
            SELECT f.*, p.id as patient_id, p.first_name, p.last_name
            FROM file_attente f
            LEFT JOIN patients p ON f.patient_id = p.id
            WHERE f.priorite = ?
            ORDER BY f.date_arrivee
            """;
        List<FileAttente> out = new ArrayList<>();
        try (Connection conn = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, priorite);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) out.add(RowMappers.mapFileAttente(rs));
            }
        } catch (SQLException e) { throw new RuntimeException(e); }
        return out;
    }

    @Override
    public List<FileAttente> findByDate(LocalDate date) {
        String sql = """
            SELECT f.*, p.id as patient_id, p.first_name, p.last_name
            FROM file_attente f
            LEFT JOIN patients p ON f.patient_id = p.id
            WHERE DATE(f.date_arrivee) = ?
            ORDER BY f.date_arrivee
            """;
        List<FileAttente> out = new ArrayList<>();
        try (Connection conn = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDate(1, Date.valueOf(date));
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) out.add(RowMappers.mapFileAttente(rs));
            }
        } catch (SQLException e) { throw new RuntimeException(e); }
        return out;
    }

    @Override
    public FileAttente getProchainPatient() {
        String sql = """
            SELECT f.*, p.id as patient_id, p.first_name, p.last_name
            FROM file_attente f
            LEFT JOIN patients p ON f.patient_id = p.id
            WHERE f.statut = 'EN_ATTENTE' AND f.priorite = 'NORMAL'
            ORDER BY f.position
            LIMIT 1
            """;
        try (Connection conn = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return RowMappers.mapFileAttente(rs);
                return null;
            }
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    @Override
    public FileAttente getProchainPatientUrgent() {
        String sql = """
            SELECT f.*, p.id as patient_id, p.first_name, p.last_name
            FROM file_attente f
            LEFT JOIN patients p ON f.patient_id = p.id
            WHERE f.statut = 'EN_ATTENTE' AND f.priorite = 'URGENT'
            ORDER BY f.position
            LIMIT 1
            """;
        try (Connection conn = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return RowMappers.mapFileAttente(rs);
                return null;
            }
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    @Override
    public void updatePositions() {
        // Mettre à jour les positions des patients en attente
        String updateSql = """
            UPDATE file_attente f
            JOIN (
                SELECT id, 
                       ROW_NUMBER() OVER (ORDER BY priorite DESC, date_arrivee) as new_position
                FROM file_attente 
                WHERE statut = 'EN_ATTENTE'
            ) as temp ON f.id = temp.id
            SET f.position = temp.new_position
            WHERE f.statut = 'EN_ATTENTE'
            """;

        try (Connection conn = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(updateSql)) {
            ps.executeUpdate();
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    @Override
    public int getPositionSuivante() {
        String sql = "SELECT COUNT(*) + 1 FROM file_attente WHERE statut = 'EN_ATTENTE'";
        try (Connection conn = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) { throw new RuntimeException(e); }
        return 1;
    }

    @Override
    public int getNombrePatientsEnAttente() {
        String sql = "SELECT COUNT(*) FROM file_attente WHERE statut = 'EN_ATTENTE'";
        try (Connection conn = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) { throw new RuntimeException(e); }
        return 0;
    }

    @Override
    public boolean prendreEnCharge(Long fileAttenteId, Long medecinId) {
        String sql = "UPDATE file_attente SET statut = 'EN_COURS', updated_at = NOW() WHERE id = ? AND statut = 'EN_ATTENTE'";
        try (Connection conn = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, fileAttenteId);
            int rows = ps.executeUpdate();

            if (rows > 0) {
                updatePositions();
                return true;
            }
        } catch (SQLException e) { throw new RuntimeException(e); }
        return false;
    }

    @Override
    public boolean terminer(Long fileAttenteId) {
        String sql = "UPDATE file_attente SET statut = 'TERMINE', position = 0, updated_at = NOW() WHERE id = ? AND statut IN ('EN_ATTENTE', 'EN_COURS')";
        try (Connection conn = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, fileAttenteId);
            int rows = ps.executeUpdate();

            if (rows > 0) {
                updatePositions();
                return true;
            }
        } catch (SQLException e) { throw new RuntimeException(e); }
        return false;
    }

    @Override
    public boolean reordonner(Long fileAttenteId, Integer nouvellePosition) {
        // Vérifier si la position est valide
        int maxPosition = getNombrePatientsEnAttente();
        if (nouvellePosition < 1 || nouvellePosition > maxPosition) {
            return false;
        }

        String sql = "UPDATE file_attente SET position = ? WHERE id = ? AND statut = 'EN_ATTENTE'";
        try (Connection conn = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, nouvellePosition);
            ps.setLong(2, fileAttenteId);
            int rows = ps.executeUpdate();

            if (rows > 0) {
                updatePositions();
                return true;
            }
        } catch (SQLException e) { throw new RuntimeException(e); }
        return false;
    }

    @Override
    public long countByStatut(String statut) {
        String sql = "SELECT COUNT(*) FROM file_attente WHERE statut = ?";
        try (Connection conn = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, statut);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getLong(1);
                }
            }
        } catch (SQLException e) { throw new RuntimeException(e); }
        return 0;
    }

    @Override
    public long countByPriorite(String priorite) {
        String sql = "SELECT COUNT(*) FROM file_attente WHERE priorite = ?";
        try (Connection conn = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, priorite);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getLong(1);
                }
            }
        } catch (SQLException e) { throw new RuntimeException(e); }
        return 0;
    }
}