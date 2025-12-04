// ma/teethcare/repository/common/RowMappers.java
package ma.teethcare.repository.common;

import ma.teethcare.entities.*;
import ma.teethcare.entities.enums.*;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;

public final class RowMappers {

    private RowMappers() {}

    // Mapper pour Utilisateur
    public static Utilisateur mapUtilisateur(ResultSet rs) throws SQLException {
        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setIdUser(rs.getLong("idUser"));
        utilisateur.setNom(rs.getString("nom"));
        utilisateur.setEmail(rs.getString("email"));
        utilisateur.setAdresse(rs.getString("adresse"));
        utilisateur.setCin(rs.getString("cin"));
        utilisateur.setTelephone(rs.getString("telephone"));
        utilisateur.setLogin(rs.getString("login"));
        utilisateur.setMotDePass(rs.getString("motDePass"));
        utilisateur.setSexe(Sexe.valueOf(rs.getString("sexe")));
        utilisateur.setLastLoginDate(rs.getDate("lastLoginDate") != null ? rs.getDate("lastLoginDate").toLocalDate() : null);
        utilisateur.setDateNaissance(rs.getDate("dateNaissance") != null ? rs.getDate("dateNaissance").toLocalDate() : null);
        utilisateur.setSalaire(rs.getDouble("salaire"));
        utilisateur.setPrime(rs.getDouble("prime"));
        utilisateur.setDateRecrutement(rs.getDate("dateRecrutement") != null ? rs.getDate("dateRecrutement").toLocalDate() : null);
        utilisateur.setSoldeConge(rs.getInt("soldeConge"));
        utilisateur.setType(TypeUtilisateur.valueOf(rs.getString("type")));
        utilisateur.setSpecialite(rs.getString("specialite"));
        utilisateur.setNumCNSS(rs.getString("numCNSS"));
        utilisateur.setCommission(rs.getDouble("commission"));
        return utilisateur;
    }

    // Mapper pour CabinetMedicale
    public static CabinetMedicale mapCabinetMedicale(ResultSet rs) throws SQLException {
        CabinetMedicale cabinet = new CabinetMedicale();
        cabinet.setIdUser(rs.getLong("idUser"));
        cabinet.setNom(rs.getString("nom"));
        cabinet.setEmail(rs.getString("email"));
        cabinet.setAdresse(rs.getString("adresse"));
        cabinet.setLogo(rs.getString("logo"));
        cabinet.setCin(rs.getString("cin"));
        cabinet.setTel1(rs.getString("tel1"));
        cabinet.setTel2(rs.getString("tel2"));
        cabinet.setSiteweb(rs.getString("siteweb"));
        cabinet.setInstagram(rs.getString("instagram"));
        return cabinet;
    }

    // Mapper pour Charges
    public static Charges mapCharges(ResultSet rs) throws SQLException {
        Charges charge = new Charges();
        charge.setId(rs.getLong("id"));
        charge.setTitre(rs.getString("titre"));
        charge.setDescription(rs.getString("description"));
        charge.setMontant(rs.getDouble("montant"));
        charge.setDate(rs.getTimestamp("date") != null ? rs.getTimestamp("date").toLocalDateTime() : null);
        return charge;
    }

    // Mapper pour Revenues
    public static Revenues mapRevenues(ResultSet rs) throws SQLException {
        Revenues revenue = new Revenues();
        revenue.setId(rs.getLong("id"));
        revenue.setTitre(rs.getString("titre"));
        revenue.setDescription(rs.getString("description"));
        revenue.setMontant(rs.getDouble("montant"));
        revenue.setDate(rs.getTimestamp("date") != null ? rs.getTimestamp("date").toLocalDateTime() : null);
        return revenue;
    }

    // Mapper pour Statistiques
    public static Statistiques mapStatistiques(ResultSet rs) throws SQLException {
        Statistiques statistique = new Statistiques();
        statistique.setId(rs.getLong("id"));
        statistique.setNom(rs.getString("nom"));
        statistique.setCategorie(CategorieStatistique.valueOf(rs.getString("categorie")));
        statistique.setChiffre(rs.getDouble("chiffre"));
        statistique.setDateCalcul(rs.getDate("dateCalcul") != null ? rs.getDate("dateCalcul").toLocalDate() : null);
        return statistique;
    }

    // Mapper pour Role
    public static Role mapRole(ResultSet rs) throws SQLException {
        Role role = new Role();
        role.setIdRole(rs.getLong("idRole"));
        role.setLibelle(rs.getString("libelle"));
        String privilegesStr = rs.getString("privileges");
        if (privilegesStr != null && !privilegesStr.isEmpty()) {
            role.setPrivileges(Arrays.asList(privilegesStr.split(",")));
        }
        return role;
    }

    // Mapper pour Notification
    public static Notification mapNotification(ResultSet rs) throws SQLException {
        Notification notification = new Notification();
        notification.setId(rs.getLong("id"));
        notification.setTitre(rs.getString("titre"));
        notification.setMessage(rs.getString("message"));
        notification.setDate(rs.getDate("date") != null ? rs.getDate("date").toLocalDate() : null);
        notification.setTime(rs.getTime("time") != null ? rs.getTime("time").toLocalTime() : null);
        notification.setType(TypeNotification.valueOf(rs.getString("type")));
        notification.setPriorite(PrioriteNotification.valueOf(rs.getString("priorite")));
        return notification;
    }

    // Mapper pour AgendaMensuel
    public static AgendaMensuel mapAgendaMensuel(ResultSet rs) throws SQLException {
        AgendaMensuel agenda = new AgendaMensuel();
        agenda.setId(rs.getLong("id"));
        agenda.setMois(Mois.valueOf(rs.getString("mois")));
        return agenda;
    }

    // Mapper pour Jour
    public static Jour mapJour(ResultSet rs) throws SQLException {
        Jour jour = new Jour();
        jour.setId(rs.getLong("id"));
        jour.setNomJour(rs.getString("nomJour"));
        jour.setDisponible(rs.getBoolean("disponible"));
        return jour;
    }
}