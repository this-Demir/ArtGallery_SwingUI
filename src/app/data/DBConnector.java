package app.data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnector {

    // Database credentials
    private static final String URL = "jdbc:mysql://localhost:3306/artgallerysystem";
    private static final String USER = "root";
    private static final String PASSWORD = "123456789";


    public static Connection connect() {
        try {
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            System.err.println("Connection failed: " + e.getMessage());
            return null;
        }
    }

}

