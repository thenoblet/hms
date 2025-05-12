package gtp.hms.dao;

import gtp.hms.model.employee.Doctor;
import gtp.hms.model.employee.EmployeeType;
import gtp.hms.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Data Access Object (DAO) for handling database operations related to {@link Doctor} entities.
 * Provides methods to interact with both the employee and doctor database tables.
 *
 * <p>This class handles:
 * <ul>
 *   <li>Retrieval of doctors by various criteria (ID, employee number, specialty)</li>
 *   <li>Retrieval of all doctors</li>
 * </ul>
 */
public class DoctorDAO {
    /**
     * Finds a doctor by their employee number.
     *
     * @param employeeNumber the unique employee number to search for
     * @return the Doctor object if found, null otherwise
     * @throws SQLException if a database access error occurs
     */
    public Doctor findByEmployeeNumber(int employeeNumber) {
        String sql = "SELECT e.*, d.specialty FROM employee e " +
                "JOIN doctor d ON e.id = d.id " +
                "WHERE e.employee_number = ? AND e.employee_type = 'doctor'";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, employeeNumber);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return extractDoctorFromResultSet(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Retrieves all doctors from the database.
     *
     * @return a list of all Doctor entities, empty list if none found
     * @throws SQLException if a database access error occurs
     */
    public List<Doctor> findAll() {
        List<Doctor> doctors = new ArrayList<>();
        String sql = "SELECT e.*, d.specialty FROM employee e " +
                "JOIN doctor d ON e.id = d.id " +
                "WHERE e.employee_type = 'doctor'";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                doctors.add(extractDoctorFromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return doctors;
    }

    /**
     * Finds a doctor by their unique identifier.
     *
     * @param id the UUID of the doctor to find
     * @return the Doctor object if found, null otherwise
     * @throws SQLException if a database access error occurs
     */
    public Doctor findById(UUID id) {
        String sql = "SELECT e.*, d.specialty FROM employee e " +
                "JOIN doctor d ON e.id = d.id " +
                "WHERE e.id = ? AND e.employee_type = 'doctor'";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setObject(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return extractDoctorFromResultSet(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Finds all doctors with a specific medical specialty.
     *
     * @param specialty the medical specialty to search for
     * @return a list of matching Doctor entities, empty list if none found
     * @throws SQLException if a database access error occurs
     */
    public List<Doctor> findBySpecialty(String specialty) {
        List<Doctor> doctors = new ArrayList<>();
        String sql = "SELECT e.*, d.specialty FROM employee e " +
                "JOIN doctor d ON e.id = d.id " +
                "WHERE e.employee_type = 'doctor' AND d.specialty = ?";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, specialty);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                doctors.add(extractDoctorFromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return doctors;
    }

    /**
     * Helper method to extract Doctor data from a ResultSet row.
     *
     * @param rs the ResultSet containing the doctor data
     * @return a populated Doctor object
     * @throws SQLException if a database access error occurs
     */
    private Doctor extractDoctorFromResultSet(ResultSet rs) throws SQLException {
        return new Doctor(
                UUID.fromString(rs.getString("id")),
                rs.getInt("employee_number"),
                rs.getString("first_name"),
                rs.getString("middle_name"),
                rs.getString("last_name"),
                rs.getString("address"),
                rs.getString("phone_number"),
                EmployeeType.valueOf(rs.getString("employee_type")),
                rs.getTimestamp("created_at").toLocalDateTime(),
                rs.getTimestamp("updated_at").toLocalDateTime(),
                rs.getString("specialty")
        );
    }
}