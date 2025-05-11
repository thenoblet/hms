package gtp.hms;

import gtp.hms.model.Patient;
import gtp.hms.util.DatabaseSeeder;
import gtp.hms.service.PatientService;
import java.util.UUID;

public class Main {
    public static void main(String[] args) {
        try {
            // Seed the database first
            //DatabaseSeeder.seedDatabase();

            PatientService patientService = new PatientService();

            // Check for existing patient
            Patient patient = patientService.findByPatientNumber(10005);

            if (patient == null) {
                // Register new patient if not found
                UUID patientId = patientService.registerNewPatient(
                        10005,
                        "Elias",
                        "Tetteh",
                        "Djonu",
                        "50-Asaman St.",
                        "0553777471"
                );
                System.out.printf("✅ Added new Patient with ID: %s%n", patientId);

                // Retrieve the newly created patient
                patient = patientService.findByPatientNumber(10005);
            } else {
                System.out.printf("ℹ️ Patient with number %s already exists (ID: %s)%n",
                        10005, patient.getId());
            }

            // Update patient information
            if (patient != null) {
                System.out.println("Before update: " + patient);
                patient.setFirstName("Jettey");

                patientService.updatePatient(patient);
                System.out.println("After update: " + patientService.findByPatientNumber(10005));
            }

        } catch (Exception e) {
            System.err.println("❌ Error in patient operations: " + e.getMessage());
            e.printStackTrace();
        }
    }
}