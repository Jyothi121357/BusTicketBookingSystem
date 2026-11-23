package dao;

import db.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class BookingDAO {

    public boolean bookTicket(int userId, int busId) {
        boolean success = false;
        try {
            Connection conn = DBConnection.getConnection();
            String query = "INSERT INTO bookings (user_id, bus_id) VALUES (?, ?)";
            PreparedStatement statement = conn.prepareStatement(query);
            statement.setInt(1, userId);
            statement.setInt(2, busId);

            int rows = statement.executeUpdate();
            if (rows > 0)
                success = true;

        } catch (Exception e) {
            System.out.println("Error booking ticket: " + e.getMessage());
        }
        return success;
    }

    public void getMyBookings(int userId) {
        try {
            Connection conn = DBConnection.getConnection();
            String query = "SELECT b.id, bu.bus_number, bu.operator, b.booking_date " +
                    "FROM bookings b JOIN buses bu ON b.bus_id = bu.id " +
                    "WHERE b.user_id = ?";
            PreparedStatement statement = conn.prepareStatement(query);
            statement.setInt(1, userId);
            ResultSet rs = statement.executeQuery();

            System.out.println("\n--- Your Bookings ---");
            boolean hasBookings = false;
            while (rs.next()) {
                hasBookings = true;
                System.out.println("Booking ID: " + rs.getInt("id") +
                        " | Bus: " + rs.getString("bus_number") +
                        " | Operator: " + rs.getString("operator") +
                        " | Date: " + rs.getString("booking_date"));
            }
            if (!hasBookings) {
                System.out.println("You have no bookings yet.");
            }
        } catch (Exception e) {
            System.out.println("Error fetching bookings: " + e.getMessage());
        }
    }

    public boolean cancelBooking(int bookingId) {
        boolean success = false;
        try {
            Connection conn = DBConnection.getConnection();
            String query = "DELETE FROM bookings WHERE id = ?";
            PreparedStatement statement = conn.prepareStatement(query);
            statement.setInt(1, bookingId);
            int rows = statement.executeUpdate();
            if (rows > 0)
                success = true;
        } catch (Exception e) {
            System.out.println("Error canceling: " + e.getMessage());
        }
        return success;
    }

    public void getAllBookings() {
        try {
            Connection conn = DBConnection.getConnection();
            String query = "SELECT b.id, u.name, u.email, bu.bus_number, b.booking_date " +
                    "FROM bookings b " +
                    "JOIN users u ON b.user_id = u.id " +
                    "JOIN buses bu ON b.bus_id = bu.id";
            PreparedStatement statement = conn.prepareStatement(query);
            ResultSet rs = statement.executeQuery();

            System.out.println("\n--- All System Bookings ---");
            boolean hasBookings = false;
            while (rs.next()) {
                hasBookings = true;
                System.out.println("Booking ID: " + rs.getInt("id") +
                        " | User: " + rs.getString("name") + " (" + rs.getString("email") + ")" +
                        " | Bus: " + rs.getString("bus_number") +
                        " | Date: " + rs.getString("booking_date"));
            }
            if (!hasBookings) {
                System.out.println("No bookings found in the system.");
            }
        } catch (Exception e) {
            System.out.println("Error fetching all bookings: " + e.getMessage());
        }
    }

    // THIS IS THE METHOD THAT WAS MISSING
    public boolean isBusFull(int busId, int capacity) {
        try {
            Connection conn = DBConnection.getConnection();
            String query = "SELECT COUNT(*) FROM bookings WHERE bus_id = ?";
            PreparedStatement statement = conn.prepareStatement(query);
            statement.setInt(1, busId);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                int currentBookings = rs.getInt(1);
                return currentBookings >= capacity;
            }
        } catch (Exception e) {
            System.out.println("Error checking capacity: " + e.getMessage());
        }
        return false;
    }
}