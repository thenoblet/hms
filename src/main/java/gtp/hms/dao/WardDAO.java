package gtp.hms.dao;

import gtp.hms.exception.DaoException;
import gtp.hms.model.Ward;
import gtp.hms.util.DatabaseConnection;

import java.sql.*;
import java.util.UUID;


public class WardDAO {

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
}
