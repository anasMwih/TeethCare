package ma.teethcare.repository.modules.finances.impl.mySQL;

import ma.teethcare.conf.SessionFactory;
import ma.teethcare.entities.Charges;
import ma.teethcare.repository.common.RowMappers;
import ma.teethcare.repository.modules.finances.api.ChargesRepository;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ChargesRepositoryImpl implements ChargesRepository {

    @Override
    public List<Charges> findAll() {
        String sql = "SELECT * FROM Charges ORDER BY date DESC";
        List<Charges> out = new ArrayList<>();
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) out.add(RowMappers.mapCharges(rs));
        } catch (SQLException e) { throw new RuntimeException(e); }
        return out;
    }

    @Override
    public Charges findById(Long id) {
        String sql = "SELECT * FROM Charges WHERE id = ?";
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return RowMappers.mapCharges(rs);
                return null;
            }
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    @Override
    public void create(Charges charge) {
        String sql = "INSERT INTO Charges(titre, description, montant, date, idCabinet) VALUES(?,?,?,?,?)";
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, charge.getTitre());
            ps.setString(2, charge.getDescription());
            ps.setDouble(3, charge.getMontant());
            ps.setTimestamp(4, Timestamp.valueOf(charge.getDate()));

            if (charge.getCabinetMedicale() != null) ps.setLong(5, charge.getCabinetMedicale().getIdUser());
            else ps.setNull(5, Types.BIGINT);

            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) charge.setId(keys.getLong(1));
            }
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    @Override
    public void update(Charges charge) {
        String sql = "UPDATE Charges SET titre=?, description=?, montant=?, date=?, idCabinet=? WHERE id=?";
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, charge.getTitre());
            ps.setString(2, charge.getDescription());
            ps.setDouble(3, charge.getMontant());
            ps.setTimestamp(4, Timestamp.valueOf(charge.getDate()));

            if (charge.getCabinetMedicale() != null) ps.setLong(5, charge.getCabinetMedicale().getIdUser());
            else ps.setNull(5, Types.BIGINT);

            ps.setLong(6, charge.getId());
            ps.executeUpdate();
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    @Override
    public void delete(Charges charge) {
        if (charge != null) deleteById(charge.getId());
    }

    @Override
    public void deleteById(Long id) {
        String sql = "DELETE FROM Charges WHERE id = ?";
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, id);
            ps.executeUpdate();
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    @Override
    public List<Charges> findByCabinetMedicaleId(Long cabinetId) {
        String sql = "SELECT * FROM Charges WHERE idCabinet = ? ORDER BY date DESC";
        List<Charges> out = new ArrayList<>();
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, cabinetId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) out.add(RowMappers.mapCharges(rs));
            }
        } catch (SQLException e) { throw new RuntimeException(e); }
        return out;
    }

    @Override
    public List<Charges> findByDateBetween(LocalDateTime start, LocalDateTime end) {
        String sql = "SELECT * FROM Charges WHERE date BETWEEN ? AND ? ORDER BY date DESC";
        List<Charges> out = new ArrayList<>();
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setTimestamp(1, Timestamp.valueOf(start));
            ps.setTimestamp(2, Timestamp.valueOf(end));
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) out.add(RowMappers.mapCharges(rs));
            }
        } catch (SQLException e) { throw new RuntimeException(e); }
        return out;
    }

    @Override
    public List<Charges> findByMontantGreaterThan(Double montant) {
        String sql = "SELECT * FROM Charges WHERE montant > ? ORDER BY montant DESC";
        List<Charges> out = new ArrayList<>();
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setDouble(1, montant);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) out.add(RowMappers.mapCharges(rs));
            }
        } catch (SQLException e) { throw new RuntimeException(e); }
        return out;
    }

    @Override
    public double getTotalChargesByCabinet(Long cabinetId) {
        String sql = "SELECT SUM(montant) FROM Charges WHERE idCabinet = ?";
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, cabinetId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getDouble(1);
                return 0.0;
            }
        } catch (SQLException e) { throw new RuntimeException(e); }
    }
}