package gtp.hms.model.employee;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Represents a Doctor employee in the hospital management system.
 * Extends the base Employee class with doctor-specific attributes.
 */
public class Doctor extends Employee {
    private String specialty;

    /**
     * Default constructor that sets the employee type to DOCTOR.
     */
    public Doctor() {
        setEmployeeType(EmployeeType.doctor);
    }

    /**
     * Creates a new Doctor with basic information.
     *
     * @param employeeNumber unique employee identification number
     * @param firstName doctor's first name
     * @param middleName doctor's middle name (optional)
     * @param lastName doctor's last name
     * @param address doctor's contact address
     * @param phoneNumber doctor's contact phone number
     * @param specialty doctor's medical specialty
     */
    public Doctor(int employeeNumber, String firstName, String middleName, String lastName,
                  String address, String phoneNumber, String specialty) {
        super(employeeNumber, firstName, middleName, lastName, address, phoneNumber, EmployeeType.doctor);
        this.specialty = specialty;
    }

    /**
     * Creates a Doctor with complete information including system metadata.
     *
     * @param id unique system identifier
     * @param employeeNumber unique employee identification number
     * @param firstName doctor's first name
     * @param middleName doctor's middle name (optional)
     * @param lastName doctor's last name
     * @param address doctor's contact address
     * @param phoneNumber doctor's contact phone number
     * @param employeeType type of employee (should be DOCTOR)
     * @param createdAt timestamp when record was created
     * @param updatedAt timestamp when record was last updated
     * @param specialty doctor's medical specialty
     */
    public Doctor(UUID id, int employeeNumber, String firstName, String middleName, String lastName,
                  String address, String phoneNumber, EmployeeType employeeType,
                  LocalDateTime createdAt, LocalDateTime updatedAt, String specialty) {
        super(id, employeeNumber, firstName, middleName, lastName, address, phoneNumber,
                EmployeeType.doctor, createdAt, updatedAt);
        this.specialty = specialty;
    }

    /**
     * Gets the doctor's medical specialty.
     *
     * @return the specialty field
     */
    public String getSpecialty() {
        return specialty;
    }

    /**
     * Sets the doctor's medical specialty.
     *
     * @param specialty the specialty to set
     */
    public void setSpecialty(String specialty) {
        this.specialty = specialty;
    }
}