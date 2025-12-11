package ma.teethcare.repository.common;

import ma.teethcare.entities.*;
import ma.teethcare.entities.enums.*;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class RowMappers {

    // ==================== PATIENT ET ASSOCIÉS ====================

    public static Patient mapPatient(ResultSet rs) throws SQLException {
        Patient patient = new Patient();
        patient.setId(rs.getLong("id"));
        patient.setFirstName(rs.getString("first_name"));
        patient.setLastName(rs.getString("last_name"));
        patient.setDateOfBirth(rs.getDate("date_of_birth") != null ?
                rs.getDate("date_of_birth").toLocalDate() : null);
        patient.setGender(Patient.Gender.fromCode(rs.getString("gender")));
        patient.setPhone(rs.getString("phone"));
        patient.setEmail(rs.getString("email"));
        patient.setAddress(rs.getString("address"));
        patient.setInsuranceNumber(rs.getString("insurance_number"));
        patient.setCreatedAt(rs.getTimestamp("created_at") != null ?
                rs.getTimestamp("created_at").toLocalDateTime() : null);
        patient.setUpdatedAt(rs.getTimestamp("updated_at") != null ?
                rs.getTimestamp("updated_at").toLocalDateTime() : null);
        return patient;
    }

    public static Antecedent mapAntecedent(ResultSet rs) throws SQLException {
        Antecedent antecedent = new Antecedent();
        antecedent.setId(rs.getLong("id"));
        antecedent.setNom(rs.getString("nom"));
        antecedent.setDescription(rs.getString("description"));
        antecedent.setCategorie(CategorieAntecedent.valueOf(rs.getString("categorie")));
        antecedent.setNiveauRisque(NiveauRisque.valueOf(rs.getString("niveau_risque")));
        antecedent.setDateDiagnostic(rs.getDate("date_diagnostic") != null ?
                rs.getDate("date_diagnostic").toLocalDate() : null);
        antecedent.setCommentaire(rs.getString("commentaire"));
        antecedent.setDateCreation(rs.getTimestamp("created_at") != null ?
                rs.getTimestamp("created_at").toLocalDateTime() : null);
        antecedent.setDateMiseAJour(rs.getTimestamp("updated_at") != null ?
                rs.getTimestamp("updated_at").toLocalDateTime() : null);
        return antecedent;
    }

    // ==================== CONSULTATION ET DOSSIER ====================

    public static Consultation mapConsultation(ResultSet rs) throws SQLException {
        Consultation consultation = new Consultation();
        consultation.setId(rs.getLong("id"));
        consultation.setDate(rs.getDate("date") != null ?
                rs.getDate("date").toLocalDate() : null);
        consultation.setMotif(rs.getString("motif"));
        consultation.setObservation(rs.getString("observation"));
        consultation.setDiagnostic(rs.getString("diagnostic"));
        consultation.setPrix(rs.getDouble("prix"));
        consultation.setStatut(rs.getString("statut"));
        consultation.setCreatedAt(rs.getTimestamp("created_at") != null ?
                rs.getTimestamp("created_at").toLocalDateTime() : null);
        consultation.setUpdatedAt(rs.getTimestamp("updated_at") != null ?
                rs.getTimestamp("updated_at").toLocalDateTime() : null);

        // Mapping Patient (si jointure)
        try {
            if (hasColumn(rs, "patient_id")) {
                Patient patient = new Patient();
                patient.setId(rs.getLong("patient_id"));
                patient.setFirstName(rs.getString("patient_first_name"));
                patient.setLastName(rs.getString("patient_last_name"));
                consultation.setPatient(patient);
            }
        } catch (SQLException e) {
            // Colonne non présente, on ignore
        }

        return consultation;
    }

    public static DossierMedical mapDossierMedical(ResultSet rs) throws SQLException {
        DossierMedical dossier = new DossierMedical();
        dossier.setId(rs.getLong("id"));
        dossier.setDateCreation(rs.getDate("date_creation") != null ?
                rs.getDate("date_creation").toLocalDate() : null);
        dossier.setGroupeSanguin(rs.getString("groupe_sanguin"));
        dossier.setAllergies(rs.getString("allergies"));
        dossier.setAntecedentsFamiliaux(rs.getString("antecedents_familiaux"));
        dossier.setTraitementsEnCours(rs.getString("traitements_en_cours"));
        dossier.setRemarques(rs.getString("remarques"));
        dossier.setCreatedAt(rs.getTimestamp("created_at") != null ?
                rs.getTimestamp("created_at").toLocalDateTime() : null);
        dossier.setUpdatedAt(rs.getTimestamp("updated_at") != null ?
                rs.getTimestamp("updated_at").toLocalDateTime() : null);
        return dossier;
    }

    // ==================== FACTURE ET FINANCES ====================

    public static Facture mapFacture(ResultSet rs) throws SQLException {
        Facture facture = new Facture();
        facture.setId(rs.getLong("id"));
        facture.setNumeroFacture(rs.getString("numero_facture"));
        facture.setMontantTotal(rs.getDouble("montant_total"));
        facture.setMontantPaye(rs.getDouble("montant_paye"));
        facture.setMontantRestant(rs.getDouble("montant_restant"));
        facture.setStatut(rs.getString("statut"));
        facture.setModePaiement(rs.getString("mode_paiement"));
        facture.setDateFacturation(rs.getTimestamp("date_facturation") != null ?
                rs.getTimestamp("date_facturation").toLocalDateTime() : null);
        facture.setDateEcheance(rs.getDate("date_echeance") != null ?
                rs.getDate("date_echeance").toLocalDate() : null);
        facture.setCreatedAt(rs.getTimestamp("created_at") != null ?
                rs.getTimestamp("created_at").toLocalDateTime() : null);
        facture.setUpdatedAt(rs.getTimestamp("updated_at") != null ?
                rs.getTimestamp("updated_at").toLocalDateTime() : null);

        // Mapping Consultation
        try {
            if (hasColumn(rs, "consultation_id")) {
                Consultation consultation = new Consultation();
                consultation.setId(rs.getLong("consultation_id"));
                facture.setConsultation(consultation);
            }
        } catch (SQLException e) {
            // Colonne non présente, on ignore
        }

        return facture;
    }

    public static SituationFinanciere mapSituationFinanciere(ResultSet rs) throws SQLException {
        SituationFinanciere situation = new SituationFinanciere();
        situation.setId(rs.getLong("id"));
        situation.setSolde(rs.getDouble("solde"));
        situation.setCreditMax(rs.getDouble("credit_max"));
        situation.setDateDerniereMiseAJour(rs.getTimestamp("date_derniere_mise_a_jour") != null ?
                rs.getTimestamp("date_derniere_mise_a_jour").toLocalDateTime() : null);
        situation.setStatut(rs.getString("statut"));
        situation.setRemarques(rs.getString("remarques"));
        situation.setCreatedAt(rs.getTimestamp("created_at") != null ?
                rs.getTimestamp("created_at").toLocalDateTime() : null);
        situation.setUpdatedAt(rs.getTimestamp("updated_at") != null ?
                rs.getTimestamp("updated_at").toLocalDateTime() : null);
        return situation;
    }

    public static Charges mapCharges(ResultSet rs) throws SQLException {
        Charges charge = new Charges();
        charge.setId(rs.getLong("id"));
        charge.setLibelle(rs.getString("libelle"));
        charge.setDescription(rs.getString("description"));
        charge.setMontant(rs.getDouble("montant"));
        charge.setCategorie(rs.getString("categorie"));
        charge.setDate(rs.getDate("date") != null ?
                rs.getDate("date").toLocalDate() : null);
        charge.setCreatedAt(rs.getTimestamp("created_at") != null ?
                rs.getTimestamp("created_at").toLocalDateTime() : null);
        return charge;
    }

    public static Revenues mapRevenues(ResultSet rs) throws SQLException {
        Revenues revenue = new Revenues();
        revenue.setId(rs.getLong("id"));
        revenue.setLibelle(rs.getString("libelle"));
        revenue.setDescription(rs.getString("description"));
        revenue.setMontant(rs.getDouble("montant"));
        revenue.setCategorie(rs.getString("categorie"));
        revenue.setDate(rs.getDate("date") != null ?
                rs.getDate("date").toLocalDate() : null);
        revenue.setCreatedAt(rs.getTimestamp("created_at") != null ?
                rs.getTimestamp("created_at").toLocalDateTime() : null);
        return revenue;
    }

    // ==================== ORDONNANCE ET MÉDICAMENTS ====================

    public static Ordonnance mapOrdonnance(ResultSet rs) throws SQLException {
        Ordonnance ordonnance = new Ordonnance();
        ordonnance.setId(rs.getLong("id"));
        ordonnance.setDate(rs.getDate("date") != null ?
                rs.getDate("date").toLocalDate() : null);
        ordonnance.setInstructions(rs.getString("instructions"));
        ordonnance.setDureeValidite(rs.getInt("duree_validite"));
        ordonnance.setCreatedAt(rs.getTimestamp("created_at") != null ?
                rs.getTimestamp("created_at").toLocalDateTime() : null);
        ordonnance.setUpdatedAt(rs.getTimestamp("updated_at") != null ?
                rs.getTimestamp("updated_at").toLocalDateTime() : null);
        return ordonnance;
    }

    public static Medicament mapMedicament(ResultSet rs) throws SQLException {
        Medicament medicament = new Medicament();
        medicament.setId(rs.getLong("id"));
        medicament.setNom(rs.getString("nom"));
        medicament.setType(rs.getString("type"));
        medicament.setForme(rs.getString("forme"));
        medicament.setPrix(rs.getDouble("prix"));
        medicament.setCodeCIP(rs.getString("code_cip"));
        medicament.setLaboratoire(rs.getString("laboratoire"));
        medicament.setContreIndications(rs.getString("contre_indications"));
        medicament.setEffetsSecondaires(rs.getString("effets_secondaires"));
        medicament.setCreatedAt(rs.getTimestamp("created_at") != null ?
                rs.getTimestamp("created_at").toLocalDateTime() : null);
        medicament.setUpdatedAt(rs.getTimestamp("updated_at") != null ?
                rs.getTimestamp("updated_at").toLocalDateTime() : null);
        return medicament;
    }

    public static Prescription mapPrescription(ResultSet rs) throws SQLException {
        Prescription prescription = new Prescription();
        prescription.setId(rs.getLong("id"));
        prescription.setQuantite(rs.getInt("quantite"));
        prescription.setPosologie(rs.getString("posologie"));
        prescription.setDureeTraitement(rs.getInt("duree_traitement"));
        prescription.setCreatedAt(rs.getTimestamp("created_at") != null ?
                rs.getTimestamp("created_at").toLocalDateTime() : null);
        prescription.setUpdatedAt(rs.getTimestamp("updated_at") != null ?
                rs.getTimestamp("updated_at").toLocalDateTime() : null);

        // Mapping Ordonnance
        try {
            if (hasColumn(rs, "ordonnance_id")) {
                Ordonnance ordonnance = new Ordonnance();
                ordonnance.setId(rs.getLong("ordonnance_id"));
                prescription.setOrdonnance(ordonnance);
            }
        } catch (SQLException e) {
            // Colonne non présente, on ignore
        }

        // Mapping Medicament
        try {
            if (hasColumn(rs, "medicament_id")) {
                Medicament medicament = new Medicament();
                medicament.setId(rs.getLong("medicament_id"));
                medicament.setNom(rs.getString("medicament_nom"));
                medicament.setPrix(rs.getDouble("medicament_prix"));
                prescription.setMedicament(medicament);
            }
        } catch (SQLException e) {
            // Colonne non présente, on ignore
        }

        return prescription;
    }

    // ==================== RDV ET AGENDA ====================

    public static RDV mapRDV(ResultSet rs) throws SQLException {
        RDV rdv = new RDV();
        rdv.setId(rs.getLong("id"));
        rdv.setDateRdv(rs.getTimestamp("date_rdv") != null ?
                rs.getTimestamp("date_rdv").toLocalDateTime() : null);
        rdv.setStatut(rs.getString("statut"));
        rdv.setNotes(rs.getString("notes"));
        rdv.setTypeConsultation(rs.getString("type_consultation"));
        rdv.setCreatedAt(rs.getTimestamp("created_at") != null ?
                rs.getTimestamp("created_at").toLocalDateTime() : null);
        rdv.setUpdatedAt(rs.getTimestamp("updated_at") != null ?
                rs.getTimestamp("updated_at").toLocalDateTime() : null);

        // Mapping Patient
        try {
            if (hasColumn(rs, "patient_id")) {
                Patient patient = new Patient();
                patient.setId(rs.getLong("patient_id"));
                patient.setFirstName(rs.getString("patient_first_name"));
                patient.setLastName(rs.getString("patient_last_name"));
                rdv.setPatient(patient);
            }
        } catch (SQLException e) {
            // Colonne non présente, on ignore
        }

        // Mapping Medecin
        try {
            if (hasColumn(rs, "medecin_id")) {
                Medecin medecin = new Medecin();
                medecin.setId(rs.getLong("medecin_id"));
                medecin.setNom(rs.getString("medecin_nom"));
                medecin.setPrenom(rs.getString("medecin_prenom"));
                rdv.setMedecin(medecin);
            }
        } catch (SQLException e) {
            // Colonne non présente, on ignore
        }

        return rdv;
    }

    public static AgendaMensuel mapAgendaMensuel(ResultSet rs) throws SQLException {
        AgendaMensuel agenda = new AgendaMensuel();
        agenda.setId(rs.getLong("id"));
        agenda.setMois(Mois.valueOf(rs.getString("mois")));
        agenda.setAnnee(rs.getInt("annee"));
        agenda.setCreatedAt(rs.getTimestamp("created_at") != null ?
                rs.getTimestamp("created_at").toLocalDateTime() : null);
        agenda.setUpdatedAt(rs.getTimestamp("updated_at") != null ?
                rs.getTimestamp("updated_at").toLocalDateTime() : null);
        return agenda;
    }

    // ==================== ACTES ET INTERVENTIONS ====================

    public static Acte mapActe(ResultSet rs) throws SQLException {
        Acte acte = new Acte();
        acte.setIdActe(rs.getLong("id"));  // Note: setIdActe() pas setId()
        acte.setLibelle(rs.getString("libelle"));
        acte.setCategorie(rs.getString("categorie"));
        acte.setPrixDeBase(rs.getDouble("prix_base"));
        acte.setCreatedAt(rs.getTimestamp("created_at") != null ?
                rs.getTimestamp("created_at").toLocalDateTime() : null);
        acte.setUpdatedAt(rs.getTimestamp("updated_at") != null ?
                rs.getTimestamp("updated_at").toLocalDateTime() : null);
        return acte;
    }

    public static InterventionMedecin mapInterventionMedecin(ResultSet rs) throws SQLException {
        InterventionMedecin intervention = new InterventionMedecin();
        intervention.setId(rs.getLong("id"));
        intervention.setDateIntervention(rs.getDate("date_intervention") != null ?
                rs.getDate("date_intervention").toLocalDate() : null);
        intervention.setTypeIntervention(rs.getString("type_intervention"));
        intervention.setDescription(rs.getString("description"));
        intervention.setDuree(rs.getInt("duree"));
        intervention.setCout(rs.getDouble("cout"));
        intervention.setCreatedAt(rs.getTimestamp("created_at") != null ?
                rs.getTimestamp("created_at").toLocalDateTime() : null);
        intervention.setUpdatedAt(rs.getTimestamp("updated_at") != null ?
                rs.getTimestamp("updated_at").toLocalDateTime() : null);

        // Mapping Consultation
        try {
            if (hasColumn(rs, "consultation_id")) {
                Consultation consultation = new Consultation();
                consultation.setId(rs.getLong("consultation_id"));
                consultation.setDate(rs.getDate("consultation_date") != null ?
                        rs.getDate("consultation_date").toLocalDate() : null);
                intervention.setConsultation(consultation);
            }
        } catch (SQLException e) {
            // Colonne non présente, on ignore
        }

        // Mapping Medecin
        try {
            if (hasColumn(rs, "medecin_id")) {
                Medecin medecin = new Medecin();
                medecin.setId(rs.getLong("medecin_id"));
                medecin.setNom(rs.getString("medecin_nom"));
                medecin.setPrenom(rs.getString("medecin_prenom"));
                intervention.setMedecin(medecin);
            }
        } catch (SQLException e) {
            // Colonne non présente, on ignore
        }

        return intervention;
    }

    // ==================== CERTIFICATS ====================

    public static Certificat mapCertificat(ResultSet rs) throws SQLException {
        Certificat certificat = new Certificat();
        certificat.setId(rs.getLong("id"));
        certificat.setDateEmission(rs.getDate("date_emission") != null ?
                rs.getDate("date_emission").toLocalDate() : null);
        certificat.setDateExpiration(rs.getDate("date_expiration") != null ?
                rs.getDate("date_expiration").toLocalDate() : null);
        certificat.setType(rs.getString("type"));
        certificat.setDescription(rs.getString("description"));
        certificat.setCreatedAt(rs.getTimestamp("created_at") != null ?
                rs.getTimestamp("created_at").toLocalDateTime() : null);
        certificat.setUpdatedAt(rs.getTimestamp("updated_at") != null ?
                rs.getTimestamp("updated_at").toLocalDateTime() : null);

        // Mapping DossierMedical
        try {
            if (hasColumn(rs, "dossier_medical_id")) {
                DossierMedical dossier = new DossierMedical();
                dossier.setId(rs.getLong("dossier_id"));
                dossier.setDateCreation(rs.getDate("dossier_date") != null ?
                        rs.getDate("dossier_date").toLocalDate() : null);
                certificat.setDossierMedical(dossier);
            }
        } catch (SQLException e) {
            // Colonne non présente, on ignore
        }

        // Mapping Consultation
        try {
            if (hasColumn(rs, "consultation_id")) {
                Consultation consultation = new Consultation();
                consultation.setId(rs.getLong("consultation_id"));
                consultation.setDate(rs.getDate("consultation_date") != null ?
                        rs.getDate("consultation_date").toLocalDate() : null);
                certificat.setConsultation(consultation);
            }
        } catch (SQLException e) {
            // Colonne non présente, on ignore
        }

        return certificat;
    }

    // ==================== FILE D'ATTENTE ====================

    public static FileAttente mapFileAttente(ResultSet rs) throws SQLException {
        FileAttente file = new FileAttente();
        file.setId(rs.getLong("id"));
        file.setDateArrivee(rs.getTimestamp("date_arrivee") != null ?
                rs.getTimestamp("date_arrivee").toLocalDateTime() : null);
        file.setPosition(rs.getInt("position"));
        file.setStatut(rs.getString("statut"));
        file.setPriorite(rs.getString("priorite"));
        file.setCreatedAt(rs.getTimestamp("created_at") != null ?
                rs.getTimestamp("created_at").toLocalDateTime() : null);
        file.setUpdatedAt(rs.getTimestamp("updated_at") != null ?
                rs.getTimestamp("updated_at").toLocalDateTime() : null);

        // Mapping Patient
        try {
            if (hasColumn(rs, "patient_id")) {
                Patient patient = new Patient();
                patient.setId(rs.getLong("patient_id"));
                patient.setFirstName(rs.getString("first_name"));
                patient.setLastName(rs.getString("last_name"));
                file.setPatient(patient);
            }
        } catch (SQLException e) {
            // Colonne non présente, on ignore
        }

        return file;
    }

    // ==================== UTILISATEURS ET STAFF ====================

    public static Utilisateur mapUtilisateur(ResultSet rs) throws SQLException {
        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setId(rs.getLong("id"));
        utilisateur.setNom(rs.getString("nom"));
        utilisateur.setPrenom(rs.getString("prenom"));
        utilisateur.setEmail(rs.getString("email"));
        utilisateur.setTelephone(rs.getString("telephone"));
        utilisateur.setType(TypeUtilisateur.valueOf(rs.getString("type")));
        utilisateur.setDateNaissance(rs.getDate("date_naissance") != null ?
                rs.getDate("date_naissance").toLocalDate() : null);
        utilisateur.setAdresse(rs.getString("adresse"));
        utilisateur.setDateCreation(rs.getTimestamp("created_at") != null ?
                rs.getTimestamp("created_at").toLocalDateTime() : null);
        utilisateur.setDateMiseAJour(rs.getTimestamp("updated_at") != null ?
                rs.getTimestamp("updated_at").toLocalDateTime() : null);
        return utilisateur;
    }

    public static Staff mapStaff(ResultSet rs) throws SQLException {
        Staff staff = new Staff();
        staff.setId(rs.getLong("id"));
        staff.setNom(rs.getString("nom"));
        staff.setPrenom(rs.getString("prenom"));
        staff.setEmail(rs.getString("email"));
        staff.setTelephone(rs.getString("telephone"));
        staff.setSalaire(rs.getDouble("salaire"));
        staff.setPrime(rs.getDouble("prime"));
        staff.setDateRecrutement(rs.getDate("date_recrutement") != null ?
                rs.getDate("date_recrutement").toLocalDate() : null);
        staff.setPoste(rs.getString("poste"));
        staff.setCreatedAt(rs.getTimestamp("created_at") != null ?
                rs.getTimestamp("created_at").toLocalDateTime() : null);
        staff.setUpdatedAt(rs.getTimestamp("updated_at") != null ?
                rs.getTimestamp("updated_at").toLocalDateTime() : null);
        return staff;
    }

    public static Medecin mapMedecin(ResultSet rs) throws SQLException {
        Medecin medecin = new Medecin();
        // Hérité de Utilisateur
        medecin.setId(rs.getLong("id"));
        medecin.setNom(rs.getString("nom"));
        medecin.setPrenom(rs.getString("prenom"));
        medecin.setEmail(rs.getString("email"));
        medecin.setTelephone(rs.getString("telephone"));

        // Spécifique à Medecin
        medecin.setSpecialite(rs.getString("specialite"));
        medecin.setNumeroRPPS(rs.getString("numero_rpps"));
        medecin.setNumeroOrdre(rs.getString("numero_ordre"));
        medecin.setTitre(rs.getString("titre"));
        medecin.setAnneesExperience(rs.getInt("annees_experience"));
        medecin.setTauxHoraire(rs.getDouble("taux_horaire"));
        medecin.setDisponible(rs.getBoolean("disponible"));

        // Dates
        medecin.setDateCreation(rs.getTimestamp("created_at") != null ?
                rs.getTimestamp("created_at").toLocalDateTime() : null);
        medecin.setDateMiseAJour(rs.getTimestamp("updated_at") != null ?
                rs.getTimestamp("updated_at").toLocalDateTime() : null);

        return medecin;
    }

    public static Admin mapAdmin(ResultSet rs) throws SQLException {
        Admin admin = new Admin();
        admin.setId(rs.getLong("id"));
        admin.setNom(rs.getString("nom"));
        admin.setPrenom(rs.getString("prenom"));
        admin.setEmail(rs.getString("email"));
        admin.setTelephone(rs.getString("telephone"));
        admin.setNiveauAcces(rs.getInt("niveau_acces"));
        admin.setCreatedAt(rs.getTimestamp("created_at") != null ?
                rs.getTimestamp("created_at").toLocalDateTime() : null);
        admin.setUpdatedAt(rs.getTimestamp("updated_at") != null ?
                rs.getTimestamp("updated_at").toLocalDateTime() : null);
        return admin;
    }

    public static Secretaire mapSecretaire(ResultSet rs) throws SQLException {
        Secretaire secretaire = new Secretaire();
        secretaire.setId(rs.getLong("id"));
        secretaire.setNom(rs.getString("nom"));
        secretaire.setPrenom(rs.getString("prenom"));
        secretaire.setEmail(rs.getString("email"));
        secretaire.setTelephone(rs.getString("telephone"));
        secretaire.setNumCaisse(rs.getString("num_caisse"));
        secretaire.setCommission(rs.getDouble("commission"));
        secretaire.setCreatedAt(rs.getTimestamp("created_at") != null ?
                rs.getTimestamp("created_at").toLocalDateTime() : null);
        secretaire.setUpdatedAt(rs.getTimestamp("updated_at") != null ?
                rs.getTimestamp("updated_at").toLocalDateTime() : null);
        return secretaire;
    }

    // ==================== AUTRES ENTITÉS ====================

    public static CabinetMedicale mapCabinetMedicale(ResultSet rs) throws SQLException {
        CabinetMedicale cabinet = new CabinetMedicale();
        cabinet.setId(rs.getLong("id"));
        cabinet.setNom(rs.getString("nom"));
        cabinet.setEmail(rs.getString("email"));
        cabinet.setTelephone(rs.getString("telephone"));
        cabinet.setAdresse(rs.getString("adresse"));
        cabinet.setLogo(rs.getString("logo"));
        cabinet.setSiret(rs.getString("siret"));
        cabinet.setCreatedAt(rs.getTimestamp("created_at") != null ?
                rs.getTimestamp("created_at").toLocalDateTime() : null);
        cabinet.setUpdatedAt(rs.getTimestamp("updated_at") != null ?
                rs.getTimestamp("updated_at").toLocalDateTime() : null);
        return cabinet;
    }

    public static Notification mapNotification(ResultSet rs) throws SQLException {
        Notification notification = new Notification();
        notification.setId(rs.getLong("id"));
        notification.setTitre(rs.getString("titre"));
        notification.setMessage(rs.getString("message"));
        notification.setType(TypeNotification.valueOf(rs.getString("type")));
        notification.setPriorite(PrioriteNotification.valueOf(rs.getString("priorite")));
        notification.setDate(rs.getDate("date") != null ?
                rs.getDate("date").toLocalDate() : null);
        notification.setHeure(rs.getTime("heure") != null ?
                rs.getTime("heure").toLocalTime() : null);
        notification.setLu(rs.getBoolean("lu"));
        notification.setCreatedAt(rs.getTimestamp("created_at") != null ?
                rs.getTimestamp("created_at").toLocalDateTime() : null);
        return notification;
    }

    public static Statistiques mapStatistiques(ResultSet rs) throws SQLException {
        Statistiques stats = new Statistiques();
        stats.setId(rs.getLong("id"));
        stats.setNom(rs.getString("nom"));
        stats.setCategorie(CategorieStatistique.valueOf(rs.getString("categorie")));
        stats.setChiffre(rs.getDouble("chiffre"));
        stats.setDate(rs.getDate("date") != null ?
                rs.getDate("date").toLocalDate() : null);
        stats.setCreatedAt(rs.getTimestamp("created_at") != null ?
                rs.getTimestamp("created_at").toLocalDateTime() : null);
        return stats;
    }

    public static Role mapRole(ResultSet rs) throws SQLException {
        Role role = new Role();
        role.setId(rs.getLong("id"));
        role.setNom(rs.getString("nom"));
        role.setDescription(rs.getString("description"));
        role.setCreatedAt(rs.getTimestamp("created_at") != null ?
                rs.getTimestamp("created_at").toLocalDateTime() : null);
        return role;
    }

    public static Logs mapLogs(ResultSet rs) throws SQLException {
        Logs log = new Logs();
        log.setId(rs.getLong("id"));
        log.setAction(rs.getString("action"));
        log.setModule(rs.getString("module"));
        log.setUtilisateurId(rs.getLong("utilisateur_id"));
        log.setDetails(rs.getString("details"));
        log.setIpAdresse(rs.getString("ip_adresse"));
        log.setDateAction(rs.getTimestamp("date_action") != null ?
                rs.getTimestamp("date_action").toLocalDateTime() : null);
        return log;
    }

    public static Adresse mapAdresse(ResultSet rs) throws SQLException {
        Adresse adresse = new Adresse();
        adresse.setId(rs.getLong("id"));
        adresse.setRue(rs.getString("rue"));
        adresse.setVille(rs.getString("ville"));
        adresse.setCodePostal(rs.getString("code_postal"));
        adresse.setPays(rs.getString("pays"));
        adresse.setCreatedAt(rs.getTimestamp("created_at") != null ?
                rs.getTimestamp("created_at").toLocalDateTime() : null);
        adresse.setUpdatedAt(rs.getTimestamp("updated_at") != null ?
                rs.getTimestamp("updated_at").toLocalDateTime() : null);
        return adresse;
    }

    public static Jour mapJour(ResultSet rs) throws SQLException {
        Jour jour = new Jour();
        jour.setId(rs.getLong("id"));
        jour.setNom(rs.getString("nom"));
        jour.setDate(rs.getDate("date") != null ?
                rs.getDate("date").toLocalDate() : null);
        jour.setHeureDebut(rs.getTime("heure_debut") != null ?
                rs.getTime("heure_debut").toLocalTime() : null);
        jour.setHeureFin(rs.getTime("heure_fin") != null ?
                rs.getTime("heure_fin").toLocalTime() : null);
        jour.setDisponible(rs.getBoolean("disponible"));
        return jour;
    }

    // ==================== MÉTHODE UTILITAIRE ====================

    private static boolean hasColumn(ResultSet rs, String columnName) {
        try {
            rs.findColumn(columnName);
            return true;
        } catch (SQLException e) {
            return false;
        }
    }
}