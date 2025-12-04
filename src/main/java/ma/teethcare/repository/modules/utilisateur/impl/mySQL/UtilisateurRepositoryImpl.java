package ma.teethcare.repository.modules.utilisateur.impl.mySQL;

import ma.teethcare.conf.SessionFactory;
import ma.teethcare.entities.Utilisateur;
import ma.teethcare.repository.common.RowMappers;
import ma.teethcare.repository.modules.utilisateur.api.UtilisateurRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UtilisateurRepositoryImpl implements UtilisateurRepository {

    @Override
    public List<Utilisateur> findAll() {
        String sql = "SELECT * FROM Utilisateur ORDER BY nom";
        List<Utilisateur> out = new ArrayList<>();
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) out.add(RowMappers.mapUtilisateur(rs));
        } catch (SQLException e) { throw new RuntimeException(e); }
        return out;
    }

    @Override
    public Utilisateur findById(Long id) {
        String sql = "SELECT * FROM Utilisateur WHERE idUser = ?";
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return RowMappers.mapUtilisateur(rs);
                return null;
            }
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    @Override
    public void create(Utilisateur u) {
        String sql = """
            INSERT INTO Utilisateur(nom, email, adresse, cin, telephone, login, motDePass, sexe, 
            lastLoginDate, dateNaissance, salaire, prime, dateRecrutement, soldeConge, type, 
            specialite, numCNSS, commission, idCabinet)
            VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)
            """;
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, u.getNom());
            ps.setString(2, u.getEmail());
            ps.setString(3, u.getAdresse());
            ps.setString(4, u.getCin());
            ps.setString(5, u.getTelephone());
            ps.setString(6, u.getLogin());
            ps.setString(7, u.getMotDePass());
            ps.setString(8, u.getSexe().name());

            if (u.getLastLoginDate() != null) ps.setDate(9, Date.valueOf(u.getLastLoginDate()));
            else ps.setNull(9, Types.DATE);

            if (u.getDateNaissance() != null) ps.setDate(10, Date.valueOf(u.getDateNaissance()));
            else ps.setNull(10, Types.DATE);

            ps.setDouble(11, u.getSalaire());
            ps.setDouble(12, u.getPrime());

            if (u.getDateRecrutement() != null) ps.setDate(13, Date.valueOf(u.getDateRecrutement()));
            else ps.setNull(13, Types.DATE);

            ps.setInt(14, u.getSoldeConge());
            ps.setString(15, u.getType().name());
            ps.setString(16, u.getSpecialite());
            ps.setString(17, u.getNumCNSS());
            ps.setDouble(18, u.getCommission());

            if (u.getCabinetMedicale() != null) ps.setLong(19, u.getCabinetMedicale().getIdUser());
            else ps.setNull(19, Types.BIGINT);

            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) u.setIdUser(keys.getLong(1));
            }
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    @Override
    public void update(Utilisateur u) {
        String sql = """
            UPDATE Utilisateur SET nom=?, email=?, adresse=?, cin=?, telephone=?, login=?, 
            motDePass=?, sexe=?, lastLoginDate=?, dateNaissance=?, salaire=?, prime=?, 
            dateRecrutement=?, soldeConge=?, type=?, specialite=?, numCNSS=?, commission=?, 
            idCabinet=? WHERE idUser=?
            """;
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, u.getNom());
            ps.setString(2, u.getEmail());
            ps.setString(3, u.getAdresse());
            ps.setString(4, u.getCin());
            ps.setString(5, u.getTelephone());
            ps.setString(6, u.getLogin());
            ps.setString(7, u.getMotDePass());
            ps.setString(8, u.getSexe().name());

            if (u.getLastLoginDate() != null) ps.setDate(9, Date.valueOf(u.getLastLoginDate()));
            else ps.setNull(9, Types.DATE);

            if (u.getDateNaissance() != null) ps.setDate(10, Date.valueOf(u.getDateNaissance()));
            else ps.setNull(10, Types.DATE);

            ps.setDouble(11, u.getSalaire());
            ps.setDouble(12, u.getPrime());

            if (u.getDateRecrutement() != null) ps.setDate(13, Date.valueOf(u.getDateRecrutement()));
            else ps.setNull(13, Types.DATE);

            ps.setInt(14, u.getSoldeConge());
            ps.setString(15, u.getType().name());
            ps.setString(16, u.getSpecialite());
            ps.setString(17, u.getNumCNSS());
            ps.setDouble(18, u.getCommission());

            if (u.getCabinetMedicale() != null) ps.setLong(19, u.getCabinetMedicale().getIdUser());
            else ps.setNull(19, Types.BIGINT);

            ps.setLong(20, u.getIdUser());
            ps.executeUpdate();
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    @Override
    public void delete(Utilisateur u) {
        if (u != null) deleteById(u.getIdUser());
    }

    @Override
    public void deleteById(Long id) {
        String sql = "DELETE FROM Utilisateur WHERE idUser = ?";
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, id);
            ps.executeUpdate();
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    @Override
    public Optional<Utilisateur> findByEmail(String email) {
        String sql = "SELECT * FROM Utilisateur WHERE email = ?";
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return Optional.of(RowMappers.mapUtilisateur(rs));
                return Optional.empty();
            }
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    @Override
    public Optional<Utilisateur> findByLogin(String login) {
        String sql = "SELECT * FROM Utilisateur WHERE login = ?";
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, login);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return Optional.of(RowMappers.mapUtilisateur(rs));
                return Optional.empty();
            }
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    @Override
    public List<Utilisateur> findByType(String type) {
        String sql = "SELECT * FROM Utilisateur WHERE type = ? ORDER BY nom";
        List<Utilisateur> out = new ArrayList<>();
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, type);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) out.add(RowMappers.mapUtilisateur(rs));
            }
        } catch (SQLException e) { throw new RuntimeException(e); }
        return out;
    }

    @Override
    public List<Utilisateur> findByCabinetMedicaleId(Long cabinetId) {
        String sql = "SELECT * FROM Utilisateur WHERE idCabinet = ? ORDER BY nom";
        List<Utilisateur> out = new ArrayList<>();
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, cabinetId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) out.add(RowMappers.mapUtilisateur(rs));
            }
        } catch (SQLException e) { throw new RuntimeException(e); }
        return out;
    }

    @Override
    public List<Utilisateur> searchByNom(String keyword) {
        String sql = "SELECT * FROM Utilisateur WHERE nom LIKE ? OR email LIKE ? ORDER BY nom";
        List<Utilisateur> out = new ArrayList<>();
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            String like = "%" + keyword + "%";
            ps.setString(1, like);
            ps.setString(2, like);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) out.add(RowMappers.mapUtilisateur(rs));
            }
        } catch (SQLException e) { throw new RuntimeException(e); }
        return out;
    }

    @Override
    public boolean existsByLogin(String login) {
        String sql = "SELECT 1 FROM Utilisateur WHERE login = ?";
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, login);
            try (ResultSet rs = ps.executeQuery()) { return rs.next(); }
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    @Override
    public boolean existsByEmail(String email) {
        String sql = "SELECT 1 FROM Utilisateur WHERE email = ?";
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) { return rs.next(); }
        } catch (SQLException e) { throw new RuntimeException(e); }
    }
}