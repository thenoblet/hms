package gtp.hms.service;

import gtp.hms.dao.PatientDAO;
import gtp.hms.exception.DaoException;
import gtp.hms.model.Patient;

import java.util.List;
import java.util.UUID;

public class PatientService {
    private final PatientDAO patientDAO;


    public PatientService() {
        this.patientDAO = new PatientDAO();
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

    public boolean deletePatient(UUID id) {
        try {
            patientDAO.delete(id);
            return true;
        } catch (DaoException e) {
            throw new RuntimeException("Failed to delete patient", e);
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
                throw new RuntimeException("Patient with number " + patientNumber + " not found");
            }
            return patient;
        } catch (DaoException e) {
            throw new RuntimeException("Failed to get patient by number", e);
        }
    }
}
