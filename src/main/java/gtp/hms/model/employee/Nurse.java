package gtp.hms.model.employee;

import java.time.LocalDateTime;
import java.util.UUID;

public class Nurse extends Employee {
    private String rotation;
    private double salary;
    private UUID departmentId;

    public Nurse() {
        setEmployeeType(EmployeeType.nurse);
    }


    public Nurse(int employeeNumber, String firstName, String middleName, String lastName,
                 String address,  String phoneNumber, String rotation, double salary, UUID departmentId) {

        super(employeeNumber, firstName, middleName, lastName, address, phoneNumber, EmployeeType.nurse);
        this.rotation = rotation;
        this.salary = salary;
        this.departmentId = departmentId;
    }


    public Nurse(UUID id, int employeeNumber, String firstName, String middleName, String lastName,
                 String address, String phoneNumber, EmployeeType employeeType, String rotation, double salary,
                 UUID departmentId, LocalDateTime createdAt, LocalDateTime updatedAt) {

        super(id, employeeNumber, firstName, middleName, lastName,
                address, phoneNumber, EmployeeType.nurse, createdAt, updatedAt);

        this.rotation = rotation;
        this.salary = salary;
        this.departmentId = departmentId;
    }

    public String getRotation() {
        return rotation;
    }

    public void setRotation(String rotation) {
       this.rotation = rotation;
    }

    public double getSalary() {
        return salary;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }

    public UUID getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(UUID departmentId) {
        this.departmentId = departmentId;
    }
}
