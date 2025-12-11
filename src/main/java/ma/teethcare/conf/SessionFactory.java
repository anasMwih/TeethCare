package ma.teethcare.conf;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public final class SessionFactory {

    private static SessionFactory instance;
    private Connection connection;

    // Constructeur privé pour Singleton
    private SessionFactory() {
        try {
            // Charger les propriétés de configuration
            Properties props = new Properties();
            props.load(getClass().getClassLoader().getResourceAsStream("config/db.properties"));

            String url = props.getProperty("db.url");
            String username = props.getProperty("db.username");
            String password = props.getProperty("db.password");

            // Charger le driver MySQL
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Établir la connexion
            this.connection = DriverManager.getConnection(url, username, password);
            System.out.println("✅ Connexion à la base de données établie avec succès !");

        } catch (ClassNotFoundException e) {
            System.err.println("❌ Driver JDBC MySQL non trouvé !");
            System.err.println("➡️ Ajoutez mysql-connector-java dans pom.xml");
            e.printStackTrace();

        } catch (SQLException e) {
            System.err.println("❌ Erreur de connexion à la base de données !");
            System.err.println("➡️ Vérifiez :");
            System.err.println("   - MySQL est-il démarré ? (sudo service mysql start)");
            System.err.println("   - La base 'teethcare_db' existe-t-elle ?");
            System.err.println("   - Les identifiants sont-ils corrects ?");
            e.printStackTrace();

        } catch (Exception e) {
            System.err.println("❌ Erreur lors de l'initialisation de SessionFactory");
            e.printStackTrace();
        }
    }

    // Méthode Singleton pour obtenir l'instance
    public static SessionFactory getInstance() {
        if (instance == null) {
            synchronized (SessionFactory.class) {
                if (instance == null) {
                    instance = new SessionFactory();
                }
            }
        }
        return instance;
    }

    // Obtenir la connexion
    public Connection getConnection() {
        try {
            // Vérifier si la connexion est toujours valide
            if (connection == null || connection.isClosed()) {
                // Re-créer une nouvelle connexion
                reconnect();
            }
        } catch (SQLException e) {
            System.err.println("❌ Erreur lors de la vérification de la connexion");
            reconnect();
        }
        return connection;
    }

    // Fermer la connexion
    public void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("✅ Connexion fermée avec succès");
            }
        } catch (SQLException e) {
            System.err.println("❌ Erreur lors de la fermeture de la connexion");
            e.printStackTrace();
        }
    }

    // Reconnecter en cas de problème
    private void reconnect() {
        try {
            closeConnection(); // Fermer l'ancienne connexion si elle existe

            Properties props = new Properties();
            props.load(getClass().getClassLoader().getResourceAsStream("config/db.properties"));

            String url = props.getProperty("db.url");
            String username = props.getProperty("db.username");
            String password = props.getProperty("db.password");

            this.connection = DriverManager.getConnection(url, username, password);
            System.out.println("✅ Reconnexion à la base de données réussie !");

        } catch (Exception e) {
            System.err.println("❌ Échec de la reconnexion à la base de données");
            e.printStackTrace();
        }
    }

    // Méthode pour tester la connexion
    public boolean testConnection() {
        try {
            Connection testConn = getConnection();
            if (testConn != null && !testConn.isClosed()) {
                System.out.println("✅ Test de connexion réussi !");
                return true;
            }
        } catch (SQLException e) {
            System.err.println("❌ Test de connexion échoué");
        }
        return false;
    }
}