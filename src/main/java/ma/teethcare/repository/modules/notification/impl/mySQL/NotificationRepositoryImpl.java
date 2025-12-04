package ma.teethcare.repository.modules.notification.impl.mySQL;

import ma.teethcare.conf.SessionFactory;
import ma.teethcare.entities.Notification;
import ma.teethcare.entities.enums.PrioriteNotification;
import ma.teethcare.entities.enums.TypeNotification;
import ma.teethcare.repository.common.RowMappers;
import ma.teethcare.repository.modules.notification.api.NotificationRepository;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class NotificationRepositoryImpl implements NotificationRepository {

    @Override
    public List<Notification> findAll() {
        String sql = "SELECT * FROM Notification ORDER BY date DESC, time DESC";
        List<Notification> out = new ArrayList<>();
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) out.add(RowMappers.mapNotification(rs));
        } catch (SQLException e) { throw new RuntimeException(e); }
        return out;
    }

    @Override
    public Notification findById(Long id) {
        String sql = "SELECT * FROM Notification WHERE id = ?";
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return RowMappers.mapNotification(rs);
                return null;
            }
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    @Override
    public void create(Notification notification) {
        String sql = "INSERT INTO Notification(titre, message, date, time, type, priorite, idUtilisateur) VALUES(?,?,?,?,?,?,?)";
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, notification.getTitre().toString());
            ps.setString(2, notification.getMessage());
            ps.setDate(3, Date.valueOf(notification.getDate()));
            ps.setTime(4, Time.valueOf(notification.getTime()));
            ps.setString(5, notification.getType().name());
            ps.setString(6, notification.getPriorite().name());

            if (notification.getUtilisateur() != null) ps.setLong(7, notification.getUtilisateur().getIdUser());
            else ps.setNull(7, Types.BIGINT);

            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) notification.setId(keys.getLong(1));
            }
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    @Override
    public void update(Notification notification) {
        String sql = "UPDATE Notification SET titre=?, message=?, date=?, time=?, type=?, priorite=?, idUtilisateur=? WHERE id=?";
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, notification.getTitre().toString());
            ps.setString(2, notification.getMessage());
            ps.setDate(3, Date.valueOf(notification.getDate()));
            ps.setTime(4, Time.valueOf(notification.getTime()));
            ps.setString(5, notification.getType().name());
            ps.setString(6, notification.getPriorite().name());

            if (notification.getUtilisateur() != null) ps.setLong(7, notification.getUtilisateur().getIdUser());
            else ps.setNull(7, Types.BIGINT);

            ps.setLong(8, notification.getId());
            ps.executeUpdate();
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    @Override
    public void delete(Notification notification) {
        if (notification != null) deleteById(notification.getId());
    }

    @Override
    public void deleteById(Long id) {
        String sql = "DELETE FROM Notification WHERE id = ?";
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, id);
            ps.executeUpdate();
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    @Override
    public List<Notification> findByUtilisateurId(Long utilisateurId) {
        String sql = "SELECT * FROM Notification WHERE idUtilisateur = ? ORDER BY date DESC, time DESC";
        List<Notification> out = new ArrayList<>();
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, utilisateurId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) out.add(RowMappers.mapNotification(rs));
            }
        } catch (SQLException e) { throw new RuntimeException(e); }
        return out;
    }

    @Override
    public List<Notification> findByType(TypeNotification type) {
        String sql = "SELECT * FROM Notification WHERE type = ? ORDER BY date DESC, time DESC";
        List<Notification> out = new ArrayList<>();
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, type.name());
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) out.add(RowMappers.mapNotification(rs));
            }
        } catch (SQLException e) { throw new RuntimeException(e); }
        return out;
    }

    @Override
    public List<Notification> findByPriorite(PrioriteNotification priorite) {
        String sql = "SELECT * FROM Notification WHERE priorite = ? ORDER BY date DESC, time DESC";
        List<Notification> out = new ArrayList<>();
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, priorite.name());
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) out.add(RowMappers.mapNotification(rs));
            }
        } catch (SQLException e) { throw new RuntimeException(e); }
        return out;
    }

    @Override
    public List<Notification> findByDate(LocalDate date) {
        String sql = "SELECT * FROM Notification WHERE date = ? ORDER BY time DESC";
        List<Notification> out = new ArrayList<>();
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setDate(1, Date.valueOf(date));
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) out.add(RowMappers.mapNotification(rs));
            }
        } catch (SQLException e) { throw new RuntimeException(e); }
        return out;
    }

    @Override
    public List<Notification> findUnreadByUtilisateur(Long utilisateurId) {
        // Ajouter un champ 'lu' dans la table Notification si nécessaire
        String sql = "SELECT * FROM Notification WHERE idUtilisateur = ? ORDER BY date DESC, time DESC";
        List<Notification> out = new ArrayList<>();
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, utilisateurId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) out.add(RowMappers.mapNotification(rs));
            }
        } catch (SQLException e) { throw new RuntimeException(e); }
        return out;
    }

    @Override
    public void markAsRead(Long notificationId) {
        // Implémenter si le champ 'lu' existe
    }

    @Override
    public void markAllAsRead(Long utilisateurId) {
        // Implémenter si le champ 'lu' existe
    }
}