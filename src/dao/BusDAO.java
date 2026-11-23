package dao;

import db.DBConnection;
import model.Bus;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class BusDAO {

    public boolean addBus(Bus bus) {
        boolean success = false;
        try {
            Connection conn = DBConnection.getConnection();
            String query = "INSERT INTO buses (bus_number, capacity, operator) VALUES (?, ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setString(1, bus.getBusNumber());
            pstmt.setInt(2, bus.getCapacity());
            pstmt.setString(3, bus.getOperator());

            int rows = pstmt.executeUpdate();
            if (rows > 0)
                success = true;

        } catch (Exception e) {
            System.out.println("Error adding bus: " + e.getMessage());
        }
        return success;
    }

    public List<Bus> getAllBuses() {
        List<Bus> buses = new ArrayList<>();
        try {
            Connection conn = DBConnection.getConnection();
            String query = "SELECT * FROM buses";
            PreparedStatement pstmt = conn.prepareStatement(query);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Bus bus = new Bus(
                        rs.getString("bus_number"),
                        rs.getInt("capacity"),
                        rs.getString("operator"));
                bus.setId(rs.getInt("id"));
                buses.add(bus);
            }
        } catch (Exception e) {
            System.out.println("Error fetching buses: " + e.getMessage());
        }
        return buses;
    }

    public boolean deleteBus(int busId) {
        boolean success = false;
        try {
            Connection conn = DBConnection.getConnection();
            String query = "DELETE FROM buses WHERE id = ?";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, busId);
            int rows = pstmt.executeUpdate();
            if (rows > 0)
                success = true;
        } catch (Exception e) {
            System.out.println("Error deleting bus: " + e.getMessage());
        }
        return success;
    }
}