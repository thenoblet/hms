package gtp.hms.dao;

import gtp.hms.exception.DaoException;
import gtp.hms.model.Patient;
import gtp.hms.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PatientDAO {

    public UUID create(Patient patient) throws DaoException {
        String sql = "INSERT INTO patient (patient_number, first_name, middle_name, last_name," +
                "address, telephone_number) VALUES (?,?,?,?,?,?)";


        try {
            Connection conn = DatabaseConnection.getInstance().getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            setPatientParameters(stmt, patient);
            stmt.executeUpdate();

            ResultSet generatedKeys = stmt.getGeneratedKeys();
            if (generatedKeys.next()) {
                UUID id = UUID.fromString(generatedKeys.getString(1));
                patient.setId(id);
                return id;
            } else {
                throw new DaoException("Creating Patient failed. No ID obtained.");
            }

        } catch (SQLException e) {
            throw new DaoException("Creating Patient failed.", e);
        }
    }

    public UUID create(Patient patient, Connection conn) throws DaoException {
        String sql = "INSERT INTO patient (patient_number, first_name, middle_name, last_name," +
                "address, telephone_number) VALUES (?,?,?,?,?,?)";

        try {
            PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            setPatientParameters(stmt, patient);

            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new DaoException("Creating Patient failed. No rows affected.");
            }

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    UUID id = UUID.fromString(generatedKeys.getString(1));
                    patient.setId(id);
                    return id;
                } else {
                    throw new DaoException("Creating Patient failed. No ID obtained.");
                }
            }
        } catch (SQLException e) {
            throw new DaoException("Creating Patient failed.", e);
        }
    }


    public Patient findById(UUID id) throws DaoException {
        String sql = "SELECT * FROM patient WHERE id = ?";

        try {
            Connection conn = DatabaseConnection.getInstance().getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);

            stmt.setObject(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToPatient(rs);
                } else {
                    throw new DaoException("Patient with id" + id + " not found.");
                }
            }
        } catch (SQLException e) {
            throw new DaoException("Finding Patient failed.", e);
        }
    }

    public Patient findByPatientNumber(int patientNumber) throws DaoException {
        String sql = "SELECT * FROM patient WHERE patient_number = ?";

        try {
            Connection conn = DatabaseConnection.getInstance().getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);

            stmt.setInt(1, patientNumber);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToPatient(rs);
                }

                return null;
            }

        } catch (SQLException e) {
            throw new DaoException("Finding Patient failed.", e);
        }
    }

    public void update(Patient patient) throws DaoException {
        String sql = "UPDATE patient SET " +
                "patient_number = ?, first_name = ?, middle_name = ?, last_name = ?, " +
                "address = ?, telephone_number = ?, updated_at = CURRENT_TIMESTAMP" +
                " WHERE id = ?";

        try {
            Connection conn = DatabaseConnection.getInstance().getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);

            ParameterMetaData metaData = stmt.getParameterMetaData();
            if (metaData.getParameterCount() != 7) {  // Now correct count
                throw new DaoException("SQL parameter count mismatch");
            }

            setPatientParameters(stmt, patient);
            stmt.setObject(7, patient.getId());

            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new DaoException("Patient with id" + patient.getId() + " not found.");
            }
        } catch (SQLException e) {
            throw new DaoException("Updating Patient failed.", e);
        }
    }

    public boolean delete(UUID id) throws DaoException {
        String sql = "DELETE FROM patient WHERE id = ?";

        try {
            Connection conn = DatabaseConnection.getInstance().getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);

            stmt.setObject(1, id);
            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new DaoException("Patient with id" + id + " not found.");
            }
            return affectedRows > 0;
        } catch (SQLException e) {
            throw new DaoException("Deleting Patient failed.", e);
        }
    }

    public List<Patient> findAll() throws DaoException {
        List<Patient> patients = new ArrayList<>();

        String sql = "SELECT * FROM patient";
        try {
            Connection conn = DatabaseConnection.getInstance().getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                patients.add(mapResultSetToPatient(rs));
            }
            return patients;
        } catch (SQLException e) {
            throw new DaoException("Finding all patients failed.", e);
        }
    }

    public List<Patient> searchByName(String nameQuery) throws DaoException {
        List<Patient> patients = new ArrayList<>();

        String sql = "SELECT * FROM patient WHERE " +
                "LOWER(first_name) LIKE LOWER(?) OR LOWER(last_name) LIKE LOWER(?)";
        try {
            Connection conn = DatabaseConnection.getInstance().getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);

            String searchTerm = "%" + nameQuery + "%";
            stmt.setString(1, searchTerm);
            stmt.setString(2, searchTerm);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    patients.add(mapResultSetToPatient(rs));
                }
                return patients;
            }
        } catch (SQLException e) {
            throw new DaoException("Error searching patients by name", e);
        }
    }

    public void setDefaultParameters(PreparedStatement stmt, Patient patient) throws SQLException {
        stmt.setString(1, patient.getFirstName());
        stmt.setString(2, patient.getMiddleName());
        stmt.setString(3, patient.getLastName());
        stmt.setString(4, patient.getAddress());
        stmt.setString(5, patient.getTelephoneNumber());
    }

    public static void setPatientParameters(PreparedStatement stmt, Patient patient) throws SQLException {
        stmt.setInt(1, patient.getPatientNumber());

        if (patient.getFirstName() != null) {
            stmt.setString(2, patient.getFirstName());
        } else {
            stmt.setNull(2, Types.VARCHAR);
        }

        if (patient.getMiddleName() != null) {
            stmt.setString(3, patient.getMiddleName());
        } else {
            stmt.setNull(3, Types.VARCHAR);
        }

        if (patient.getLastName() != null) {
            stmt.setString(4, patient.getLastName());
        }

        if (patient.getAddress() != null) {
            stmt.setString(5, patient.getAddress());
        } else {
            stmt.setNull(5, Types.VARCHAR);
        }

        if (patient.getTelephoneNumber() != null) {
            stmt.setString(6, patient.getTelephoneNumber());
        } else {
            stmt.setNull(6, Types.VARCHAR);
        }

    }

    private Patient mapResultSetToPatient(ResultSet resultSet) throws SQLException {
        Patient patient = new Patient();

        patient.setId((UUID) resultSet.getObject("id"));
        patient.setPatientNumber(resultSet.getInt("patient_number"));
        patient.setFirstName(resultSet.getString("first_name"));
        patient.setMiddleName(resultSet.getString("middle_name"));
        patient.setLastName(resultSet.getString("last_name"));
        patient.setAddress(resultSet.getString("address"));
        patient.setTelephoneNumber(resultSet.getString("telephone_number"));
        patient.setCreatedAt(resultSet.getTimestamp("created_at").toLocalDateTime());
        patient.setUpdatedAt(resultSet.getTimestamp("updated_at").toLocalDateTime());

        return patient;
    }
}
