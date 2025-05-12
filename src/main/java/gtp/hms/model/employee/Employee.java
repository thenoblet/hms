package gtp.hms.model.employee;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Base class representing an employee in the hospital management system.
 * Contains common attributes and methods for all employee types.
 */
public class Employee {
    private UUID id;
    private int employeeNumber;
    private String firstName;
    private String middleName;
    private String lastName;
    private String address;
    private String phoneNumber;
    private EmployeeType employeeType;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    /**
     * Default constructor.
     */
    public Employee() {}

    /**
     * Constructor for creating new employee records with automatic ID and timestamp generation.
     *
     * @param employeeNumber unique employee identification number
     * @param firstName employee's first name
     * @param middleName employee's middle name (optional)
     * @param lastName employee's last name
     * @param address employee's contact address
     * @param phoneNumber employee's contact phone number
     * @param employeeType type of employee (DOCTOR, NURSE, etc.)
     */
    public Employee(int employeeNumber, String firstName, String middleName,
                    String lastName, String address, String phoneNumber,
                    EmployeeType employeeType) {
        this.id = UUID.randomUUID();
        this.employeeNumber = employeeNumber;
        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.employeeType = employeeType;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * Constructor for complete employee records including system metadata.
     *
     * @param id unique system identifier
     * @param employeeNumber unique employee identification number
     * @param firstName employee's first name
     * @param middleName employee's middle name (optional)
     * @param lastName employee's last name
     * @param address employee's contact address
     * @param phoneNumber employee's contact phone number
     * @param employeeType type of employee (DOCTOR, NURSE, etc.)
     * @param createdAt timestamp when record was created
     * @param updatedAt timestamp when record was last updated
     */
    public Employee(UUID id, int employeeNumber, String firstName, String middleName, String lastName,
                    String address, String phoneNumber, EmployeeType employeeType,
                    LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.employeeNumber = employeeNumber;
        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.employeeType = employeeType;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // Accessor methods with standard Javadoc

    /**
     * Gets the unique identifier for this employee.
     * @return employee's UUID
     */
    public UUID getId() {
        return id;
    }

    /**
     * Sets the unique identifier for this employee.
     * @param id the UUID to set
     */
    public void setId(UUID id) {
        this.id = id;
    }

    /**
     * Gets the employee's identification number.
     * @return employee number
     */
    public int getEmployeeNumber() {
        return employeeNumber;
    }

    /**
     * Sets the employee's identification number.
     * @param employeeNumber the number to set
     */
    public void setEmployeeNumber(int employeeNumber) {
        this.employeeNumber = employeeNumber;
    }

    /**
     * Gets the employee's first name.
     * @return first name
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Sets the employee's first name.
     * @param firstName the name to set
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * Gets the employee's middle name.
     * @return middle name (may be null)
     */
    public String getMiddleName() {
        return middleName;
    }

    /**
     * Sets the employee's middle name.
     * @param middleName the name to set (may be null)
     */
    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    /**
     * Gets the employee's last name.
     * @return last name
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Sets the employee's last name.
     * @param lastName the name to set
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * Gets the employee's full name (first + middle + last names).
     * @return concatenated full name string
     */
    public String getFullName() {
        return firstName + " " + middleName + " " + lastName;
    }

    /**
     * Gets the employee's contact address.
     * @return address string
     */
    public String getAddress() {
        return address;
    }

    /**
     * Sets the employee's contact address.
     * @param address the address to set
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * Gets the employee's phone number.
     * @return phone number string
     */
    public String getPhoneNumber() {
        return phoneNumber;
    }

    /**
     * Sets the employee's phone number.
     * @param phoneNumber the number to set
     */
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    /**
     * Gets the employee's type (DOCTOR, NURSE, etc.).
     * @return employee type enum
     */
    public EmployeeType getEmployeeType() {
        return employeeType;
    }

    /**
     * Sets the employee's type (DOCTOR, NURSE, etc.).
     * @param employeeType the type to set
     */
    public void setEmployeeType(EmployeeType employeeType) {
        this.employeeType = employeeType;
    }

    /**
     * Gets the timestamp when this employee record was created.
     * @return creation timestamp
     */
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    /**
     * Sets the timestamp when this employee record was created.
     * @param createdAt the timestamp to set
     */
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    /**
     * Gets the timestamp when this employee record was last updated.
     * @return last update timestamp
     */
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    /**
     * Sets the timestamp when this employee record was last updated.
     * @param updatedAt the timestamp to set
     */
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    /**
     * Returns a string representation of the employee.
     * @return string containing all employee fields
     */
    @Override
    public String toString() {
        return "Employee [id=" + id + ", employeeNumber=" + employeeNumber + ", " +
                "firstName=" + firstName + ", middleName=" + middleName + ", lastName=" + lastName + ", " +
                "address=" + address + ", phoneNumber=" + phoneNumber + ", employeeType=" + employeeType + ", " +
                "createdAt=" + createdAt + ", updatedAt=" + updatedAt + "]";
    }
}