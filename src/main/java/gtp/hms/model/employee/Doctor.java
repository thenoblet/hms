package gtp.hms.model.employee;

import java.time.LocalDateTime;
import java.util.UUID;

public class Doctor extends Employee {
    private String speciality;

    public Doctor() {
        setEmployeeType(EmployeeType.DOCTOR);
    }

    public Doctor(int employeeNumber, String firstName, String middleName, String lastName,
                  String address, String phoneNumber, String speciality) {

        super(employeeNumber, firstName, middleName, lastName, address, phoneNumber, EmployeeType.DOCTOR);

        this.speciality = speciality;
    }

    public Doctor(UUID id, int employeeNumber, String firstName, String middleName, String lastName,
                  String address, String phoneNumber, EmployeeType employeeType,
                  LocalDateTime createdAt, LocalDateTime updatedAt, String speciality) {

        super(id, employeeNumber, firstName, middleName, lastName, address, phoneNumber, EmployeeType.DOCTOR, createdAt, updatedAt);
        this.speciality = speciality;
    }

    public String getSpeciality() {
        return speciality;
    }

    public void setSpeciality(String speciality) {
        this.speciality = speciality;
    }

}
