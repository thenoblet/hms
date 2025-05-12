package gtp.hms;

import gtp.hms.dao.DepartmentDAO;
import gtp.hms.dao.DoctorDAO;
import gtp.hms.dao.PatientAdmissionDAO;
import gtp.hms.dao.WardDAO;
import gtp.hms.model.Patient;
import gtp.hms.model.PatientAdmission;
import gtp.hms.model.Ward;
import gtp.hms.model.employee.Doctor;
import gtp.hms.service.PatientAdmissionService;
import gtp.hms.util.DatabaseSeeder;
import gtp.hms.service.PatientService;

import java.util.List;
import java.util.UUID;
import java.util.Random;

public class Main {
    public static void main(String[] args) {
        try {
            // Initialize services and DAOs
            PatientService patientService = new PatientService();
            WardDAO wardDAO = new WardDAO();
            DoctorDAO doctorDAO = new DoctorDAO();
            DepartmentDAO departmentDAO = new DepartmentDAO();
            PatientAdmissionService patientAdmissionService = new PatientAdmissionService();

            // Handle first patient (10005)
            handlePatientOperations(patientService, 10005, "Elias", "Tetteh", "Djonu");
            demonstratePatientDeletion(patientService, patientAdmissionService, 10005);

            // Handle second patient with random number
            Random rand = new Random();
            int randomPatientNumber = 10000 + rand.nextInt(90000); // 5-digit number
            Patient admittedPatient = handleAdmissionProcess(
                    patientService,
                    wardDAO,
                    doctorDAO,
                    departmentDAO,
                    patientAdmissionService,
                    randomPatientNumber,
                    "Patrick", "Joan", "Kubi"
            );

            // Display admission records
            if (admittedPatient != null) {
                displayAdmissionRecords(patientAdmissionService, admittedPatient);
            }

        } catch (Exception e) {
            System.err.println("❌ Error in patient operations: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void handlePatientOperations(PatientService patientService,
                                                int patientNumber,
                                                String firstName,
                                                String middleName,
                                                String lastName) throws Exception {
        Patient patient = patientService.findByPatientNumber(patientNumber);

        if (patient == null) {
            UUID patientId = patientService.registerNewPatient(
                    patientNumber, firstName, middleName, lastName,
                    "50-Asaman St.", "0553777471"
            );
            System.out.printf("✅ Added new Patient with ID: %s%n", patientId);
            patient = patientService.findByPatientNumber(patientNumber);
        } else {
            System.out.printf("ℹ️ Patient with number %s already exists (ID: %s)%n",
                    patientNumber, patient.getId());
        }

        // Update example
        if (patient != null) {
            System.out.println("Before update: " + patient);
            patient.setFirstName("Ohene");
            patient.setMiddleName("Mike");
            patientService.updatePatient(patient);
            System.out.println("After update: " + patientService.findByPatientNumber(patientNumber));
        }
    }

    private static Patient handleAdmissionProcess(PatientService patientService,
                                                  WardDAO wardDAO,
                                                  DoctorDAO doctorDAO,
                                                  DepartmentDAO departmentDAO,
                                                  PatientAdmissionService admissionService,
                                                  int patientNumber,
                                                  String firstName,
                                                  String middleName,
                                                  String lastName) throws Exception {
        Patient patient = patientService.findByPatientNumber(patientNumber);

        if (patient == null) {
            UUID patientId = patientService.registerNewPatient(
                    patientNumber, firstName, middleName, lastName,
                    "214 ODK Road", "03232445443"
            );
            System.out.printf("✅ Admitted new Patient with ID: %s%n", patientId);
            patient = patientService.findByPatientNumber(patientNumber);
        }

        if (patient == null) {
            System.err.println("Failed to create/find patient");
            return null;
        }

        // Admission process
        UUID cardioDeptId = departmentDAO.findDepartmentIdByCode("CARD");
        if (cardioDeptId == null) {
            System.err.println("Cardiology department not found");
            return null;
        }

        UUID wardId = wardDAO.findWardIdByNumber(1, cardioDeptId);
        if (wardId == null) {
            System.err.println("Ward not found in Cardiology department");
            return null;
        }

        Ward ward = wardDAO.findById(wardId);
        if (ward == null) {
            System.err.println("Ward details not found");
            return null;
        }

        List<Doctor> doctors = doctorDAO.findAll();
        if (doctors.isEmpty()) {
            System.err.println("No doctors available");
            return null;
        }

        int bedNumber = 5;
        Doctor doctor = doctors.get(0); // Get first available doctor

        try {
            UUID admissionId = admissionService.admitPatient(
                    patient.getId(),
                    ward.getWardNumber(),
                    ward.getDepartmentId(),
                    bedNumber,
                    doctor.getId(),
                    "Heart condition"
            );
            System.out.printf("✅ Admission created with ID: %s%n", admissionId);
        } catch (Exception e) {
            System.err.println("Failed to admit patient: " + e.getMessage());
        }

        return patient;
    }

    private static void displayAdmissionRecords(PatientAdmissionService admissionService,
                                                Patient patient) throws Exception {
        System.out.println("\n=== Admission Records ===");

        // Get all admissions
        List<PatientAdmission> allAdmissions = admissionService.getPatientAdmissions(patient.getId());
        if (allAdmissions.isEmpty()) {
            System.out.println("No admission records found");
        } else {
            for (PatientAdmission admission : allAdmissions) {
                System.out.println(admission);
            }
        }

        // Get current admission
        PatientAdmission currentAdmission = admissionService.getCurrentAdmission(patient.getId());
        System.out.println("\n=== Current Admission ===");
        System.out.println(currentAdmission != null ? currentAdmission : "No current admission");
    }

    private static void demonstratePatientDeletion(PatientService patientService,
                                                   PatientAdmissionService admissionService,
                                                   int patientNumber) throws Exception {
        System.out.println("\n=== Patient Deletion Demo ===");

        // Find the patient first
        Patient patientToDelete = patientService.findByPatientNumber(patientNumber);
        if (patientToDelete == null) {
            System.out.printf("Patient with number %d not found\n", patientNumber);
            return;
        }

        // Check for active admissions
        PatientAdmission currentAdmission = admissionService.getCurrentAdmission(patientToDelete.getId());
        if (currentAdmission != null) {
            System.out.println("Cannot delete patient - they have an active admission:");
            System.out.println(currentAdmission);
            return;
        }

        // Delete the patient
        boolean deleted = patientService.deletePatient(patientToDelete.getId());
        if (deleted) {
            System.out.printf("✅ Successfully deleted patient %d (%s %s)\n",
                    patientNumber, patientToDelete.getFirstName(), patientToDelete.getLastName());

            // Verify deletion
            Patient verify = patientService.findByPatientNumber(patientNumber);
            System.out.println("Verification: " + (verify == null ? "Patient not found (deletion confirmed)" : "Deletion failed!"));
        } else {
            System.out.printf("❌ Failed to delete patient %d\n", patientNumber);
        }
    }
}