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

public class EmployeeDAO {
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

    public void addDoctor(Doctor doctor) throws SQLException {
        Connection conn = null;

        try {
            conn = DatabaseConnection.getInstance().getConnection();
            conn.setAutoCommit(false);

            UUID employeeId = addEmployee(doctor);

            String sql = "INSERT INTO doctor (id, specialty) values(?,?)";

            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setObject(1, employeeId);
                stmt.setString(2, doctor.getSpeciality());
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
