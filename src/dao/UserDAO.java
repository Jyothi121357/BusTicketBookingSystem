package dao;

import db.DBConnection;
import model.User;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAO {

    public boolean registerUser(User user) {
        String query = "INSERT INTO users (name, email, password, role) VALUES (?, ?, ?, ?)";
        try (Connection con = DBConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(query)) {
            ps.setString(1, user.getName());
            ps.setString(2, user.getEmail());
            ps.setString(3, user.getPassword());
            // Default to "USER" if no role is provided
            ps.setString(4, user.getRole() == null ? "USER" : user.getRole());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Registration failed: " + e.getMessage());
            return false;
        }
    }

    public User loginUser(String email, String password) {
        String query = "SELECT * FROM users WHERE email = ? AND password = ?";
        try (Connection con = DBConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(query)) {
            ps.setString(1, email);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                User user = new User();
                user.setId(rs.getInt("id"));
                user.setName(rs.getString("name"));
                user.setEmail(rs.getString("email"));
                user.setPassword(rs.getString("password"));
                user.setRole(rs.getString("role"));
                return user;
            }
        } catch (SQLException e) {
            System.out.println("Login failed: " + e.getMessage());
        }
        return null;
    }

    // --- NEW METHOD ADDED HERE ---
    // This permanently changes the role in the database
    public void updateUserRole(String email, String newRole) {
        String query = "UPDATE users SET role = ? WHERE email = ?";
        try (Connection con = DBConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(query)) {
            ps.setString(1, newRole);
            ps.setString(2, email);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Failed to update role: " + e.getMessage());
        }
    }
}