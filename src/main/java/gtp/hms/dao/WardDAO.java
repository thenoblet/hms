package gtp.hms.dao;

import gtp.hms.exception.DaoException;
import gtp.hms.model.Ward;
import gtp.hms.util.DatabaseConnection;

import java.sql.*;
import java.util.UUID;

/**
 * Data Access Object for managing ward records in the database.
 * Handles CRUD operations for wards including finding available beds.
 */
public class WardDAO {

    private static final String FIND_BY_NUMBER_SQL =
            "SELECT id FROM ward WHERE ward_number = ? AND department_id = ?";

    private static final String FIND_BY_ID_SQL =
            "SELECT w.*, d.department_name " +
                    "FROM ward w " +
                    "JOIN department d ON w.department_id = d.id " +
                    "WHERE w.id = ?";

    /**
     * Creates a new ward record in the database.
     *
     * @param ward the ward object containing data to insert
     * @return UUID of the newly created ward
     * @throws DaoException if creation fails or no ID is generated
     */
    public UUID create(Ward ward) throws DaoException {
        String sql = "INSERT INTO ward (ward_number, number_of_beds, department_id," +
                "supervisor_id) VALUES (?, ?, ?, ?)";

        try {
            Connection conn = DatabaseConnection.getInstance().getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            setDefaultParameters(stmt, ward);
            stmt.executeUpdate();

            ResultSet generatedKeys = stmt.getGeneratedKeys();
            if (generatedKeys.next()) {
                UUID id = UUID.fromString(generatedKeys.getString(1));
                ward.setId(id);
                return id;
            } else {
                throw new DaoException("Creating ward failed. No id obtained.");
            }
        } catch (SQLException e) {
            throw new DaoException("Creating ward failed.", e);
        }
    }

    /**
     * Finds a ward ID by its number and department.
     *
     * @param wardNumber the ward number to search for
     * @param departmentId the department ID the ward belongs to
     * @return UUID of the matching ward
     * @throws DaoException if ward is not found or database error occurs
     */
    public UUID findWardIdByNumber(int wardNumber, UUID departmentId) throws DaoException {
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(FIND_BY_NUMBER_SQL)) {

            stmt.setInt(1, wardNumber);
            stmt.setObject(2, departmentId);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return (UUID) rs.getObject("id");
                }
                throw new DaoException("Ward not found with number: " + wardNumber +
                        " in department: " + departmentId);
            }
        } catch (SQLException e) {
            throw new DaoException("Error finding ward by number", e);
        }
    }

    /**
     * Finds a complete ward record by its ID.
     *
     * @param wardId the UUID of the ward to find
     * @return complete Ward object with all fields populated
     * @throws DaoException if ward is not found or database error occurs
     */
    public Ward findById(UUID wardId) throws DaoException {
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(FIND_BY_ID_SQL)) {

            stmt.setObject(1, wardId);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToWard(rs);
                }
                throw new DaoException("Ward not found with ID: " + wardId);
            }
        } catch (SQLException e) {
            throw new DaoException("Error finding ward by ID: " + wardId, e);
        }
    }

    /**
     * Checks if a specific bed in a ward is currently available.
     *
     * @param wardNumber the ward number to check
     * @param departmentId the department ID of the ward
     * @param bedNumber the specific bed number to check
     * @return true if the bed is available, false otherwise
     * @throws DaoException if database error occurs during check
     */
    public boolean isBedAvailable(int wardNumber, UUID departmentId, int bedNumber) throws DaoException {
        String sql = "SELECT COUNT(*) = 0 FROM patient_admission pa " +
                "JOIN ward w ON pa.ward_id = w.id " +
                "WHERE w.ward_number = ? " +
                "AND w.department_id = ? " +
                "AND pa.bed_number = ? " +
                "AND pa.discharge_date IS NULL";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, wardNumber);
            stmt.setObject(2, departmentId);
            stmt.setInt(3, bedNumber);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getBoolean(1);
                }
                return false;
            }
        } catch (SQLException e) {
            throw new DaoException("Error checking bed availability", e);
        }
    }

    /**
     * Sets parameters on a PreparedStatement from a Ward object.
     *
     * @param stmt the PreparedStatement to configure
     * @param ward the ward data to use
     * @throws SQLException if there's an error setting parameters
     */
    public void setDefaultParameters(PreparedStatement stmt, Ward ward) throws SQLException {
        stmt.setInt(1, ward.getWardNumber());
        stmt.setInt(2, ward.getNumberOfBeds());

        if (ward.getDepartmentId() != null) {
            stmt.setObject(3, ward.getDepartmentId());
        } else {
            stmt.setNull(3, Types.OTHER);
        }

        if (ward.getSupervisorId() != null) {
            stmt.setObject(4, ward.getSupervisorId());
        } else {
            stmt.setNull(4, Types.OTHER);
        }
    }

    /**
     * Maps a database ResultSet to a Ward object.
     *
     * @param rs the ResultSet containing ward data
     * @return populated Ward object
     * @throws SQLException if there's an error reading the ResultSet
     */
    private Ward mapResultSetToWard(ResultSet rs) throws SQLException {
        Ward ward = new Ward();
        ward.setId((UUID) rs.getObject("id"));
        ward.setWardNumber(rs.getInt("ward_number"));
        ward.setNumberOfBeds(rs.getInt("number_of_beds"));
        ward.setDepartmentId((UUID) rs.getObject("department_id"));
        ward.setSupervisorId((UUID) rs.getObject("supervisor_id"));
        ward.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        ward.setUpdatedAt(rs.getTimestamp("updated_at").toLocalDateTime());
        return ward;
    }
}