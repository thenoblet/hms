package gtp.hms.service;

import gtp.hms.dao.PatientAdmissionDAO;
import gtp.hms.dao.WardDAO;
import gtp.hms.exception.ServiceException;
import gtp.hms.model.PatientAdmission;
import gtp.hms.model.Ward;
import gtp.hms.util.DatabaseConnection;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class PatientAdmissionService {
    private final PatientAdmissionDAO admissionDAO;
    private final WardDAO wardDAO;

    public PatientAdmissionService() {
        this.admissionDAO = new PatientAdmissionDAO();
        this.wardDAO = new WardDAO();
    }

    public UUID admitPatient(UUID patientId, int wardNumber, UUID departmentId,
                                    int bedNumber, UUID doctorId, String diagnosis)
            throws ServiceException {

        Connection conn = null;
        try {
            conn = DatabaseConnection.getInstance().getConnection();
            conn.setAutoCommit(false);

            UUID wardId;
            Ward ward;

            try {
                wardId = wardDAO.findWardIdByNumber(wardNumber, departmentId);
                ward = wardDAO.findById(wardId);
            } catch (Exception e) {
                throw new ServiceException("Failed to find ward information", e);
            }


            if (conn.isClosed()) {
                // If connection is closed, get a new one
                conn = DatabaseConnection.getInstance().getConnection();
                conn.setAutoCommit(false);
            }

            // 2. Create admission record
            PatientAdmission admission = new PatientAdmission();
            admission.setPatientId(patientId);
            admission.setWardId(wardId);
            admission.setBedNumber(bedNumber);
            admission.setDiagnosis(diagnosis);
            admission.setTreatingDoctorId(doctorId);
            admission.setAdmissionDate(LocalDate.now());
            admission.setIsCurrent(true);

            UUID admissionId = admissionDAO.create(admission, conn);
            conn.commit();
            return admissionId;

        } catch (Exception e) {
            if (conn != null) {
                try { conn.rollback(); } catch (SQLException ignored) {}
            }
            throw new ServiceException("Failed to admit patient", e);
        }
    }

    public List<PatientAdmission> getPatientAdmissions(UUID patientId) throws ServiceException {
        try {
            return admissionDAO.findByPatientId(patientId);
        } catch (Exception e) {
            throw new ServiceException("Failed to find patient admissions", e);
        }
    }

    public PatientAdmission getCurrentAdmission(UUID patientId) throws ServiceException {
        try {
            return admissionDAO.findCurrentAdmissionByPatientId(patientId);
        } catch (Exception e) {
            throw new ServiceException("Failed to find current admission", e);
        }
    }
}