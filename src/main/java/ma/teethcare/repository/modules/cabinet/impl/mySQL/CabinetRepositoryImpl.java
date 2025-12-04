package ma.teethcare.repository.modules.cabinet.impl.mySQL;

import ma.teethcare.conf.SessionFactory;
import ma.teethcare.entities.CabinetMedicale;
import ma.teethcare.repository.common.RowMappers;
import ma.teethcare.repository.modules.cabinet.api.CabinetRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CabinetRepositoryImpl implements CabinetRepository {

    @Override
    public List<CabinetMedicale> findAll() {
        String sql = "SELECT * FROM CabinetMedicale ORDER BY nom";
        List<CabinetMedicale> out = new ArrayList<>();
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) out.add(RowMappers.mapCabinetMedicale(rs));
        } catch (SQLException e) { throw new RuntimeException(e); }
        return out;
    }

    @Override
    public CabinetMedicale findById(Long id) {
        String sql = "SELECT * FROM CabinetMedicale WHERE idUser = ?";
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return RowMappers.mapCabinetMedicale(rs);
                return null;
            }
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    @Override
    public void create(CabinetMedicale cabinet) {
        String sql = """
            INSERT INTO CabinetMedicale(nom, email, adresse, logo, cin, tel1, tel2, siteweb, instagram)
            VALUES(?,?,?,?,?,?,?,?,?)
            """;
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, cabinet.getNom());
            ps.setString(2, cabinet.getEmail());
            ps.setString(3, cabinet.getAdresse());
            ps.setString(4, cabinet.getLogo());
            ps.setString(5, cabinet.getCin());
            ps.setString(6, cabinet.getTel1());
            ps.setString(7, cabinet.getTel2());
            ps.setString(8, cabinet.getSiteweb());
            ps.setString(9, cabinet.getInstagram());

            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) cabinet.setIdUser(keys.getLong(1));
            }
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    @Override
    public void update(CabinetMedicale cabinet) {
        String sql = """
            UPDATE CabinetMedicale SET nom=?, email=?, adresse=?, logo=?, cin=?, tel1=?, tel2=?, 
            siteweb=?, instagram=? WHERE idUser=?
            """;
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, cabinet.getNom());
            ps.setString(2, cabinet.getEmail());
            ps.setString(3, cabinet.getAdresse());
            ps.setString(4, cabinet.getLogo());
            ps.setString(5, cabinet.getCin());
            ps.setString(6, cabinet.getTel1());
            ps.setString(7, cabinet.getTel2());
            ps.setString(8, cabinet.getSiteweb());
            ps.setString(9, cabinet.getInstagram());
            ps.setLong(10, cabinet.getIdUser());

            ps.executeUpdate();
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    @Override
    public void delete(CabinetMedicale cabinet) {
        if (cabinet != null) deleteById(cabinet.getIdUser());
    }

    @Override
    public void deleteById(Long id) {
        String sql = "DELETE FROM CabinetMedicale WHERE idUser = ?";
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, id);
            ps.executeUpdate();
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    @Override
    public Optional<CabinetMedicale> findByEmail(String email) {
        String sql = "SELECT * FROM CabinetMedicale WHERE email = ?";
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return Optional.of(RowMappers.mapCabinetMedicale(rs));
                return Optional.empty();
            }
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    @Override
    public Optional<CabinetMedicale> findByNom(String nom) {
        String sql = "SELECT * FROM CabinetMedicale WHERE nom = ?";
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, nom);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return Optional.of(RowMappers.mapCabinetMedicale(rs));
                return Optional.empty();
            }
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    @Override
    public boolean existsByEmail(String email) {
        String sql = "SELECT 1 FROM CabinetMedicale WHERE email = ?";
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) { return rs.next(); }
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    @Override
    public boolean existsByCin(String cin) {
        String sql = "SELECT 1 FROM CabinetMedicale WHERE cin = ?";
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, cin);
            try (ResultSet rs = ps.executeQuery()) { return rs.next(); }
        } catch (SQLException e) { throw new RuntimeException(e); }
    }
}