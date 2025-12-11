-- 1. Insertion des 7 utilisateurs
INSERT INTO Utilisateurs (nom, prenom, email, mot_de_passe, role, telephone, specialite) VALUES
                                                                                             ('Admin', 'Super', 'admin@teethcare.ma', '$2y$10$xxxxxxxx', 'ADMIN', '0611111111', NULL),
                                                                                             ('Smith', 'John', 'smith@teethcare.ma', '$2y$10$xxxxxxxx', 'MEDECIN', '0622222222', 'Orthodontie'),
                                                                                             ('Martin', 'Sophie', 'martin@teethcare.ma', '$2y$10$xxxxxxxx', 'MEDECIN', '0633333333', 'Chirurgie dentaire'),
                                                                                             ('Lee', 'David', 'lee@teethcare.ma', '$2y$10$xxxxxxxx', 'MEDECIN', '0644444444', 'Pédodontie'),
                                                                                             ('Sec', 'Emma', 'emma@teethcare.ma', '$2y$10$xxxxxxxx', 'SECRETAIRE', '0655555555', NULL),
                                                                                             ('Sec', 'Paul', 'paul@teethcare.ma', '$2y$10$xxxxxxxx', 'SECRETAIRE', '0666666666', NULL),
                                                                                             ('Sec', 'Lina', 'lina@teethcare.ma', '$2y$10$xxxxxxxx', 'SECRETAIRE', '0677777777', NULL);

-- 2. Insertion des 8 patients
INSERT INTO Patients (nom, prenom, adresse, telephone, email, dateNaissance, sexe, assurance) VALUES
                                                                                                  ('Dupont', 'Jean', '123 Rue de Paris, Casablanca', '0612345678', 'jean.dupont@mail.com', '1980-05-15', 'Homme', 'CNSS'),
                                                                                                  ('Martin', 'Sophie', '456 Av. Mohammed V, Rabat', '0623456789', 'sophie.martin@mail.com', '1992-08-22', 'Femme', 'CNOPS'),
                                                                                                  ('Benali', 'Karim', '789 Bd Hassan II, Marrakech', '0634567890', 'karim.benali@mail.com', '1975-03-10', 'Homme', 'Autre'),
                                                                                                  ('El Amrani', 'Fatima', '101 Rue Atlas, Tanger', '0645678901', 'fatima.elamrani@mail.com', '1988-11-30', 'Femme', 'CNSS'),
                                                                                                  ('Khalid', 'Omar', '202 Av. des FAR, Fès', '0656789012', 'omar.khalid@mail.com', '1995-07-14', 'Homme', 'Aucune'),
                                                                                                  ('Zahra', 'Lina', '303 Rue Moulay Ismail, Meknès', '0667890123', 'lina.zahra@mail.com', '2000-01-25', 'Femme', 'CNOPS'),
                                                                                                  ('Toumi', 'Youssef', '404 Bd Mohammed VI, Agadir', '0678901234', 'youssef.toumi@mail.com', '1983-09-18', 'Homme', 'CNSS'),
                                                                                                  ('Rahmani', 'Amina', '505 Av. Al Qods, Oujda', '0689012345', 'amina.rahmani@mail.com', '1990-12-05', 'Femme', 'Autre');

-- 3. Insertion des 5 RDV
INSERT INTO RDV (patient_id, utilisateur_id, date_rdv, duree, statut, notes) VALUES
                                                                                 (1, 2, '2025-04-10 09:00:00', 30, 'TERMINE', 'Contrôle annuel'),
                                                                                 (2, 3, '2025-04-11 10:30:00', 45, 'CONFIRME', 'Douleur molaire'),
                                                                                 (3, 2, '2025-04-12 14:00:00', 30, 'PLANIFIE', 'Nettoyage'),
                                                                                 (4, 4, '2025-04-13 11:00:00', 60, 'CONFIRME', 'Consultation enfant'),
                                                                                 (5, 3, '2025-04-14 16:30:00', 30, 'ANNULE', 'Patient a reporté');

-- 4. Insertion des 6 consultations
INSERT INTO Consultations (patient_id, utilisateur_id, rdv_id, diagnostic, traitement, notes) VALUES
                                                                                                  (1, 2, 1, 'Caries sur dent 36', 'Obturation composite', 'Patient sensible au froid'),
                                                                                                  (2, 3, 2, 'Gingivite modérée', 'Détartrage + conseils hygiène', 'Brosse électrique recommandée'),
                                                                                                  (3, 2, NULL, 'Absence de pathologie', 'Nettoyage professionnel', 'Dentition saine'),
                                                                                                  (4, 4, 4, 'Pulpite dent 45', 'Dévitalisation', 'Rendez-vous endodontiste'),
                                                                                                  (5, 3, NULL, 'Aphte buccal', 'Bain de bouche antiseptique', 'Résolution sous 5 jours'),
                                                                                                  (6, 2, NULL, 'Orthodontie contrôle', 'Ajustement appareil', 'Prochaine visite dans 1 mois');

-- 5. Insertion des 5 factures
INSERT INTO Factures (consultation_id, montant, statut, mode_paiement) VALUES
                                                                           (1, 500.00, 'PAYEE', 'CARTE'),
                                                                           (2, 300.00, 'PAYEE', 'ESPECES'),
                                                                           (3, 250.00, 'NON_PAYEE', NULL),
                                                                           (4, 1200.00, 'PARTIELLEMENT_PAYEE', 'CHEQUE'),
                                                                           (5, 150.00, 'PAYEE', 'VIREMENT');

-- 6. Insertion des 3 ordonnances
INSERT INTO Ordonnances (consultation_id, medicaments, posologie) VALUES
                                                                      (1, 'Paracétamol 500mg', '1 comprimé toutes les 6 heures si douleur'),
                                                                      (2, 'Bain de bouche à la chlorhexidine', '2 fois par jour pendant 7 jours'),
                                                                      (4, 'Amoxicilline 1g', '1 comprimé toutes les 12 heures pendant 7 jours');

-- 7. (Optionnel) Antécédents
INSERT INTO Antecedents (nom, categorie, niveauRisque) VALUES
                                                           ('Allergie pénicilline', 'ALLERGIE', 'CRITIQUE'),
                                                           ('Diabète type 2', 'MALADIE_CHRONIQUE', 'ELEVE'),
                                                           ('Tabagisme', 'HABITUDE_DE_VIE', 'MODERE');

INSERT INTO Patient_Antecedents (patient_id, antecedent_id) VALUES
                                                                (1, 1),
                                                                (2, 2),
                                                                (3, 3);