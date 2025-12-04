package ma.teethcare.repository.modules.agenda.impl.mySQL;

import ma.teethcare.conf.SessionFactory;
import ma.teethcare.entities.AgendaMensuel;
import ma.teethcare.entities.Jour;
import ma.teethcare.entities.enums.Mois;
import ma.teethcare.repository.common.RowMappers;
import ma.teethcare.repository.modules.agenda.api.AgendaRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AgendaRepositoryImpl implements AgendaRepository {

    @Override
    public List<AgendaMensuel> findAll() {
        String sql = "SELECT * FROM AgendaMensuel ORDER BY mois";
        List<AgendaMensuel> out = new ArrayList<>();
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) out.add(RowMappers.mapAgendaMensuel(rs));
        } catch (SQLException e) { throw new RuntimeException(e); }
        return out;
    }

    @Override
    public AgendaMensuel findById(Long id) {
        String sql = "SELECT * FROM AgendaMensuel WHERE id = ?";
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return RowMappers.mapAgendaMensuel(rs);
                return null;
            }
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    @Override
    public void create(AgendaMensuel agenda) {
        String sql = "INSERT INTO AgendaMensuel(mois, idUtilisateur) VALUES(?,?)";
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, agenda.getMois().name());

            if (agenda.getMedecin() != null) ps.setLong(2, agenda.getMedecin().getIdUser());
            else ps.setNull(2, Types.BIGINT);

            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) agenda.setId(keys.getLong(1));
            }
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    @Override
    public void update(AgendaMensuel agenda) {
        String sql = "UPDATE AgendaMensuel SET mois=?, idUtilisateur=? WHERE id=?";
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, agenda.getMois().name());

            if (agenda.getMedecin() != null) ps.setLong(2, agenda.getMedecin().getIdUser());
            else ps.setNull(2, Types.BIGINT);

            ps.setLong(3, agenda.getId());
            ps.executeUpdate();
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    @Override
    public void delete(AgendaMensuel agenda) {
        if (agenda != null) deleteById(agenda.getId());
    }

    @Override
    public void deleteById(Long id) {
        // Supprimer d'abord les jours associés
        String deleteJoursSql = "DELETE FROM Jour WHERE idAgenda = ?";
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(deleteJoursSql)) {
            ps.setLong(1, id);
            ps.executeUpdate();
        } catch (SQLException e) { throw new RuntimeException(e); }

        // Supprimer l'agenda
        String sql = "DELETE FROM AgendaMensuel WHERE id = ?";
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, id);
            ps.executeUpdate();
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    @Override
    public AgendaMensuel findByMoisAndUtilisateurId(Mois mois, Long utilisateurId) {
        String sql = "SELECT * FROM AgendaMensuel WHERE mois = ? AND idUtilisateur = ?";
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, mois.name());
            ps.setLong(2, utilisateurId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return RowMappers.mapAgendaMensuel(rs);
                return null;
            }
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    @Override
    public List<AgendaMensuel> findByUtilisateurId(Long utilisateurId) {
        String sql = "SELECT * FROM AgendaMensuel WHERE idUtilisateur = ? ORDER BY mois";
        List<AgendaMensuel> out = new ArrayList<>();
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, utilisateurId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) out.add(RowMappers.mapAgendaMensuel(rs));
            }
        } catch (SQLException e) { throw new RuntimeException(e); }
        return out;
    }

    @Override
    public void addJourNonDisponible(Long agendaId, Jour jour) {
        String sql = "INSERT INTO Jour(nomJour, disponible, idAgenda) VALUES(?,?,?)";
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, jour.getNomJour());
            ps.setBoolean(2, jour.isDisponible());
            ps.setLong(3, agendaId);

            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) jour.setId(keys.getLong(1));
            }
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    @Override
    public void removeJourNonDisponible(Long agendaId, Jour jour) {
        String sql = "DELETE FROM Jour WHERE idAgenda = ? AND nomJour = ?";
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, agendaId);
            ps.setString(2, jour.getNomJour());
            ps.executeUpdate();
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    @Override
    public List<Jour> getJoursNonDisponibles(Long agendaId) {
        String sql = "SELECT * FROM Jour WHERE idAgenda = ? AND disponible = FALSE";
        List<Jour> out = new ArrayList<>();
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, agendaId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) out.add(RowMappers.mapJour(rs));
            }
        } catch (SQLException e) { throw new RuntimeException(e); }
        return out;
    }

    @Override
    public boolean isJourDisponible(Long agendaId, String nomJour) {
        String sql = "SELECT disponible FROM Jour WHERE idAgenda = ? AND nomJour = ?";
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, agendaId);
            ps.setString(2, nomJour);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getBoolean("disponible");
                return true; // Si le jour n'est pas défini, il est disponible par défaut
            }
        } catch (SQLException e) { throw new RuntimeException(e); }
    }
}