package gtp.hms.service;

import gtp.hms.dao.PatientDAO;
import gtp.hms.dao.PatientAdmissionDAO;
import gtp.hms.exception.DaoException;
import gtp.hms.exception.ServiceException;
import gtp.hms.model.Patient;
import gtp.hms.model.PatientAdmission;
import gtp.hms.util.DatabaseConnection;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public class PatientService {
    private final PatientDAO patientDAO;
    private final PatientAdmissionDAO patientAdmissionDAO;

    public PatientService() {
        this.patientDAO = new PatientDAO();
        this.patientAdmissionDAO = new PatientAdmissionDAO();
    }

    public UUID registerNewPatient(int patientNumber, String firstName, String middleName,
                                      String lastName, String address, String phoneNumber) {


        Patient patient = new Patient();
        patient.setPatientNumber(patientNumber);
        patient.setFirstName(firstName);
        patient.setMiddleName(middleName);
        patient.setLastName(lastName);
        patient.setAddress(address);
        patient.setTelephoneNumber(phoneNumber);

        try {
            return patientDAO.create(patient);
        } catch (DaoException e) {
            throw new RuntimeException("Failed to register new patient", e);
        }
    }

    public UUID admitNewPatient(int patientNumber, String firstName, String middleName,
                                String lastName, String address, String phoneNumber,
                                UUID wardId, int bedNumber, UUID treatingDoctorId) throws DaoException {

        Connection conn = null;
        try {
            conn = DatabaseConnection.getInstance().getConnection();
            conn.setAutoCommit(false);

            Patient patient = new Patient();
            patient.setPatientNumber(patientNumber);
            patient.setFirstName(firstName);
            patient.setMiddleName(middleName);
            patient.setLastName(lastName);
            patient.setAddress(address);
            patient.setTelephoneNumber(phoneNumber);

            UUID patientId = patientDAO.create(patient, conn);

            PatientAdmission admission = new PatientAdmission();
            admission.setPatientId(patientId);
            admission.setWardId(wardId);
            admission.setBedNumber(bedNumber);
            admission.setTreatingDoctorId(treatingDoctorId);
            admission.setAdmissionDate(LocalDate.now());
            admission.setIsCurrent(true);

            patientAdmissionDAO.create(admission, conn);

            conn.commit();
            return patientId;

        } catch (Exception e) {
            if (conn != null) {
                try {
                   conn.rollback();
                } catch (SQLException ignored) {}
            }
            throw new DaoException("Failed to admit patient", e);
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException ignored) {}
            }
        }

    }

    public List<Patient> searchPatients(String nameQuery) {
        try {
            return patientDAO.searchByName(nameQuery);
        } catch (DaoException e) {
            throw new RuntimeException("Failed to search patients", e);
        }
    }

    public void updatePatient(Patient patient) {
        try {
            patientDAO.update(patient);
        } catch (DaoException e) {
            throw new RuntimeException("Failed to update patient", e);
        }
    }

    public boolean deletePatient(UUID patientId) throws ServiceException {
        try {
            List<PatientAdmission> admissions = patientAdmissionDAO.findByPatientId(patientId);
            if (admissions.stream().anyMatch(PatientAdmission::getIsCurrent)) {
                throw new ServiceException("Patient has active admissions");
            }

            return patientDAO.delete(patientId);
        } catch (DaoException e) {
            throw new ServiceException("Failed to delete patient", e);
        }
    }

    public Patient getPatientById(UUID id) {
        try {
            return patientDAO.findById(id);
        } catch (DaoException e) {
            throw new RuntimeException("Failed to get patient by id", e);
        }
    }

    public Patient findByPatientNumber(int patientNumber) {
        try {
            Patient patient = patientDAO.findByPatientNumber(patientNumber);
            if (patient == null) {
                //throw new RuntimeException("Patient with number " + patientNumber + " not found");
                return null;
            }

            return patient;
        } catch (DaoException e) {
            throw new RuntimeException("Failed to get patient by number", e);
        }
    }
}
