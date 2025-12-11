package ma.teethcare.repository.test;

import ma.teethcare.conf.SessionFactory;
import ma.teethcare.repository.modules.patient.impl.mySQL.PatientRepositoryImpl;
import java.sql.*;

public class TestRepo {

    public static void main(String[] args) {
        System.out.println("🧪 TEST REPOSITORY SIMPLE");
        System.out.println("=".repeat(40));

        // Test 1: Vérifier SessionFactory
        testSessionFactory();

        // Test 2: Créer et tester PatientRepository
        testPatientRepository();

        // Test 3: Test direct SQL
        testDirectSQL();
    }

    static void testSessionFactory() {
        System.out.println("\n🔌 TEST 1: SESSIONFACTORY");
        System.out.println("-".repeat(20));

        try {
            var factory = SessionFactory.getInstance();
            var conn = factory.getConnection();

            System.out.println("✅ SessionFactory OK");
            System.out.println("   Base: " + conn.getCatalog());
            System.out.println("   MySQL: " + conn.getMetaData().getDatabaseProductVersion());

            // Vérifier tables
            var meta = conn.getMetaData();
            var tables = meta.getTables(null, null, "Patients", null);

            if (tables.next()) {
                System.out.println("   ✅ Table 'Patients' existe");
            } else {
                System.err.println("   ❌ Table 'Patients' n'existe pas !");
            }

            conn.close();

        } catch (Exception e) {
            System.err.println("❌ SessionFactory échoue: " + e.getMessage());
        }
    }

    static void testPatientRepository() {
        System.out.println("\n👥 TEST 2: PATIENT REPOSITORY");
        System.out.println("-".repeat(20));

        try {
            // 1. Créer le repository
            System.out.println("Création PatientRepositoryImpl...");
            PatientRepositoryImpl repo = new PatientRepositoryImpl();
            System.out.println("✅ Repository créé");

            // 2. Tester findAll()
            System.out.println("\nAppel findAll()...");
            try {
                var patients = repo.findAll();
                System.out.println("✅ findAll() réussi");
                System.out.println("   Résultat: " +
                        (patients == null ? "null" : patients.size() + " patients"));

                if (patients != null && !patients.isEmpty()) {
                    System.out.println("   Premier patient: " +
                            patients.get(0).getPrenom() + " " + patients.get(0).getNom());
                }

            } catch (Exception e) {
                System.err.println("❌ findAll() échoue: " + e.getMessage());
                System.err.println("   Cause: " + e.getCause());
            }

        } catch (Exception e) {
            System.err.println("❌ Impossible de créer repository: " + e.getMessage());
        }
    }

    static void testDirectSQL() {
        System.out.println("\n🗃️ TEST 3: SQL DIRECT");
        System.out.println("-".repeat(20));

        try (Connection conn = SessionFactory.getInstance().getConnection()) {

            // 1. Compter les patients
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT COUNT(*) as total FROM Patients");

            if (rs.next()) {
                int total = rs.getInt("total");
                System.out.println("✅ Patients en base: " + total);
            }

            // 2. Afficher 3 patients
            System.out.println("\n📋 3 premiers patients:");
            rs = stmt.executeQuery("SELECT id, nom, prenom, email FROM Patients LIMIT 3");

            while (rs.next()) {
                System.out.println("   👤 ID: " + rs.getLong("id") +
                        " - " + rs.getString("prenom") +
                        " " + rs.getString("nom") +
                        " (" + rs.getString("email") + ")");
            }

            // 3. Insérer un patient test
            System.out.println("\n➕ Insertion patient test...");
            String insertSQL = "INSERT INTO Patients (nom, prenom, email, telephone, sexe, assurance) " +
                    "VALUES (?, ?, ?, ?, ?, ?)";

            PreparedStatement pstmt = conn.prepareStatement(insertSQL);
            pstmt.setString(1, "TestRepo");
            pstmt.setString(2, "Java");
            pstmt.setString(3, "test.repo@email.com");
            pstmt.setString(4, "0611223344");
            pstmt.setString(5, "Homme");
            pstmt.setString(6, "CNSS");

            int rows = pstmt.executeUpdate();
            System.out.println("✅ " + rows + " patient inséré");

            // 4. Recompter
            rs = stmt.executeQuery("SELECT COUNT(*) as total FROM Patients");
            if (rs.next()) {
                System.out.println("   Nouveau total: " + rs.getInt("total"));
            }

        } catch (Exception e) {
            System.err.println("❌ SQL direct échoue: " + e.getMessage());
        }
    }
}