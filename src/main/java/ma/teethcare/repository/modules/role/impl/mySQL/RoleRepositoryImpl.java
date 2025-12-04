package ma.teethcare.repository.modules.role.impl.mySQL;

import ma.teethcare.conf.SessionFactory;
import ma.teethcare.entities.Role;
import ma.teethcare.entities.Utilisateur;
import ma.teethcare.repository.common.RowMappers;
import ma.teethcare.repository.modules.role.api.RoleRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RoleRepositoryImpl implements RoleRepository {

    @Override
    public List<Role> findAll() {
        String sql = "SELECT * FROM Role ORDER BY libelle";
        List<Role> out = new ArrayList<>();
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) out.add(RowMappers.mapRole(rs));
        } catch (SQLException e) { throw new RuntimeException(e); }
        return out;
    }

    @Override
    public Role findById(Long id) {
        String sql = "SELECT * FROM Role WHERE idRole = ?";
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return RowMappers.mapRole(rs);
                return null;
            }
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    @Override
    public void create(Role role) {
        String sql = "INSERT INTO Role(libelle, privileges) VALUES(?,?)";
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, role.getLibelle());
            String privilegesStr = String.join(",", role.getPrivileges());
            ps.setString(2, privilegesStr);

            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) role.setIdRole(keys.getLong(1));
            }
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    @Override
    public void update(Role role) {
        String sql = "UPDATE Role SET libelle=?, privileges=? WHERE idRole=?";
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, role.getLibelle());
            String privilegesStr = String.join(",", role.getPrivileges());
            ps.setString(2, privilegesStr);
            ps.setLong(3, role.getIdRole());

            ps.executeUpdate();
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    @Override
    public void delete(Role role) {
        if (role != null) deleteById(role.getIdRole());
    }

    @Override
    public void deleteById(Long id) {
        // Supprimer d'abord les associations dans Utilisateur_Role
        String deleteAssocSql = "DELETE FROM Utilisateur_Role WHERE idRole = ?";
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(deleteAssocSql)) {
            ps.setLong(1, id);
            ps.executeUpdate();
        } catch (SQLException e) { throw new RuntimeException(e); }

        // Supprimer le rôle
        String sql = "DELETE FROM Role WHERE idRole = ?";
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, id);
            ps.executeUpdate();
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    @Override
    public Role findByLibelle(String libelle) {
        String sql = "SELECT * FROM Role WHERE libelle = ?";
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, libelle);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return RowMappers.mapRole(rs);
                return null;
            }
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    @Override
    public List<Utilisateur> getUtilisateursByRole(Long roleId) {
        String sql = """
            SELECT u.* FROM Utilisateur u
            JOIN Utilisateur_Role ur ON u.idUser = ur.idUtilisateur
            WHERE ur.idRole = ?
            ORDER BY u.nom
            """;
        List<Utilisateur> out = new ArrayList<>();
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, roleId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) out.add(RowMappers.mapUtilisateur(rs));
            }
        } catch (SQLException e) { throw new RuntimeException(e); }
        return out;
    }

    @Override
    public void addPrivilegeToRole(Long roleId, String privilege) {
        Role role = findById(roleId);
        if (role != null) {
            List<String> privileges = role.getPrivileges();
            if (!privileges.contains(privilege)) {
                privileges.add(privilege);
                update(role);
            }
        }
    }

    @Override
    public void removePrivilegeFromRole(Long roleId, String privilege) {
        Role role = findById(roleId);
        if (role != null) {
            List<String> privileges = role.getPrivileges();
            privileges.remove(privilege);
            update(role);
        }
    }

    @Override
    public List<String> getPrivilegesByRole(Long roleId) {
        Role role = findById(roleId);
        return role != null ? role.getPrivileges() : new ArrayList<>();
    }
}