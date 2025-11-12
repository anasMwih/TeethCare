package ma.teethcare.modules.patient.fileBase_implementation;

import ma.teethcare.modules.patient.api.PatientRepository;
import ma.teethcare.entities.Patient;
import ma.teethcare.config.Db;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PatientRepositoryImpl implements PatientRepository {

    // === CRUD BASIQUE ===

    @Override
    public Patient save(Patient patient) {
        String sql = "INSERT INTO patients (nom, prenom, date_naissance, sexe, telephone, email, adresse, assurance) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = Db.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            setPatientParameters(stmt, patient);

            int affectedRows = stmt.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        patient.setId(rs.getLong(1));
                    }
                }
            }
            return patient;
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la sauvegarde du patient: " + e.getMessage(), e);
        }
    }

    @Override
    public Patient update(Patient patient) {
        String sql = "UPDATE patients SET nom=?, prenom=?, date_naissance=?, sexe=?, telephone=?, email=?, adresse=?, assurance=? " +
                "WHERE id=?";

        try (Connection conn = Db.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            setPatientParameters(stmt, patient);
            stmt.setLong(9, patient.getId());

            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new RuntimeException("Aucun patient trouvé avec l'ID: " + patient.getId());
            }
            return patient;
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la modification du patient: " + e.getMessage(), e);
        }
    }

    @Override
    public void delete(Patient patient) {
        deleteById(patient.getId());
    }

    @Override
    public void deleteById(Long id) {
        String sql = "DELETE FROM patients WHERE id=?";

        try (Connection conn = Db.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);
            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new RuntimeException("Aucun patient trouvé avec l'ID: " + id);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la suppression du patient: " + e.getMessage(), e);
        }
    }

    @Override
    public Optional<Patient> findById(Long id) {
        String sql = "SELECT * FROM patients WHERE id=?";

        try (Connection conn = Db.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return Optional.of(mapResultSetToPatient(rs));
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la recherche du patient: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Patient> findAll() {
        List<Patient> patients = new ArrayList<>();
        String sql = "SELECT * FROM patients ORDER BY nom, prenom";

        try (Connection conn = Db.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                patients.add(mapResultSetToPatient(rs));
            }
            return patients;
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la récupération des patients: " + e.getMessage(), e);
        }
    }

    @Override
    public Long count() {
        String sql = "SELECT COUNT(*) FROM patients";

        try (Connection conn = Db.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            if (rs.next()) {
                return rs.getLong(1);
            }
            return 0L;
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors du comptage des patients: " + e.getMessage(), e);
        }
    }

    // === RECHERCHES SPÉCIFIQUES ===

    @Override
    public List<Patient> findByNom(String nom) {
        List<Patient> patients = new ArrayList<>();
        String sql = "SELECT * FROM patients WHERE nom LIKE ? ORDER BY nom, prenom";

        try (Connection conn = Db.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, "%" + nom + "%");
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                patients.add(mapResultSetToPatient(rs));
            }
            return patients;
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la recherche par nom: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Patient> findByPrenom(String prenom) {
        List<Patient> patients = new ArrayList<>();
        String sql = "SELECT * FROM patients WHERE prenom LIKE ? ORDER BY nom, prenom";

        try (Connection conn = Db.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, "%" + prenom + "%");
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                patients.add(mapResultSetToPatient(rs));
            }
            return patients;
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la recherche par prénom: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Patient> findByNomComplet(String nom, String prenom) {
        List<Patient> patients = new ArrayList<>();
        String sql = "SELECT * FROM patients WHERE nom LIKE ? AND prenom LIKE ? ORDER BY nom, prenom";

        try (Connection conn = Db.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, "%" + nom + "%");
            stmt.setString(2, "%" + prenom + "%");
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                patients.add(mapResultSetToPatient(rs));
            }
            return patients;
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la recherche par nom complet: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Patient> findByTelephone(String telephone) {
        List<Patient> patients = new ArrayList<>();
        String sql = "SELECT * FROM patients WHERE telephone LIKE ? ORDER BY nom, prenom";

        try (Connection conn = Db.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, "%" + telephone + "%");
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                patients.add(mapResultSetToPatient(rs));
            }
            return patients;
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la recherche par téléphone: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Patient> findByEmail(String email) {
        List<Patient> patients = new ArrayList<>();
        String sql = "SELECT * FROM patients WHERE email LIKE ? ORDER BY nom, prenom";

        try (Connection conn = Db.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, "%" + email + "%");
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                patients.add(mapResultSetToPatient(rs));
            }
            return patients;
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la recherche par email: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Patient> findByAssurance(String assurance) {
        List<Patient> patients = new ArrayList<>();
        String sql = "SELECT * FROM patients WHERE assurance LIKE ? ORDER BY nom, prenom";

        try (Connection conn = Db.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, "%" + assurance + "%");
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                patients.add(mapResultSetToPatient(rs));
            }
            return patients;
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la recherche par assurance: " + e.getMessage(), e);
        }
    }

    // === STATISTIQUES ===

    @Override
    public List<Patient> findPatientsRecents(int jours) {
        List<Patient> patients = new ArrayList<>();
        String sql = "SELECT * FROM patients WHERE date_creation >= DATE_SUB(CURDATE(), INTERVAL ? DAY) ORDER BY date_creation DESC";

        try (Connection conn = Db.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, jours);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                patients.add(mapResultSetToPatient(rs));
            }
            return patients;
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la recherche des patients récents: " + e.getMessage(), e);
        }
    }

    @Override
    public Optional<Patient> findByTelephoneExact(String telephone) {
        String sql = "SELECT * FROM patients WHERE telephone = ?";

        try (Connection conn = Db.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, telephone);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return Optional.of(mapResultSetToPatient(rs));
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la recherche par téléphone exact: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean existsByEmail(String email) {
        String sql = "SELECT COUNT(*) FROM patients WHERE email = ?";

        try (Connection conn = Db.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getLong(1) > 0;
            }
            return false;
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la vérification d'existence par email: " + e.getMessage(), e);
        }
    }

    // === MÉTHODES UTILITAIRES ===

    private void setPatientParameters(PreparedStatement stmt, Patient patient) throws SQLException {
        stmt.setString(1, patient.getNom());
        stmt.setString(2, patient.getPrenom());
        stmt.setDate(3, patient.getDateNaissance() != null ? Date.valueOf(patient.getDateNaissance()) : null);
        stmt.setString(4, patient.getSexe());
        stmt.setString(5, patient.getTelephone());
        stmt.setString(6, patient.getEmail());
        stmt.setString(7, patient.getAdresse());
        stmt.setString(8, patient.getAssurance());
    }

    private Patient mapResultSetToPatient(ResultSet rs) throws SQLException {
        Patient patient = new Patient();
        patient.setId(rs.getLong("id"));
        patient.setNom(rs.getString("nom"));
        patient.setPrenom(rs.getString("prenom"));

        Date dateNaissance = rs.getDate("date_naissance");
        patient.setDateNaissance(dateNaissance != null ? dateNaissance.toLocalDate() : null);

        patient.setSexe(rs.getString("sexe"));
        patient.setTelephone(rs.getString("telephone"));
        patient.setEmail(rs.getString("email"));
        patient.setAdresse(rs.getString("adresse"));
        patient.setAssurance(rs.getString("assurance"));

        return patient;
    }
}