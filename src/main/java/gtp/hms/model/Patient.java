package gtp.hms.model;

import java.time.LocalDateTime;
import java.util.UUID;

public class Patient {
    private UUID id;
    private int patientNumber;
    private String firstName;
    private String middleName;
    private String lastName;
    private String address;
    private String telephoneNumber;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Patient() {}

    public Patient(int patientNumber, String firstName, String middleName, String lastName,
                  String address, String telephoneNumber) {

        this.id = UUID.randomUUID();
        this.patientNumber = patientNumber;
        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
        this.address = address;
        this.telephoneNumber = telephoneNumber;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public Patient(UUID id, int patientNumber, String firstName, String middleName, String lastName,
                   String address, String telephoneNumber,
                   LocalDateTime createdAt, LocalDateTime updatedAt) {

        this.id = id;
        this.patientNumber = patientNumber;
        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
        this.address = address;
        this.telephoneNumber = telephoneNumber;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public void setId() {
        this.id = UUID.randomUUID();
    }

    public int getPatientNumber() {
        return patientNumber;
    }

    public void setPatientNumber(int patientNumber) {
        this.patientNumber = patientNumber;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTelephoneNumber() {
        return telephoneNumber;
    }

    public void setTelephoneNumber(String telephoneNumber) {
        this.telephoneNumber = telephoneNumber;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public void setUpdatedAt() {
        this.updatedAt = LocalDateTime.now();
    }

    @Override
    public String toString() {
        return "Patient [id=" + id + ", patientNumber=" + patientNumber + ", firstName=" + firstName + ", middleName=" + middleName + ", lastName=" + lastName + ", address=" + address + ", telephoneNumber=" + telephoneNumber + ", createdAt=" + createdAt + ", updatedAt=" + updatedAt + "]";
    }
}
