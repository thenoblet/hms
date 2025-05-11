package gtp.hms.dao;

import gtp.hms.exception.DaoException;
import gtp.hms.model.Department;
import gtp.hms.util.DatabaseConnection;


import java.sql.*;
import java.util.UUID;

public class DepartmentDAO {
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
