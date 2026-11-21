package dao;

import db.DBConnection;
import model.Bus;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BusDAO {
    public boolean addBus(Bus bus) {
        String query = "INSERT INTO buses (bus_number, capacity, operator) VALUES (?, ?, ?)";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {
            ps.setString(1, bus.getBusNumber());
            ps.setInt(2, bus.getCapacity());
            ps.setString(3, bus.getOperator());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Failed to add bus: " + e.getMessage());
            return false;
        }
    }

    public List<Bus> getAllBuses() {
        List<Bus> busList = new ArrayList<>();
        String query = "SELECT * FROM buses";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Bus bus = new Bus();
                bus.setId(rs.getInt("id"));
                bus.setBusNumber(rs.getString("bus_number"));
                bus.setCapacity(rs.getInt("capacity"));
                bus.setOperator(rs.getString("operator"));
                busList.add(bus);
            }
        } catch (SQLException e) {
            System.out.println("Failed to fetch buses: " + e.getMessage());
        }
        return busList;
    }
}