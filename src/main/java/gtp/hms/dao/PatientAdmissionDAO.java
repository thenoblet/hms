package gtp.hms.dao;

import gtp.hms.exception.DaoException;
import gtp.hms.model.PatientAdmission;
import gtp.hms.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Data Access Object for managing patient admission records in the database.
 * Handles CRUD operations for patient admissions including finding current admissions.
 */
public class PatientAdmissionDAO {
    private static final String INSERT_SQL = "INSERT INTO patient_admission (patient_id, ward_id, bed_number, diagnosis," +
            "treating_doctor_id, admission_date, is_current) VALUES (?,?,?,?,?,?,?)";

    /**
     * Creates a new patient admission record in the database.
     *
     * @param patientAdmission the admission record to create
     * @return UUID of the created admission or null if creation failed
     * @throws DaoException if there's an error creating the record
     */
    public UUID create(PatientAdmission patientAdmission) throws DaoException {
        try {
            Connection conn = DatabaseConnection.getInstance().getConnection();
            PreparedStatement stmt = conn.prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS);

            setPatientAdmissionParams(stmt, patientAdmission);
            stmt.executeUpdate();

            ResultSet generatedKeys = stmt.getGeneratedKeys();
            if (generatedKeys.next()) {
                UUID id = UUID.fromString(generatedKeys.getString(1));
                patientAdmission.setId(id);
                return id;
            } else {
                return null;
            }
        } catch (SQLException e) {
            throw new DaoException("Error creating patient admission record", e);
        }
    }

    /**
     * Creates a new patient admission record using an existing database connection.
     *
     * @param patientAdmission the admission record to create
     * @param conn the existing database connection to use
     * @return UUID of the created admission
     * @throws DaoException if connection is invalid or creation fails
     */
    public UUID create(PatientAdmission patientAdmission, Connection conn) throws DaoException {
        if (conn == null) {
            throw new DaoException("Database connection is null");
        }

        try {
            if (conn.isClosed()) {
                throw new DaoException("Connection is closed");
            }

            PreparedStatement stmt = conn.prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS);
            setPatientAdmissionParams(stmt, patientAdmission);

            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new DaoException("Error creating patient admission record");
            }

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    UUID id = UUID.fromString(rs.getString(1));
                    patientAdmission.setId(id);
                    return id;
                } else {
                    throw new DaoException("Error creating patient admission record");
                }
            }

        } catch (SQLException e) {
            throw new DaoException("Error creating patient admission record", e);
        }
    }

    /**
     * Finds all admission records for a specific patient.
     *
     * @param patientId the patient's UUID
     * @return list of admissions ordered by date (newest first)
     * @throws DaoException if there's an error accessing the database
     */
    public List<PatientAdmission> findByPatientId(UUID patientId) throws DaoException {
        String sql = "SELECT * FROM patient_admission WHERE patient_id = ? ORDER BY admission_date DESC";
        List<PatientAdmission> admissions = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setObject(1, patientId);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    admissions.add(mapResultSetToPatientAdmission(rs));
                }
            }
            return admissions;
        } catch (SQLException e) {
            throw new DaoException("Error finding admissions by patient ID", e);
        }
    }

    /**
     * Finds the current active admission for a patient.
     *
     * @param patientId the patient's UUID
     * @return the current admission or null if none found
     * @throws DaoException if there's an error accessing the database
     */
    public PatientAdmission findCurrentAdmissionByPatientId(UUID patientId) throws DaoException {
        String sql = "SELECT * FROM patient_admission WHERE patient_id = ? AND is_current = TRUE";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setObject(1, patientId);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToPatientAdmission(rs);
                }
                return null;
            }
        } catch (SQLException e) {
            throw new DaoException("Error finding current admission", e);
        }
    }

    /**
     * Maps a database ResultSet to a PatientAdmission object.
     *
     * @param rs the ResultSet containing admission data
     * @return populated PatientAdmission object
     * @throws SQLException if there's an error reading the ResultSet
     */
    private PatientAdmission mapResultSetToPatientAdmission(ResultSet rs) throws SQLException {
        PatientAdmission admission = new PatientAdmission();
        admission.setId(UUID.fromString(rs.getString("id")));
        admission.setPatientId(UUID.fromString(rs.getString("patient_id")));
        admission.setWardId(UUID.fromString(rs.getString("ward_id")));
        admission.setBedNumber(rs.getInt("bed_number"));
        admission.setDiagnosis(rs.getString("diagnosis"));
        admission.setTreatingDoctorId(UUID.fromString(rs.getString("treating_doctor_id")));
        admission.setAdmissionDate(rs.getDate("admission_date").toLocalDate());

        if (rs.getDate("discharge_date") != null) {
            admission.setDischargeDate(rs.getDate("discharge_date").toLocalDate());
        }

        admission.setIsCurrent(rs.getBoolean("is_current"));
        return admission;
    }

    /**
     * Sets parameters on a PreparedStatement from a PatientAdmission object.
     *
     * @param stmt the PreparedStatement to configure
     * @param admission the admission data to use
     * @throws SQLException if there's an error setting parameters
     */
    private void setPatientAdmissionParams(PreparedStatement stmt, PatientAdmission admission)
            throws SQLException {
        stmt.setObject(1, admission.getPatientId());
        stmt.setObject(2, admission.getWardId());
        stmt.setInt(3, admission.getBedNumber());
        stmt.setObject(4, admission.getDiagnosis());
        stmt.setObject(5, admission.getTreatingDoctorId());
        stmt.setObject(6, admission.getAdmissionDate());
        stmt.setBoolean(7, admission.getIsCurrent());
    }
}