package ma.teethcare.repository.modules.finances.impl.mySQL;

import ma.teethcare.conf.SessionFactory;
import ma.teethcare.entities.Revenues;
import ma.teethcare.repository.common.RowMappers;
import ma.teethcare.repository.modules.finances.api.RevenuesRepository;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class RevenuesRepositoryImpl implements RevenuesRepository {

    @Override
    public List<Revenues> findAll() {
        String sql = "SELECT * FROM Revenues ORDER BY date DESC";
        List<Revenues> out = new ArrayList<>();
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) out.add(RowMappers.mapRevenues(rs));
        } catch (SQLException e) { throw new RuntimeException(e); }
        return out;
    }

    @Override
    public Revenues findById(Long id) {
        String sql = "SELECT * FROM Revenues WHERE id = ?";
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return RowMappers.mapRevenues(rs);
                return null;
            }
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    @Override
    public void create(Revenues revenue) {
        String sql = "INSERT INTO Revenues(titre, description, montant, date, idCabinet) VALUES(?,?,?,?,?)";
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, revenue.getTitre());
            ps.setString(2, revenue.getDescription());
            ps.setDouble(3, revenue.getMontant());
            ps.setTimestamp(4, Timestamp.valueOf(revenue.getDate()));

            if (revenue.getCabinetMedicale() != null) ps.setLong(5, revenue.getCabinetMedicale().getIdUser());
            else ps.setNull(5, Types.BIGINT);

            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) revenue.setId(keys.getLong(1));
            }
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    @Override
    public void update(Revenues revenue) {
        String sql = "UPDATE Revenues SET titre=?, description=?, montant=?, date=?, idCabinet=? WHERE id=?";
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, revenue.getTitre());
            ps.setString(2, revenue.getDescription());
            ps.setDouble(3, revenue.getMontant());
            ps.setTimestamp(4, Timestamp.valueOf(revenue.getDate()));

            if (revenue.getCabinetMedicale() != null) ps.setLong(5, revenue.getCabinetMedicale().getIdUser());
            else ps.setNull(5, Types.BIGINT);

            ps.setLong(6, revenue.getId());
            ps.executeUpdate();
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    @Override
    public void delete(Revenues revenue) {
        if (revenue != null) deleteById(revenue.getId());
    }

    @Override
    public void deleteById(Long id) {
        String sql = "DELETE FROM Revenues WHERE id = ?";
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, id);
            ps.executeUpdate();
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    @Override
    public List<Revenues> findByCabinetMedicaleId(Long cabinetId) {
        String sql = "SELECT * FROM Revenues WHERE idCabinet = ? ORDER BY date DESC";
        List<Revenues> out = new ArrayList<>();
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, cabinetId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) out.add(RowMappers.mapRevenues(rs));
            }
        } catch (SQLException e) { throw new RuntimeException(e); }
        return out;
    }

    @Override
    public List<Revenues> findByDateBetween(LocalDateTime start, LocalDateTime end) {
        String sql = "SELECT * FROM Revenues WHERE date BETWEEN ? AND ? ORDER BY date DESC";
        List<Revenues> out = new ArrayList<>();
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setTimestamp(1, Timestamp.valueOf(start));
            ps.setTimestamp(2, Timestamp.valueOf(end));
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) out.add(RowMappers.mapRevenues(rs));
            }
        } catch (SQLException e) { throw new RuntimeException(e); }
        return out;
    }

    @Override
    public List<Revenues> findByMontantGreaterThan(Double montant) {
        String sql = "SELECT * FROM Revenues WHERE montant > ? ORDER BY montant DESC";
        List<Revenues> out = new ArrayList<>();
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setDouble(1, montant);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) out.add(RowMappers.mapRevenues(rs));
            }
        } catch (SQLException e) { throw new RuntimeException(e); }
        return out;
    }

    @Override
    public double getTotalRevenuesByCabinet(Long cabinetId) {
        String sql = "SELECT SUM(montant) FROM Revenues WHERE idCabinet = ?";
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, cabinetId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getDouble(1);
                return 0.0;
            }
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    @Override
    public List<Revenues> getTopRevenues(Long cabinetId, int limit) {
        String sql = "SELECT * FROM Revenues WHERE idCabinet = ? ORDER BY montant DESC LIMIT ?";
        List<Revenues> out = new ArrayList<>();
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, cabinetId);
            ps.setInt(2, limit);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) out.add(RowMappers.mapRevenues(rs));
            }
        } catch (SQLException e) { throw new RuntimeException(e); }
        return out;
    }
}