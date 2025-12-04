// ma/teethcare/conf/SessionFactory.java
package ma.teethcare.conf;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class SessionFactory {
    private static SessionFactory instance;
    private Connection connection;

    private SessionFactory() {
        try {
            Properties props = new Properties();
            InputStream input = getClass().getClassLoader().getResourceAsStream("config/application.properties");
            if (input == null) {
                throw new RuntimeException("Unable to find application.properties");
            }
            props.load(input);

            String url = props.getProperty("db.url");
            String username = props.getProperty("db.username");
            String password = props.getProperty("db.password");

            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(url, username, password);
        } catch (ClassNotFoundException | SQLException | IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to initialize database connection", e);
        }
    }

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

    public Connection getConnection() {
        return connection;
    }
}