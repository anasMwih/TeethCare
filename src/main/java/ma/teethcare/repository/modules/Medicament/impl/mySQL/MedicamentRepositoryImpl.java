package ma.teethcare.repository.modules.medicament.impl.mySQL;

import ma.teethcare.entities.Medicament;
import ma.teethcare.repository.modules.medicament.api.MedicamentRepository;
import ma.teethcare.repository.common.RowMappers;
import ma.teethcare.conf.SessionFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MedicamentRepositoryImpl implements MedicamentRepository {

    // -------- CRUD --------
    @Override
    public List<Medicament> findAll() {
        String sql = "SELECT * FROM medicaments ORDER BY nom";
        List<Medicament> out = new ArrayList<>();
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) out.add(RowMappers.mapMedicament(rs));
        } catch (SQLException e) { throw new RuntimeException(e); }
        return out;
    }

    @Override
    public Medicament findById(Long id) {
        String sql = "SELECT * FROM medicaments WHERE id = ?";
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return RowMappers.mapMedicament(rs);
                return null;
            }
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    @Override
    public void create(Medicament medicament) {
        String sql = """
            INSERT INTO medicaments(nom, type, forme, prix, created_at)
            VALUES(?, ?, ?, ?, ?)
            """;
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, medicament.getNom());
            ps.setString(2, medicament.getType());
            ps.setString(3, medicament.getForme());
            ps.setDouble(4, medicament.getPrix());
            ps.setTimestamp(5, new Timestamp(System.currentTimeMillis()));

            ps.executeUpdate();

            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) medicament.setId(keys.getLong(1));
            }
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    @Override
    public void update(Medicament medicament) {
        String sql = """
            UPDATE medicaments SET 
                nom = ?, type = ?, forme = ?, prix = ?, updated_at = ?
            WHERE id = ?
            """;
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, medicament.getNom());
            ps.setString(2, medicament.getType());
            ps.setString(3, medicament.getForme());
            ps.setDouble(4, medicament.getPrix());
            ps.setTimestamp(5, new Timestamp(System.currentTimeMillis()));
            ps.setLong(6, medicament.getId());

            ps.executeUpdate();
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    @Override
    public void delete(Medicament medicament) {
        if (medicament != null) deleteById(medicament.getId());
    }

    @Override
    public void deleteById(Long id) {
        String sql = "DELETE FROM medicaments WHERE id = ?";
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, id);
            ps.executeUpdate();
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    // -------- Méthodes spécifiques --------
    @Override
    public Optional<Medicament> findByNom(String nom) {
        String sql = "SELECT * FROM medicaments WHERE nom = ?";
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, nom);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return Optional.of(RowMappers.mapMedicament(rs));
                return Optional.empty();
            }
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    @Override
    public List<Medicament> findByNomContaining(String keyword) {
        String sql = "SELECT * FROM medicaments WHERE nom LIKE ? ORDER BY nom";
        List<Medicament> out = new ArrayList<>();
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, "%" + keyword + "%");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) out.add(RowMappers.mapMedicament(rs));
            }
        } catch (SQLException e) { throw new RuntimeException(e); }
        return out;
    }

    @Override
    public List<Medicament> findByType(String type) {
        String sql = "SELECT * FROM medicaments WHERE type = ? ORDER BY nom";
        List<Medicament> out = new ArrayList<>();
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, type);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) out.add(RowMappers.mapMedicament(rs));
            }
        } catch (SQLException e) { throw new RuntimeException(e); }
        return out;
    }

    @Override
    public List<Medicament> findByForme(String forme) {
        String sql = "SELECT * FROM medicaments WHERE forme = ? ORDER BY nom";
        List<Medicament> out = new ArrayList<>();
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, forme);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) out.add(RowMappers.mapMedicament(rs));
            }
        } catch (SQLException e) { throw new RuntimeException(e); }
        return out;
    }

    @Override
    public List<Medicament> findByPrixLessThan(Double prixMax) {
        String sql = "SELECT * FROM medicaments WHERE prix < ? ORDER BY prix";
        List<Medicament> out = new ArrayList<>();
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setDouble(1, prixMax);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) out.add(RowMappers.mapMedicament(rs));
            }
        } catch (SQLException e) { throw new RuntimeException(e); }
        return out;
    }

    @Override
    public List<Medicament> findByPrixBetween(Double prixMin, Double prixMax) {
        String sql = "SELECT * FROM medicaments WHERE prix BETWEEN ? AND ? ORDER BY prix";
        List<Medicament> out = new ArrayList<>();
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setDouble(1, prixMin);
            ps.setDouble(2, prixMax);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) out.add(RowMappers.mapMedicament(rs));
            }
        } catch (SQLException e) { throw new RuntimeException(e); }
        return out;
    }

    @Override
    public List<Medicament> search(String nom, String type, String forme, Double prixMax) {
        StringBuilder sql = new StringBuilder("SELECT * FROM medicaments WHERE 1=1");
        List<Object> params = new ArrayList<>();

        if (nom != null && !nom.isEmpty()) {
            sql.append(" AND nom LIKE ?");
            params.add("%" + nom + "%");
        }
        if (type != null && !type.isEmpty()) {
            sql.append(" AND type = ?");
            params.add(type);
        }
        if (forme != null && !forme.isEmpty()) {
            sql.append(" AND forme = ?");
            params.add(forme);
        }
        if (prixMax != null) {
            sql.append(" AND prix <= ?");
            params.add(prixMax);
        }

        sql.append(" ORDER BY nom");

        List<Medicament> out = new ArrayList<>();
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql.toString())) {

            for (int i = 0; i < params.size(); i++) {
                Object param = params.get(i);
                if (param instanceof String) {
                    ps.setString(i + 1, (String) param);
                } else if (param instanceof Double) {
                    ps.setDouble(i + 1, (Double) param);
                }
            }

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) out.add(RowMappers.mapMedicament(rs));
            }
        } catch (SQLException e) { throw new RuntimeException(e); }
        return out;
    }

    @Override
    public long countByType(String type) {
        String sql = "SELECT COUNT(*) FROM medicaments WHERE type = ?";
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
    public long countByForme(String forme) {
        String sql = "SELECT COUNT(*) FROM medicaments WHERE forme = ?";
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, forme);
            try (ResultSet rs = ps.executeQuery()) {
                rs.next();
                return rs.getLong(1);
            }
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    @Override
    public List<Medicament> findAllOrderByNom(int limit, int offset) {
        String sql = "SELECT * FROM medicaments ORDER BY nom LIMIT ? OFFSET ?";
        List<Medicament> out = new ArrayList<>();
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, limit);
            ps.setInt(2, offset);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) out.add(RowMappers.mapMedicament(rs));
            }
        } catch (SQLException e) { throw new RuntimeException(e); }
        return out;
    }

    @Override
    public List<Medicament> findAllOrderByPrix(int limit, int offset) {
        String sql = "SELECT * FROM medicaments ORDER BY prix LIMIT ? OFFSET ?";
        List<Medicament> out = new ArrayList<>();
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, limit);
            ps.setInt(2, offset);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) out.add(RowMappers.mapMedicament(rs));
            }
        } catch (SQLException e) { throw new RuntimeException(e); }
        return out;
    }
}