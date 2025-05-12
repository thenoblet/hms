package gtp.hms.dao;

import gtp.hms.exception.DaoException;
import gtp.hms.model.employee.Doctor;
import gtp.hms.model.employee.Employee;
import gtp.hms.model.employee.Nurse;
import gtp.hms.util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

/**
 * Data Access Object (DAO) for handling database operations related to Employee entities
 * and their specialised subclasses (Doctor and Nurse).
 *
 * This class provides methods for:
 * - Adding new employees (generic and specialised)
 * - Retrieving employee information
 *
 * The class handles transactions for operations that span multiple tables.
 */
public class EmployeeDAO {

    /**
     * Adds a generic employee record to the database.
     *
     * @param employee the employee entity to be added
     * @return the generated UUID of the newly created employee
     * @throws SQLException if:
     *   - A database access error occurs
     *   - No generated ID is returned from the database
     */
    public UUID addEmployee(Employee employee) throws SQLException {
        String sql = "INSERT INTO employee (employee_number, first_name, middle_name, last_name," +
                "address, phone_number, employee_type) values(?,?,?,?,?,?,?::employee_type)" +
                "RETURNING id";

        Connection conn = DatabaseConnection.getInstance().getConnection();
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, employee.getEmployeeNumber());
            stmt.setString(2, employee.getFirstName());
            stmt.setString(3, employee.getMiddleName());
            stmt.setString(4, employee.getLastName());
            stmt.setString(5, employee.getAddress());
            stmt.setString(6, employee.getPhoneNumber());
            stmt.setString(7, employee.getEmployeeType().toString());

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getObject("id", UUID.class);
            }

            throw new SQLException("Creating employee failed, no ID found");
        }
    }

    /**
     * Adds a doctor to the database as a transaction spanning both employee and doctor tables.
     *
     * @param doctor the doctor entity to be added
     * @throws SQLException if:
     *   - A database access error occurs
     *   - The transaction needs to be rolled back
     */
    public void addDoctor(Doctor doctor) throws SQLException {
        Connection conn = null;

        try {
            conn = DatabaseConnection.getInstance().getConnection();
            conn.setAutoCommit(false);

            UUID employeeId = addEmployee(doctor);

            String sql = "INSERT INTO doctor (id, specialty) values(?,?)";

            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setObject(1, employeeId);
                stmt.setString(2, doctor.getSpecialty());
                stmt.executeUpdate();
            }

            conn.commit();
            doctor.setId(employeeId);
        } catch (SQLException e) {
            if (conn != null) {
                conn.rollback();
                throw e;
            }
        } finally {
            if (conn != null) {
                conn.setAutoCommit(true);
            }
        }
    }

    /**
     * Adds a nurse to the database as a transaction spanning both employee and nurse tables.
     *
     * @param nurse the nurse entity to be added
     * @throws SQLException if:
     *   - A database access error occurs
     *   - The transaction needs to be rolled back
     */
    public void addNurse(Nurse nurse) throws SQLException {
        Connection conn = null;

        try {
            conn = DatabaseConnection.getInstance().getConnection();
            conn.setAutoCommit(false);

            UUID employeeId = addEmployee(nurse);

            String sql = "INSERT INTO nurse (id, rotation, salary, department_id) values(?, ?, ?, ?)";

            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setObject(1, employeeId);
                stmt.setString(2, nurse.getRotation());
                stmt.setDouble(3, nurse.getSalary());
                stmt.setObject(4, nurse.getDepartmentId());

                conn.commit();
                nurse.setId(employeeId);
            }
        } catch (SQLException e) {
            if (conn != null) {
                conn.rollback();
                throw e;
            }
        } finally {
            if (conn != null) {
                conn.setAutoCommit(true);
            }
        }
    }

    /**
     * Retrieves an employee by their unique identifier.
     *
     * @param employeeId the UUID of the employee to retrieve
     * @return the Employee entity
     * @throws DaoException if:
     *   - The employee is not found
     *   - A database access error occurs
     */
    public Employee getEmployee(UUID employeeId) throws DaoException {
        Connection conn = null;

        try {
            conn = DatabaseConnection.getInstance().getConnection();
            conn.setAutoCommit(false);

            String sql = "SELECT * FROM employee WHERE id = ?";

            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setObject(1, employeeId);
                ResultSet rs = stmt.executeQuery();

                if (rs.next()) {
                    return mapResultSetToEmployee(rs);
                } else {
                    throw new SQLException("Employee with id " + employeeId + " not found");
                }
            }
        } catch (SQLException e) {
            throw new DaoException("Getting employee failed", e);
        }
    }

    /**
     * Maps a ResultSet row to an Employee entity.
     *
     * @param rs the ResultSet containing employee data
     * @return a populated Employee entity
     * @throws SQLException if a database access error occurs
     */
    private Employee mapResultSetToEmployee(ResultSet rs) throws SQLException {
        Employee employee = new Employee();

        employee.setId(rs.getObject("id", UUID.class));
        employee.setEmployeeNumber(rs.getInt("employee_number"));
        employee.setFirstName(rs.getString("first_name"));
        employee.setMiddleName(rs.getString("middle_name"));
        employee.setLastName(rs.getString("last_name"));
        employee.setAddress(rs.getString("address"));
        employee.setPhoneNumber(rs.getString("phone_number"));
        employee.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        employee.setUpdatedAt(rs.getTimestamp("updated_at").toLocalDateTime());

        return employee;
    }
}