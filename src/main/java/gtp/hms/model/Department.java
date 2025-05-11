package gtp.hms.model;

import java.time.LocalDateTime;
import java.util.UUID;

public class Department {
    private UUID id;
    private String departmentName;
    private String description;
    private int departmentCode;
    private int numberOfWards;
    private String building;
    private UUID hospitalId;
    private UUID directorId;
    public LocalDateTime createdAt;
    public LocalDateTime updatedAt;

    public Department() {}


    public Department(String departmentName, String description, int departmentCode,
                      int numberOfWards, String building, UUID hospitalId, UUID directorId) {

        this.id = UUID.randomUUID();
        this.departmentName = departmentName;
        this.description = description;
        this.departmentCode = departmentCode;
        this.numberOfWards = numberOfWards;
        this.building = building;
        this.hospitalId = hospitalId;
        this.directorId = directorId;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public Department(UUID id, String departmentName, String description, int departmentCode,
                      int numberOfWards, String building, UUID hospitalId, UUID directorId,
                      LocalDateTime createdAt, LocalDateTime updatedAt) {

        this.id = id;
        this.departmentName = departmentName;
        this.description = description;
        this.departmentCode = departmentCode;
        this.numberOfWards = numberOfWards;
        this.building = building;
        this.hospitalId = hospitalId;
        this.directorId = directorId;
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

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getDepartmentCode() {
        return departmentCode;
    }

    public void setDepartmentCode(int departmentCode) {
        this.departmentCode = departmentCode;
    }

    public int getNumberOfWards() {
        return numberOfWards;
    }

    public void setNumberOfWards(int numberOfWards) {
        this.numberOfWards = numberOfWards;
    }

    public String getBuilding() {
        return building;
    }

    public void setBuilding(String building) {
        this.building = building;
    }

    public UUID getHospitalId() {
        return hospitalId;
    }

    public void setHospitalId(UUID hospitalId) {
        this.hospitalId = hospitalId;
    }

    public UUID getDirectorId() {
        return directorId;
    }

    public void setDirectorId(UUID directorId) {
        this.directorId = directorId;
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


}
