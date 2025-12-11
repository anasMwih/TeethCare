package ma.teethcare.repository.modules.acte.impl.mySQL;

import ma.teethcare.entities.Acte;
import ma.teethcare.repository.modules.acte.api.ActeRepository;
import ma.teethcare.repository.common.RowMappers;
import ma.teethcare.conf.SessionFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ActeRepositoryImpl implements ActeRepository {

    // -------- CRUD --------
    @Override
    public List<Acte> findAll() {
        String sql = "SELECT * FROM actes ORDER BY libelle";
        List<Acte> out = new ArrayList<>();
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) out.add(RowMappers.mapActe(rs));
        } catch (SQLException e) { throw new RuntimeException(e); }
        return out;
    }

    @Override
    public Acte findById(Long id) {
        String sql = "SELECT * FROM actes WHERE id = ?";
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return RowMappers.mapActe(rs);
                return null;
            }
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    @Override
    public void create(Acte acte) {
        String sql = """
            INSERT INTO actes(libelle, categorie, prix_base, created_at)
            VALUES(?, ?, ?, ?)
            """;
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, acte.getLibelle());
            ps.setString(2, acte.getCategorie());
            ps.setDouble(3, acte.getPrixDeBase());
            ps.setTimestamp(4, new Timestamp(System.currentTimeMillis()));

            ps.executeUpdate();

            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) acte.setId(keys.getLong(1));
            }
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    @Override
    public void update(Acte acte) {
        String sql = """
            UPDATE actes SET 
                libelle = ?, categorie = ?, prix_base = ?, updated_at = ?
            WHERE id = ?
            """;
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, acte.getLibelle());
            ps.setString(2, acte.getCategorie());
            ps.setDouble(3, acte.getPrixDeBase());
            ps.setTimestamp(4, new Timestamp(System.currentTimeMillis()));
            ps.setLong(5, acte.getId());

            ps.executeUpdate();
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    @Override
    public void delete(Acte acte) {
        if (acte != null) deleteById(acte.getId());
    }

    @Override
    public void deleteById(Long id) {
        String sql = "DELETE FROM actes WHERE id = ?";
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, id);
            ps.executeUpdate();
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    // -------- Méthodes spécifiques --------
    @Override
    public Optional<Acte> findByLibelle(String libelle) {
        String sql = "SELECT * FROM actes WHERE libelle = ?";
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, libelle);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return Optional.of(RowMappers.mapActe(rs));
                return Optional.empty();
            }
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    @Override
    public List<Acte> findByLibelleContaining(String keyword) {
        String sql = "SELECT * FROM actes WHERE libelle LIKE ? ORDER BY libelle";
        List<Acte> out = new ArrayList<>();
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, "%" + keyword + "%");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) out.add(RowMappers.mapActe(rs));
            }
        } catch (SQLException e) { throw new RuntimeException(e); }
        return out;
    }

    @Override
    public List<Acte> findByCategorie(String categorie) {
        String sql = "SELECT * FROM actes WHERE categorie = ? ORDER BY libelle";
        List<Acte> out = new ArrayList<>();
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, categorie);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) out.add(RowMappers.mapActe(rs));
            }
        } catch (SQLException e) { throw new RuntimeException(e); }
        return out;
    }

    @Override
    public List<String> findAllCategories() {
        String sql = "SELECT DISTINCT categorie FROM actes ORDER BY categorie";
        List<String> out = new ArrayList<>();
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) out.add(rs.getString("categorie"));
        } catch (SQLException e) { throw new RuntimeException(e); }
        return out;
    }

    @Override
    public List<Acte> findByPrixDeBaseLessThan(Double prixMax) {
        String sql = "SELECT * FROM actes WHERE prix_base < ? ORDER BY prix_base";
        List<Acte> out = new ArrayList<>();
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setDouble(1, prixMax);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) out.add(RowMappers.mapActe(rs));
            }
        } catch (SQLException e) { throw new RuntimeException(e); }
        return out;
    }

    @Override
    public List<Acte> findByPrixDeBaseBetween(Double prixMin, Double prixMax) {
        String sql = "SELECT * FROM actes WHERE prix_base BETWEEN ? AND ? ORDER BY prix_base";
        List<Acte> out = new ArrayList<>();
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setDouble(1, prixMin);
            ps.setDouble(2, prixMax);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) out.add(RowMappers.mapActe(rs));
            }
        } catch (SQLException e) { throw new RuntimeException(e); }
        return out;
    }

    @Override
    public List<Acte> search(String libelle, String categorie, Double prixMin, Double prixMax) {
        StringBuilder sql = new StringBuilder("SELECT * FROM actes WHERE 1=1");
        List<Object> params = new ArrayList<>();

        if (libelle != null && !libelle.isEmpty()) {
            sql.append(" AND libelle LIKE ?");
            params.add("%" + libelle + "%");
        }
        if (categorie != null && !categorie.isEmpty()) {
            sql.append(" AND categorie = ?");
            params.add(categorie);
        }
        if (prixMin != null) {
            sql.append(" AND prix_base >= ?");
            params.add(prixMin);
        }
        if (prixMax != null) {
            sql.append(" AND prix_base <= ?");
            params.add(prixMax);
        }

        sql.append(" ORDER BY libelle");

        List<Acte> out = new ArrayList<>();
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
                while (rs.next()) out.add(RowMappers.mapActe(rs));
            }
        } catch (SQLException e) { throw new RuntimeException(e); }
        return out;
    }

    @Override
    public long countByCategorie(String categorie) {
        String sql = "SELECT COUNT(*) FROM actes WHERE categorie = ?";
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, categorie);
            try (ResultSet rs = ps.executeQuery()) {
                rs.next();
                return rs.getLong(1);
            }
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    @Override
    public Double getMoyennePrixByCategorie(String categorie) {
        String sql = "SELECT AVG(prix_base) as moyenne FROM actes WHERE categorie = ?";
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, categorie);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getDouble("moyenne");
                }
            }
        } catch (SQLException e) { throw new RuntimeException(e); }
        return 0.0;
    }

    @Override
    public List<Acte> findAllOrderByLibelle(int limit, int offset) {
        String sql = "SELECT * FROM actes ORDER BY libelle LIMIT ? OFFSET ?";
        List<Acte> out = new ArrayList<>();
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, limit);
            ps.setInt(2, offset);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) out.add(RowMappers.mapActe(rs));
            }
        } catch (SQLException e) { throw new RuntimeException(e); }
        return out;
    }

    @Override
    public List<Acte> findAllOrderByPrix(int limit, int offset) {
        String sql = "SELECT * FROM actes ORDER BY prix_base LIMIT ? OFFSET ?";
        List<Acte> out = new ArrayList<>();
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, limit);
            ps.setInt(2, offset);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) out.add(RowMappers.mapActe(rs));
            }
        } catch (SQLException e) { throw new RuntimeException(e); }
        return out;
    }

    @Override
    public List<Acte> findAllOrderByCategorie(int limit, int offset) {
        String sql = "SELECT * FROM actes ORDER BY categorie, libelle LIMIT ? OFFSET ?";
        List<Acte> out = new ArrayList<>();
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, limit);
            ps.setInt(2, offset);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) out.add(RowMappers.mapActe(rs));
            }
        } catch (SQLException e) { throw new RuntimeException(e); }
        return out;
    }
}