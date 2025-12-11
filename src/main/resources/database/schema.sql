-- Table Patients
CREATE TABLE IF NOT EXISTS Patients (
                                        id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                        nom VARCHAR(80)      NOT NULL,
    prenom VARCHAR(80)   NOT NULL,
    adresse VARCHAR(150),
    telephone VARCHAR(30),
    email VARCHAR(120),
    dateNaissance DATE,
    dateCreation DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    sexe ENUM('Homme','Femme') NOT NULL,
    assurance ENUM('CNOPS','CNSS','Autre','Aucune') NOT NULL,
    UNIQUE KEY uk_patients_email (email),
    KEY idx_patients_nom (nom, prenom)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Table Antecedents
CREATE TABLE Antecedents (
                             id BIGINT AUTO_INCREMENT PRIMARY KEY,
                             nom VARCHAR(100) NOT NULL,
                             categorie ENUM(
        'ALLERGIE',
        'MALADIE_CHRONIQUE',
        'CONTRE_INDICATION',
        'TRAITEMENT_EN_COURS',
        'ANTECEDENT_CHIRURGICAL',
        'ANTECEDENT_INFECTIEUX',
        'ANTECEDENT_DENTAIRE',
        'HABITUDE_DE_VIE',
        'AUTRE'
    ) NOT NULL,
                             niveauRisque ENUM('FAIBLE', 'MODERE', 'ELEVE', 'CRITIQUE') NOT NULL
);

-- Table Association Patient-Antecedent
CREATE TABLE Patient_Antecedents (
                                     patient_id BIGINT NOT NULL,
                                     antecedent_id BIGINT NOT NULL,
                                     PRIMARY KEY (patient_id, antecedent_id),
                                     FOREIGN KEY (patient_id) REFERENCES Patients(id) ON DELETE CASCADE,
                                     FOREIGN KEY (antecedent_id) REFERENCES Antecedents(id) ON DELETE CASCADE
);

-- Table Utilisateurs (Admin, Médecin, Secrétaire)
CREATE TABLE Utilisateurs (
                              id BIGINT AUTO_INCREMENT PRIMARY KEY,
                              nom VARCHAR(80) NOT NULL,
                              prenom VARCHAR(80) NOT NULL,
                              email VARCHAR(120) NOT NULL UNIQUE,
                              mot_de_passe VARCHAR(255) NOT NULL,
                              role ENUM('ADMIN', 'MEDECIN', 'SECRETAIRE') NOT NULL,
                              telephone VARCHAR(30),
                              specialite VARCHAR(100), -- pour les médecins
                              date_inscription DATETIME DEFAULT CURRENT_TIMESTAMP,
                              KEY idx_utilisateurs_role (role)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Table RDV (Rendez-vous)
CREATE TABLE RDV (
                     id BIGINT AUTO_INCREMENT PRIMARY KEY,
                     patient_id BIGINT NOT NULL,
                     utilisateur_id BIGINT NOT NULL, -- médecin ou secrétaire qui crée
                     date_rdv DATETIME NOT NULL,
                     duree INT NOT NULL DEFAULT 30 COMMENT 'Durée en minutes',
                     statut ENUM('PLANIFIE', 'CONFIRME', 'ANNULE', 'TERMINE') DEFAULT 'PLANIFIE',
                     notes TEXT,
                     FOREIGN KEY (patient_id) REFERENCES Patients(id) ON DELETE CASCADE,
                     FOREIGN KEY (utilisateur_id) REFERENCES Utilisateurs(id) ON DELETE CASCADE,
                     KEY idx_rdv_date (date_rdv, statut)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Table Consultations
CREATE TABLE Consultations (
                               id BIGINT AUTO_INCREMENT PRIMARY KEY,
                               patient_id BIGINT NOT NULL,
                               utilisateur_id BIGINT NOT NULL COMMENT 'Médecin qui consulte',
                               rdv_id BIGINT,
                               date_consultation DATETIME DEFAULT CURRENT_TIMESTAMP,
                               diagnostic TEXT,
                               traitement TEXT,
                               notes TEXT,
                               FOREIGN KEY (patient_id) REFERENCES Patients(id) ON DELETE CASCADE,
                               FOREIGN KEY (utilisateur_id) REFERENCES Utilisateurs(id) ON DELETE CASCADE,
                               FOREIGN KEY (rdv_id) REFERENCES RDV(id) ON DELETE SET NULL,
                               KEY idx_consultations_patient (patient_id, date_consultation)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Table Factures
CREATE TABLE Factures (
                          id BIGINT AUTO_INCREMENT PRIMARY KEY,
                          consultation_id BIGINT NOT NULL UNIQUE,
                          montant DECIMAL(10,2) NOT NULL CHECK (montant >= 0),
                          date_facture DATETIME DEFAULT CURRENT_TIMESTAMP,
                          statut ENUM('NON_PAYEE', 'PARTIELLEMENT_PAYEE', 'PAYEE') DEFAULT 'NON_PAYEE',
                          mode_paiement ENUM('ESPECES', 'CHEQUE', 'CARTE', 'VIREMENT', 'AUTRE'),
                          FOREIGN KEY (consultation_id) REFERENCES Consultations(id) ON DELETE CASCADE,
                          KEY idx_factures_statut (statut)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Table Ordonnances
CREATE TABLE Ordonnances (
                             id BIGINT AUTO_INCREMENT PRIMARY KEY,
                             consultation_id BIGINT NOT NULL,
                             medicaments TEXT NOT NULL,
                             posologie TEXT,
                             date_prescription DATETIME DEFAULT CURRENT_TIMESTAMP,
                             FOREIGN KEY (consultation_id) REFERENCES Consultations(id) ON DELETE CASCADE,
                             KEY idx_ordonnances_consultation (consultation_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;