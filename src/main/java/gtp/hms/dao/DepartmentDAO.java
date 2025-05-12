package gtp.hms.dao;

import gtp.hms.exception.DaoException;
import gtp.hms.model.Department;
import gtp.hms.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Data Access Object (DAO) for handling CRUD operations for {@link Department} entities.
 * Provides methods to interact with the department database table.
 *
 * <p>This class handles:
 * <ul>
 *   <li>Department creation</li>
 *   <li>Department retrieval by ID or department code</li>
 *   <li>Retrieval of all departments</li>
 * </ul>
 *
 * @throws DaoException if any database access error occurs
 */
public class DepartmentDAO {
    /**
     * Creates a new department record in the database.
     *
     * @param department the department entity to be created
     * @return the generated UUID of the newly created department
     * @throws DaoException if:
     *                      <ul>
     *                        <li>Database access error occurs</li>
     *                        <li>No generated ID is returned from the database</li>
     *                      </ul>
     */
    public UUID create(Department department) throws DaoException {
        String sql = "INSERT INTO department (department_name, department_code, number_of_wards," +
                "building, hospital_id, director_id) VALUES (?,?,?,?,?,?)";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            setDefaultParameters(stmt, department);
            stmt.executeUpdate();

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    UUID id = UUID.fromString(generatedKeys.getString(1));
                    department.setId(id);
                    return id;
                } else {
                    throw new DaoException("Creating department failed, no ID obtained.");
                }
            }
        } catch (SQLException e) {
            throw new DaoException("Error creating department", e);
        }
    }

    /**
     * Retrieves a department by its unique identifier.
     *
     * @param id the UUID of the department to find
     * @return the found Department entity
     * @throws DaoException if:
     *                      <ul>
     *                        <li>Department with specified ID is not found</li>
     *                        <li>Database access error occurs</li>
     *                      </ul>
     */
    public Department findById(UUID id) throws DaoException {
        String sql = "SELECT * FROM department WHERE id = ?";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setObject(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToDepartment(rs);
                } else {
                    throw new DaoException("Department with id " + id + " not found.");
                }
            }

        } catch (SQLException e) {
            throw new DaoException("Error finding department by ID", e);
        }
    }

    /**
     * Retrieves all departments from the database, ordered by department name.
     *
     * @return a list of all Department entities
     * @throws DaoException if a database access error occurs
     */
    public List<Department> findAll() throws DaoException {
        String sql = "SELECT * FROM department ORDER BY department_name";
        List<Department> departments = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                departments.add(mapResultSetToDepartment(rs));
            }
            return departments;

        } catch (SQLException e) {
            throw new DaoException("Error retrieving all departments", e);
        }
    }

    /**
     * Finds a department ID by its unique department code.
     *
     * @param departmentCode the department code to search for
     * @return the UUID of the department with the specified code
     * @throws DaoException if:
     *                      <ul>
     *                        <li>Department with specified code is not found</li>
     *                        <li>Database access error occurs</li>
     *                      </ul>
     */
    public UUID findDepartmentIdByCode(String departmentCode) throws DaoException {
        String sql = "SELECT id FROM department WHERE department_code = ?";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, departmentCode);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return (UUID) rs.getObject("id");
                }
                throw new DaoException("Department not found with code: " + departmentCode);
            }
        } catch (SQLException e) {
            throw new DaoException("Error finding department by code", e);
        }
    }

    /**
     * Helper method to set default parameters for department PreparedStatements.
     *
     * @param statement the PreparedStatement to set parameters on
     * @param department the department entity containing the values
     * @throws SQLException if a database access error occurs
     */
    public void setDefaultParameters(PreparedStatement statement, Department department) throws SQLException {
        statement.setString(1, department.getDepartmentName());
        statement.setInt(2, department.getDepartmentCode());
        statement.setInt(3, department.getNumberOfWards());
        statement.setString(4, department.getBuilding());
        statement.setObject(5, department.getHospitalId());

        if (department.getDirectorId() != null) {
            statement.setObject(6, department.getDirectorId());
        } else {
            statement.setNull(6, Types.OTHER);
        }
    }

    /**
     * Maps a ResultSet row to a Department entity.
     *
     * @param resultSet the ResultSet containing department data
     * @return a populated Department entity
     * @throws SQLException if a database access error occurs
     */
    private Department mapResultSetToDepartment(ResultSet resultSet) throws SQLException {
        Department department = new Department();
        department.setId((UUID) resultSet.getObject("id"));
        department.setDepartmentName(resultSet.getString("department_name"));
        department.setDepartmentCode(resultSet.getInt("department_code"));
        department.setNumberOfWards(resultSet.getInt("number_of_wards"));
        department.setBuilding(resultSet.getString("building"));
        department.setHospitalId((UUID) resultSet.getObject("hospital_id"));

        UUID directorId = (UUID) resultSet.getObject("director_id");
        department.setDirectorId(resultSet.wasNull() ? null : directorId);

        department.setCreatedAt(resultSet.getTimestamp("created_at").toLocalDateTime());
        department.setUpdatedAt(resultSet.getTimestamp("updated_at").toLocalDateTime());

        return department;
    }
}