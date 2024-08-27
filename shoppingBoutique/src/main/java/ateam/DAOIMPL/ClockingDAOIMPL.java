package ateam.DAOIMPL;

import ateam.BDconnection.Connect;
import ateam.DAO.ClockingDAO;
import ateam.Models.Clocking;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClockingDAOIMPL implements ClockingDAO {

    private final Connection connection;

    public ClockingDAOIMPL() {
        Connect connect = new Connect();
        this.connection = connect.connectToDB();
    }

    @Override
    public void clockIn(int employee_ID) {
        try {
            // Check if the employee can clock in
            String checkQuery = "SELECT clock_out FROM clocking WHERE employee_ID = ? ORDER BY clocking_ID DESC LIMIT 1";
            PreparedStatement checkStmt = connection.prepareStatement(checkQuery);
            checkStmt.setInt(1, employee_ID);
            ResultSet rs = checkStmt.executeQuery();

            if (rs.next()) {
                Timestamp lastClockOut = rs.getTimestamp("clock_out");
                if (lastClockOut != null) {
                    long timeDifference = System.currentTimeMillis() - lastClockOut.getTime();
                    if (timeDifference < 2 * 60 * 1000) {
                        System.out.println("You need to wait 2 minutes from the last clock-out to clock in.");
                        return;
                    }
                }
            }

            // Allow clock in
            String insertQuery = "INSERT INTO clocking (employee_ID, clock_in) VALUES (?, ?)";
            PreparedStatement insertStmt = connection.prepareStatement(insertQuery);
            insertStmt.setInt(1, employee_ID);
            insertStmt.setTimestamp(2, new Timestamp(System.currentTimeMillis()));
            insertStmt.executeUpdate();
            System.out.println("Clocked in successfully.");
        } catch (SQLException e) {
            Logger.getLogger(ClockingDAOIMPL.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    @Override
    public void clockOut(int employee_ID) {
        try {
            // Check if the employee has a valid clock-in and no clock-out yet
            String checkQuery = "SELECT clock_in FROM clocking WHERE employee_ID = ? AND clock_out IS NULL";
            PreparedStatement checkStmt = connection.prepareStatement(checkQuery);
            checkStmt.setInt(1, employee_ID);
            ResultSet rs = checkStmt.executeQuery();

            if (rs.next()) {
                // Update clock_out time in the database
                String updateQuery = "UPDATE clocking SET clock_out = ? WHERE employee_ID = ? AND clock_out IS NULL";
                PreparedStatement updateStmt = connection.prepareStatement(updateQuery);
                updateStmt.setTimestamp(1, new Timestamp(System.currentTimeMillis()));
                updateStmt.setInt(2, employee_ID);
                updateStmt.executeUpdate();
                System.out.println("Clocked out successfully.");
            } else {
                System.out.println("No valid clock-in found or already clocked out.");
            }
        } catch (SQLException e) {
            Logger.getLogger(ClockingDAOIMPL.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    @Override
    public Clocking getClockingByEmployeeId(int employee_ID) {
        Clocking clocking = null;
        try {
            String selectQuery = "SELECT * FROM clocking WHERE employee_ID = ? ORDER BY clocking_ID DESC LIMIT 1";
            PreparedStatement stmt = connection.prepareStatement(selectQuery);
            stmt.setInt(1, employee_ID);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                clocking = new Clocking();
                clocking.setClocking_ID(rs.getInt("clocking_ID"));
                clocking.setEmployee_ID(rs.getInt("employee_ID"));
                clocking.setClock_in(rs.getTimestamp("clock_in"));
                clocking.setClock_out(rs.getTimestamp("clock_out"));
            }
        } catch (SQLException e) {
            Logger.getLogger(ClockingDAOIMPL.class.getName()).log(Level.SEVERE, null, e);
        }
        return clocking;
    }

    @Override
    public List<Clocking> getFilteredClockingRecords(String firstName, String lastName, Timestamp startDate, Timestamp endDate, String company) {
        List<Clocking> clockingRecords = new ArrayList<>();
        String query = "SELECT c.clocking_ID, c.employee_ID, c.clock_in, c.clock_out, e.first_name, e.last_name, e.company "
                + "FROM clocking c "
                + "JOIN employees e ON c.employee_ID = e.employee_ID "
                + "WHERE (e.first_name LIKE ? AND e.last_name LIKE ?) "
                + "AND (c.clock_in BETWEEN ? AND ?) "
                + "AND (e.company = ?) "
                + "ORDER BY c.clock_in";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            // Set query parameters
            preparedStatement.setString(1, "%" + firstName + "%");
            preparedStatement.setString(2, "%" + lastName + "%");
            preparedStatement.setTimestamp(3, startDate);
            preparedStatement.setTimestamp(4, endDate);
            preparedStatement.setString(5, company);

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Clocking clocking = new Clocking();
                clocking.setClocking_ID(resultSet.getInt("clocking_ID"));
                clocking.setEmployee_ID(resultSet.getInt("employee_ID"));
                clocking.setClock_in(resultSet.getTimestamp("clock_in"));
                clocking.setClock_out(resultSet.getTimestamp("clock_out"));
                clocking.setFirst_name(resultSet.getString("first_name"));
                clocking.setLast_name(resultSet.getString("last_name"));
                clocking.setCompany(resultSet.getString("company"));
                clockingRecords.add(clocking);
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Handle exceptions appropriately
        }
        return clockingRecords;
    }

    @Override
    public List<Clocking> getFilteredClockingRecords(Timestamp startDate, Timestamp endDate, String company) {
        List<Clocking> clockingRecords = new ArrayList<>();
        String query = "SELECT c.clocking_ID, c.employee_ID, c.clock_in, c.clock_out, e.first_name, e.last_name, e.company "
                + "FROM clocking c "
                + "JOIN employees e ON c.employee_ID = e.employee_ID "
                + "WHERE (c.clock_in BETWEEN ? AND ?) "
                + "AND (e.company = ?) "
                + "ORDER BY c.clock_in";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            // Set query parameters
            preparedStatement.setTimestamp(1, startDate);
            preparedStatement.setTimestamp(2, endDate);
            preparedStatement.setString(3, company);

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Clocking clocking = new Clocking();
                clocking.setClocking_ID(resultSet.getInt("clocking_ID"));
                clocking.setEmployee_ID(resultSet.getInt("employee_ID"));
                clocking.setClock_in(resultSet.getTimestamp("clock_in"));
                clocking.setClock_out(resultSet.getTimestamp("clock_out"));
                clocking.setFirst_name(resultSet.getString("first_name"));
                clocking.setLast_name(resultSet.getString("last_name"));
                clocking.setCompany(resultSet.getString("company"));
                clockingRecords.add(clocking);
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Handle exceptions appropriately
        }
        return clockingRecords;
    }

}
