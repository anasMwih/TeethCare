package ma.teethcare.repository.modules.medecin.impl.mySQL;

import ma.teethcare.entities.Medecin;
import ma.teethcare.repository.modules.medecin.api.MedecinRepository;
import ma.teethcare.repository.common.RowMappers;
import ma.teethcare.conf.SessionFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MedecinRepositoryImpl implements MedecinRepository {

    // -------- CRUD --------
    @Override
    public List<Medecin> findAll() {
        String sql = """
            SELECT u.*, m.specialite, m.numero_rpps, m.numero_ordre, m.titre, 
                   m.annees_experience, m.taux_horaire, m.disponible
            FROM utilisateurs u
            JOIN medecins m ON u.id = m.utilisateur_id
            WHERE u.type = 'MEDECIN'
            ORDER BY u.nom, u.prenom
            """;
        List<Medecin> out = new ArrayList<>();
        try (Connection conn = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) out.add(RowMappers.mapMedecin(rs));
        } catch (SQLException e) { throw new RuntimeException(e); }
        return out;
    }

    @Override
    public Medecin findById(Long id) {
        String sql = """
            SELECT u.*, m.specialite, m.numero_rpps, m.numero_ordre, m.titre, 
                   m.annees_experience, m.taux_horaire, m.disponible
            FROM utilisateurs u
            JOIN medecins m ON u.id = m.utilisateur_id
            WHERE u.id = ? AND u.type = 'MEDECIN'
            """;
        try (Connection conn = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return RowMappers.mapMedecin(rs);
                return null;
            }
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    @Override
    public void create(Medecin medecin) {
        Connection conn = null;
        try {
            conn = SessionFactory.getInstance().getConnection();
            conn.setAutoCommit(false);

            // 1. Insérer dans utilisateurs (table parente)
            String sqlUtilisateur = """
                INSERT INTO utilisateurs(nom, prenom, email, telephone, type, created_at)
                VALUES(?, ?, ?, ?, 'MEDECIN', ?)
                """;

            try (PreparedStatement psUtilisateur = conn.prepareStatement(sqlUtilisateur, Statement.RETURN_GENERATED_KEYS)) {
                psUtilisateur.setString(1, medecin.getNom());
                psUtilisateur.setString(2, medecin.getPrenom());
                psUtilisateur.setString(3, medecin.getEmail());
                psUtilisateur.setString(4, medecin.getTelephone());
                psUtilisateur.setTimestamp(5, new Timestamp(System.currentTimeMillis()));

                psUtilisateur.executeUpdate();

                try (ResultSet keys = psUtilisateur.getGeneratedKeys()) {
                    if (keys.next()) {
                        Long utilisateurId = keys.getLong(1);
                        medecin.setId(utilisateurId);

                        // 2. Insérer dans medecins (table enfant)
                        String sqlMedecin = """
                            INSERT INTO medecins(utilisateur_id, specialite, numero_rpps, numero_ordre, 
                                               titre, annees_experience, taux_horaire, disponible)
                            VALUES(?, ?, ?, ?, ?, ?, ?, ?)
                            """;

                        try (PreparedStatement psMedecin = conn.prepareStatement(sqlMedecin)) {
                            psMedecin.setLong(1, utilisateurId);
                            psMedecin.setString(2, medecin.getSpecialite());
                            psMedecin.setString(3, medecin.getNumeroRPPS());
                            psMedecin.setString(4, medecin.getNumeroOrdre());
                            psMedecin.setString(5, medecin.getTitre());
                            psMedecin.setInt(6, medecin.getAnneesExperience());
                            psMedecin.setDouble(7, medecin.getTauxHoraire());
                            psMedecin.setBoolean(8, medecin.getDisponible());

                            psMedecin.executeUpdate();
                        }
                    }
                }
            }

            conn.commit();
        } catch (SQLException e) {
            if (conn != null) {
                try { conn.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
            }
            throw new RuntimeException("Erreur lors de la création du médecin", e);
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException e) { e.printStackTrace(); }
            }
        }
    }

    @Override
    public void update(Medecin medecin) {
        Connection conn = null;
        try {
            conn = SessionFactory.getInstance().getConnection();
            conn.setAutoCommit(false);

            // 1. Mettre à jour utilisateurs
            String sqlUtilisateur = """
                UPDATE utilisateurs SET 
                    nom = ?, prenom = ?, email = ?, telephone = ?, updated_at = ?
                WHERE id = ?
                """;

            try (PreparedStatement psUtilisateur = conn.prepareStatement(sqlUtilisateur)) {
                psUtilisateur.setString(1, medecin.getNom());
                psUtilisateur.setString(2, medecin.getPrenom());
                psUtilisateur.setString(3, medecin.getEmail());
                psUtilisateur.setString(4, medecin.getTelephone());
                psUtilisateur.setTimestamp(5, new Timestamp(System.currentTimeMillis()));
                psUtilisateur.setLong(6, medecin.getId());

                psUtilisateur.executeUpdate();
            }

            // 2. Mettre à jour medecins
            String sqlMedecin = """
                UPDATE medecins SET 
                    specialite = ?, numero_rpps = ?, numero_ordre = ?, titre = ?,
                    annees_experience = ?, taux_horaire = ?, disponible = ?
                WHERE utilisateur_id = ?
                """;

            try (PreparedStatement psMedecin = conn.prepareStatement(sqlMedecin)) {
                psMedecin.setString(1, medecin.getSpecialite());
                psMedecin.setString(2, medecin.getNumeroRPPS());
                psMedecin.setString(3, medecin.getNumeroOrdre());
                psMedecin.setString(4, medecin.getTitre());
                psMedecin.setInt(5, medecin.getAnneesExperience());
                psMedecin.setDouble(6, medecin.getTauxHoraire());
                psMedecin.setBoolean(7, medecin.getDisponible());
                psMedecin.setLong(8, medecin.getId());

                psMedecin.executeUpdate();
            }

            conn.commit();
        } catch (SQLException e) {
            if (conn != null) {
                try { conn.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
            }
            throw new RuntimeException("Erreur lors de la mise à jour du médecin", e);
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException e) { e.printStackTrace(); }
            }
        }
    }

    @Override
    public void delete(Medecin medecin) {
        if (medecin != null) deleteById(medecin.getId());
    }

    @Override
    public void deleteById(Long id) {
        // Suppression en cascade grâce à ON DELETE CASCADE
        String sql = "DELETE FROM utilisateurs WHERE id = ? AND type = 'MEDECIN'";
        try (Connection conn = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            ps.executeUpdate();
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    // -------- Méthodes spécifiques --------
    @Override
    public Optional<Medecin> findByNom(String nom) {
        String sql = """
            SELECT u.*, m.specialite, m.numero_rpps, m.numero_ordre, m.titre, 
                   m.annees_experience, m.taux_horaire, m.disponible
            FROM utilisateurs u
            JOIN medecins m ON u.id = m.utilisateur_id
            WHERE u.nom = ? AND u.type = 'MEDECIN'
            """;
        try (Connection conn = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nom);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return Optional.of(RowMappers.mapMedecin(rs));
                return Optional.empty();
            }
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    @Override
    public List<Medecin> findBySpecialite(String specialite) {
        String sql = """
            SELECT u.*, m.specialite, m.numero_rpps, m.numero_ordre, m.titre, 
                   m.annees_experience, m.taux_horaire, m.disponible
            FROM utilisateurs u
            JOIN medecins m ON u.id = m.utilisateur_id
            WHERE m.specialite = ? AND u.type = 'MEDECIN'
            ORDER BY u.nom, u.prenom
            """;
        List<Medecin> out = new ArrayList<>();
        try (Connection conn = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, specialite);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) out.add(RowMappers.mapMedecin(rs));
            }
        } catch (SQLException e) { throw new RuntimeException(e); }
        return out;
    }

    @Override
    public List<String> findAllSpecialites() {
        String sql = "SELECT DISTINCT specialite FROM medecins ORDER BY specialite";
        List<String> out = new ArrayList<>();
        try (Connection conn = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) out.add(rs.getString("specialite"));
        } catch (SQLException e) { throw new RuntimeException(e); }
        return out;
    }

    @Override
    public Optional<Medecin> findByNumeroRPPS(String numeroRPPS) {
        String sql = """
            SELECT u.*, m.specialite, m.numero_rpps, m.numero_ordre, m.titre, 
                   m.annees_experience, m.taux_horaire, m.disponible
            FROM utilisateurs u
            JOIN medecins m ON u.id = m.utilisateur_id
            WHERE m.numero_rpps = ? AND u.type = 'MEDECIN'
            """;
        try (Connection conn = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, numeroRPPS);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return Optional.of(RowMappers.mapMedecin(rs));
                return Optional.empty();
            }
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    @Override
    public List<Medecin> findByDisponible(Boolean disponible) {
        String sql = """
            SELECT u.*, m.specialite, m.numero_rpps, m.numero_ordre, m.titre, 
                   m.annees_experience, m.taux_horaire, m.disponible
            FROM utilisateurs u
            JOIN medecins m ON u.id = m.utilisateur_id
            WHERE m.disponible = ? AND u.type = 'MEDECIN'
            ORDER BY u.nom, u.prenom
            """;
        List<Medecin> out = new ArrayList<>();
        try (Connection conn = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setBoolean(1, disponible);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) out.add(RowMappers.mapMedecin(rs));
            }
        } catch (SQLException e) { throw new RuntimeException(e); }
        return out;
    }

    @Override
    public Optional<Medecin> findByEmail(String email) {
        String sql = """
            SELECT u.*, m.specialite, m.numero_rpps, m.numero_ordre, m.titre, 
                   m.annees_experience, m.taux_horaire, m.disponible
            FROM utilisateurs u
            JOIN medecins m ON u.id = m.utilisateur_id
            WHERE u.email = ? AND u.type = 'MEDECIN'
            """;
        try (Connection conn = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return Optional.of(RowMappers.mapMedecin(rs));
                return Optional.empty();
            }
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    @Override
    public boolean updateDisponibilite(Long medecinId, Boolean disponible) {
        String sql = "UPDATE medecins SET disponible = ? WHERE utilisateur_id = ?";
        try (Connection conn = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setBoolean(1, disponible);
            ps.setLong(2, medecinId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { throw new RuntimeException(e); }
    }
}