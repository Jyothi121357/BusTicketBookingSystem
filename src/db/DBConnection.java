package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    // REPLACE [YOUR-PASSWORD] with your actual Supabase database password!
    private static final String URL = "jdbc:postgresql://aws-0-ap-northeast-1.pooler.supabase.com:5432/postgres?user=postgres.ywtkwxpiqwprlynejumb&password=lb6aMkpp7Wb05Uuy";

    public static Connection getConnection() {
        Connection conn = null;
        try {
            Class.forName("org.postgresql.Driver");
            conn = DriverManager.getConnection(URL);
            System.out.println("Connected to Supabase successfully!");
        } catch (ClassNotFoundException | SQLException e) {
            System.out.println("Database Connection Failed: " + e.getMessage());
        }
        return conn;
    }
}