package ma.teethcare.repository.modules.statistiques.impl.mySQL;

import ma.teethcare.conf.SessionFactory;
import ma.teethcare.entities.Statistiques;
import ma.teethcare.entities.enums.CategorieStatistique;
import ma.teethcare.repository.common.RowMappers;
import ma.teethcare.repository.modules.statistiques.api.StatistiquesRepository;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class StatistiquesRepositoryImpl implements StatistiquesRepository {

    @Override
    public List<Statistiques> findAll() {
        String sql = "SELECT * FROM Statistiques ORDER BY dateCalcul DESC";
        List<Statistiques> out = new ArrayList<>();
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) out.add(RowMappers.mapStatistiques(rs));
        } catch (SQLException e) { throw new RuntimeException(e); }
        return out;
    }

    @Override
    public Statistiques findById(Long id) {
        String sql = "SELECT * FROM Statistiques WHERE id = ?";
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return RowMappers.mapStatistiques(rs);
                return null;
            }
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    @Override
    public void create(Statistiques statistique) {
        String sql = "INSERT INTO Statistiques(nom, categorie, chiffre, dateCalcul, idCabinet) VALUES(?,?,?,?,?)";
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, statistique.getNom());
            ps.setString(2, statistique.getCategorie().name());
            ps.setDouble(3, statistique.getChiffre());
            ps.setDate(4, Date.valueOf(statistique.getDateCalcul()));

            if (statistique.getCabinetMedicale() != null) ps.setLong(5, statistique.getCabinetMedicale().getIdUser());
            else ps.setNull(5, Types.BIGINT);

            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) statistique.setId(keys.getLong(1));
            }
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    @Override
    public void update(Statistiques statistique) {
        String sql = "UPDATE Statistiques SET nom=?, categorie=?, chiffre=?, dateCalcul=?, idCabinet=? WHERE id=?";
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, statistique.getNom());
            ps.setString(2, statistique.getCategorie().name());
            ps.setDouble(3, statistique.getChiffre());
            ps.setDate(4, Date.valueOf(statistique.getDateCalcul()));

            if (statistique.getCabinetMedicale() != null) ps.setLong(5, statistique.getCabinetMedicale().getIdUser());
            else ps.setNull(5, Types.BIGINT);

            ps.setLong(6, statistique.getId());
            ps.executeUpdate();
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    @Override
    public void delete(Statistiques statistique) {
        if (statistique != null) deleteById(statistique.getId());
    }

    @Override
    public void deleteById(Long id) {
        String sql = "DELETE FROM Statistiques WHERE id = ?";
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, id);
            ps.executeUpdate();
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    @Override
    public List<Statistiques> findByCabinetMedicaleId(Long cabinetId) {
        String sql = "SELECT * FROM Statistiques WHERE idCabinet = ? ORDER BY dateCalcul DESC";
        List<Statistiques> out = new ArrayList<>();
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, cabinetId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) out.add(RowMappers.mapStatistiques(rs));
            }
        } catch (SQLException e) { throw new RuntimeException(e); }
        return out;
    }

    @Override
    public List<Statistiques> findByCategorie(CategorieStatistique categorie) {
        String sql = "SELECT * FROM Statistiques WHERE categorie = ? ORDER BY dateCalcul DESC";
        List<Statistiques> out = new ArrayList<>();
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, categorie.name());
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) out.add(RowMappers.mapStatistiques(rs));
            }
        } catch (SQLException e) { throw new RuntimeException(e); }
        return out;
    }

    @Override
    public List<Statistiques> findByDateCalculBetween(LocalDate start, LocalDate end) {
        String sql = "SELECT * FROM Statistiques WHERE dateCalcul BETWEEN ? AND ? ORDER BY dateCalcul DESC";
        List<Statistiques> out = new ArrayList<>();
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setDate(1, Date.valueOf(start));
            ps.setDate(2, Date.valueOf(end));
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) out.add(RowMappers.mapStatistiques(rs));
            }
        } catch (SQLException e) { throw new RuntimeException(e); }
        return out;
    }

    @Override
    public Statistiques findLatestByCategorie(Long cabinetId, CategorieStatistique categorie) {
        String sql = """
            SELECT * FROM Statistiques 
            WHERE idCabinet = ? AND categorie = ? 
            ORDER BY dateCalcul DESC LIMIT 1
            """;
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, cabinetId);
            ps.setString(2, categorie.name());
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return RowMappers.mapStatistiques(rs);
                return null;
            }
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    @Override
    public List<Statistiques> findTopByChiffre(Long cabinetId, int limit) {
        String sql = "SELECT * FROM Statistiques WHERE idCabinet = ? ORDER BY chiffre DESC LIMIT ?";
        List<Statistiques> out = new ArrayList<>();
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, cabinetId);
            ps.setInt(2, limit);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) out.add(RowMappers.mapStatistiques(rs));
            }
        } catch (SQLException e) { throw new RuntimeException(e); }
        return out;
    }
}