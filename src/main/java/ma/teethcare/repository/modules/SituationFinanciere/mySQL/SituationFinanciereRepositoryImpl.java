package ma.teethcare.repository.modules.finance.impl.mySQL;

import ma.teethcare.entities.SituationFinanciere;
import ma.teethcare.entities.enums.EnPromo;
import ma.teethcare.entities.enums.StatutFinancier;
import ma.teethcare.conf.SessionFactory;
import ma.teethcare.repository.common.RowMappers;
import ma.teethcare.repository.modules.finance.api.SituationFinanciereRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SituationFinanciereRepositoryImpl implements SituationFinanciereRepository {

    // -------- CRUD --------
    @Override
    public List<SituationFinanciere> findAll() {
        String sql = "SELECT * FROM situation_financiere ORDER BY credit DESC";
        List<SituationFinanciere> out = new ArrayList<>();
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) out.add(RowMappers.mapSituationFinanciere(rs));
        } catch (SQLException e) { throw new RuntimeException(e); }
        return out;
    }

    @Override
    public SituationFinanciere findById(Long id) {
        String sql = "SELECT * FROM situation_financiere WHERE idSF = ?";
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return RowMappers.mapSituationFinanciere(rs);
                return null;
            }
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    @Override
    public void create(SituationFinanciere situation) {
        String sql = "INSERT INTO situation_financiere(totale_des_actes, totale_paye, credit, statut, en_promo) " +
                "VALUES(?,?,?,?,?)";
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setDouble(1, situation.getTotaleDesActes());
            ps.setDouble(2, situation.getTotalePaye());
            ps.setDouble(3, situation.getCredit());
            ps.setString(4, situation.getStatut().name());
            ps.setString(5, situation.getEnPromo().name());
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) situation.setIdSF(keys.getLong(1));
            }
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    @Override
    public void update(SituationFinanciere situation) {
        String sql = "UPDATE situation_financiere SET totale_des_actes=?, totale_paye=?, " +
                "credit=?, statut=?, en_promo=? WHERE idSF=?";
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setDouble(1, situation.getTotaleDesActes());
            ps.setDouble(2, situation.getTotalePaye());
            ps.setDouble(3, situation.getCredit());
            ps.setString(4, situation.getStatut().name());
            ps.setString(5, situation.getEnPromo().name());
            ps.setLong(6, situation.getIdSF());
            ps.executeUpdate();
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    @Override
    public void delete(SituationFinanciere situation) {
        if (situation != null) deleteById(situation.getIdSF());
    }

    @Override
    public void deleteById(Long id) {
        String sql = "DELETE FROM situation_financiere WHERE idSF = ?";
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, id);
            ps.executeUpdate();
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    // -------- Recherches spécifiques --------
    @Override
    public List<SituationFinanciere> findByStatut(StatutFinancier statut) {
        String sql = "SELECT * FROM situation_financiere WHERE statut = ? ORDER BY credit DESC";
        return executeQuery(sql, statut.name());
    }

    @Override
    public List<SituationFinanciere> findByEnPromo(EnPromo enPromo) {
        String sql = "SELECT * FROM situation_financiere WHERE en_promo = ? ORDER BY credit DESC";
        return executeQuery(sql, enPromo.name());
    }

    @Override
    public List<SituationFinanciere> findByCreditGreaterThan(Double minCredit) {
        String sql = "SELECT * FROM situation_financiere WHERE credit > ? ORDER BY credit DESC";
        return executeQuery(sql, minCredit);
    }

    @Override
    public List<SituationFinanciere> findByCreditBetween(Double min, Double max) {
        String sql = "SELECT * FROM situation_financiere WHERE credit BETWEEN ? AND ? ORDER BY credit DESC";
        List<SituationFinanciere> out = new ArrayList<>();
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setDouble(1, min);
            ps.setDouble(2, max);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) out.add(RowMappers.mapSituationFinanciere(rs));
            }
        } catch (SQLException e) { throw new RuntimeException(e); }
        return out;
    }

    @Override
    public List<SituationFinanciere> findByCreditEquals(Double credit) {
        String sql = "SELECT * FROM situation_financiere WHERE credit = ? ORDER BY idSF";
        return executeQuery(sql, credit);
    }

    @Override
    public List<SituationFinanciere> findByTotaleDesActesBetween(Double min, Double max) {
        String sql = "SELECT * FROM situation_financiere WHERE totale_des_actes BETWEEN ? AND ? ORDER BY totale_des_actes DESC";
        List<SituationFinanciere> out = new ArrayList<>();
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setDouble(1, min);
            ps.setDouble(2, max);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) out.add(RowMappers.mapSituationFinanciere(rs));
            }
        } catch (SQLException e) { throw new RuntimeException(e); }
        return out;
    }

    @Override
    public List<SituationFinanciere> findByTotalePayeBetween(Double min, Double max) {
        String sql = "SELECT * FROM situation_financiere WHERE totale_paye BETWEEN ? AND ? ORDER BY totale_paye DESC";
        List<SituationFinanciere> out = new ArrayList<>();
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setDouble(1, min);
            ps.setDouble(2, max);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) out.add(RowMappers.mapSituationFinanciere(rs));
            }
        } catch (SQLException e) { throw new RuntimeException(e); }
        return out;
    }

    @Override
    public List<SituationFinanciere> findByCreditAndStatut(Double credit, StatutFinancier statut) {
        String sql = "SELECT * FROM situation_financiere WHERE credit = ? AND statut = ? ORDER BY idSF";
        List<SituationFinanciere> out = new ArrayList<>();
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setDouble(1, credit);
            ps.setString(2, statut.name());
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) out.add(RowMappers.mapSituationFinanciere(rs));
            }
        } catch (SQLException e) { throw new RuntimeException(e); }
        return out;
    }

    @Override
    public boolean existsById(Long id) {
        String sql = "SELECT 1 FROM situation_financiere WHERE idSF = ?";
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    @Override
    public boolean existsByStatut(StatutFinancier statut) {
        String sql = "SELECT 1 FROM situation_financiere WHERE statut = ?";
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, statut.name());
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    @Override
    public long count() {
        String sql = "SELECT COUNT(*) FROM situation_financiere";
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            rs.next();
            return rs.getLong(1);
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    @Override
    public long countByStatut(StatutFinancier statut) {
        String sql = "SELECT COUNT(*) FROM situation_financiere WHERE statut = ?";
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, statut.name());
            try (ResultSet rs = ps.executeQuery()) {
                rs.next();
                return rs.getLong(1);
            }
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    @Override
    public long countByEnPromo(EnPromo enPromo) {
        String sql = "SELECT COUNT(*) FROM situation_financiere WHERE en_promo = ?";
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, enPromo.name());
            try (ResultSet rs = ps.executeQuery()) {
                rs.next();
                return rs.getLong(1);
            }
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    @Override
    public List<SituationFinanciere> findPage(int limit, int offset) {
        String sql = "SELECT * FROM situation_financiere ORDER BY credit DESC LIMIT ? OFFSET ?";
        List<SituationFinanciere> out = new ArrayList<>();
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, limit);
            ps.setInt(2, offset);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) out.add(RowMappers.mapSituationFinanciere(rs));
            }
        } catch (SQLException e) { throw new RuntimeException(e); }
        return out;
    }

    // -------- Statistiques financières --------
    @Override
    public Double sumTotaleDesActes() {
        String sql = "SELECT SUM(totale_des_actes) FROM situation_financiere";
        return executeSumQuery(sql);
    }

    @Override
    public Double sumTotalePaye() {
        String sql = "SELECT SUM(totale_paye) FROM situation_financiere";
        return executeSumQuery(sql);
    }

    @Override
    public Double sumCredit() {
        String sql = "SELECT SUM(credit) FROM situation_financiere";
        return executeSumQuery(sql);
    }

    @Override
    public Double sumTotaleDesActesByStatut(StatutFinancier statut) {
        String sql = "SELECT SUM(totale_des_actes) FROM situation_financiere WHERE statut = ?";
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, statut.name());
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next() && rs.getObject(1) != null) {
                    return rs.getDouble(1);
                }
                return 0.0;
            }
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    @Override
    public Double sumCreditByStatut(StatutFinancier statut) {
        String sql = "SELECT SUM(credit) FROM situation_financiere WHERE statut = ?";
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, statut.name());
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next() && rs.getObject(1) != null) {
                    return rs.getDouble(1);
                }
                return 0.0;
            }
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    @Override
    public Double avgCredit() {
        String sql = "SELECT AVG(credit) FROM situation_financiere WHERE credit IS NOT NULL";
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next() && rs.getObject(1) != null) {
                return rs.getDouble(1);
            }
            return 0.0;
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    @Override
    public Double avgTotaleDesActes() {
        String sql = "SELECT AVG(totale_des_actes) FROM situation_financiere WHERE totale_des_actes IS NOT NULL";
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next() && rs.getObject(1) != null) {
                return rs.getDouble(1);
            }
            return 0.0;
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    // -------- Méthodes de mise à jour --------
    @Override
    public void updateCredit(Long id, Double nouveauCredit) {
        String sql = "UPDATE situation_financiere SET credit = ? WHERE idSF = ?";
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setDouble(1, nouveauCredit);
            ps.setLong(2, id);
            ps.executeUpdate();

            // Mettre à jour le statut automatiquement
            mettreAJourStatutAutomatique(id);
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    @Override
    public void updateStatut(Long id, StatutFinancier nouveauStatut) {
        String sql = "UPDATE situation_financiere SET statut = ? WHERE idSF = ?";
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, nouveauStatut.name());
            ps.setLong(2, id);
            ps.executeUpdate();
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    @Override
    public void updateTotalePaye(Long id, Double nouveauTotalePaye) {
        String sql = "UPDATE situation_financiere SET totale_paye = ?, credit = totale_des_actes - ? WHERE idSF = ?";
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setDouble(1, nouveauTotalePaye);
            ps.setDouble(2, nouveauTotalePaye);
            ps.setLong(3, id);
            ps.executeUpdate();

            // Mettre à jour le statut automatiquement
            mettreAJourStatutAutomatique(id);
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    @Override
    public void calculerCreditAutomatique(Long id) {
        String sql = "UPDATE situation_financiere SET credit = totale_des_actes - totale_paye WHERE idSF = ?";
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, id);
            ps.executeUpdate();
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    @Override
    public void mettreAJourStatutAutomatique(Long id) {
        // Cette requête met à jour le statut basé sur le crédit
        String sql = """
            UPDATE situation_financiere 
            SET statut = CASE 
                WHEN credit = 0 THEN 'SOLVABLE'
                WHEN credit > 0 AND credit <= 1000 THEN 'EN_RETARD'
                WHEN credit > 1000 AND credit <= 5000 THEN 'EN_NEGOCIATION'
                WHEN credit > 5000 THEN 'DEFAILLANT'
                ELSE statut
            END
            WHERE idSF = ?
            """;
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, id);
            ps.executeUpdate();
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    // -------- Recherche avancée --------
    @Override
    public List<SituationFinanciere> findSituationsProblematic() {
        String sql = "SELECT * FROM situation_financiere " +
                "WHERE statut IN ('EN_RETARD', 'EN_NEGOCIATION', 'DEFAILLANT') " +
                "ORDER BY credit DESC";
        List<SituationFinanciere> out = new ArrayList<>();
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) out.add(RowMappers.mapSituationFinanciere(rs));
        } catch (SQLException e) { throw new RuntimeException(e); }
        return out;
    }

    @Override
    public List<SituationFinanciere> findSituationsAvecPromotion() {
        String sql = "SELECT * FROM situation_financiere WHERE en_promo = 'OUI' ORDER BY credit DESC";
        List<SituationFinanciere> out = new ArrayList<>();
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) out.add(RowMappers.mapSituationFinanciere(rs));
        } catch (SQLException e) { throw new RuntimeException(e); }
        return out;
    }

    // -------- Méthodes utilitaires --------
    private List<SituationFinanciere> executeQuery(String sql, Object param) {
        List<SituationFinanciere> out = new ArrayList<>();
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            if (param instanceof String) {
                ps.setString(1, (String) param);
            } else if (param instanceof Double) {
                ps.setDouble(1, (Double) param);
            }
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) out.add(RowMappers.mapSituationFinanciere(rs));
            }
        } catch (SQLException e) { throw new RuntimeException(e); }
        return out;
    }

    private Double executeSumQuery(String sql) {
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next() && rs.getObject(1) != null) {
                return rs.getDouble(1);
            }
            return 0.0;
        } catch (SQLException e) { throw new RuntimeException(e); }
    }
}